import axios from 'axios';
import { ref } from 'vue';
import { defineStore } from 'pinia';

import type { IAgentSite } from '@/shared/model/agent-site.model';
import type { ISubscriptionPlan } from '@/shared/model/subscription-plan.model';
import { ensureStripe } from '@/shared/services/stripe.service';

interface CheckoutPayload {
  planId: number;
  billingCycle?: 'monthly' | 'yearly';
  successUrl?: string;
  cancelUrl?: string;
  agentSiteId?: number;
}

const BILLING_SUBSCRIPTION_API = 'api/billing-subscriptions';
const STRIPE_CHECKOUT_API = 'api/stripe/checkout-session';
const STRIPE_PORTAL_API = 'api/stripe/customer-portal';

export const useBillingStore = defineStore('billing', () => {
  const processing = ref(false);
  const error = ref<string | null>(null);

  const reset = () => {
    error.value = null;
  };

  const createSubscriptionRecord = async (site: IAgentSite, plan: ISubscriptionPlan) => {
    const response = await axios.post(BILLING_SUBSCRIPTION_API, {
      startDate: new Date().toISOString(),
      status: 'PENDING',
      plan,
      site,
    });

    return response.data;
  };

  const redirectToCheckout = async (payload: CheckoutPayload) => {
    processing.value = true;
    error.value = null;

    try {
      const response = await axios.post<{ sessionId: string }>(STRIPE_CHECKOUT_API, payload);
      const stripe = await ensureStripe();
      const sessionId = response.data?.sessionId;

      if (!sessionId) {
        throw new Error('Stripe sessionId is missing from the response');
      }

      const { error: stripeError } = await stripe.redirectToCheckout({ sessionId });

      if (stripeError) {
        throw stripeError;
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Unable to start Stripe checkout';
      throw err;
    } finally {
      processing.value = false;
    }
  };

  const openCustomerPortal = async () => {
    processing.value = true;
    error.value = null;

    try {
      const response = await axios.post<{ url: string }>(STRIPE_PORTAL_API, {});
      const url = response.data?.url;

      if (!url) {
        throw new Error('Stripe portal URL is missing from the response');
      }

      window.location.href = url;
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Unable to open Stripe billing portal';
      throw err;
    } finally {
      processing.value = false;
    }
  };

  return {
    processing,
    error,
    reset,
    createSubscriptionRecord,
    redirectToCheckout,
    openCustomerPortal,
  };
});

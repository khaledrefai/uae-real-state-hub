import type { Stripe } from '@stripe/stripe-js';
import { loadStripe } from '@stripe/stripe-js';

let stripePromise: Promise<Stripe | null> | null = null;

const createStripe = (): Promise<Stripe | null> => {
  const publishableKey = import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY;

  if (!publishableKey) {
    throw new Error('Stripe publishable key is not defined. Add VITE_STRIPE_PUBLISHABLE_KEY to your environment');
  }

  return loadStripe(publishableKey, {
    apiVersion: '2024-08-20',
  });
};

export const getStripe = (): Promise<Stripe | null> => {
  if (!stripePromise) {
    stripePromise = createStripe();
  }

  return stripePromise;
};

export const ensureStripe = async (): Promise<Stripe> => {
  const stripe = await getStripe();

  if (!stripe) {
    throw new Error('Unable to initialize Stripe client');
  }

  return stripe;
};

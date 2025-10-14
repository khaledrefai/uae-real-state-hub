import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import BillingSubscriptionService from './billing-subscription.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import AgentSiteService from '@/entities/agent-site/agent-site.service';
import { type IAgentSite } from '@/shared/model/agent-site.model';
import { BillingSubscription, type IBillingSubscription } from '@/shared/model/billing-subscription.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BillingSubscriptionUpdate',
  setup() {
    const billingSubscriptionService = inject('billingSubscriptionService', () => new BillingSubscriptionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const billingSubscription: Ref<IBillingSubscription> = ref(new BillingSubscription());

    const agentSiteService = inject('agentSiteService', () => new AgentSiteService());

    const agentSites: Ref<IAgentSite[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveBillingSubscription = async billingSubscriptionId => {
      try {
        const res = await billingSubscriptionService().find(billingSubscriptionId);
        res.startDate = new Date(res.startDate);
        res.endDate = new Date(res.endDate);
        billingSubscription.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.billingSubscriptionId) {
      retrieveBillingSubscription(route.params.billingSubscriptionId);
    }

    const initRelationships = () => {
      agentSiteService()
        .retrieve()
        .then(res => {
          agentSites.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      status: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      startDate: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      endDate: {},
      stripeCustomerId: {},
      stripeSubscriptionId: {},
      cancelAtPeriodEnd: {},
      isActive: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      site: {},
    };
    const v$ = useVuelidate(validationRules, billingSubscription as any);
    v$.value.$validate();

    return {
      billingSubscriptionService,
      alertService,
      billingSubscription,
      previousState,
      isSaving,
      currentLanguage,
      agentSites,
      v$,
      ...useDateFormat({ entityRef: billingSubscription }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.billingSubscription.id) {
        this.billingSubscriptionService()
          .update(this.billingSubscription)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.billingSubscription.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.billingSubscriptionService()
          .create(this.billingSubscription)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.billingSubscription.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

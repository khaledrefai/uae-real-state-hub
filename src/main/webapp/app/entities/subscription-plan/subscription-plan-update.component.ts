import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SubscriptionPlanService from './subscription-plan.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ISubscriptionPlan, SubscriptionPlan } from '@/shared/model/subscription-plan.model';
import { Currency } from '@/shared/model/enumerations/currency.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionPlanUpdate',
  setup() {
    const subscriptionPlanService = inject('subscriptionPlanService', () => new SubscriptionPlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscriptionPlan: Ref<ISubscriptionPlan> = ref(new SubscriptionPlan());
    const currencyValues: Ref<string[]> = ref(Object.keys(Currency));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSubscriptionPlan = async subscriptionPlanId => {
      try {
        const res = await subscriptionPlanService().find(subscriptionPlanId);
        subscriptionPlan.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscriptionPlanId) {
      retrieveSubscriptionPlan(route.params.subscriptionPlanId);
    }

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      code: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      priceMonthly: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      priceYearly: {},
      currency: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      stripePriceMonthlyId: {},
      stripePriceYearlyId: {},
      maxListings: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      features: {},
    };
    const v$ = useVuelidate(validationRules, subscriptionPlan as any);
    v$.value.$validate();

    return {
      subscriptionPlanService,
      alertService,
      subscriptionPlan,
      previousState,
      currencyValues,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.subscriptionPlan.id) {
        this.subscriptionPlanService()
          .update(this.subscriptionPlan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.subscriptionPlan.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.subscriptionPlanService()
          .create(this.subscriptionPlan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.subscriptionPlan.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PaymentPlanService from './payment-plan.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';
import { type IProperty } from '@/shared/model/property.model';
import { type IPaymentPlan, PaymentPlan } from '@/shared/model/payment-plan.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentPlanUpdate',
  setup() {
    const paymentPlanService = inject('paymentPlanService', () => new PaymentPlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paymentPlan: Ref<IPaymentPlan> = ref(new PaymentPlan());

    const propertyService = inject('propertyService', () => new PropertyService());

    const properties: Ref<IProperty[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaymentPlan = async paymentPlanId => {
      try {
        const res = await paymentPlanService().find(paymentPlanId);
        paymentPlan.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentPlanId) {
      retrievePaymentPlan(route.params.paymentPlanId);
    }

    const initRelationships = () => {
      propertyService()
        .retrieve()
        .then(res => {
          properties.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      monthsAfterHandover: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      property: {},
    };
    const v$ = useVuelidate(validationRules, paymentPlan as any);
    v$.value.$validate();

    return {
      paymentPlanService,
      alertService,
      paymentPlan,
      previousState,
      isSaving,
      currentLanguage,
      properties,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.paymentPlan.id) {
        this.paymentPlanService()
          .update(this.paymentPlan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.paymentPlan.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.paymentPlanService()
          .create(this.paymentPlan)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.paymentPlan.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

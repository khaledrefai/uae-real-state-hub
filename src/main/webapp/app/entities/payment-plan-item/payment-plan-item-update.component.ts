import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PaymentPlanItemService from './payment-plan-item.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PaymentPlanService from '@/entities/payment-plan/payment-plan.service';
import { type IPaymentPlan } from '@/shared/model/payment-plan.model';
import { type IPaymentPlanItem, PaymentPlanItem } from '@/shared/model/payment-plan-item.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentPlanItemUpdate',
  setup() {
    const paymentPlanItemService = inject('paymentPlanItemService', () => new PaymentPlanItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paymentPlanItem: Ref<IPaymentPlanItem> = ref(new PaymentPlanItem());

    const paymentPlanService = inject('paymentPlanService', () => new PaymentPlanService());

    const paymentPlans: Ref<IPaymentPlan[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaymentPlanItem = async paymentPlanItemId => {
      try {
        const res = await paymentPlanItemService().find(paymentPlanItemId);
        paymentPlanItem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentPlanItemId) {
      retrievePaymentPlanItem(route.params.paymentPlanItemId);
    }

    const initRelationships = () => {
      paymentPlanService()
        .retrieve()
        .then(res => {
          paymentPlans.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      orderNo: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      paymentTime: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      percentOfPayment: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      plan: {},
    };
    const v$ = useVuelidate(validationRules, paymentPlanItem as any);
    v$.value.$validate();

    return {
      paymentPlanItemService,
      alertService,
      paymentPlanItem,
      previousState,
      isSaving,
      currentLanguage,
      paymentPlans,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.paymentPlanItem.id) {
        this.paymentPlanItemService()
          .update(this.paymentPlanItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.paymentPlanItem.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.paymentPlanItemService()
          .create(this.paymentPlanItem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.paymentPlanItem.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

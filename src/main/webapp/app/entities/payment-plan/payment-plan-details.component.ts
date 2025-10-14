import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PaymentPlanService from './payment-plan.service';
import { type IPaymentPlan } from '@/shared/model/payment-plan.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentPlanDetails',
  setup() {
    const paymentPlanService = inject('paymentPlanService', () => new PaymentPlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paymentPlan: Ref<IPaymentPlan> = ref({});

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

    return {
      alertService,
      paymentPlan,

      previousState,
      t$: useI18n().t,
    };
  },
});

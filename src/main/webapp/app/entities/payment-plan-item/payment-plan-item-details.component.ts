import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PaymentPlanItemService from './payment-plan-item.service';
import { type IPaymentPlanItem } from '@/shared/model/payment-plan-item.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentPlanItemDetails',
  setup() {
    const paymentPlanItemService = inject('paymentPlanItemService', () => new PaymentPlanItemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paymentPlanItem: Ref<IPaymentPlanItem> = ref({});

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

    return {
      alertService,
      paymentPlanItem,

      previousState,
      t$: useI18n().t,
    };
  },
});

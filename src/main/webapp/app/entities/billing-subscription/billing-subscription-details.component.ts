import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import BillingSubscriptionService from './billing-subscription.service';
import { useDateFormat } from '@/shared/composables';
import { type IBillingSubscription } from '@/shared/model/billing-subscription.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BillingSubscriptionDetails',
  setup() {
    const dateFormat = useDateFormat();
    const billingSubscriptionService = inject('billingSubscriptionService', () => new BillingSubscriptionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const billingSubscription: Ref<IBillingSubscription> = ref({});

    const retrieveBillingSubscription = async billingSubscriptionId => {
      try {
        const res = await billingSubscriptionService().find(billingSubscriptionId);
        billingSubscription.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.billingSubscriptionId) {
      retrieveBillingSubscription(route.params.billingSubscriptionId);
    }

    return {
      ...dateFormat,
      alertService,
      billingSubscription,

      previousState,
      t$: useI18n().t,
    };
  },
});

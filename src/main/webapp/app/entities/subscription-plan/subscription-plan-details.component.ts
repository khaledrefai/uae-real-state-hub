import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SubscriptionPlanService from './subscription-plan.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ISubscriptionPlan } from '@/shared/model/subscription-plan.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionPlanDetails',
  setup() {
    const subscriptionPlanService = inject('subscriptionPlanService', () => new SubscriptionPlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const subscriptionPlan: Ref<ISubscriptionPlan> = ref({});

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

    return {
      alertService,
      subscriptionPlan,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});

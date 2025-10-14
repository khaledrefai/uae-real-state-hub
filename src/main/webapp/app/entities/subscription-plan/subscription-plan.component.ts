import { type Ref, defineComponent, inject, onMounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';

import SubscriptionPlanService from './subscription-plan.service';
import { type ISubscriptionPlan } from '@/shared/model/subscription-plan.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionPlan',
  setup() {
    const { t: t$ } = useI18n();
    const dataUtils = useDataUtils();
    const subscriptionPlanService = inject('subscriptionPlanService', () => new SubscriptionPlanService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    const subscriptionPlans: Ref<ISubscriptionPlan[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {
      page.value = 1;
    };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') {
        result.push('id');
      }
      return result;
    };

    const retrieveSubscriptionPlans = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = await subscriptionPlanService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        subscriptionPlans.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSubscriptionPlans();
    };

    onMounted(async () => {
      await retrieveSubscriptionPlans();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISubscriptionPlan) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSubscriptionPlan = async () => {
      try {
        await subscriptionPlanService().delete(removeId.value);
        const message = t$('uaeRealStateHubApp.subscriptionPlan.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSubscriptionPlans();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) {
        reverse.value = !reverse.value;
      } else {
        reverse.value = false;
      }
      propOrder.value = newOrder;
    };

    // Whenever order changes, reset the pagination
    watch([propOrder, reverse], async () => {
      if (page.value === 1) {
        // first page, retrieve new data
        await retrieveSubscriptionPlans();
      } else {
        // reset the pagination
        clear();
      }
    });

    // Whenever page changes, switch to the new page.
    watch(page, async () => {
      await retrieveSubscriptionPlans();
    });

    return {
      subscriptionPlans,
      handleSyncList,
      isFetching,
      retrieveSubscriptionPlans,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSubscriptionPlan,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      t$,
      ...dataUtils,
    };
  },
});

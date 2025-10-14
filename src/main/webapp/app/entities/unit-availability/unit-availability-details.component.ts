import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UnitAvailabilityService from './unit-availability.service';
import { useDateFormat } from '@/shared/composables';
import { type IUnitAvailability } from '@/shared/model/unit-availability.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UnitAvailabilityDetails',
  setup() {
    const dateFormat = useDateFormat();
    const unitAvailabilityService = inject('unitAvailabilityService', () => new UnitAvailabilityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const unitAvailability: Ref<IUnitAvailability> = ref({});

    const retrieveUnitAvailability = async unitAvailabilityId => {
      try {
        const res = await unitAvailabilityService().find(unitAvailabilityId);
        unitAvailability.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.unitAvailabilityId) {
      retrieveUnitAvailability(route.params.unitAvailabilityId);
    }

    return {
      ...dateFormat,
      alertService,
      unitAvailability,

      previousState,
      t$: useI18n().t,
    };
  },
});

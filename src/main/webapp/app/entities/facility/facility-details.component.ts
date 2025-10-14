import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import FacilityService from './facility.service';
import { type IFacility } from '@/shared/model/facility.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FacilityDetails',
  setup() {
    const facilityService = inject('facilityService', () => new FacilityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const facility: Ref<IFacility> = ref({});

    const retrieveFacility = async facilityId => {
      try {
        const res = await facilityService().find(facilityId);
        facility.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.facilityId) {
      retrieveFacility(route.params.facilityId);
    }

    return {
      alertService,
      facility,

      previousState,
      t$: useI18n().t,
    };
  },
});

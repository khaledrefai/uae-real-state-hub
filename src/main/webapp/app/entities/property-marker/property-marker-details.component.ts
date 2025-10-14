import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PropertyMarkerService from './property-marker.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPropertyMarker } from '@/shared/model/property-marker.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PropertyMarkerDetails',
  setup() {
    const propertyMarkerService = inject('propertyMarkerService', () => new PropertyMarkerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const propertyMarker: Ref<IPropertyMarker> = ref({});

    const retrievePropertyMarker = async propertyMarkerId => {
      try {
        const res = await propertyMarkerService().find(propertyMarkerId);
        propertyMarker.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.propertyMarkerId) {
      retrievePropertyMarker(route.params.propertyMarkerId);
    }

    return {
      alertService,
      propertyMarker,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});

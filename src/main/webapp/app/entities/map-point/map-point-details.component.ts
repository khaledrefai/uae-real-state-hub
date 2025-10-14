import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MapPointService from './map-point.service';
import { type IMapPoint } from '@/shared/model/map-point.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MapPointDetails',
  setup() {
    const mapPointService = inject('mapPointService', () => new MapPointService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mapPoint: Ref<IMapPoint> = ref({});

    const retrieveMapPoint = async mapPointId => {
      try {
        const res = await mapPointService().find(mapPointId);
        mapPoint.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mapPointId) {
      retrieveMapPoint(route.params.mapPointId);
    }

    return {
      alertService,
      mapPoint,

      previousState,
      t$: useI18n().t,
    };
  },
});

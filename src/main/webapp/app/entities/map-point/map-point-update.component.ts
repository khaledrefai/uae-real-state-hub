import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MapPointService from './map-point.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';
import { type IProperty } from '@/shared/model/property.model';
import { type IMapPoint, MapPoint } from '@/shared/model/map-point.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MapPointUpdate',
  setup() {
    const mapPointService = inject('mapPointService', () => new MapPointService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mapPoint: Ref<IMapPoint> = ref(new MapPoint());

    const propertyService = inject('propertyService', () => new PropertyService());

    const properties: Ref<IProperty[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      propertyService()
        .retrieve()
        .then(res => {
          properties.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      distanceKm: {},
      property: {},
    };
    const v$ = useVuelidate(validationRules, mapPoint as any);
    v$.value.$validate();

    return {
      mapPointService,
      alertService,
      mapPoint,
      previousState,
      isSaving,
      currentLanguage,
      properties,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.mapPoint.id) {
        this.mapPointService()
          .update(this.mapPoint)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.mapPoint.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mapPointService()
          .create(this.mapPoint)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.mapPoint.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

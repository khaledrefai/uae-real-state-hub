import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UnitAvailabilityService from './unit-availability.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';
import { type IProperty } from '@/shared/model/property.model';
import { type IUnitAvailability, UnitAvailability } from '@/shared/model/unit-availability.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UnitAvailabilityUpdate',
  setup() {
    const unitAvailabilityService = inject('unitAvailabilityService', () => new UnitAvailabilityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const unitAvailability: Ref<IUnitAvailability> = ref(new UnitAvailability());

    const propertyService = inject('propertyService', () => new PropertyService());

    const properties: Ref<IProperty[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveUnitAvailability = async unitAvailabilityId => {
      try {
        const res = await unitAvailabilityService().find(unitAvailabilityId);
        res.lastUpdated = new Date(res.lastUpdated);
        unitAvailability.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.unitAvailabilityId) {
      retrieveUnitAvailability(route.params.unitAvailabilityId);
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
      buildingName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      areaFrom: {},
      areaUnit: {},
      bedroomType: {},
      lastUpdated: {},
      priceCurrency: {},
      priceFrom: {},
      priceTo: {},
      unitsAvailable: {},
      property: {},
    };
    const v$ = useVuelidate(validationRules, unitAvailability as any);
    v$.value.$validate();

    return {
      unitAvailabilityService,
      alertService,
      unitAvailability,
      previousState,
      isSaving,
      currentLanguage,
      properties,
      v$,
      ...useDateFormat({ entityRef: unitAvailability }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.unitAvailability.id) {
        this.unitAvailabilityService()
          .update(this.unitAvailability)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.unitAvailability.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.unitAvailabilityService()
          .create(this.unitAvailability)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.unitAvailability.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

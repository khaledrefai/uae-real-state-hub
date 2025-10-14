import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FacilityService from './facility.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';
import { type IProperty } from '@/shared/model/property.model';
import { Facility, type IFacility } from '@/shared/model/facility.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FacilityUpdate',
  setup() {
    const facilityService = inject('facilityService', () => new FacilityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const facility: Ref<IFacility> = ref(new Facility());

    const propertyService = inject('propertyService', () => new PropertyService());

    const properties: Ref<IProperty[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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
      source: {},
      imageUrl: {},
      property: {},
    };
    const v$ = useVuelidate(validationRules, facility as any);
    v$.value.$validate();

    return {
      facilityService,
      alertService,
      facility,
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
      if (this.facility.id) {
        this.facilityService()
          .update(this.facility)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.facility.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.facilityService()
          .create(this.facility)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.facility.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

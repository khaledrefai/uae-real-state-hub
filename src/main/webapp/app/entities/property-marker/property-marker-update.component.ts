import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PropertyMarkerService from './property-marker.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IPropertyMarker, PropertyMarker } from '@/shared/model/property-marker.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PropertyMarkerUpdate',
  setup() {
    const propertyMarkerService = inject('propertyMarkerService', () => new PropertyMarkerService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const propertyMarker: Ref<IPropertyMarker> = ref(new PropertyMarker());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {};

    initRelationships();

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      extId: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      area: {},
      completionDate: {},
      latitude: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      longitude: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      developer: {},
      status: {},
      saleStatus: {},
      minPrice: {},
      coverUrl: {},
      coverJson: {},
      property: {},
    };
    const v$ = useVuelidate(validationRules, propertyMarker as any);
    v$.value.$validate();

    return {
      propertyMarkerService,
      alertService,
      propertyMarker,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.propertyMarker.id) {
        this.propertyMarkerService()
          .update(this.propertyMarker)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.propertyMarker.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.propertyMarkerService()
          .create(this.propertyMarker)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.propertyMarker.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

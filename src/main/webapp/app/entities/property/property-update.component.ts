import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PropertyService from './property.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PropertyMarkerService from '@/entities/property-marker/property-marker.service';
import { type IPropertyMarker } from '@/shared/model/property-marker.model';
import { type IProperty, Property } from '@/shared/model/property.model';
import { ListingType } from '@/shared/model/enumerations/listing-type.model';
import { PropertyStatus } from '@/shared/model/enumerations/property-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PropertyUpdate',
  setup() {
    const propertyService = inject('propertyService', () => new PropertyService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const property: Ref<IProperty> = ref(new Property());

    const propertyMarkerService = inject('propertyMarkerService', () => new PropertyMarkerService());

    const propertyMarkers: Ref<IPropertyMarker[]> = ref([]);
    const listingTypeValues: Ref<string[]> = ref(Object.keys(ListingType));
    const propertyStatusValues: Ref<string[]> = ref(Object.keys(PropertyStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveProperty = async propertyId => {
      try {
        const res = await propertyService().find(propertyId);
        res.completionDateTime = new Date(res.completionDateTime);
        res.publishedAt = new Date(res.publishedAt);
        res.updatedAt = new Date(res.updatedAt);
        property.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.propertyId) {
      retrieveProperty(route.params.propertyId);
    }

    const initRelationships = () => {
      propertyMarkerService()
        .retrieve()
        .then(res => {
          propertyMarkers.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      extId: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      slug: {},
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      developer: {},
      area: {},
      city: {},
      country: {},
      listingType: {},
      status: {},
      saleStatus: {},
      readiness: {},
      serviceCharge: {},
      furnishing: {},
      hasEscrow: {},
      postHandover: {},
      completionDateTime: {},
      minPrice: {},
      maxPrice: {},
      minPriceAed: {},
      priceCurrency: {},
      minArea: {},
      maxArea: {},
      areaUnit: {},
      latitude: {},
      longitude: {},
      coverUrl: {},
      coverJson: {},
      videoUrl: {},
      brochureUrl: {},
      layoutsPdfUrl: {},
      website: {},
      overviewMd: {},
      raw: {},
      publishedAt: {},
      updatedAt: {},
      marker: {},
    };
    const v$ = useVuelidate(validationRules, property as any);
    v$.value.$validate();

    return {
      propertyService,
      alertService,
      property,
      previousState,
      listingTypeValues,
      propertyStatusValues,
      isSaving,
      currentLanguage,
      propertyMarkers,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: property }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.property.id) {
        this.propertyService()
          .update(this.property)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.property.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.propertyService()
          .create(this.property)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.property.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

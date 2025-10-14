import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UnitBlockService from './unit-block.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';
import { type IProperty } from '@/shared/model/property.model';
import { type IUnitBlock, UnitBlock } from '@/shared/model/unit-block.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UnitBlockUpdate',
  setup() {
    const unitBlockService = inject('unitBlockService', () => new UnitBlockService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const unitBlock: Ref<IUnitBlock> = ref(new UnitBlock());

    const propertyService = inject('propertyService', () => new PropertyService());

    const properties: Ref<IProperty[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveUnitBlock = async unitBlockId => {
      try {
        const res = await unitBlockService().find(unitBlockId);
        unitBlock.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.unitBlockId) {
      retrieveUnitBlock(route.params.unitBlockId);
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
      normalizedType: {},
      unitType: {},
      bedroomsAmount: {},
      unitBedrooms: {},
      areaUnit: {},
      unitsAmount: {},
      unitsAreaFrom: {},
      unitsAreaTo: {},
      unitsAreaFromM2: {},
      unitsAreaToM2: {},
      unitsPriceFrom: {},
      unitsPriceTo: {},
      priceCurrency: {},
      typicalImageUrl: {},
      property: {},
    };
    const v$ = useVuelidate(validationRules, unitBlock as any);
    v$.value.$validate();

    return {
      unitBlockService,
      alertService,
      unitBlock,
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
      if (this.unitBlock.id) {
        this.unitBlockService()
          .update(this.unitBlock)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.unitBlock.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.unitBlockService()
          .create(this.unitBlock)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.unitBlock.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

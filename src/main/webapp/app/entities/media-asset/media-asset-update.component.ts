import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MediaAssetService from './media-asset.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';
import { type IProperty } from '@/shared/model/property.model';
import { type IMediaAsset, MediaAsset } from '@/shared/model/media-asset.model';
import { MediaKind } from '@/shared/model/enumerations/media-kind.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MediaAssetUpdate',
  setup() {
    const mediaAssetService = inject('mediaAssetService', () => new MediaAssetService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const mediaAsset: Ref<IMediaAsset> = ref(new MediaAsset());

    const propertyService = inject('propertyService', () => new PropertyService());

    const properties: Ref<IProperty[]> = ref([]);
    const mediaKindValues: Ref<string[]> = ref(Object.keys(MediaKind));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMediaAsset = async mediaAssetId => {
      try {
        const res = await mediaAssetService().find(mediaAssetId);
        mediaAsset.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.mediaAssetId) {
      retrieveMediaAsset(route.params.mediaAssetId);
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
      kind: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      url: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      mimeType: {},
      width: {},
      height: {},
      mediaSize: {},
      source: {},
      title: {},
      altText: {},
      sortOrder: {},
      property: {},
    };
    const v$ = useVuelidate(validationRules, mediaAsset as any);
    v$.value.$validate();

    return {
      mediaAssetService,
      alertService,
      mediaAsset,
      previousState,
      mediaKindValues,
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
      if (this.mediaAsset.id) {
        this.mediaAssetService()
          .update(this.mediaAsset)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.mediaAsset.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.mediaAssetService()
          .create(this.mediaAsset)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.mediaAsset.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

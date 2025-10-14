import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MediaAssetService from './media-asset.service';
import { type IMediaAsset } from '@/shared/model/media-asset.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MediaAssetDetails',
  setup() {
    const mediaAssetService = inject('mediaAssetService', () => new MediaAssetService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const mediaAsset: Ref<IMediaAsset> = ref({});

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

    return {
      alertService,
      mediaAsset,

      previousState,
      t$: useI18n().t,
    };
  },
});

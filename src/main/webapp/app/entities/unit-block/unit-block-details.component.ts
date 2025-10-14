import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UnitBlockService from './unit-block.service';
import { type IUnitBlock } from '@/shared/model/unit-block.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UnitBlockDetails',
  setup() {
    const unitBlockService = inject('unitBlockService', () => new UnitBlockService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const unitBlock: Ref<IUnitBlock> = ref({});

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

    return {
      alertService,
      unitBlock,

      previousState,
      t$: useI18n().t,
    };
  },
});

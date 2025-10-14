import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MediaAssetDetails from './media-asset-details.vue';
import MediaAssetService from './media-asset.service';
import AlertService from '@/shared/alert/alert.service';

type MediaAssetDetailsComponentType = InstanceType<typeof MediaAssetDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mediaAssetSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MediaAsset Management Detail Component', () => {
    let mediaAssetServiceStub: SinonStubbedInstance<MediaAssetService>;
    let mountOptions: MountingOptions<MediaAssetDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mediaAssetServiceStub = sinon.createStubInstance<MediaAssetService>(MediaAssetService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          mediaAssetService: () => mediaAssetServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mediaAssetServiceStub.find.resolves(mediaAssetSample);
        route = {
          params: {
            mediaAssetId: `${123}`,
          },
        };
        const wrapper = shallowMount(MediaAssetDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mediaAsset).toMatchObject(mediaAssetSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mediaAssetServiceStub.find.resolves(mediaAssetSample);
        const wrapper = shallowMount(MediaAssetDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

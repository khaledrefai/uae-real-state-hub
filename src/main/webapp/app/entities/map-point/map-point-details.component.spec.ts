import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MapPointDetails from './map-point-details.vue';
import MapPointService from './map-point.service';
import AlertService from '@/shared/alert/alert.service';

type MapPointDetailsComponentType = InstanceType<typeof MapPointDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mapPointSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MapPoint Management Detail Component', () => {
    let mapPointServiceStub: SinonStubbedInstance<MapPointService>;
    let mountOptions: MountingOptions<MapPointDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      mapPointServiceStub = sinon.createStubInstance<MapPointService>(MapPointService);

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
          mapPointService: () => mapPointServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mapPointServiceStub.find.resolves(mapPointSample);
        route = {
          params: {
            mapPointId: `${123}`,
          },
        };
        const wrapper = shallowMount(MapPointDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.mapPoint).toMatchObject(mapPointSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mapPointServiceStub.find.resolves(mapPointSample);
        const wrapper = shallowMount(MapPointDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

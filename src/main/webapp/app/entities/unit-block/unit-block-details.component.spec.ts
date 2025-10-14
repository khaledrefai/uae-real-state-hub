import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UnitBlockDetails from './unit-block-details.vue';
import UnitBlockService from './unit-block.service';
import AlertService from '@/shared/alert/alert.service';

type UnitBlockDetailsComponentType = InstanceType<typeof UnitBlockDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const unitBlockSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('UnitBlock Management Detail Component', () => {
    let unitBlockServiceStub: SinonStubbedInstance<UnitBlockService>;
    let mountOptions: MountingOptions<UnitBlockDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      unitBlockServiceStub = sinon.createStubInstance<UnitBlockService>(UnitBlockService);

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
          unitBlockService: () => unitBlockServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        unitBlockServiceStub.find.resolves(unitBlockSample);
        route = {
          params: {
            unitBlockId: `${123}`,
          },
        };
        const wrapper = shallowMount(UnitBlockDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.unitBlock).toMatchObject(unitBlockSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        unitBlockServiceStub.find.resolves(unitBlockSample);
        const wrapper = shallowMount(UnitBlockDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

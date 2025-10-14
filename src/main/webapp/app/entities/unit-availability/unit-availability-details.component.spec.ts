import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UnitAvailabilityDetails from './unit-availability-details.vue';
import UnitAvailabilityService from './unit-availability.service';
import AlertService from '@/shared/alert/alert.service';

type UnitAvailabilityDetailsComponentType = InstanceType<typeof UnitAvailabilityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const unitAvailabilitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('UnitAvailability Management Detail Component', () => {
    let unitAvailabilityServiceStub: SinonStubbedInstance<UnitAvailabilityService>;
    let mountOptions: MountingOptions<UnitAvailabilityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      unitAvailabilityServiceStub = sinon.createStubInstance<UnitAvailabilityService>(UnitAvailabilityService);

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
          unitAvailabilityService: () => unitAvailabilityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        unitAvailabilityServiceStub.find.resolves(unitAvailabilitySample);
        route = {
          params: {
            unitAvailabilityId: `${123}`,
          },
        };
        const wrapper = shallowMount(UnitAvailabilityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.unitAvailability).toMatchObject(unitAvailabilitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        unitAvailabilityServiceStub.find.resolves(unitAvailabilitySample);
        const wrapper = shallowMount(UnitAvailabilityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

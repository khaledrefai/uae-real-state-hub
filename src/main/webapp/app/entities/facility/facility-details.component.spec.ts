import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FacilityDetails from './facility-details.vue';
import FacilityService from './facility.service';
import AlertService from '@/shared/alert/alert.service';

type FacilityDetailsComponentType = InstanceType<typeof FacilityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const facilitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Facility Management Detail Component', () => {
    let facilityServiceStub: SinonStubbedInstance<FacilityService>;
    let mountOptions: MountingOptions<FacilityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      facilityServiceStub = sinon.createStubInstance<FacilityService>(FacilityService);

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
          facilityService: () => facilityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        facilityServiceStub.find.resolves(facilitySample);
        route = {
          params: {
            facilityId: `${123}`,
          },
        };
        const wrapper = shallowMount(FacilityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.facility).toMatchObject(facilitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        facilityServiceStub.find.resolves(facilitySample);
        const wrapper = shallowMount(FacilityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

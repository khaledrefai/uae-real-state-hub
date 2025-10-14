import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PropertyMarkerDetails from './property-marker-details.vue';
import PropertyMarkerService from './property-marker.service';
import AlertService from '@/shared/alert/alert.service';

type PropertyMarkerDetailsComponentType = InstanceType<typeof PropertyMarkerDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const propertyMarkerSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PropertyMarker Management Detail Component', () => {
    let propertyMarkerServiceStub: SinonStubbedInstance<PropertyMarkerService>;
    let mountOptions: MountingOptions<PropertyMarkerDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      propertyMarkerServiceStub = sinon.createStubInstance<PropertyMarkerService>(PropertyMarkerService);

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
          propertyMarkerService: () => propertyMarkerServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        propertyMarkerServiceStub.find.resolves(propertyMarkerSample);
        route = {
          params: {
            propertyMarkerId: `${123}`,
          },
        };
        const wrapper = shallowMount(PropertyMarkerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.propertyMarker).toMatchObject(propertyMarkerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        propertyMarkerServiceStub.find.resolves(propertyMarkerSample);
        const wrapper = shallowMount(PropertyMarkerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

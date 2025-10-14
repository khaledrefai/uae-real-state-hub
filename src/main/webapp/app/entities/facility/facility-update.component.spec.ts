import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FacilityUpdate from './facility-update.vue';
import FacilityService from './facility.service';
import AlertService from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';

type FacilityUpdateComponentType = InstanceType<typeof FacilityUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const facilitySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<FacilityUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Facility Management Update Component', () => {
    let comp: FacilityUpdateComponentType;
    let facilityServiceStub: SinonStubbedInstance<FacilityService>;

    beforeEach(() => {
      route = {};
      facilityServiceStub = sinon.createStubInstance<FacilityService>(FacilityService);
      facilityServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          facilityService: () => facilityServiceStub,
          propertyService: () =>
            sinon.createStubInstance<PropertyService>(PropertyService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(FacilityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.facility = facilitySample;
        facilityServiceStub.update.resolves(facilitySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(facilityServiceStub.update.calledWith(facilitySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        facilityServiceStub.create.resolves(entity);
        const wrapper = shallowMount(FacilityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.facility = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(facilityServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        facilityServiceStub.find.resolves(facilitySample);
        facilityServiceStub.retrieve.resolves([facilitySample]);

        // WHEN
        route = {
          params: {
            facilityId: `${facilitySample.id}`,
          },
        };
        const wrapper = shallowMount(FacilityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.facility).toMatchObject(facilitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        facilityServiceStub.find.resolves(facilitySample);
        const wrapper = shallowMount(FacilityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

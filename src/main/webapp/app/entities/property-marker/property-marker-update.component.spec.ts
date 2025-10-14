import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PropertyMarkerUpdate from './property-marker-update.vue';
import PropertyMarkerService from './property-marker.service';
import AlertService from '@/shared/alert/alert.service';

type PropertyMarkerUpdateComponentType = InstanceType<typeof PropertyMarkerUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const propertyMarkerSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PropertyMarkerUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PropertyMarker Management Update Component', () => {
    let comp: PropertyMarkerUpdateComponentType;
    let propertyMarkerServiceStub: SinonStubbedInstance<PropertyMarkerService>;

    beforeEach(() => {
      route = {};
      propertyMarkerServiceStub = sinon.createStubInstance<PropertyMarkerService>(PropertyMarkerService);
      propertyMarkerServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          propertyMarkerService: () => propertyMarkerServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PropertyMarkerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.propertyMarker = propertyMarkerSample;
        propertyMarkerServiceStub.update.resolves(propertyMarkerSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(propertyMarkerServiceStub.update.calledWith(propertyMarkerSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        propertyMarkerServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PropertyMarkerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.propertyMarker = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(propertyMarkerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        propertyMarkerServiceStub.find.resolves(propertyMarkerSample);
        propertyMarkerServiceStub.retrieve.resolves([propertyMarkerSample]);

        // WHEN
        route = {
          params: {
            propertyMarkerId: `${propertyMarkerSample.id}`,
          },
        };
        const wrapper = shallowMount(PropertyMarkerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.propertyMarker).toMatchObject(propertyMarkerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        propertyMarkerServiceStub.find.resolves(propertyMarkerSample);
        const wrapper = shallowMount(PropertyMarkerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

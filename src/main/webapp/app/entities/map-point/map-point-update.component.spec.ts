import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MapPointUpdate from './map-point-update.vue';
import MapPointService from './map-point.service';
import AlertService from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';

type MapPointUpdateComponentType = InstanceType<typeof MapPointUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mapPointSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MapPointUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MapPoint Management Update Component', () => {
    let comp: MapPointUpdateComponentType;
    let mapPointServiceStub: SinonStubbedInstance<MapPointService>;

    beforeEach(() => {
      route = {};
      mapPointServiceStub = sinon.createStubInstance<MapPointService>(MapPointService);
      mapPointServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          mapPointService: () => mapPointServiceStub,
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
        const wrapper = shallowMount(MapPointUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mapPoint = mapPointSample;
        mapPointServiceStub.update.resolves(mapPointSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mapPointServiceStub.update.calledWith(mapPointSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        mapPointServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MapPointUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mapPoint = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mapPointServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        mapPointServiceStub.find.resolves(mapPointSample);
        mapPointServiceStub.retrieve.resolves([mapPointSample]);

        // WHEN
        route = {
          params: {
            mapPointId: `${mapPointSample.id}`,
          },
        };
        const wrapper = shallowMount(MapPointUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.mapPoint).toMatchObject(mapPointSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mapPointServiceStub.find.resolves(mapPointSample);
        const wrapper = shallowMount(MapPointUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

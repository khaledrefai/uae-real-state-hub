import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UnitBlockUpdate from './unit-block-update.vue';
import UnitBlockService from './unit-block.service';
import AlertService from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';

type UnitBlockUpdateComponentType = InstanceType<typeof UnitBlockUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const unitBlockSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<UnitBlockUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('UnitBlock Management Update Component', () => {
    let comp: UnitBlockUpdateComponentType;
    let unitBlockServiceStub: SinonStubbedInstance<UnitBlockService>;

    beforeEach(() => {
      route = {};
      unitBlockServiceStub = sinon.createStubInstance<UnitBlockService>(UnitBlockService);
      unitBlockServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          unitBlockService: () => unitBlockServiceStub,
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
        const wrapper = shallowMount(UnitBlockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.unitBlock = unitBlockSample;
        unitBlockServiceStub.update.resolves(unitBlockSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(unitBlockServiceStub.update.calledWith(unitBlockSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        unitBlockServiceStub.create.resolves(entity);
        const wrapper = shallowMount(UnitBlockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.unitBlock = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(unitBlockServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        unitBlockServiceStub.find.resolves(unitBlockSample);
        unitBlockServiceStub.retrieve.resolves([unitBlockSample]);

        // WHEN
        route = {
          params: {
            unitBlockId: `${unitBlockSample.id}`,
          },
        };
        const wrapper = shallowMount(UnitBlockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.unitBlock).toMatchObject(unitBlockSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        unitBlockServiceStub.find.resolves(unitBlockSample);
        const wrapper = shallowMount(UnitBlockUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

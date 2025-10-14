import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UnitBlock from './unit-block.vue';
import UnitBlockService from './unit-block.service';
import AlertService from '@/shared/alert/alert.service';

type UnitBlockComponentType = InstanceType<typeof UnitBlock>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('UnitBlock Management Component', () => {
    let unitBlockServiceStub: SinonStubbedInstance<UnitBlockService>;
    let mountOptions: MountingOptions<UnitBlockComponentType>['global'];

    beforeEach(() => {
      unitBlockServiceStub = sinon.createStubInstance<UnitBlockService>(UnitBlockService);
      unitBlockServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          unitBlockService: () => unitBlockServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        unitBlockServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(UnitBlock, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(unitBlockServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.unitBlocks[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(UnitBlock, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(unitBlockServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: UnitBlockComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(UnitBlock, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        unitBlockServiceStub.retrieve.reset();
        unitBlockServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        unitBlockServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(unitBlockServiceStub.retrieve.called).toBeTruthy();
        expect(comp.unitBlocks[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(unitBlockServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        unitBlockServiceStub.retrieve.reset();
        unitBlockServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(unitBlockServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.unitBlocks[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(unitBlockServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        unitBlockServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeUnitBlock();
        await comp.$nextTick(); // clear components

        // THEN
        expect(unitBlockServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(unitBlockServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

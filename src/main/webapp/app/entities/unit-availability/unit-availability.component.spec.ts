import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UnitAvailability from './unit-availability.vue';
import UnitAvailabilityService from './unit-availability.service';
import AlertService from '@/shared/alert/alert.service';

type UnitAvailabilityComponentType = InstanceType<typeof UnitAvailability>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('UnitAvailability Management Component', () => {
    let unitAvailabilityServiceStub: SinonStubbedInstance<UnitAvailabilityService>;
    let mountOptions: MountingOptions<UnitAvailabilityComponentType>['global'];

    beforeEach(() => {
      unitAvailabilityServiceStub = sinon.createStubInstance<UnitAvailabilityService>(UnitAvailabilityService);
      unitAvailabilityServiceStub.retrieve.resolves({ headers: {} });

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
          unitAvailabilityService: () => unitAvailabilityServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        unitAvailabilityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(UnitAvailability, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(unitAvailabilityServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.unitAvailabilities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(UnitAvailability, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(unitAvailabilityServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: UnitAvailabilityComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(UnitAvailability, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        unitAvailabilityServiceStub.retrieve.reset();
        unitAvailabilityServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        unitAvailabilityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(unitAvailabilityServiceStub.retrieve.called).toBeTruthy();
        expect(comp.unitAvailabilities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(unitAvailabilityServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        unitAvailabilityServiceStub.retrieve.reset();
        unitAvailabilityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(unitAvailabilityServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.unitAvailabilities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(unitAvailabilityServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        unitAvailabilityServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeUnitAvailability();
        await comp.$nextTick(); // clear components

        // THEN
        expect(unitAvailabilityServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(unitAvailabilityServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

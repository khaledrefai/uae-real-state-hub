import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PropertyMarker from './property-marker.vue';
import PropertyMarkerService from './property-marker.service';
import AlertService from '@/shared/alert/alert.service';

type PropertyMarkerComponentType = InstanceType<typeof PropertyMarker>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PropertyMarker Management Component', () => {
    let propertyMarkerServiceStub: SinonStubbedInstance<PropertyMarkerService>;
    let mountOptions: MountingOptions<PropertyMarkerComponentType>['global'];

    beforeEach(() => {
      propertyMarkerServiceStub = sinon.createStubInstance<PropertyMarkerService>(PropertyMarkerService);
      propertyMarkerServiceStub.retrieve.resolves({ headers: {} });

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
          propertyMarkerService: () => propertyMarkerServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        propertyMarkerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PropertyMarker, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(propertyMarkerServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.propertyMarkers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(PropertyMarker, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(propertyMarkerServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PropertyMarkerComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PropertyMarker, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        propertyMarkerServiceStub.retrieve.reset();
        propertyMarkerServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        propertyMarkerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(propertyMarkerServiceStub.retrieve.called).toBeTruthy();
        expect(comp.propertyMarkers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(propertyMarkerServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        propertyMarkerServiceStub.retrieve.reset();
        propertyMarkerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(propertyMarkerServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.propertyMarkers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(propertyMarkerServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        propertyMarkerServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePropertyMarker();
        await comp.$nextTick(); // clear components

        // THEN
        expect(propertyMarkerServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(propertyMarkerServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ContactLead from './contact-lead.vue';
import ContactLeadService from './contact-lead.service';
import AlertService from '@/shared/alert/alert.service';

type ContactLeadComponentType = InstanceType<typeof ContactLead>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ContactLead Management Component', () => {
    let contactLeadServiceStub: SinonStubbedInstance<ContactLeadService>;
    let mountOptions: MountingOptions<ContactLeadComponentType>['global'];

    beforeEach(() => {
      contactLeadServiceStub = sinon.createStubInstance<ContactLeadService>(ContactLeadService);
      contactLeadServiceStub.retrieve.resolves({ headers: {} });

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
          contactLeadService: () => contactLeadServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        contactLeadServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ContactLead, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(contactLeadServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.contactLeads[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(ContactLead, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(contactLeadServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: ContactLeadComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ContactLead, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        contactLeadServiceStub.retrieve.reset();
        contactLeadServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        contactLeadServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(contactLeadServiceStub.retrieve.called).toBeTruthy();
        expect(comp.contactLeads[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(contactLeadServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        contactLeadServiceStub.retrieve.reset();
        contactLeadServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(contactLeadServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.contactLeads[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(contactLeadServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        contactLeadServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeContactLead();
        await comp.$nextTick(); // clear components

        // THEN
        expect(contactLeadServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(contactLeadServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

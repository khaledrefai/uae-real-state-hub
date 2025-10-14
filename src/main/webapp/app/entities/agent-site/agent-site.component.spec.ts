import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AgentSite from './agent-site.vue';
import AgentSiteService from './agent-site.service';
import AlertService from '@/shared/alert/alert.service';

type AgentSiteComponentType = InstanceType<typeof AgentSite>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AgentSite Management Component', () => {
    let agentSiteServiceStub: SinonStubbedInstance<AgentSiteService>;
    let mountOptions: MountingOptions<AgentSiteComponentType>['global'];

    beforeEach(() => {
      agentSiteServiceStub = sinon.createStubInstance<AgentSiteService>(AgentSiteService);
      agentSiteServiceStub.retrieve.resolves({ headers: {} });

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
          agentSiteService: () => agentSiteServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        agentSiteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AgentSite, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(agentSiteServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.agentSites[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(AgentSite, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(agentSiteServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: AgentSiteComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AgentSite, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        agentSiteServiceStub.retrieve.reset();
        agentSiteServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        agentSiteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(agentSiteServiceStub.retrieve.called).toBeTruthy();
        expect(comp.agentSites[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(agentSiteServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        agentSiteServiceStub.retrieve.reset();
        agentSiteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(agentSiteServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.agentSites[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(agentSiteServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        agentSiteServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAgentSite();
        await comp.$nextTick(); // clear components

        // THEN
        expect(agentSiteServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(agentSiteServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

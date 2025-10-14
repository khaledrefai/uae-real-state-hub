import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AgentProfile from './agent-profile.vue';
import AgentProfileService from './agent-profile.service';
import AlertService from '@/shared/alert/alert.service';

type AgentProfileComponentType = InstanceType<typeof AgentProfile>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AgentProfile Management Component', () => {
    let agentProfileServiceStub: SinonStubbedInstance<AgentProfileService>;
    let mountOptions: MountingOptions<AgentProfileComponentType>['global'];

    beforeEach(() => {
      agentProfileServiceStub = sinon.createStubInstance<AgentProfileService>(AgentProfileService);
      agentProfileServiceStub.retrieve.resolves({ headers: {} });

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
          agentProfileService: () => agentProfileServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        agentProfileServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AgentProfile, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(agentProfileServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.agentProfiles[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(AgentProfile, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(agentProfileServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: AgentProfileComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AgentProfile, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        agentProfileServiceStub.retrieve.reset();
        agentProfileServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        agentProfileServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(agentProfileServiceStub.retrieve.called).toBeTruthy();
        expect(comp.agentProfiles[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(agentProfileServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        agentProfileServiceStub.retrieve.reset();
        agentProfileServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(agentProfileServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.agentProfiles[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(agentProfileServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        agentProfileServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAgentProfile();
        await comp.$nextTick(); // clear components

        // THEN
        expect(agentProfileServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(agentProfileServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

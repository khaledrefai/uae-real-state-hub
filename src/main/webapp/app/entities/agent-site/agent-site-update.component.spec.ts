import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AgentSiteUpdate from './agent-site-update.vue';
import AgentSiteService from './agent-site.service';
import AlertService from '@/shared/alert/alert.service';

import AgentProfileService from '@/entities/agent-profile/agent-profile.service';
import SubscriptionPlanService from '@/entities/subscription-plan/subscription-plan.service';

type AgentSiteUpdateComponentType = InstanceType<typeof AgentSiteUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const agentSiteSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AgentSiteUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AgentSite Management Update Component', () => {
    let comp: AgentSiteUpdateComponentType;
    let agentSiteServiceStub: SinonStubbedInstance<AgentSiteService>;

    beforeEach(() => {
      route = {};
      agentSiteServiceStub = sinon.createStubInstance<AgentSiteService>(AgentSiteService);
      agentSiteServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          agentSiteService: () => agentSiteServiceStub,
          agentProfileService: () =>
            sinon.createStubInstance<AgentProfileService>(AgentProfileService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          subscriptionPlanService: () =>
            sinon.createStubInstance<SubscriptionPlanService>(SubscriptionPlanService, {
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
        const wrapper = shallowMount(AgentSiteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.agentSite = agentSiteSample;
        agentSiteServiceStub.update.resolves(agentSiteSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(agentSiteServiceStub.update.calledWith(agentSiteSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        agentSiteServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AgentSiteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.agentSite = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(agentSiteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        agentSiteServiceStub.find.resolves(agentSiteSample);
        agentSiteServiceStub.retrieve.resolves([agentSiteSample]);

        // WHEN
        route = {
          params: {
            agentSiteId: `${agentSiteSample.id}`,
          },
        };
        const wrapper = shallowMount(AgentSiteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.agentSite).toMatchObject(agentSiteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        agentSiteServiceStub.find.resolves(agentSiteSample);
        const wrapper = shallowMount(AgentSiteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

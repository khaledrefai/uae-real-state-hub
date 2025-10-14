import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AgentProfileUpdate from './agent-profile-update.vue';
import AgentProfileService from './agent-profile.service';
import AlertService from '@/shared/alert/alert.service';

type AgentProfileUpdateComponentType = InstanceType<typeof AgentProfileUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const agentProfileSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AgentProfileUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AgentProfile Management Update Component', () => {
    let comp: AgentProfileUpdateComponentType;
    let agentProfileServiceStub: SinonStubbedInstance<AgentProfileService>;

    beforeEach(() => {
      route = {};
      agentProfileServiceStub = sinon.createStubInstance<AgentProfileService>(AgentProfileService);
      agentProfileServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          agentProfileService: () => agentProfileServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AgentProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.agentProfile = agentProfileSample;
        agentProfileServiceStub.update.resolves(agentProfileSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(agentProfileServiceStub.update.calledWith(agentProfileSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        agentProfileServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AgentProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.agentProfile = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(agentProfileServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        agentProfileServiceStub.find.resolves(agentProfileSample);
        agentProfileServiceStub.retrieve.resolves([agentProfileSample]);

        // WHEN
        route = {
          params: {
            agentProfileId: `${agentProfileSample.id}`,
          },
        };
        const wrapper = shallowMount(AgentProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.agentProfile).toMatchObject(agentProfileSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        agentProfileServiceStub.find.resolves(agentProfileSample);
        const wrapper = shallowMount(AgentProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

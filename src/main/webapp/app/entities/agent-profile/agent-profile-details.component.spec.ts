import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AgentProfileDetails from './agent-profile-details.vue';
import AgentProfileService from './agent-profile.service';
import AlertService from '@/shared/alert/alert.service';

type AgentProfileDetailsComponentType = InstanceType<typeof AgentProfileDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const agentProfileSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AgentProfile Management Detail Component', () => {
    let agentProfileServiceStub: SinonStubbedInstance<AgentProfileService>;
    let mountOptions: MountingOptions<AgentProfileDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      agentProfileServiceStub = sinon.createStubInstance<AgentProfileService>(AgentProfileService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          agentProfileService: () => agentProfileServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        agentProfileServiceStub.find.resolves(agentProfileSample);
        route = {
          params: {
            agentProfileId: `${123}`,
          },
        };
        const wrapper = shallowMount(AgentProfileDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.agentProfile).toMatchObject(agentProfileSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        agentProfileServiceStub.find.resolves(agentProfileSample);
        const wrapper = shallowMount(AgentProfileDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

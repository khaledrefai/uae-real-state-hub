import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AgentSiteDetails from './agent-site-details.vue';
import AgentSiteService from './agent-site.service';
import AlertService from '@/shared/alert/alert.service';

type AgentSiteDetailsComponentType = InstanceType<typeof AgentSiteDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const agentSiteSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AgentSite Management Detail Component', () => {
    let agentSiteServiceStub: SinonStubbedInstance<AgentSiteService>;
    let mountOptions: MountingOptions<AgentSiteDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      agentSiteServiceStub = sinon.createStubInstance<AgentSiteService>(AgentSiteService);

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
          agentSiteService: () => agentSiteServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        agentSiteServiceStub.find.resolves(agentSiteSample);
        route = {
          params: {
            agentSiteId: `${123}`,
          },
        };
        const wrapper = shallowMount(AgentSiteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.agentSite).toMatchObject(agentSiteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        agentSiteServiceStub.find.resolves(agentSiteSample);
        const wrapper = shallowMount(AgentSiteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

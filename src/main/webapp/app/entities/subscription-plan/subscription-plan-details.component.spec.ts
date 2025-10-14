import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionPlanDetails from './subscription-plan-details.vue';
import SubscriptionPlanService from './subscription-plan.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionPlanDetailsComponentType = InstanceType<typeof SubscriptionPlanDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionPlanSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SubscriptionPlan Management Detail Component', () => {
    let subscriptionPlanServiceStub: SinonStubbedInstance<SubscriptionPlanService>;
    let mountOptions: MountingOptions<SubscriptionPlanDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      subscriptionPlanServiceStub = sinon.createStubInstance<SubscriptionPlanService>(SubscriptionPlanService);

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
          subscriptionPlanService: () => subscriptionPlanServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscriptionPlanServiceStub.find.resolves(subscriptionPlanSample);
        route = {
          params: {
            subscriptionPlanId: `${123}`,
          },
        };
        const wrapper = shallowMount(SubscriptionPlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionPlan).toMatchObject(subscriptionPlanSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionPlanServiceStub.find.resolves(subscriptionPlanSample);
        const wrapper = shallowMount(SubscriptionPlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

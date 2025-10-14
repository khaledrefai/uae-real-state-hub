import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import BillingSubscriptionDetails from './billing-subscription-details.vue';
import BillingSubscriptionService from './billing-subscription.service';
import AlertService from '@/shared/alert/alert.service';

type BillingSubscriptionDetailsComponentType = InstanceType<typeof BillingSubscriptionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const billingSubscriptionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('BillingSubscription Management Detail Component', () => {
    let billingSubscriptionServiceStub: SinonStubbedInstance<BillingSubscriptionService>;
    let mountOptions: MountingOptions<BillingSubscriptionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      billingSubscriptionServiceStub = sinon.createStubInstance<BillingSubscriptionService>(BillingSubscriptionService);

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
          billingSubscriptionService: () => billingSubscriptionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        billingSubscriptionServiceStub.find.resolves(billingSubscriptionSample);
        route = {
          params: {
            billingSubscriptionId: `${123}`,
          },
        };
        const wrapper = shallowMount(BillingSubscriptionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.billingSubscription).toMatchObject(billingSubscriptionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        billingSubscriptionServiceStub.find.resolves(billingSubscriptionSample);
        const wrapper = shallowMount(BillingSubscriptionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

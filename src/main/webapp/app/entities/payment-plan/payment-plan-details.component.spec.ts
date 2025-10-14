import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentPlanDetails from './payment-plan-details.vue';
import PaymentPlanService from './payment-plan.service';
import AlertService from '@/shared/alert/alert.service';

type PaymentPlanDetailsComponentType = InstanceType<typeof PaymentPlanDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentPlanSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PaymentPlan Management Detail Component', () => {
    let paymentPlanServiceStub: SinonStubbedInstance<PaymentPlanService>;
    let mountOptions: MountingOptions<PaymentPlanDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      paymentPlanServiceStub = sinon.createStubInstance<PaymentPlanService>(PaymentPlanService);

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
          paymentPlanService: () => paymentPlanServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        paymentPlanServiceStub.find.resolves(paymentPlanSample);
        route = {
          params: {
            paymentPlanId: `${123}`,
          },
        };
        const wrapper = shallowMount(PaymentPlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.paymentPlan).toMatchObject(paymentPlanSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentPlanServiceStub.find.resolves(paymentPlanSample);
        const wrapper = shallowMount(PaymentPlanDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

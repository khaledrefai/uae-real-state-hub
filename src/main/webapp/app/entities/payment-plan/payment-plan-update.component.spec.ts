import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentPlanUpdate from './payment-plan-update.vue';
import PaymentPlanService from './payment-plan.service';
import AlertService from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';

type PaymentPlanUpdateComponentType = InstanceType<typeof PaymentPlanUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentPlanSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PaymentPlanUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PaymentPlan Management Update Component', () => {
    let comp: PaymentPlanUpdateComponentType;
    let paymentPlanServiceStub: SinonStubbedInstance<PaymentPlanService>;

    beforeEach(() => {
      route = {};
      paymentPlanServiceStub = sinon.createStubInstance<PaymentPlanService>(PaymentPlanService);
      paymentPlanServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          paymentPlanService: () => paymentPlanServiceStub,
          propertyService: () =>
            sinon.createStubInstance<PropertyService>(PropertyService, {
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
        const wrapper = shallowMount(PaymentPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentPlan = paymentPlanSample;
        paymentPlanServiceStub.update.resolves(paymentPlanSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentPlanServiceStub.update.calledWith(paymentPlanSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        paymentPlanServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PaymentPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentPlan = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentPlanServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        paymentPlanServiceStub.find.resolves(paymentPlanSample);
        paymentPlanServiceStub.retrieve.resolves([paymentPlanSample]);

        // WHEN
        route = {
          params: {
            paymentPlanId: `${paymentPlanSample.id}`,
          },
        };
        const wrapper = shallowMount(PaymentPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.paymentPlan).toMatchObject(paymentPlanSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentPlanServiceStub.find.resolves(paymentPlanSample);
        const wrapper = shallowMount(PaymentPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

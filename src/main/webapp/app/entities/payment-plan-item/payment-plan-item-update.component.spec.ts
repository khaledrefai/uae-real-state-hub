import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentPlanItemUpdate from './payment-plan-item-update.vue';
import PaymentPlanItemService from './payment-plan-item.service';
import AlertService from '@/shared/alert/alert.service';

import PaymentPlanService from '@/entities/payment-plan/payment-plan.service';

type PaymentPlanItemUpdateComponentType = InstanceType<typeof PaymentPlanItemUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentPlanItemSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PaymentPlanItemUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PaymentPlanItem Management Update Component', () => {
    let comp: PaymentPlanItemUpdateComponentType;
    let paymentPlanItemServiceStub: SinonStubbedInstance<PaymentPlanItemService>;

    beforeEach(() => {
      route = {};
      paymentPlanItemServiceStub = sinon.createStubInstance<PaymentPlanItemService>(PaymentPlanItemService);
      paymentPlanItemServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          paymentPlanItemService: () => paymentPlanItemServiceStub,
          paymentPlanService: () =>
            sinon.createStubInstance<PaymentPlanService>(PaymentPlanService, {
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
        const wrapper = shallowMount(PaymentPlanItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentPlanItem = paymentPlanItemSample;
        paymentPlanItemServiceStub.update.resolves(paymentPlanItemSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentPlanItemServiceStub.update.calledWith(paymentPlanItemSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        paymentPlanItemServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PaymentPlanItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentPlanItem = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentPlanItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        paymentPlanItemServiceStub.find.resolves(paymentPlanItemSample);
        paymentPlanItemServiceStub.retrieve.resolves([paymentPlanItemSample]);

        // WHEN
        route = {
          params: {
            paymentPlanItemId: `${paymentPlanItemSample.id}`,
          },
        };
        const wrapper = shallowMount(PaymentPlanItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.paymentPlanItem).toMatchObject(paymentPlanItemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentPlanItemServiceStub.find.resolves(paymentPlanItemSample);
        const wrapper = shallowMount(PaymentPlanItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

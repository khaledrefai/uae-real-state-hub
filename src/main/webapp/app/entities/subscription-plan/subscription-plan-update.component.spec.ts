import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionPlanUpdate from './subscription-plan-update.vue';
import SubscriptionPlanService from './subscription-plan.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionPlanUpdateComponentType = InstanceType<typeof SubscriptionPlanUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionPlanSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SubscriptionPlanUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SubscriptionPlan Management Update Component', () => {
    let comp: SubscriptionPlanUpdateComponentType;
    let subscriptionPlanServiceStub: SinonStubbedInstance<SubscriptionPlanService>;

    beforeEach(() => {
      route = {};
      subscriptionPlanServiceStub = sinon.createStubInstance<SubscriptionPlanService>(SubscriptionPlanService);
      subscriptionPlanServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          subscriptionPlanService: () => subscriptionPlanServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SubscriptionPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionPlan = subscriptionPlanSample;
        subscriptionPlanServiceStub.update.resolves(subscriptionPlanSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionPlanServiceStub.update.calledWith(subscriptionPlanSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        subscriptionPlanServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SubscriptionPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionPlan = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionPlanServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        subscriptionPlanServiceStub.find.resolves(subscriptionPlanSample);
        subscriptionPlanServiceStub.retrieve.resolves([subscriptionPlanSample]);

        // WHEN
        route = {
          params: {
            subscriptionPlanId: `${subscriptionPlanSample.id}`,
          },
        };
        const wrapper = shallowMount(SubscriptionPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionPlan).toMatchObject(subscriptionPlanSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionPlanServiceStub.find.resolves(subscriptionPlanSample);
        const wrapper = shallowMount(SubscriptionPlanUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

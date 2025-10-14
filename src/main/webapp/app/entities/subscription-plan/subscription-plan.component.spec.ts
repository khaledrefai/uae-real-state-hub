import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SubscriptionPlan from './subscription-plan.vue';
import SubscriptionPlanService from './subscription-plan.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionPlanComponentType = InstanceType<typeof SubscriptionPlan>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SubscriptionPlan Management Component', () => {
    let subscriptionPlanServiceStub: SinonStubbedInstance<SubscriptionPlanService>;
    let mountOptions: MountingOptions<SubscriptionPlanComponentType>['global'];

    beforeEach(() => {
      subscriptionPlanServiceStub = sinon.createStubInstance<SubscriptionPlanService>(SubscriptionPlanService);
      subscriptionPlanServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          subscriptionPlanService: () => subscriptionPlanServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscriptionPlanServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SubscriptionPlan, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(subscriptionPlanServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.subscriptionPlans[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(SubscriptionPlan, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(subscriptionPlanServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SubscriptionPlanComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SubscriptionPlan, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        subscriptionPlanServiceStub.retrieve.reset();
        subscriptionPlanServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        subscriptionPlanServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(subscriptionPlanServiceStub.retrieve.called).toBeTruthy();
        expect(comp.subscriptionPlans[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(subscriptionPlanServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        subscriptionPlanServiceStub.retrieve.reset();
        subscriptionPlanServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(subscriptionPlanServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.subscriptionPlans[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(subscriptionPlanServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        subscriptionPlanServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSubscriptionPlan();
        await comp.$nextTick(); // clear components

        // THEN
        expect(subscriptionPlanServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(subscriptionPlanServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

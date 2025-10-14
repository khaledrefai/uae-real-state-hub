import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import BillingSubscription from './billing-subscription.vue';
import BillingSubscriptionService from './billing-subscription.service';
import AlertService from '@/shared/alert/alert.service';

type BillingSubscriptionComponentType = InstanceType<typeof BillingSubscription>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('BillingSubscription Management Component', () => {
    let billingSubscriptionServiceStub: SinonStubbedInstance<BillingSubscriptionService>;
    let mountOptions: MountingOptions<BillingSubscriptionComponentType>['global'];

    beforeEach(() => {
      billingSubscriptionServiceStub = sinon.createStubInstance<BillingSubscriptionService>(BillingSubscriptionService);
      billingSubscriptionServiceStub.retrieve.resolves({ headers: {} });

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
          billingSubscriptionService: () => billingSubscriptionServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        billingSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(BillingSubscription, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(billingSubscriptionServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.billingSubscriptions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(BillingSubscription, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(billingSubscriptionServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: BillingSubscriptionComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(BillingSubscription, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        billingSubscriptionServiceStub.retrieve.reset();
        billingSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        billingSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(billingSubscriptionServiceStub.retrieve.called).toBeTruthy();
        expect(comp.billingSubscriptions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(billingSubscriptionServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        billingSubscriptionServiceStub.retrieve.reset();
        billingSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(billingSubscriptionServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.billingSubscriptions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(billingSubscriptionServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        billingSubscriptionServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeBillingSubscription();
        await comp.$nextTick(); // clear components

        // THEN
        expect(billingSubscriptionServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(billingSubscriptionServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});

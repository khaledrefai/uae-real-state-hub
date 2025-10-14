import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import BillingSubscriptionUpdate from './billing-subscription-update.vue';
import BillingSubscriptionService from './billing-subscription.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import AgentSiteService from '@/entities/agent-site/agent-site.service';

type BillingSubscriptionUpdateComponentType = InstanceType<typeof BillingSubscriptionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const billingSubscriptionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<BillingSubscriptionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('BillingSubscription Management Update Component', () => {
    let comp: BillingSubscriptionUpdateComponentType;
    let billingSubscriptionServiceStub: SinonStubbedInstance<BillingSubscriptionService>;

    beforeEach(() => {
      route = {};
      billingSubscriptionServiceStub = sinon.createStubInstance<BillingSubscriptionService>(BillingSubscriptionService);
      billingSubscriptionServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          billingSubscriptionService: () => billingSubscriptionServiceStub,
          agentSiteService: () =>
            sinon.createStubInstance<AgentSiteService>(AgentSiteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(BillingSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(BillingSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.billingSubscription = billingSubscriptionSample;
        billingSubscriptionServiceStub.update.resolves(billingSubscriptionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(billingSubscriptionServiceStub.update.calledWith(billingSubscriptionSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        billingSubscriptionServiceStub.create.resolves(entity);
        const wrapper = shallowMount(BillingSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.billingSubscription = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(billingSubscriptionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        billingSubscriptionServiceStub.find.resolves(billingSubscriptionSample);
        billingSubscriptionServiceStub.retrieve.resolves([billingSubscriptionSample]);

        // WHEN
        route = {
          params: {
            billingSubscriptionId: `${billingSubscriptionSample.id}`,
          },
        };
        const wrapper = shallowMount(BillingSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.billingSubscription).toMatchObject(billingSubscriptionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        billingSubscriptionServiceStub.find.resolves(billingSubscriptionSample);
        const wrapper = shallowMount(BillingSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

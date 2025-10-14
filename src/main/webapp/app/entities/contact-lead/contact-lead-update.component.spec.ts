import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import ContactLeadUpdate from './contact-lead-update.vue';
import ContactLeadService from './contact-lead.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import AgentSiteService from '@/entities/agent-site/agent-site.service';
import PropertyService from '@/entities/property/property.service';

type ContactLeadUpdateComponentType = InstanceType<typeof ContactLeadUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const contactLeadSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ContactLeadUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ContactLead Management Update Component', () => {
    let comp: ContactLeadUpdateComponentType;
    let contactLeadServiceStub: SinonStubbedInstance<ContactLeadService>;

    beforeEach(() => {
      route = {};
      contactLeadServiceStub = sinon.createStubInstance<ContactLeadService>(ContactLeadService);
      contactLeadServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          contactLeadService: () => contactLeadServiceStub,
          agentSiteService: () =>
            sinon.createStubInstance<AgentSiteService>(AgentSiteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
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

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(ContactLeadUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(ContactLeadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.contactLead = contactLeadSample;
        contactLeadServiceStub.update.resolves(contactLeadSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(contactLeadServiceStub.update.calledWith(contactLeadSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        contactLeadServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ContactLeadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.contactLead = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(contactLeadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        contactLeadServiceStub.find.resolves(contactLeadSample);
        contactLeadServiceStub.retrieve.resolves([contactLeadSample]);

        // WHEN
        route = {
          params: {
            contactLeadId: `${contactLeadSample.id}`,
          },
        };
        const wrapper = shallowMount(ContactLeadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.contactLead).toMatchObject(contactLeadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        contactLeadServiceStub.find.resolves(contactLeadSample);
        const wrapper = shallowMount(ContactLeadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

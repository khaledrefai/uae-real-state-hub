import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ContactLeadDetails from './contact-lead-details.vue';
import ContactLeadService from './contact-lead.service';
import AlertService from '@/shared/alert/alert.service';

type ContactLeadDetailsComponentType = InstanceType<typeof ContactLeadDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const contactLeadSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ContactLead Management Detail Component', () => {
    let contactLeadServiceStub: SinonStubbedInstance<ContactLeadService>;
    let mountOptions: MountingOptions<ContactLeadDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      contactLeadServiceStub = sinon.createStubInstance<ContactLeadService>(ContactLeadService);

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
          contactLeadService: () => contactLeadServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        contactLeadServiceStub.find.resolves(contactLeadSample);
        route = {
          params: {
            contactLeadId: `${123}`,
          },
        };
        const wrapper = shallowMount(ContactLeadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.contactLead).toMatchObject(contactLeadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        contactLeadServiceStub.find.resolves(contactLeadSample);
        const wrapper = shallowMount(ContactLeadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});

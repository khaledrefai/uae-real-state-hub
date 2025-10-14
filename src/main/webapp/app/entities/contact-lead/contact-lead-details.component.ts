import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ContactLeadService from './contact-lead.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat } from '@/shared/composables';
import { type IContactLead } from '@/shared/model/contact-lead.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ContactLeadDetails',
  setup() {
    const dateFormat = useDateFormat();
    const contactLeadService = inject('contactLeadService', () => new ContactLeadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const contactLead: Ref<IContactLead> = ref({});

    const retrieveContactLead = async contactLeadId => {
      try {
        const res = await contactLeadService().find(contactLeadId);
        contactLead.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.contactLeadId) {
      retrieveContactLead(route.params.contactLeadId);
    }

    return {
      ...dateFormat,
      alertService,
      contactLead,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});

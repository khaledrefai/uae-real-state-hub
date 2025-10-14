import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ContactLeadService from './contact-lead.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import AgentSiteService from '@/entities/agent-site/agent-site.service';
import { type IAgentSite } from '@/shared/model/agent-site.model';
import PropertyService from '@/entities/property/property.service';
import { type IProperty } from '@/shared/model/property.model';
import { ContactLead, type IContactLead } from '@/shared/model/contact-lead.model';
import { LeadStatus } from '@/shared/model/enumerations/lead-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ContactLeadUpdate',
  setup() {
    const contactLeadService = inject('contactLeadService', () => new ContactLeadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const contactLead: Ref<IContactLead> = ref(new ContactLead());

    const agentSiteService = inject('agentSiteService', () => new AgentSiteService());

    const agentSites: Ref<IAgentSite[]> = ref([]);

    const propertyService = inject('propertyService', () => new PropertyService());

    const properties: Ref<IProperty[]> = ref([]);
    const leadStatusValues: Ref<string[]> = ref(Object.keys(LeadStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveContactLead = async contactLeadId => {
      try {
        const res = await contactLeadService().find(contactLeadId);
        res.createdAt = new Date(res.createdAt);
        contactLead.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.contactLeadId) {
      retrieveContactLead(route.params.contactLeadId);
    }

    const initRelationships = () => {
      agentSiteService()
        .retrieve()
        .then(res => {
          agentSites.value = res.data;
        });
      propertyService()
        .retrieve()
        .then(res => {
          properties.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      email: {},
      phone: {},
      whatsapp: {},
      message: {},
      source: {},
      utm: {},
      createdAt: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      status: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      site: {},
      property: {},
    };
    const v$ = useVuelidate(validationRules, contactLead as any);
    v$.value.$validate();

    return {
      contactLeadService,
      alertService,
      contactLead,
      previousState,
      leadStatusValues,
      isSaving,
      currentLanguage,
      agentSites,
      properties,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: contactLead }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.contactLead.id) {
        this.contactLeadService()
          .update(this.contactLead)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.contactLead.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.contactLeadService()
          .create(this.contactLead)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.contactLead.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

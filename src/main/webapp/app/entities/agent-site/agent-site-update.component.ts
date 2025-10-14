import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AgentSiteService from './agent-site.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import AgentProfileService from '@/entities/agent-profile/agent-profile.service';
import { type IAgentProfile } from '@/shared/model/agent-profile.model';
import SubscriptionPlanService from '@/entities/subscription-plan/subscription-plan.service';
import { type ISubscriptionPlan } from '@/shared/model/subscription-plan.model';
import { AgentSite, type IAgentSite } from '@/shared/model/agent-site.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AgentSiteUpdate',
  setup() {
    const agentSiteService = inject('agentSiteService', () => new AgentSiteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const agentSite: Ref<IAgentSite> = ref(new AgentSite());

    const agentProfileService = inject('agentProfileService', () => new AgentProfileService());

    const agentProfiles: Ref<IAgentProfile[]> = ref([]);

    const subscriptionPlanService = inject('subscriptionPlanService', () => new SubscriptionPlanService());

    const subscriptionPlans: Ref<ISubscriptionPlan[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAgentSite = async agentSiteId => {
      try {
        const res = await agentSiteService().find(agentSiteId);
        agentSite.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.agentSiteId) {
      retrieveAgentSite(route.params.agentSiteId);
    }

    const initRelationships = () => {
      agentProfileService()
        .retrieve()
        .then(res => {
          agentProfiles.value = res.data;
        });
      subscriptionPlanService()
        .retrieve()
        .then(res => {
          subscriptionPlans.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      slug: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
      },
      displayName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      theme: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      primaryColor: {},
      secondaryColor: {},
      logoUrl: {},
      contactEmail: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      contactPhone: {},
      contactWhatsapp: {},
      domain: {},
      isActive: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      owner: {},
      plan: {},
    };
    const v$ = useVuelidate(validationRules, agentSite as any);
    v$.value.$validate();

    return {
      agentSiteService,
      alertService,
      agentSite,
      previousState,
      isSaving,
      currentLanguage,
      agentProfiles,
      subscriptionPlans,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.agentSite.id) {
        this.agentSiteService()
          .update(this.agentSite)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.agentSite.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.agentSiteService()
          .create(this.agentSite)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.agentSite.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

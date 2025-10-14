import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AgentProfileService from './agent-profile.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { AgentProfile, type IAgentProfile } from '@/shared/model/agent-profile.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AgentProfileUpdate',
  setup() {
    const agentProfileService = inject('agentProfileService', () => new AgentProfileService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const agentProfile: Ref<IAgentProfile> = ref(new AgentProfile());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAgentProfile = async agentProfileId => {
      try {
        const res = await agentProfileService().find(agentProfileId);
        agentProfile.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.agentProfileId) {
      retrieveAgentProfile(route.params.agentProfileId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      externalUserId: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      fullName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      companyName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      email: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      phone: {},
      whatsapp: {},
      country: {},
      city: {},
      website: {},
      active: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, agentProfile as any);
    v$.value.$validate();

    return {
      agentProfileService,
      alertService,
      agentProfile,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.agentProfile.id) {
        this.agentProfileService()
          .update(this.agentProfile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('uaeRealStateHubApp.agentProfile.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.agentProfileService()
          .create(this.agentProfile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('uaeRealStateHubApp.agentProfile.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

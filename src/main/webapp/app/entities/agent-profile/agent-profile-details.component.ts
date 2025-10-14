import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AgentProfileService from './agent-profile.service';
import { type IAgentProfile } from '@/shared/model/agent-profile.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AgentProfileDetails',
  setup() {
    const agentProfileService = inject('agentProfileService', () => new AgentProfileService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const agentProfile: Ref<IAgentProfile> = ref({});

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

    return {
      alertService,
      agentProfile,

      previousState,
      t$: useI18n().t,
    };
  },
});

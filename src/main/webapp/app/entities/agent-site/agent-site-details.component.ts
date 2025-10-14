import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AgentSiteService from './agent-site.service';
import { type IAgentSite } from '@/shared/model/agent-site.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AgentSiteDetails',
  setup() {
    const agentSiteService = inject('agentSiteService', () => new AgentSiteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const agentSite: Ref<IAgentSite> = ref({});

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

    return {
      alertService,
      agentSite,

      previousState,
      t$: useI18n().t,
    };
  },
});

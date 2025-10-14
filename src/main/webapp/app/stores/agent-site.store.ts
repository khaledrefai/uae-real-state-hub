import axios from 'axios';
import { computed, ref } from 'vue';
import { defineStore } from 'pinia';

import type { IAgentSite } from '@/shared/model/agent-site.model';
import type { IAgentProfile } from '@/shared/model/agent-profile.model';
import type { ISubscriptionPlan } from '@/shared/model/subscription-plan.model';

interface AgentSiteFilters {
  slug?: string;
  ownerId?: number;
}

const AGENT_SITE_API = 'api/agent-sites';
const PUBLIC_AGENT_SITE_API = 'api/public/agent-sites';
const AGENT_PROFILE_API = 'api/agent-profiles';
const SUBSCRIPTION_PLAN_API = 'api/subscription-plans';

const toSingleOrNull = <T>(items: T[]): T | null => (items.length > 0 ? items[0] : null);

export const useAgentSiteStore = defineStore('agentSite', () => {
  const site = ref<IAgentSite | null>(null);
  const owner = ref<IAgentProfile | null>(null);
  const availablePlans = ref<ISubscriptionPlan[]>([]);
  const loading = ref(false);
  const planLoading = ref(false);
  const lastFilters = ref<AgentSiteFilters>({});

  const isActive = computed(() => Boolean(site.value?.isActive));
  const hasOwner = computed(() => Boolean(site.value?.owner?.id ?? owner.value?.id));

  const buildFilterParams = (filters: AgentSiteFilters) => {
    const params: Record<string, unknown> = {};

    if (filters.slug) {
      params['slug.equals'] = filters.slug;
    }

    if (filters.ownerId) {
      params['ownerId.equals'] = filters.ownerId;
    }

    return params;
  };

  const fetchSite = async (filters: AgentSiteFilters) => {
    loading.value = true;
    lastFilters.value = { ...filters };

    try {
      if (filters.slug && !filters.ownerId) {
        try {
          const response = await axios.get<IAgentSite>(`${PUBLIC_AGENT_SITE_API}/${filters.slug}`);
          site.value = response.data;
        } catch (error) {
          site.value = null;
          owner.value = null;
          throw error;
        }

        owner.value = site.value?.owner ?? null;
        return site.value;
      }

      const response = await axios.get<IAgentSite[]>(AGENT_SITE_API, {
        params: {
          ...buildFilterParams(filters),
          page: 0,
          size: 1,
        },
      });

      site.value = toSingleOrNull(response.data);

      if (site.value?.owner?.id) {
        owner.value = site.value.owner;
      } else if (filters.ownerId) {
        await fetchOwner(filters.ownerId);
      } else {
        owner.value = null;
      }

      return site.value;
    } finally {
      loading.value = false;
    }
  };

  const fetchOwner = async (ownerId: number) => {
    const response = await axios.get<IAgentProfile>(`${AGENT_PROFILE_API}/${ownerId}`);
    owner.value = response.data;
    return owner.value;
  };

  const fetchPlans = async () => {
    if (availablePlans.value.length) {
      return availablePlans.value;
    }

    planLoading.value = true;

    try {
      const response = await axios.get<ISubscriptionPlan[]>(SUBSCRIPTION_PLAN_API, {
        params: {
          page: 0,
          size: 50,
          sort: ['price.asc'],
        },
        paramsSerializer: {
          indexes: null,
        },
      });

      availablePlans.value = response.data;
      return availablePlans.value;
    } finally {
      planLoading.value = false;
    }
  };

  const refresh = async () => {
    if (!lastFilters.value.slug && !lastFilters.value.ownerId && !site.value?.id) {
      return site.value;
    }

    const filters: AgentSiteFilters = { ...lastFilters.value };

    if (!filters.slug && site.value?.slug) {
      filters.slug = site.value.slug;
    }

    const inferredOwnerId = site.value?.owner?.id ?? owner.value?.id;
    if (!filters.ownerId && inferredOwnerId) {
      filters.ownerId = inferredOwnerId;
    }

    return fetchSite(filters);
  };

  const upsertSite = async (payload: Partial<IAgentSite>) => {
    if (site.value?.id) {
      const response = await axios.patch<IAgentSite>(`${AGENT_SITE_API}/${site.value.id}`, payload, {
        headers: {
          'Content-Type': 'application/merge-patch+json',
        },
      });
      site.value = response.data;
    } else {
      const response = await axios.post<IAgentSite>(AGENT_SITE_API, payload);
      site.value = response.data;
    }

    if (site.value?.owner) {
      owner.value = site.value.owner;
    }

    return site.value;
  };

  const setOwner = (profile: IAgentProfile | null) => {
    owner.value = profile;
    if (site.value) {
      site.value.owner = profile;
    }
  };

  return {
    site,
    owner,
    availablePlans,
    loading,
    planLoading,
    isActive,
    hasOwner,
    fetchSite,
    fetchOwner,
    fetchPlans,
    refresh,
    upsertSite,
    setOwner,
  };
});

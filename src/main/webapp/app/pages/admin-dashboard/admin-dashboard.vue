<template>
  <div class="admin-dashboard container py-4">
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4">
      <div>
        <h1 class="display-5 mb-2">Platform control centre</h1>
        <p class="text-muted mb-0">Monitor property ingestion, agent onboarding, and live storefronts.</p>
      </div>
      <button class="btn btn-outline-primary" type="button" @click="refresh" :disabled="loading">Refresh data</button>
    </div>

    <div class="row mb-4">
      <div class="col-md-3" v-for="metric in metrics" :key="metric.label">
        <div class="metric-card shadow-sm">
          <div class="metric-value">{{ metric.value }}</div>
          <div class="metric-label">{{ metric.label }}</div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-lg-8 mb-4">
        <div class="card shadow-sm h-100">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h2 class="h5 mb-0">Recent agent storefronts</h2>
            <router-link class="btn btn-sm btn-outline-secondary" to="/admin/agent-site">Manage all</router-link>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table mb-0">
                <thead>
                  <tr>
                    <th scope="col">Display name</th>
                    <th scope="col">Slug</th>
                    <th scope="col">Plan</th>
                    <th scope="col">Created</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="site in recentSites" :key="site.id">
                    <td>{{ site.displayName }}</td>
                    <td>/store/{{ site.slug }}</td>
                    <td>{{ site.plan?.name || '—' }}</td>
                    <td>{{ formatDate(site.createdDate) }}</td>
                  </tr>
                  <tr v-if="!recentSites.length">
                    <td colspan="4" class="text-center text-muted py-4">No storefronts launched yet.</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-4 mb-4">
        <div class="card shadow-sm h-100">
          <div class="card-header">
            <h2 class="h5 mb-0">Latest leads</h2>
          </div>
          <div class="card-body">
            <ul class="list-unstyled mb-0">
              <li v-for="lead in latestLeads" :key="lead.id" class="mb-3">
                <div class="font-weight-semibold">{{ lead.name }}</div>
                <div class="text-muted small">{{ lead.email || lead.phone || '—' }}</div>
                <div class="small">{{ formatRelative(lead.createdAt) }}</div>
              </li>
              <li v-if="!latestLeads.length" class="text-muted">No leads captured in the last 7 days.</li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div class="card shadow-sm">
      <div class="card-header d-flex justify-content-between align-items-center">
        <div>
          <h2 class="h5 mb-0">Daily Reely ingestion</h2>
          <small class="text-muted">Last run: {{ lastImportRelative }}</small>
        </div>
        <button class="btn btn-sm btn-primary" type="button" @click="triggerSync" :disabled="loading || syncing">
          Trigger manual sync
        </button>
      </div>
      <div class="card-body">
        <p class="mb-0 text-muted">
          The scheduler runs every morning at 06:00 Gulf Standard Time. Manual sync will enqueue a fresh pull immediately.
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import { computed, onMounted, ref } from 'vue';

import { useAlertService } from '@/shared/alert/alert.service';
import type { IAgentSite } from '@/shared/model/agent-site.model';
import type { IContactLead } from '@/shared/model/contact-lead.model';

dayjs.extend(relativeTime);

const alertService = useAlertService();

interface DashboardMetric {
  label: string;
  value: number | string;
}

interface PropertyRefreshResult {
  propertiesCreated: number;
  propertiesUpdated: number;
  propertiesSkipped: number;
  markersCreated: number;
  markersUpdated: number;
  syncedAt?: string;
}

const loading = ref(false);
const syncing = ref(false);
const metrics = ref<DashboardMetric[]>([
  { label: 'Properties', value: 0 },
  { label: 'Agent profiles', value: 0 },
  { label: 'Published storefronts', value: 0 },
  { label: 'Leads captured', value: 0 },
]);

const recentSites = ref<Array<IAgentSite & { createdDate?: string }>>([]);
const latestLeads = ref<IContactLead[]>([]);
const lastImport = ref<Date | null>(null);

const formatDate = (value?: string | Date) => (value ? dayjs(value).format('DD MMM YYYY') : '—');
const formatRelative = (value?: string | Date) => (value ? dayjs(value).fromNow() : '—');
const lastImportRelative = computed(() => (lastImport.value ? dayjs(lastImport.value).fromNow() : 'not yet recorded'));

const loadMetrics = async () => {
  loading.value = true;
  try {
    const [propertyCount, agentCount, siteCount, leadCount, sitesRes, leadsRes] = await Promise.all([
      axios.get<number>('api/properties/count'),
      axios.get<number>('api/agent-profiles/count'),
      axios.get<number>('api/agent-sites/count'),
      axios.get<number>('api/contact-leads/count'),
      axios.get<IAgentSite[]>('api/agent-sites', {
        params: {
          size: 5,
          sort: ['id,desc'],
        },
        paramsSerializer: {
          indexes: null,
        },
      }),
      axios.get<IContactLead[]>('api/contact-leads', {
        params: {
          size: 5,
          sort: ['createdAt,desc'],
        },
        paramsSerializer: {
          indexes: null,
        },
      }),
    ]);

    metrics.value = [
      { label: 'Properties', value: propertyCount.data },
      { label: 'Agent profiles', value: agentCount.data },
      { label: 'Published storefronts', value: siteCount.data },
      { label: 'Leads captured', value: leadCount.data },
    ];

    recentSites.value = sitesRes.data;
    latestLeads.value = leadsRes.data;
  } catch (error) {
    console.error('Failed to load admin metrics', error);
    if (error?.response) {
      alertService.showHttpError(error.response);
    } else {
      alertService.showError('Unable to fetch dashboard statistics.');
    }
  } finally {
    loading.value = false;
  }
};

const refresh = () => {
  void loadMetrics();
};

const triggerSync = async () => {
  syncing.value = true;
  try {
    const { data } = await axios.post<PropertyRefreshResult>('api/admin/reely/import');
    const summary = `Reely sync complete: ${data.propertiesCreated} created, ${data.propertiesUpdated} updated, ${data.markersCreated} new markers.`;
    alertService.showSuccess(summary);
    if (data.syncedAt) {
      lastImport.value = new Date(data.syncedAt);
    } else {
      lastImport.value = new Date();
    }
    await loadMetrics();
  } catch (error: any) {
    if (error?.response) {
      alertService.showHttpError(error.response);
    } else {
      alertService.showError('Unable to trigger manual sync.');
    }
  } finally {
    syncing.value = false;
  }
};

onMounted(async () => {
  await loadMetrics();
});
</script>

<style scoped>
.metric-card {
  background: #ffffff;
  border-radius: 1rem;
  padding: 1.5rem;
}

.metric-value {
  font-size: 2.2rem;
  font-weight: 700;
}

.metric-label {
  text-transform: uppercase;
  font-size: 0.8rem;
  letter-spacing: 0.08em;
  color: rgba(31, 44, 61, 0.65);
}
</style>

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
/* Clean Professional Admin Dashboard */
.admin-dashboard {
  min-height: 100vh;
  background: #f8f9fa;
  padding-bottom: 2rem;
}

/* Header */
.admin-dashboard h1 {
  color: #1a202c;
  font-weight: 700;
}

.admin-dashboard .text-muted {
  color: #718096 !important;
  font-size: 1rem;
}

/* Metric Cards */
.metric-card {
  background: white;
  border-radius: 0.75rem;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
}

.metric-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #6366f1;
}

.metric-value {
  font-size: 2rem;
  font-weight: 700;
  color: #6366f1;
  line-height: 1.2;
  margin-bottom: 0.25rem;
}

.metric-label {
  text-transform: uppercase;
  font-size: 0.75rem;
  letter-spacing: 0.05em;
  color: #718096;
  font-weight: 600;
}

/* Cards */
.card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 0.75rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease;
}

.card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  background: #f7fafc;
  border-bottom: 1px solid #e2e8f0;
  padding: 1rem 1.25rem;
  font-weight: 600;
}

.card-header h2 {
  color: #1a202c;
  font-size: 1.125rem;
  font-weight: 700;
}

.card-header small {
  color: #718096;
  font-weight: 500;
}

.card-body {
  padding: 1.25rem;
}

/* Buttons */
.btn {
  border-radius: 0.5rem;
  padding: 0.625rem 1.25rem;
  font-weight: 600;
  transition: all 0.2s ease;
  border: none;
  font-size: 0.875rem;
}

.btn-primary {
  background: #6366f1;
  color: white;
}

.btn-primary:hover {
  background: #4f46e5;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}

.btn-outline-primary {
  border: 1px solid white;
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.btn-outline-primary:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: white;
  color: white;
}

.btn-outline-secondary,
.btn-sm.btn-outline-secondary {
  border: 1px solid #cbd5e0;
  color: #4a5568;
  background: white;
}

.btn-outline-secondary:hover,
.btn-sm.btn-outline-secondary:hover {
  background: #f7fafc;
  border-color: #a0aec0;
  color: #2d3748;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Tables */
.table {
  background: transparent;
}

.table thead th {
  border-bottom: 2px solid #e2e8f0;
  color: #4a5568;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.75rem;
  letter-spacing: 0.05em;
  padding: 0.75rem 1rem;
  background: #f7fafc;
}

.table tbody tr {
  transition: all 0.2s ease;
  border-bottom: 1px solid #e2e8f0;
}

.table tbody tr:hover {
  background: #f7fafc;
}

.table td {
  vertical-align: middle;
  padding: 0.875rem 1rem;
  color: #2d3748;
  font-weight: 500;
}

.table .text-center {
  padding: 2rem;
}

/* List Styling */
.list-unstyled li {
  padding: 0.75rem 0;
  border-bottom: 1px solid #e2e8f0;
}

.list-unstyled li:last-child {
  border-bottom: none;
}

.font-weight-semibold {
  font-weight: 600;
  color: #1a202c;
  font-size: 0.95rem;
}

/* Typography */
.small {
  font-size: 0.875rem;
  color: #718096;
}

/* Responsive Design */
@media (max-width: 768px) {
  .metric-card {
    margin-bottom: 1rem;
  }

  .admin-dashboard h1 {
    font-size: 1.75rem;
  }

  .metric-value {
    font-size: 1.75rem;
  }

  .btn {
    width: 100%;
    margin-bottom: 0.5rem;
  }
}

/* Loading States */
.spinner-border {
  width: 2rem;
  height: 2rem;
  border-width: 0.25rem;
  color: #6366f1;
}
</style>

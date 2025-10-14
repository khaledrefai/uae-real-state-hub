<template>
  <div class="admin-import container py-4">
    <div class="mb-4">
      <h1 class="display-5 mb-2">Manual Reely import</h1>
      <p class="text-muted mb-0">
        Run an on-demand sync against the Reely feed to refresh properties and project markers. The job may take a few minutes depending on
        upstream latency.
      </p>
    </div>

    <div class="card shadow-sm">
      <div class="card-body p-4">
        <div class="d-flex flex-wrap align-items-center justify-content-between mb-3">
          <div>
            <h2 class="h5 mb-1">Trigger import</h2>
            <p class="text-muted mb-0">Only administrators can execute this job. A summary will be shown once the sync finishes.</p>
          </div>
          <button class="btn btn-primary" type="button" :disabled="loading" @click="triggerImport">
            <span v-if="loading" class="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>
            {{ loading ? 'Importing…' : 'Start import' }}
          </button>
        </div>

        <div v-if="lastResult" class="mt-4">
          <div class="alert alert-success" role="alert">
            <h3 class="h6 mb-2">Latest run finished {{ formatDate(lastResult.syncedAt) }}</h3>
            <p class="mb-1">Summary:</p>
            <ul class="mb-0 pl-3">
              <li>
                <strong>{{ lastResult.propertiesCreated }}</strong> properties created
              </li>
              <li>
                <strong>{{ lastResult.propertiesUpdated }}</strong> properties updated
              </li>
              <li>
                <strong>{{ lastResult.propertiesSkipped }}</strong> properties skipped
              </li>
              <li>
                <strong>{{ lastResult.markersCreated }}</strong> markers created
              </li>
              <li>
                <strong>{{ lastResult.markersUpdated }}</strong> markers updated
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import dayjs from 'dayjs';
import { ref } from 'vue';

import { useAlertService } from '@/shared/alert/alert.service';

interface PropertyRefreshResult {
  propertiesCreated: number;
  propertiesUpdated: number;
  propertiesSkipped: number;
  markersCreated: number;
  markersUpdated: number;
  syncedAt: string;
}

const alertService = useAlertService();
const loading = ref(false);
const lastResult = ref<PropertyRefreshResult | null>(null);

const triggerImport = async () => {
  if (loading.value) {
    return;
  }

  loading.value = true;
  try {
    const { data } = await axios.post<PropertyRefreshResult>('api/admin/reely/import');
    lastResult.value = data;
    alertService.showSuccess('Reely data import completed successfully.');
  } catch (error: any) {
    if (error?.response) {
      alertService.showHttpError(error.response);
    } else {
      alertService.showError('Unable to trigger the Reely import. Please try again.');
    }
  } finally {
    loading.value = false;
  }
};

const formatDate = (iso: string) => dayjs(iso).format('MMM D, YYYY HH:mm');
</script>

<style scoped>
.admin-import {
  max-width: 960px;
}
</style>

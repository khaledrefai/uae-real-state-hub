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
/* Modern Admin Import Page */
.admin-import {
  max-width: 1000px;
  min-height: 100vh;
  padding-top: 3rem;
  padding-bottom: 3rem;
}

.admin-import h1 {
  color: #2d3748;
  font-weight: 900;
  letter-spacing: -0.5px;
}

.admin-import .text-muted {
  color: #718096 !important;
  font-weight: 500;
  font-size: 1.05rem;
}

/* Modern Card */
.card {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 2px solid rgba(102, 126, 234, 0.1);
  border-radius: 1.75rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.12);
  overflow: hidden;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.18);
  transform: translateY(-4px);
}

.card-body {
  padding: 2.5rem !important;
}

.card-body h2 {
  color: #2d3748;
  font-weight: 800;
  margin-bottom: 0.5rem;
}

.card-body .text-muted {
  color: #718096 !important;
  font-weight: 500;
}

/* Modern Button */
.btn {
  border-radius: 0.875rem;
  padding: 0.875rem 2rem;
  font-weight: 700;
  transition: all 0.3s ease;
  border: none;
  position: relative;
  overflow: hidden;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-size: 0.9rem;
}

.btn::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition:
    width 0.6s,
    height 0.6s;
}

.btn:hover::before {
  width: 350px;
  height: 350px;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.5);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 0.6;
  }
  50% {
    opacity: 0.8;
  }
}

/* Spinner */
.spinner-border {
  width: 1.25rem;
  height: 1.25rem;
  border-width: 0.2rem;
}

.spinner-border-sm {
  width: 1rem;
  height: 1rem;
}

.mr-2 {
  margin-right: 0.5rem;
}

/* Alert Styling */
.alert {
  border-radius: 1.25rem;
  border: none;
  padding: 1.75rem 2rem;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.alert::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #11998e 0%, #38ef7d 100%);
}

.alert-success {
  background: linear-gradient(135deg, rgba(17, 153, 142, 0.08) 0%, rgba(56, 239, 125, 0.08) 100%);
  color: #0c5460;
  border-left: 4px solid #11998e;
}

.alert h3 {
  color: #0c5460;
  font-weight: 800;
  font-size: 1.1rem;
  margin-bottom: 1rem;
}

.alert p {
  font-weight: 600;
  margin-bottom: 0.75rem;
  color: #155724;
}

.alert ul {
  list-style: none;
  padding-left: 0;
  margin: 0;
}

.alert li {
  padding: 0.5rem 0;
  padding-left: 1.5rem;
  position: relative;
  color: #155724;
  font-weight: 500;
  transition: all 0.3s ease;
}

.alert li::before {
  content: '✓';
  position: absolute;
  left: 0;
  font-weight: 900;
  color: #11998e;
  font-size: 1.1rem;
}

.alert li:hover {
  transform: translateX(8px);
  color: #0c5460;
}

.alert strong {
  font-weight: 900;
  color: #11998e;
  font-size: 1.15rem;
}

/* Utility Classes */
.pl-3 {
  padding-left: 1rem;
}

.mb-0 {
  margin-bottom: 0;
}

.mb-1 {
  margin-bottom: 0.25rem;
}

.mb-2 {
  margin-bottom: 0.5rem;
}

.mb-3 {
  margin-bottom: 1rem;
}

.mb-4 {
  margin-bottom: 1.5rem;
}

.mt-4 {
  margin-top: 1.5rem;
}

.d-flex {
  display: flex;
}

.flex-wrap {
  flex-wrap: wrap;
}

.align-items-center {
  align-items: center;
}

.justify-content-between {
  justify-content: space-between;
}

/* Responsive Design */
@media (max-width: 768px) {
  .admin-import {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }

  .admin-import h1 {
    font-size: 2rem;
  }

  .card-body {
    padding: 1.75rem !important;
  }

  .btn {
    width: 100%;
    margin-top: 1rem;
  }

  .d-flex.flex-wrap {
    flex-direction: column;
    align-items: flex-start !important;
  }
}

/* Selection */
::selection {
  background: rgba(102, 126, 234, 0.3);
  color: #2d3748;
}
</style>

zx
<template>
  <div class="agent-dashboard container py-4">
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4">
      <div>
        <h1 class="display-5 mb-2">Agent dashboard</h1>
        <p class="text-muted mb-0">Manage your storefront, listings, and billing in one workspace.</p>
      </div>
      <router-link v-if="sitePublicUrl" class="btn btn-outline-primary" :to="sitePublicUrl"> View storefront </router-link>
    </div>

    <div class="row mb-4">
      <div class="col-md-4" v-for="metric in overviewMetrics" :key="metric.label">
        <div class="metric-card shadow-sm mb-3">
          <div class="metric-value">{{ metric.value }}</div>
          <div class="metric-label">{{ metric.label }}</div>
        </div>
      </div>
    </div>

    <ul class="nav nav-pills mb-4">
      <li class="nav-item" v-for="tab in tabs" :key="tab.id">
        <button class="nav-link" :class="{ active: activeTab === tab.id }" @click="activeTab = tab.id" type="button">
          {{ tab.label }}
        </button>
      </li>
    </ul>

    <section v-if="activeTab === 'overview'">
      <div class="card shadow-sm mb-4" v-if="isAdmin">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h2 class="h5 mb-0">Daily sync status</h2>
          <span class="badge badge-success" v-if="syncHealthy">On track</span>
          <span class="badge badge-warning" v-else>Check sync</span>
        </div>
        <div class="card-body">
          <p class="mb-3">
            The latest Reely import completed <strong>{{ lastSyncRelative }}</strong
            >. Review suggested updates before publishing to your storefront.
          </p>
          <button class="btn btn-outline-secondary" type="button" @click="refreshProperties" :disabled="loadingProperties">
            Refresh property catalogue
          </button>
        </div>
      </div>

      <div class="card shadow-sm">
        <div class="card-header">
          <h2 class="h5 mb-0">Recent leads</h2>
        </div>
        <div class="card-body">
          <p v-if="!recentLeads.length" class="text-muted mb-0">
            No leads captured yet. Your storefront contact forms feed straight into this list.
          </p>
          <div v-else class="table-responsive">
            <table class="table table-hover mb-0">
              <thead>
                <tr>
                  <th scope="col">Name</th>
                  <th scope="col">Email</th>
                  <th scope="col">Phone</th>
                  <th scope="col">Created</th>
                  <th scope="col">Status</th>
                  <th scope="col">Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="lead in recentLeads" :key="lead.id">
                  <td>{{ lead.name }}</td>
                  <td>{{ lead.email || '—' }}</td>
                  <td>{{ lead.phone || lead.whatsapp || '—' }}</td>
                  <td>{{ formatDate(lead.createdAt) }}</td>
                  <td>
                    <span
                      class="badge badge-pill"
                      :class="{
                        'badge-info': lead.status === 'NEW',
                        'badge-primary': lead.status === 'CONTACTED',
                        'badge-warning': lead.status === 'QUALIFIED',
                        'badge-success': lead.status === 'WON' || lead.status === 'RESOLVED',
                        'badge-secondary': lead.status === 'LOST',
                      }"
                    >
                      {{ lead.status || 'NEW' }}
                    </span>
                  </td>
                  <td>
                    <button class="btn btn-sm btn-outline-primary mr-1" @click="viewLeadDetails(lead)" title="View Details">
                      <i class="fa fa-eye"></i>
                    </button>
                    <button
                      v-if="lead.status !== 'RESOLVED'"
                      class="btn btn-sm btn-outline-success"
                      @click="markAsResolved(lead)"
                      title="Mark as Resolved"
                    >
                      <i class="fa fa-check"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>

    <section v-if="activeTab === 'branding'">
      <div class="card shadow-sm">
        <div class="card-header">
          <h2 class="h5 mb-0">Brand & contact details</h2>
        </div>
        <div class="card-body">
          <form @submit.prevent="saveBranding">
            <div class="form-row">
              <div class="form-group col-md-6">
                <label for="displayName">Display name</label>
                <input id="displayName" v-model.trim="siteForm.displayName" type="text" class="form-control" required />
              </div>
              <div class="form-group col-md-6">
                <label for="theme">Theme</label>
                <select id="theme" v-model="siteForm.theme" class="form-control">
                  <option value="modern">Modern</option>
                  <option value="minimal">Minimal</option>
                  <option value="luxury">Luxury</option>
                </select>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group col-md-6">
                <label for="primaryColor">Primary colour</label>
                <input id="primaryColor" v-model="siteForm.primaryColor" type="color" class="form-control" />
              </div>
              <div class="form-group col-md-6">
                <label for="secondaryColor">Secondary colour</label>
                <input id="secondaryColor" v-model="siteForm.secondaryColor" type="color" class="form-control" />
              </div>
            </div>
            <div class="form-group">
              <label for="logoUpload">Logo</label>
              <input id="logoUpload" type="file" accept="image/*" class="form-control-file" @change="onLogoSelected" />
              <small class="form-text text-muted">PNG or SVG recommended. Max 2MB.</small>
              <div v-if="siteForm.logoUrl" class="logo-preview mt-3">
                <img :src="siteForm.logoUrl" alt="Current logo" />
              </div>
            </div>
            <div class="form-row">
              <div class="form-group col-md-4">
                <label for="contactEmail">Contact email</label>
                <input id="contactEmail" v-model.trim="siteForm.contactEmail" type="email" class="form-control" required />
              </div>
              <div class="form-group col-md-4">
                <label for="contactPhone">Contact phone</label>
                <input id="contactPhone" v-model.trim="siteForm.contactPhone" type="tel" class="form-control" />
              </div>
              <div class="form-group col-md-4">
                <label for="contactWhatsapp">WhatsApp</label>
                <input id="contactWhatsapp" v-model.trim="siteForm.contactWhatsapp" type="tel" class="form-control" />
              </div>
            </div>
            <button class="btn btn-primary" type="submit" :disabled="savingBranding || uploadingLogo">
              <span v-if="savingBranding || uploadingLogo">Saving…</span>
              <span v-else>Save changes</span>
            </button>
          </form>
        </div>
      </div>
    </section>

    <section v-if="activeTab === 'properties'">
      <div class="card shadow-sm">
        <div class="card-header d-flex flex-wrap justify-content-between align-items-center">
          <h2 class="h5 mb-0">Property catalogue</h2>
          <div class="form-inline">
            <input
              v-model="propertySearch"
              type="search"
              class="form-control form-control-sm"
              placeholder="Search by name or city"
              @input="onPropertySearch"
            />
          </div>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-hover mb-0">
              <thead>
                <tr>
                  <th scope="col">Name</th>
                  <th scope="col">City</th>
                  <th scope="col">Listing type</th>
                  <th scope="col">Status</th>
                  <th scope="col">Min price</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="property in propertyStore.filteredItems" :key="property.id">
                  <td>{{ property.name }}</td>
                  <td>{{ property.city || '—' }}</td>
                  <td>{{ property.listingType || '—' }}</td>
                  <td>{{ property.status || '—' }}</td>
                  <td>
                    <span v-if="property.minPrice">AED {{ property.minPrice.toLocaleString() }}</span>
                    <span v-else>—</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>

    <section v-if="activeTab === 'billing'">
      <div class="card shadow-sm mb-4">
        <div class="card-header">
          <h2 class="h5 mb-0">Subscription status</h2>
        </div>
        <div class="card-body">
          <div v-if="loadingSubscription" class="text-center py-3">
            <div class="spinner-border text-primary" role="status">
              <span class="sr-only">Loading subscription...</span>
            </div>
          </div>
          <div v-else>
            <div class="row mb-3">
              <div class="col-md-6">
                <label class="font-weight-bold d-block">Current plan:</label>
                <p class="mb-2">
                  <strong class="text-primary">{{ site?.plan?.name || 'Free trial' }}</strong>
                  <span v-if="site?.plan?.features" class="text-muted ml-2">— {{ site.plan.features }}</span>
                </p>
              </div>
              <div class="col-md-6" v-if="currentSubscription">
                <label class="font-weight-bold d-block">Status:</label>
                <p class="mb-2">
                  <span
                    class="badge badge-pill"
                    :class="{
                      'badge-success': currentSubscription.status === 'ACTIVE',
                      'badge-warning': currentSubscription.status === 'PENDING',
                      'badge-danger': currentSubscription.status === 'CANCELLED' || currentSubscription.status === 'EXPIRED',
                      'badge-info': currentSubscription.status === 'TRIALING',
                    }"
                  >
                    {{ currentSubscription.status }}
                  </span>
                  <span v-if="currentSubscription.cancelAtPeriodEnd" class="badge badge-warning ml-2"> Cancels at period end </span>
                </p>
              </div>
            </div>

            <div v-if="currentSubscription" class="row mb-3">
              <div class="col-md-4">
                <label class="font-weight-bold d-block text-muted small">Start Date:</label>
                <p class="mb-0">{{ formatDate(currentSubscription.startDate) }}</p>
              </div>
              <div class="col-md-4" v-if="currentSubscription.endDate">
                <label class="font-weight-bold d-block text-muted small">End Date:</label>
                <p class="mb-0">{{ formatDate(currentSubscription.endDate) }}</p>
              </div>
              <div class="col-md-4" v-else>
                <label class="font-weight-bold d-block text-muted small">Renewal:</label>
                <p class="mb-0">Auto-renews monthly</p>
              </div>
            </div>

            <div v-if="!currentSubscription" class="alert alert-info mb-3">
              <i class="fa fa-info-circle mr-2"></i>
              You're currently on a free trial. Subscribe to a plan to unlock all features and publish your storefront.
            </div>

            <p class="text-muted mb-3">
              Manage payment methods, view invoices, and update your billing information through Stripe's secure portal.
            </p>
            <button
              class="btn btn-outline-primary"
              type="button"
              @click="openBillingPortal"
              :disabled="billingStore.processing || !currentSubscription"
            >
              <i class="fa fa-external-link-alt mr-2"></i>
              Open Stripe billing portal
            </button>
          </div>
        </div>
      </div>
      <div class="card shadow-sm">
        <div class="card-header">
          <h2 class="h5 mb-0">Available plans</h2>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-4 mb-3" v-for="plan in planOptions" :key="plan.id">
              <div class="plan-card" :class="{ current: site?.plan?.id === plan.id }">
                <div class="plan-name">{{ plan.name }}</div>
                <div class="plan-price">
                  {{ plan.currency || 'AED' }} {{ plan.priceMonthly }}<span class="text-muted small">/month</span>
                </div>
                <p class="plan-description">{{ plan.features || 'Contact us for details' }}</p>
                <div class="mb-2 text-muted small">
                  <i class="fa fa-check-circle text-success mr-1"></i>
                  Up to {{ plan.maxListings }} listings
                </div>
                <button
                  v-if="site?.plan?.id !== plan.id"
                  class="btn btn-sm btn-primary mt-2"
                  @click="changePlan(plan)"
                  :disabled="billingStore.processing"
                >
                  {{ currentSubscription ? 'Change to this plan' : 'Subscribe now' }}
                </button>
                <span v-else class="badge badge-success mt-2">Current plan</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <!-- Lead Details Modal -->
  <div v-if="selectedLead" class="modal fade show" style="display: block; background: rgba(0, 0, 0, 0.5)" @click.self="closeLeadModal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Lead Details</h5>
          <button type="button" class="close" @click="closeLeadModal">
            <span>&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row">
            <div class="col-md-6 mb-3">
              <label class="font-weight-bold">Name:</label>
              <p>{{ selectedLead.name }}</p>
            </div>
            <div class="col-md-6 mb-3">
              <label class="font-weight-bold">Email:</label>
              <p>{{ selectedLead.email || '—' }}</p>
            </div>
            <div class="col-md-6 mb-3">
              <label class="font-weight-bold">Phone:</label>
              <p>{{ selectedLead.phone || '—' }}</p>
            </div>
            <div class="col-md-6 mb-3">
              <label class="font-weight-bold">WhatsApp:</label>
              <p>{{ selectedLead.whatsapp || '—' }}</p>
            </div>
            <div class="col-md-6 mb-3">
              <label class="font-weight-bold">Source:</label>
              <p>{{ selectedLead.source || '—' }}</p>
            </div>
            <div class="col-md-6 mb-3">
              <label class="font-weight-bold">Created:</label>
              <p>{{ formatDate(selectedLead.createdAt) }}</p>
            </div>
            <div class="col-md-12 mb-3">
              <label class="font-weight-bold">Property:</label>
              <p>{{ selectedLead.property?.name || '—' }}</p>
            </div>
            <div class="col-md-12 mb-3">
              <label class="font-weight-bold">Message:</label>
              <p class="border rounded p-2" style="min-height: 60px; background-color: #f8f9fa">
                {{ selectedLead.message || 'No message provided' }}
              </p>
            </div>
            <div class="col-md-12 mb-3">
              <label class="font-weight-bold">Status:</label>
              <select v-model="selectedLeadStatus" class="form-control" @change="updateLeadStatus" :disabled="updatingStatus">
                <option value="NEW">New</option>
                <option value="CONTACTED">Contacted</option>
                <option value="QUALIFIED">Qualified</option>
                <option value="WON">Won</option>
                <option value="LOST">Lost</option>
                <option value="RESOLVED">Resolved</option>
              </select>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="closeLeadModal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import { computed, onMounted, reactive, ref, watch } from 'vue';

import { useAgentSiteStore, useBillingStore, usePropertyCatalogStore, useStore as useAccountStore } from '@/store';
import type { IAgentProfile } from '@/shared/model/agent-profile.model';
import type { IContactLead } from '@/shared/model/contact-lead.model';
import type { ISubscriptionPlan } from '@/shared/model/subscription-plan.model';
import type { IBillingSubscription } from '@/shared/model/billing-subscription.model';
import { useSupabaseStorage } from '@/shared/composables/use-supabase-storage';
import { useAlertService } from '@/shared/alert/alert.service';
import { Authority } from '@/shared/security/authority';

dayjs.extend(relativeTime);

const agentSiteStore = useAgentSiteStore();
const propertyStore = usePropertyCatalogStore();
propertyStore.setApiBase('api/properties');
const billingStore = useBillingStore();
const accountStore = useAccountStore();
const alertService = useAlertService();
const { upload: uploadAsset, uploading: uploadingLogo } = useSupabaseStorage();

const activeTab = ref<'overview' | 'branding' | 'properties' | 'billing'>('overview');
const tabs = [
  { id: 'overview' as const, label: 'Overview' },
  { id: 'branding' as const, label: 'Branding' },
  { id: 'properties' as const, label: 'Properties' },
  { id: 'billing' as const, label: 'Billing' },
];

const site = computed(() => agentSiteStore.site);
const sitePublicUrl = computed(() => (site.value?.slug ? `/store/${site.value.slug}` : null));

const siteForm = reactive({
  displayName: '',
  theme: 'modern',
  primaryColor: '#1E3C72',
  secondaryColor: '#3DB8C0',
  contactEmail: '',
  contactPhone: '',
  contactWhatsapp: '',
  logoUrl: '' as string | null,
});

watch(
  site,
  value => {
    if (!value) {
      return;
    }
    siteForm.displayName = value.displayName ?? '';
    siteForm.theme = value.theme ?? 'modern';
    siteForm.primaryColor = value.primaryColor ?? '#1E3C72';
    siteForm.secondaryColor = value.secondaryColor ?? '#3DB8C0';
    siteForm.contactEmail = value.contactEmail ?? accountStore.account?.email ?? '';
    siteForm.contactPhone = value.contactPhone ?? '';
    siteForm.contactWhatsapp = value.contactWhatsapp ?? '';
    siteForm.logoUrl = value.logoUrl ?? null;

    // Update CSS variables with agent's branding colors
    if (value.primaryColor) {
      document.documentElement.style.setProperty('--primary-color', value.primaryColor);
      // Create darker shade for hover states
      const primaryDark = adjustColorBrightness(value.primaryColor, -20);
      document.documentElement.style.setProperty('--primary-color-dark', primaryDark);
    }
    if (value.secondaryColor) {
      document.documentElement.style.setProperty('--secondary-color', value.secondaryColor);
    }
  },
  { immediate: true },
);

// Helper function to darken/lighten colors
const adjustColorBrightness = (color: string, percent: number): string => {
  const num = parseInt(color.replace('#', ''), 16);
  const amt = Math.round(2.55 * percent);
  const R = (num >> 16) + amt;
  const G = ((num >> 8) & 0x00ff) + amt;
  const B = (num & 0x0000ff) + amt;
  return (
    '#' +
    (
      0x1000000 +
      (R < 255 ? (R < 1 ? 0 : R) : 255) * 0x10000 +
      (G < 255 ? (G < 1 ? 0 : G) : 255) * 0x100 +
      (B < 255 ? (B < 1 ? 0 : B) : 255)
    )
      .toString(16)
      .slice(1)
  );
};

const pendingLogo = ref<File | null>(null);
const savingBranding = ref(false);
const loadingProperties = ref(false);
const leadCount = ref(0);
const recentLeads = ref<IContactLead[]>([]);
const lastSync = ref<Date | null>(null);
const currentSubscription = ref<IBillingSubscription | null>(null);
const loadingSubscription = ref(false);

// Lead details modal
const selectedLead = ref<IContactLead | null>(null);
const selectedLeadStatus = ref<string>('');
const updatingStatus = ref(false);

const planOptions = computed<ISubscriptionPlan[]>(() => agentSiteStore.availablePlans);

const overviewMetrics = computed(() => [
  { label: 'Published properties', value: propertyStore.totalItems },
  { label: 'Captured leads', value: leadCount.value },
  { label: 'Current plan', value: site.value?.plan?.name ?? 'Free trial' },
]);

const isAdmin = computed(() => accountStore.account?.authorities?.includes(Authority.ADMIN) ?? false);

const syncHealthy = computed(() => {
  if (!lastSync.value) {
    return false;
  }
  return dayjs().diff(lastSync.value, 'hour') < 24;
});

const lastSyncRelative = computed(() => (lastSync.value ? dayjs(lastSync.value).fromNow() : 'waiting for first sync'));

const formatDate = (value?: Date | string) => (value ? dayjs(value).format('DD MMM YYYY HH:mm') : '—');

const propertySearch = ref('');

const onPropertySearch = () => {
  propertyStore.setFilters({ search: propertySearch.value }, true);
};

const onLogoSelected = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input?.files?.length) {
    pendingLogo.value = input.files[0];
    const localUrl = URL.createObjectURL(pendingLogo.value);
    siteForm.logoUrl = localUrl;
  }
};

const loadLeadInsights = async (siteId: number) => {
  try {
    const [countResponse, listResponse] = await Promise.all([
      axios.get<number>('api/contact-leads/count', {
        params: {
          'siteId.equals': siteId,
        },
      }),
      axios.get<IContactLead[]>('api/contact-leads', {
        params: {
          'siteId.equals': siteId,
          size: 5,
          sort: ['createdAt,desc'],
        },
        paramsSerializer: {
          indexes: null,
        },
      }),
    ]);

    leadCount.value = countResponse.data;
    recentLeads.value = listResponse.data.map(lead => ({
      ...lead,
      createdAt: lead.createdAt ? new Date(lead.createdAt) : undefined,
    }));
  } catch (error) {
    console.warn('Failed to load lead insights', error);
  }
};

const loadBillingSubscription = async (siteId: number) => {
  loadingSubscription.value = true;
  try {
    const response = await axios.get<IBillingSubscription[]>('api/billing-subscriptions', {
      params: {
        'siteId.equals': siteId,
        'isActive.equals': true,
        size: 1,
        sort: ['startDate,desc'],
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    if (response.data && response.data.length > 0) {
      currentSubscription.value = {
        ...response.data[0],
        startDate: response.data[0].startDate ? new Date(response.data[0].startDate) : undefined,
        endDate: response.data[0].endDate ? new Date(response.data[0].endDate) : undefined,
      };
    } else {
      currentSubscription.value = null;
    }
  } catch (error) {
    console.warn('Failed to load billing subscription', error);
    currentSubscription.value = null;
  } finally {
    loadingSubscription.value = false;
  }
};

const viewLeadDetails = (lead: IContactLead) => {
  selectedLead.value = lead;
  selectedLeadStatus.value = lead.status || 'NEW';
};

const closeLeadModal = () => {
  selectedLead.value = null;
  selectedLeadStatus.value = '';
};

const updateLeadStatus = async () => {
  if (!selectedLead.value || !selectedLeadStatus.value) {
    return;
  }

  updatingStatus.value = true;
  try {
    await axios.patch(`api/contact-leads/${selectedLead.value.id}`, {
      ...selectedLead.value,
      status: selectedLeadStatus.value,
    });

    // Update the lead in the list
    const index = recentLeads.value.findIndex(l => l.id === selectedLead.value!.id);
    if (index !== -1) {
      recentLeads.value[index].status = selectedLeadStatus.value as any;
    }

    // Update the selected lead
    if (selectedLead.value) {
      selectedLead.value.status = selectedLeadStatus.value as any;
    }

    alertService.showSuccess('Lead status updated successfully');
  } catch (error) {
    alertService.showError('Failed to update lead status');
    console.error('Failed to update lead status', error);
  } finally {
    updatingStatus.value = false;
  }
};

const markAsResolved = async (lead: IContactLead) => {
  if (!confirm(`Mark "${lead.name}" as resolved?`)) {
    return;
  }

  try {
    await axios.patch(`api/contact-leads/${lead.id}`, {
      ...lead,
      status: 'RESOLVED',
    });

    // Update the lead in the list
    const index = recentLeads.value.findIndex(l => l.id === lead.id);
    if (index !== -1) {
      recentLeads.value[index].status = 'RESOLVED' as any;
    }

    alertService.showSuccess('Lead marked as resolved');
  } catch (error) {
    alertService.showError('Failed to update lead status');
    console.error('Failed to mark lead as resolved', error);
  }
};

const refreshProperties = async () => {
  loadingProperties.value = true;
  try {
    const { data } = await axios.post('api/properties/refresh');
    const created = Number(data?.propertiesCreated ?? 0);
    const updated = Number(data?.propertiesUpdated ?? 0);
    const skipped = Number(data?.propertiesSkipped ?? 0);
    const summary = `Created ${created} / Updated ${updated} / Skipped ${skipped}`;
    await propertyStore.fetchPage({ page: 0, size: 20 });
    alertService.showSuccess(`Property catalogue refreshed from external feed. ${summary}`);
  } catch (error: any) {
    console.error('Unable to refresh properties from external feed', error);
    if (error?.response) {
      alertService.showHttpError(error.response);
    } else {
      alertService.showError('Unable to refresh properties right now.');
    }
  } finally {
    loadingProperties.value = false;
  }
};

const saveBranding = async () => {
  if (!site.value?.id) {
    alertService.showError('Create your storefront first via onboarding.');
    return;
  }

  savingBranding.value = true;
  try {
    let logoUrl = site.value.logoUrl ?? null;
    if (pendingLogo.value) {
      const extension = pendingLogo.value.name.split('.').pop() ?? 'png';
      const path = `agent-sites/${site.value.id}/logo-${Date.now()}.${extension}`;
      logoUrl = await uploadAsset(path, pendingLogo.value, { contentType: pendingLogo.value.type });
      pendingLogo.value = null;
    }

    await agentSiteStore.upsertSite({
      id: site.value.id,
      slug: site.value.slug,
      displayName: siteForm.displayName,
      theme: siteForm.theme,
      primaryColor: siteForm.primaryColor,
      secondaryColor: siteForm.secondaryColor,
      logoUrl,
      contactEmail: siteForm.contactEmail,
      contactPhone: siteForm.contactPhone,
      contactWhatsapp: siteForm.contactWhatsapp,
      isActive: true,
      owner: site.value.owner ? { id: site.value.owner.id } : null,
      plan: site.value.plan ? { id: site.value.plan.id } : null,
    });

    alertService.showSuccess('Brand settings saved');
  } catch (error: any) {
    console.error('Unable to save branding', error);
    if (error?.response) {
      alertService.showHttpError(error.response);
    } else {
      alertService.showError('Unable to update branding at the moment.');
    }
  } finally {
    savingBranding.value = false;
  }
};

const openBillingPortal = async () => {
  try {
    await billingStore.openCustomerPortal();
  } catch (error) {
    alertService.showError(billingStore.error || 'Stripe portal is not available yet.');
  }
};

const changePlan = async (plan: ISubscriptionPlan) => {
  if (!site.value?.id) {
    alertService.showError('No site found. Please complete onboarding first.');
    return;
  }

  const action = currentSubscription.value ? 'change your subscription to' : 'subscribe to';
  const confirmed = confirm(`Are you sure you want to ${action} ${plan.name}?`);

  if (!confirmed) {
    return;
  }

  try {
    const origin = window.location.origin;
    await billingStore.redirectToCheckout({
      planId: plan.id!,
      agentSiteId: site.value.id,
      successUrl: `${origin}/agent/dashboard?tab=billing&checkout=success`,
      cancelUrl: `${origin}/agent/dashboard?tab=billing&checkout=cancelled`,
    });
  } catch (error) {
    console.error('Failed to initiate plan change', error);
    alertService.showError('Unable to start checkout. Please try again or contact support.');
  }
};

const loadAgentContext = async () => {
  const login = accountStore.account?.login;
  if (!login) {
    return;
  }

  try {
    const profileResponse = await axios.get<IAgentProfile[]>('api/agent-profiles', {
      params: {
        'externalUserId.equals': login,
        page: 0,
        size: 1,
      },
    });

    const profile = profileResponse.data[0] ?? null;
    if (profile) {
      agentSiteStore.setOwner(profile);
    }

    const siteRecord = await agentSiteStore.fetchSite({ ownerId: profile?.id });
    await propertyStore.fetchPage({ page: 0, size: 20 });

    if (siteRecord?.id) {
      await Promise.all([loadLeadInsights(siteRecord.id), loadBillingSubscription(siteRecord.id)]);
    }

    lastSync.value = new Date();
  } catch (error) {
    console.error('Failed to load agent context', error);
    if ((error as any)?.response) {
      alertService.showHttpError((error as any).response);
    }
  }
};

onMounted(async () => {
  await agentSiteStore.fetchPlans();
  await loadAgentContext();

  // Check for checkout callback status
  const urlParams = new URLSearchParams(window.location.search);
  const checkoutStatus = urlParams.get('checkout');
  const tab = urlParams.get('tab');

  if (checkoutStatus === 'success') {
    alertService.showSuccess('Subscription updated successfully! Your new plan is now active.');
    if (tab === 'billing') {
      activeTab.value = 'billing';
    }
    // Clean up URL
    window.history.replaceState({}, document.title, window.location.pathname);
  } else if (checkoutStatus === 'cancelled') {
    alertService.showInfo('Checkout was cancelled. Your subscription remains unchanged.');
    if (tab === 'billing') {
      activeTab.value = 'billing';
    }
    // Clean up URL
    window.history.replaceState({}, document.title, window.location.pathname);
  }
});
</script>

<style scoped>
/* Clean Professional Dashboard - Uses Agent's Branding Colors */
.agent-dashboard {
  min-height: 100vh;
  background: #f8f9fa;
  padding-bottom: 2rem;
}

/* Header */
.agent-dashboard h1 {
  color: #1a202c;
  font-weight: 700;
}

.agent-dashboard > .container > .d-flex > div > p {
  color: #718096;
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
  border-color: var(--primary-color, #3182ce);
}

.metric-value {
  font-size: 2rem;
  font-weight: 700;
  color: var(--primary-color, #3182ce);
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

/* Navigation Pills */
.nav-pills {
  background: white;
  padding: 0.5rem;
  border-radius: 0.75rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.nav-pills .nav-link {
  color: #4a5568;
  border-radius: 0.5rem;
  padding: 0.625rem 1.25rem;
  font-weight: 600;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.nav-pills .nav-link:hover {
  background: #f7fafc;
  color: var(--primary-color, #3182ce);
}

.nav-pills .nav-link.active {
  background: var(--primary-color, #3182ce);
  color: white;
  box-shadow: 0 2px 8px rgba(49, 130, 206, 0.3);
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

.card-body {
  padding: 1.25rem;
}

/* Forms */
.form-control {
  border-radius: 0.5rem;
  border: 1px solid #cbd5e0;
  padding: 0.625rem 0.875rem;
  transition: all 0.2s ease;
}

.form-control:focus {
  border-color: var(--primary-color, #3182ce);
  box-shadow: 0 0 0 3px rgba(49, 130, 206, 0.1);
  outline: none;
}

/* Buttons */
.btn {
  border-radius: 0.5rem;
  padding: 0.625rem 1.25rem;
  font-weight: 600;
  transition: all 0.2s ease;
  font-size: 0.875rem;
}

.btn-primary {
  background: var(--primary-color, #3182ce);
  border-color: var(--primary-color, #3182ce);
  color: white;
}

.btn-primary:hover {
  background: var(--primary-color-dark, #2c5aa0);
  border-color: var(--primary-color-dark, #2c5aa0);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(49, 130, 206, 0.3);
}

.btn-outline-primary {
  border: 1px solid var(--primary-color, #3182ce);
  color: var(--primary-color, #3182ce);
  background: transparent;
}

.btn-outline-primary:hover {
  background: var(--primary-color, #3182ce);
  color: white;
}

.btn-outline-secondary {
  border: 1px solid #cbd5e0;
  color: #4a5568;
  background: white;
}

.btn-outline-secondary:hover {
  background: #f7fafc;
  border-color: #a0aec0;
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
  padding: 0.75rem;
  background: #f7fafc;
}

.table tbody tr {
  transition: all 0.2s ease;
}

.table tbody tr:hover {
  background: #f7fafc;
}

.table td {
  vertical-align: middle;
  padding: 0.875rem 0.75rem;
  border-bottom: 1px solid #e2e8f0;
}

/* Badges */
.badge {
  padding: 0.375rem 0.75rem;
  border-radius: 0.375rem;
  font-weight: 600;
  font-size: 0.75rem;
}

.badge-success {
  background: #10b981;
  color: white;
}

.badge-info {
  background: #3b82f6;
  color: white;
}

.badge-warning {
  background: #f59e0b;
  color: white;
}

.badge-danger {
  background: #ef4444;
  color: white;
}

.badge-secondary {
  background: #6b7280;
  color: white;
}

.badge-primary {
  background: var(--primary-color, #3182ce);
  color: white;
}

/* Plan Cards */
.plan-card {
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 0.75rem;
  padding: 1.5rem;
  height: 100%;
  transition: all 0.2s ease;
}

.plan-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  border-color: var(--primary-color, #3182ce);
}

.plan-card.current {
  border-color: var(--primary-color, #3182ce);
  background: rgba(49, 130, 206, 0.05);
  box-shadow: 0 4px 12px rgba(49, 130, 206, 0.15);
}

.plan-name {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  color: #1a202c;
}

.plan-price {
  font-size: 2rem;
  font-weight: 700;
  color: var(--primary-color, #3182ce);
  margin-bottom: 0.75rem;
}

.plan-description {
  color: #718096;
  font-size: 0.875rem;
  margin-bottom: 1rem;
  line-height: 1.5;
}

/* Logo Preview */
.logo-preview img {
  max-height: 100px;
  border-radius: 0.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Modal */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1050;
  overflow-y: auto;
  background: rgba(0, 0, 0, 0.5);
}

.modal-dialog {
  margin: 1.75rem auto;
  max-width: 800px;
}

.modal-content {
  border-radius: 0.75rem;
  border: none;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  background: var(--primary-color, #3182ce);
  color: white;
  border-bottom: none;
  padding: 1.25rem 1.5rem;
  border-radius: 0.75rem 0.75rem 0 0;
}

.modal-title {
  font-weight: 700;
  font-size: 1.25rem;
}

.modal-body {
  padding: 1.5rem;
}

.modal-footer {
  background: #f7fafc;
  border-top: 1px solid #e2e8f0;
  padding: 1rem 1.5rem;
}

/* Alerts */
.alert {
  border-radius: 0.5rem;
  border: 1px solid;
  padding: 1rem 1.25rem;
}

.alert-info {
  background: #eff6ff;
  color: #1e40af;
  border-color: #bfdbfe;
}

/* Utility Classes */
.mr-1 {
  margin-right: 0.25rem;
}
.mr-2 {
  margin-right: 0.5rem;
}
.ml-2 {
  margin-left: 0.5rem;
}

.badge-pill {
  border-radius: 50px;
}

.spinner-border {
  width: 2rem;
  height: 2rem;
  border-width: 0.25rem;
}

/* Responsive */
@media (max-width: 768px) {
  .metric-card {
    margin-bottom: 1rem;
  }

  .nav-pills {
    flex-direction: column;
  }

  .nav-pills .nav-link {
    margin-bottom: 0.5rem;
  }

  .plan-card {
    margin-bottom: 1rem;
  }
}

/* Dynamic Color Variables - Will be set from agent's primaryColor/secondaryColor */
:root {
  --primary-color: #3182ce;
  --primary-color-dark: #2c5aa0;
  --secondary-color: #48bb78;
}
</style>

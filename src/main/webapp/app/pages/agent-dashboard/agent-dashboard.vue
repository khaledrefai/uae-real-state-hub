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
                </tr>
              </thead>
              <tbody>
                <tr v-for="lead in recentLeads" :key="lead.id">
                  <td>{{ lead.name }}</td>
                  <td>{{ lead.email || '—' }}</td>
                  <td>{{ lead.phone || lead.whatsapp || '—' }}</td>
                  <td>{{ formatDate(lead.createdAt) }}</td>
                  <td>
                    <span class="badge badge-pill badge-info">{{ lead.status || 'NEW' }}</span>
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
          <p class="mb-3">
            Current plan:
            <strong>{{ site?.plan?.name || 'Free trial' }}</strong>
            <span v-if="site?.plan?.description">— {{ site.plan.description }}</span>
          </p>
          <p class="text-muted">Manage payment methods, invoices, and upgrades directly through Stripe.</p>
          <button class="btn btn-outline-primary" type="button" @click="openBillingPortal" :disabled="billingStore.processing">
            Open Stripe billing portal
          </button>
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
                <div class="plan-price">AED {{ plan.price }}</div>
                <p class="plan-description">{{ plan.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
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
  },
  { immediate: true },
);

const pendingLogo = ref<File | null>(null);
const savingBranding = ref(false);
const loadingProperties = ref(false);
const leadCount = ref(0);
const recentLeads = ref<IContactLead[]>([]);
const lastSync = ref<Date | null>(null);

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
  } catch (error) {
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
  } catch (error) {
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
      await loadLeadInsights(siteRecord.id);
    }

    lastSync.value = new Date();
  } catch (error) {
    console.error('Failed to load agent context', error);
    if (error?.response) {
      alertService.showHttpError(error.response);
    }
  }
};

onMounted(async () => {
  await agentSiteStore.fetchPlans();
  await loadAgentContext();
});
</script>

<style scoped>
.metric-card {
  background: #ffffff;
  border-radius: 1rem;
  padding: 1.5rem;
}

.metric-value {
  font-size: 2rem;
  font-weight: 700;
}

.metric-label {
  text-transform: uppercase;
  font-size: 0.75rem;
  letter-spacing: 0.08em;
  color: rgba(27, 39, 53, 0.7);
}

.logo-preview img {
  max-height: 80px;
}

.plan-card {
  border: 1px solid #d6deeb;
  border-radius: 1rem;
  padding: 1.5rem;
  height: 100%;
}

.plan-card.current {
  border-color: #244a87;
  background: rgba(36, 74, 135, 0.08);
}
</style>

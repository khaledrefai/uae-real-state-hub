<template>
  <div v-if="loading" class="storefront-loading container py-5 text-center">
    <div class="spinner-border text-primary spinner-lg" role="status"></div>
    <p class="text-muted mt-3 mb-0">Preparing the storefront experience...</p>
  </div>
  <div v-else-if="notFound" class="container py-5 text-center">
    <h1 class="display-4 mb-3">Storefront not found</h1>
    <p class="lead text-muted mb-4">The requested agent site is no longer available.</p>
    <router-link class="btn btn-outline-primary" to="/">Back to home</router-link>
  </div>
  <div v-else class="storefront" :style="heroGradient">
    <header class="storefront-hero text-light">
      <div class="container py-5">
        <div class="row align-items-center">
          <div class="col-lg-8">
            <div v-if="site?.logoUrl" class="d-flex align-items-center mb-4">
              <img :src="site.logoUrl" alt="Agent logo" class="brand-logo mr-3" />
              <div>
                <h1 class="hero-title mb-1">{{ site.displayName }}</h1>
                <p class="mb-0 text-uppercase small">{{ themeTagline }}</p>
              </div>
            </div>
            <div v-else class="mb-4">
              <h1 class="hero-title mb-2">{{ site?.displayName }}</h1>
              <p class="lead mb-0">{{ themeTagline }}</p>
            </div>
            <div class="hero-metrics">
              <div class="hero-metric">
                <span class="hero-metric__label text-uppercase small">Active listings</span>
                <span class="hero-metric__value h3 mb-0">{{ formattedTotalItems }}</span>
              </div>
              <div v-if="cityCount" class="hero-metric">
                <span class="hero-metric__label text-uppercase small">Featured cities</span>
                <span class="hero-metric__value h3 mb-0">{{ cityCount }}</span>
              </div>
              <div v-if="averagePrice" class="hero-metric">
                <span class="hero-metric__label text-uppercase small">Avg starting price</span>
                <span class="hero-metric__value h3 mb-0">{{ averagePrice }}</span>
              </div>
            </div>
          </div>
          <div class="col-lg-4 text-lg-right mt-4 mt-lg-0">
            <div class="contact-actions">
              <a v-if="site?.contactWhatsapp" :href="whatsappUrl" class="btn btn-light btn-lg mb-2" target="_blank" rel="noopener">
                Message on WhatsApp
              </a>
              <a v-if="site?.contactEmail" :href="`mailto:${site.contactEmail}`" class="btn btn-outline-light d-block mb-2">
                Email our team
              </a>
              <a v-if="site?.contactPhone" :href="`tel:${site.contactPhone}`" class="btn btn-outline-light d-block"> Call us directly </a>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main class="storefront-main">
      <div class="container py-5">
        <div class="row">
          <div class="col-xl-9">
            <section class="filters card shadow-sm border-0 mb-4">
              <div class="card-header d-flex flex-wrap align-items-center justify-content-between">
                <div>
                  <h2 class="h5 mb-1">Find your next property</h2>
                  <small class="text-muted">Use filters to refine {{ formattedTotalItems }} listings.</small>
                </div>
                <button type="button" class="btn btn-link btn-sm p-0" @click="toggleAdvancedFilters">
                  {{ showAdvancedFilters ? 'Hide advanced filters' : 'Advanced filters' }}
                </button>
              </div>
              <div class="card-body">
                <form class="filter-grid" @submit.prevent>
                  <div class="form-group">
                    <label for="search">Search</label>
                    <input
                      id="search"
                      v-model="searchTerm"
                      type="search"
                      class="form-control"
                      placeholder="Palm Jumeirah, Downtown, waterfront..."
                    />
                  </div>
                  <div class="form-group">
                    <label for="country">Country</label>
                    <select id="country" v-model="selectedCountry" class="form-control">
                      <option value="">All countries</option>
                      <option v-for="country in propertyStore.countries" :key="country" :value="country">{{ country }}</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label for="city">City</label>
                    <select id="city" v-model="selectedCity" class="form-control">
                      <option value="">All cities</option>
                      <option v-for="city in propertyStore.cities" :key="city" :value="city">{{ city }}</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label for="listingType">Listing type</label>
                    <select id="listingType" v-model="selectedListingType" class="form-control">
                      <option value="">All types</option>
                      <option v-for="option in listingTypeOptions" :key="option.value" :value="option.value">
                        {{ option.label }}
                      </option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" v-model="selectedStatus" class="form-control">
                      <option value="">Any status</option>
                      <option v-for="option in statusOptions" :key="option.value" :value="option.value">
                        {{ option.label }}
                      </option>
                    </select>
                  </div>
                </form>

                <transition name="fade">
                  <div v-if="showAdvancedFilters" class="advanced-filter-grid mt-3">
                    <div class="form-group">
                      <label for="developer">Developer</label>
                      <input id="developer" v-model="selectedDeveloper" type="text" class="form-control" placeholder="Emaar, DAMAC..." />
                    </div>
                    <div class="form-group">
                      <label for="minPrice">Min price (AED)</label>
                      <input id="minPrice" v-model="minPrice" type="number" min="0" step="1000" class="form-control" placeholder="500000" />
                    </div>
                    <div class="form-group">
                      <label for="maxPrice">Max price (AED)</label>
                      <input
                        id="maxPrice"
                        v-model="maxPrice"
                        type="number"
                        min="0"
                        step="1000"
                        class="form-control"
                        placeholder="5000000"
                      />
                    </div>
                    <div class="form-group">
                      <label for="minArea">Min area (sq.ft)</label>
                      <input id="minArea" v-model="minArea" type="number" min="0" step="50" class="form-control" placeholder="800" />
                    </div>
                  </div>
                </transition>

                <div class="d-flex flex-wrap align-items-center justify-content-between mt-3">
                  <small class="text-muted mb-2 mb-sm-0">{{ resultSummary }}</small>
                  <div class="d-flex flex-wrap align-items-center">
                    <label class="mb-0 mr-2 text-muted small" for="pageSize">Results per page</label>
                    <select
                      id="pageSize"
                      v-model.number="pageSizeSelection"
                      class="custom-select custom-select-sm mr-3"
                      :disabled="propertyStore.loading"
                    >
                      <option v-for="size in pageSizeOptions" :key="size" :value="size">{{ size }}</option>
                    </select>
                    <button type="button" class="btn btn-outline-secondary btn-sm" @click="clearFilters" :disabled="propertyStore.loading">
                      Clear filters
                    </button>
                  </div>
                </div>
              </div>
            </section>

            <section aria-live="polite">
              <div v-if="propertyStore.loading" class="row">
                <div v-for="index in skeletonCardCount" :key="`skeleton-${index}`" class="col-md-6 mb-4">
                  <div class="property-card property-card--skeleton card border-0 shadow-sm">
                    <div class="property-card__media skeleton-block"></div>
                    <div class="card-body">
                      <div class="skeleton-line w-75 mb-3"></div>
                      <div class="skeleton-line w-50 mb-2"></div>
                      <div class="skeleton-line w-100 mb-4"></div>
                      <div class="d-flex justify-content-between">
                        <div class="skeleton-line w-50"></div>
                        <div class="skeleton-button"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else-if="displayProperties.length" class="row">
                <div v-for="property in displayProperties" :key="property.id" class="col-md-6 mb-4">
                  <div
                    class="property-card card h-100 shadow-sm border-0"
                    :class="{ 'property-card--highlight': highlightedPropertyId === property.id }"
                    @mouseenter="highlightedPropertyId = property.id ?? null"
                    @mouseleave="highlightedPropertyId = null"
                  >
                    <div class="property-card__media" :style="coverImage(property.coverUrl)">
                      <span v-if="property.listingType" class="badge badge-light property-card__badge">
                        {{ listingTypeLabel(property.listingType) }}
                      </span>
                    </div>
                    <div class="card-body d-flex flex-column">
                      <div class="d-flex justify-content-between align-items-start mb-2">
                        <h3 class="h5 mb-0">{{ property.name }}</h3>
                        <span v-if="property.status" class="badge badge-pill badge-primary text-uppercase small">
                          {{ statusLabel(property.status) }}
                        </span>
                      </div>
                      <p class="text-muted small mb-2">
                        <span v-if="property.city">{{ property.city }}</span>
                        <span v-if="property.city && property.country">, </span>
                        <span v-if="property.country">{{ property.country }}</span>
                      </p>
                      <p class="property-card__excerpt text-muted">
                        {{ propertyExcerpt(property) }}
                      </p>
                      <div class="mt-auto pt-3 d-flex justify-content-between align-items-end">
                        <div>
                          <div class="font-weight-bold">{{ priceLabel(property) }}</div>
                          <small v-if="property.minArea" class="text-muted">
                            From {{ property.minArea.toLocaleString() }} {{ property.areaUnit || 'sq.ft' }}
                          </small>
                        </div>
                        <router-link :to="propertyLink(property)" class="btn btn-outline-primary btn-sm"> View details </router-link>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else class="card border-0 shadow-sm text-center py-5">
                <div class="card-body">
                  <h2 class="h5 mb-2">No listings match your filters</h2>
                  <p class="text-muted mb-3">Try adjusting your filters or clearing them to browse all available properties.</p>
                  <button class="btn btn-primary" type="button" @click="clearFilters" :disabled="propertyStore.loading">
                    Reset filters
                  </button>
                </div>
              </div>
            </section>

            <nav v-if="totalPages > 1" class="mt-4">
              <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: propertyStore.activePage === 0 || propertyStore.loading }">
                  <button
                    class="page-link"
                    type="button"
                    @click="previousPage"
                    :disabled="propertyStore.activePage === 0 || propertyStore.loading"
                  >
                    Previous
                  </button>
                </li>
                <li v-for="page in paginationRange" :key="page" class="page-item" :class="{ active: page === propertyStore.activePage }">
                  <button class="page-link" type="button" @click="goToPage(page)" :disabled="propertyStore.loading">
                    {{ page + 1 }}
                  </button>
                </li>
                <li class="page-item" :class="{ disabled: propertyStore.activePage >= totalPages - 1 || propertyStore.loading }">
                  <button
                    class="page-link"
                    type="button"
                    @click="nextPage"
                    :disabled="propertyStore.activePage >= totalPages - 1 || propertyStore.loading"
                  >
                    Next
                  </button>
                </li>
              </ul>
            </nav>
          </div>

          <div class="col-xl-3">
            <aside class="storefront-sidebar">
              <div class="card shadow-sm border-0 mb-4">
                <div class="card-body">
                  <h2 class="h5 mb-3">Talk to us</h2>
                  <p class="text-muted mb-4">
                    {{ site?.displayName }} helps you secure the right property with local expertise and fast response times.
                  </p>
                  <ul class="list-unstyled small mb-4">
                    <li v-if="site?.contactPhone" class="mb-2">
                      <strong>Call:</strong> <a :href="`tel:${site.contactPhone}`">{{ site.contactPhone }}</a>
                    </li>
                    <li v-if="site?.contactEmail" class="mb-2">
                      <strong>Email:</strong> <a :href="`mailto:${site.contactEmail}`">{{ site.contactEmail }}</a>
                    </li>
                    <li v-if="site?.contactWhatsapp" class="mb-2">
                      <strong>WhatsApp:</strong>
                      <a :href="whatsappUrl" target="_blank" rel="noopener">Chat now</a>
                    </li>
                  </ul>
                  <div class="d-flex flex-wrap">
                    <a
                      v-if="site?.contactWhatsapp"
                      :href="whatsappUrl"
                      class="btn btn-success btn-sm mr-2 mb-2"
                      target="_blank"
                      rel="noopener"
                    >
                      Message us
                    </a>
                    <a v-if="site?.contactEmail" :href="`mailto:${site.contactEmail}`" class="btn btn-outline-primary btn-sm mb-2">
                      Email
                    </a>
                  </div>
                </div>
              </div>

              <div class="card shadow-sm border-0 mb-4" v-if="propertyStore.markers.length">
                <div class="card-body">
                  <h2 class="h6 text-uppercase text-muted mb-3">Explore on map</h2>
                  <map-view :markers="propertyStore.markers" :highlight-id="highlightedPropertyId" :link-base="propertyLinkBase"></map-view>
                </div>
              </div>

              <div class="card shadow-sm border-0">
                <div class="card-body">
                  <h2 class="h6 text-uppercase text-muted mb-3">Quick insights</h2>
                  <ul class="list-unstyled mb-0 small">
                    <li class="mb-2"><strong>Total listings:</strong> {{ formattedTotalItems }}</li>
                    <li class="mb-2" v-if="cityCount"><strong>Active cities:</strong> {{ cityCount }}</li>
                    <li class="mb-2" v-if="averagePrice"><strong>Avg starting price:</strong> {{ averagePrice }}</li>
                    <li class="mb-0"><strong>Updated:</strong> {{ lastUpdated }}</li>
                  </ul>
                </div>
              </div>
            </aside>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';

import { useAgentSiteStore, usePropertyCatalogStore } from '@/store';
import MapView from '@/pages/storefront/map-view.vue';
import { useAlertService } from '@/shared/alert/alert.service';

const route = useRoute();
const alertService = useAlertService();

const agentSiteStore = useAgentSiteStore();
const propertyStore = usePropertyCatalogStore();
propertyStore.setApiBase('api/public/properties');

const loading = ref(true);
const notFound = ref(false);

const searchTerm = ref('');
const selectedCountry = ref('');
const selectedCity = ref('');
const selectedListingType = ref('');
const selectedStatus = ref('');
const selectedDeveloper = ref('');
const minPrice = ref('');
const maxPrice = ref('');
const minArea = ref('');

const showAdvancedFilters = ref(false);
const highlightedPropertyId = ref<number | null>(null);

const pageSizeOptions = [12, 24, 36];
const skeletonCardCount = 6;

const site = computed(() => agentSiteStore.site);
const themeTagline = computed(() => (site.value?.theme ? `${site.value.theme} | UAE Property Specialists` : 'UAE Property Specialists'));

const heroGradient = computed(() => {
  if (!site.value?.primaryColor || !site.value?.secondaryColor) {
    return {};
  }
  return {
    background: `linear-gradient(135deg, ${site.value.primaryColor} 0%, ${site.value.secondaryColor} 100%)`,
  };
});

const whatsappUrl = computed(() => {
  if (!site.value?.contactWhatsapp) {
    return '#';
  }
  const digits = site.value.contactWhatsapp.replace(/[^0-9]+/g, '');
  return `https://wa.me/${digits}`;
});

const propertyLinkBase = computed(() => (site.value?.slug ? `/store/${site.value.slug}/properties` : ''));

const propertyLink = (property: { id?: number | null }) => {
  if (!property.id) {
    return '#';
  }
  return `${propertyLinkBase.value}/${property.id}`;
};

const coverImage = (url?: string | null) => ({
  backgroundImage: url ? `url(${url})` : 'linear-gradient(135deg, rgba(36,74,135,0.2), rgba(61,184,192,0.2))',
});

const listingTypeDictionary: Record<string, string> = {
  SALE: 'For sale',
  RENT: 'For rent',
  OFF_PLAN: 'Off-plan',
};

const listingTypeLabel = (value?: string | null) => {
  if (!value) {
    return '';
  }
  return listingTypeDictionary[value] ?? value.replace(/_/g, ' ').replace(/\b\w/g, letter => letter.toUpperCase());
};

const statusLabel = (value?: string | null) => {
  if (!value) {
    return '';
  }
  return value
    .toString()
    .split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

const priceLabel = (property: { minPrice?: number | null; maxPrice?: number | null; priceCurrency?: string | null }) => {
  const currency = property.priceCurrency ?? 'AED';
  const { minPrice: min, maxPrice: max } = property;
  if (typeof min === 'number' && typeof max === 'number' && min !== max) {
    return `${currency} ${min.toLocaleString()} - ${max.toLocaleString()}`;
  }
  if (typeof min === 'number') {
    return `${currency} ${min.toLocaleString()}`;
  }
  if (typeof max === 'number') {
    return `${currency} ${max.toLocaleString()}`;
  }
  return 'Price on request';
};

const propertyExcerpt = (property: { overviewMd?: string | null }) => {
  if (!property.overviewMd) {
    return 'Request more information to learn about this development.';
  }
  const clean = property.overviewMd.replace(/[#*`]/g, '').replace(/\s+/g, ' ').trim();
  if (clean.length <= 140) {
    return clean;
  }
  return `${clean.slice(0, 137)}...`;
};

const formattedTotalItems = computed(() => propertyStore.totalItems.toLocaleString());
const displayProperties = computed(() => propertyStore.filteredItems);
const cityCount = computed(() => propertyStore.cities.length);

const averagePriceValue = computed(() => {
  const prices = propertyStore.filteredItems
    .map(property => property.minPrice)
    .filter((value): value is number => typeof value === 'number');
  if (!prices.length) {
    return null;
  }
  const sum = prices.reduce((total, value) => total + value, 0);
  return Math.round(sum / prices.length);
});

const averagePrice = computed(() => {
  if (!averagePriceValue.value) {
    return '';
  }
  return `AED ${averagePriceValue.value.toLocaleString()}`;
});

const lastUpdated = computed(() => {
  const times = propertyStore.filteredItems
    .map(property => property.updatedAt ?? property.publishedAt ?? null)
    .filter((value): value is Date | string => Boolean(value))
    .map(value => new Date(value).getTime())
    .filter(time => Number.isFinite(time));

  if (!times.length) {
    return 'Today';
  }

  const latest = new Date(Math.max(...times));
  const today = new Date();
  if (latest.toDateString() === today.toDateString()) {
    return 'Today';
  }

  return new Intl.DateTimeFormat(undefined, {
    month: 'short',
    day: 'numeric',
  }).format(latest);
});

const totalPages = computed(() => {
  if (!propertyStore.pageSize) {
    return 1;
  }
  return Math.max(1, Math.ceil(propertyStore.totalItems / propertyStore.pageSize));
});

const paginationRange = computed(() => {
  const pages: number[] = [];
  const total = totalPages.value;
  const current = propertyStore.activePage;

  const windowSize = Math.min(5, total);
  let start = Math.max(0, current - Math.floor(windowSize / 2));
  let end = start + windowSize - 1;

  if (end >= total) {
    end = total - 1;
    start = Math.max(0, end - windowSize + 1);
  }

  for (let index = start; index <= end; index += 1) {
    pages.push(index);
  }

  return pages;
});

const resultSummary = computed(() => {
  const total = propertyStore.totalItems;
  if (!total) {
    return 'Showing 0 listings';
  }
  const start = propertyStore.activePage * propertyStore.pageSize + 1;
  const end = Math.min(total, (propertyStore.activePage + 1) * propertyStore.pageSize);
  return `Showing ${start}-${end} of ${total.toLocaleString()} listings`;
});

const defaultListingTypes = ['SALE', 'RENT', 'OFF_PLAN'];
const listingTypeOptions = computed(() => {
  const unique = new Set<string>();
  defaultListingTypes.forEach(type => unique.add(type));
  propertyStore.listingTypes.forEach(type => {
    if (type) {
      unique.add(type);
    }
  });
  return Array.from(unique).map(value => ({
    value,
    label: listingTypeLabel(value),
  }));
});

const statusOptions = computed(() =>
  propertyStore.statuses.map(value => ({
    value,
    label: statusLabel(value),
  })),
);

const toggleAdvancedFilters = () => {
  showAdvancedFilters.value = !showAdvancedFilters.value;
};

const toNumberOrUndefined = (value: string) => {
  const trimmed = value.trim();
  if (!trimmed.length) {
    return undefined;
  }
  const numeric = Number(trimmed);
  return Number.isFinite(numeric) ? numeric : undefined;
};

const updateFilters = () => {
  void propertyStore.setFilters(
    {
      search: searchTerm.value || undefined,
      country: selectedCountry.value || undefined,
      city: selectedCity.value || undefined,
      listingType: selectedListingType.value || undefined,
      status: selectedStatus.value || undefined,
      developer: selectedDeveloper.value || undefined,
      minPrice: toNumberOrUndefined(minPrice.value),
      maxPrice: toNumberOrUndefined(maxPrice.value),
      minArea: toNumberOrUndefined(minArea.value),
    },
    true,
  );
};

let filterUpdateHandle: ReturnType<typeof setTimeout> | undefined;
let suppressFilterWatch = false;

const scheduleFilterUpdate = () => {
  if (suppressFilterWatch) {
    return;
  }
  if (filterUpdateHandle) {
    clearTimeout(filterUpdateHandle);
  }
  filterUpdateHandle = window.setTimeout(() => {
    filterUpdateHandle = undefined;
    updateFilters();
  }, 250);
};

watch(
  [searchTerm, selectedCountry, selectedCity, selectedListingType, selectedStatus, selectedDeveloper, minPrice, maxPrice, minArea],
  () => {
    scheduleFilterUpdate();
  },
);

const clearFilters = async () => {
  suppressFilterWatch = true;
  searchTerm.value = '';
  selectedCountry.value = '';
  selectedCity.value = '';
  selectedListingType.value = '';
  selectedStatus.value = '';
  selectedDeveloper.value = '';
  minPrice.value = '';
  maxPrice.value = '';
  minArea.value = '';
  highlightedPropertyId.value = null;
  await nextTick();
  suppressFilterWatch = false;
  await propertyStore.resetFilters();
};

const goToPage = (page: number) => {
  if (page === propertyStore.activePage || page < 0 || page >= totalPages.value) {
    return;
  }
  void propertyStore.fetchPage({ page });
};

const previousPage = () => {
  goToPage(propertyStore.activePage - 1);
};

const nextPage = () => {
  goToPage(propertyStore.activePage + 1);
};

const pageSizeSelection = ref(propertyStore.pageSize);

const changePageSize = (size: number) => {
  if (!size || size === propertyStore.pageSize) {
    return;
  }
  void propertyStore.fetchPage({ page: 0, size });
};

watch(
  () => propertyStore.pageSize,
  size => {
    pageSizeSelection.value = size;
  },
  { immediate: true },
);

watch(pageSizeSelection, size => {
  changePageSize(size);
});

onBeforeUnmount(() => {
  if (filterUpdateHandle) {
    clearTimeout(filterUpdateHandle);
  }
});

onMounted(async () => {
  const slug = route.params.slug as string;
  try {
    const sitePromise = agentSiteStore.fetchSite({ slug });
    const propertiesPromise = propertyStore.fetchPage({ page: 0, size: propertyStore.pageSize });
    const [siteRecord] = await Promise.all([sitePromise, propertiesPromise]);
    if (!siteRecord) {
      notFound.value = true;
      return;
    }
  } catch (error) {
    console.error('Failed to load storefront', error);
    alertService.showError('We were unable to load this storefront.');
    notFound.value = true;
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.storefront {
  min-height: 100vh;
  background-color: #f3f6fb;
}

.storefront-loading .spinner-lg {
  width: 3rem;
  height: 3rem;
}

.storefront-hero {
  position: relative;
  background: linear-gradient(135deg, rgba(7, 21, 40, 0.92), rgba(13, 71, 161, 0.85));
  overflow: hidden;
}

.storefront-hero::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(18, 63, 120, 0.3), rgba(13, 35, 70, 0.6));
  mix-blend-mode: screen;
  pointer-events: none;
}

.storefront-hero > .container {
  position: relative;
  z-index: 1;
}

.hero-title {
  font-size: 3rem;
  font-weight: 700;
}

.brand-logo {
  height: 70px;
  width: 70px;
  object-fit: contain;
  border-radius: 1rem;
  background: rgba(255, 255, 255, 0.2);
  padding: 0.5rem;
}

.hero-metrics {
  display: flex;
  flex-wrap: wrap;
  margin: -0.5rem;
}

.hero-metric {
  backdrop-filter: blur(6px);
  background: rgba(255, 255, 255, 0.1);
  border-radius: 1.25rem;
  padding: 1rem 1.5rem;
  margin: 0.5rem;
  min-width: 150px;
}

.hero-metric__label {
  opacity: 0.85;
  letter-spacing: 0.08em;
}

.hero-metric__value {
  font-weight: 600;
}

.storefront-main {
  margin-top: -3rem;
}

.filters .card-header {
  background: rgba(243, 246, 251, 0.7);
}

.filter-grid {
  display: grid;
  gap: 1.25rem;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
}

.advanced-filter-grid {
  display: grid;
  gap: 1.25rem;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
}

.property-card {
  border-radius: 1.5rem;
  overflow: hidden;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.property-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 18px 32px rgba(15, 44, 88, 0.1);
}

.property-card--highlight {
  box-shadow: 0 18px 34px rgba(22, 82, 240, 0.18);
}

.property-card__media {
  height: 200px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.property-card__badge {
  position: absolute;
  top: 1rem;
  left: 1rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.property-card__excerpt {
  flex-grow: 1;
  line-height: 1.5;
}

.property-card--skeleton {
  background: linear-gradient(135deg, rgba(245, 247, 250, 0.9), rgba(235, 240, 250, 0.9));
}

.skeleton-block {
  height: 200px;
  background: linear-gradient(90deg, rgba(220, 229, 240, 0.6) 0%, rgba(229, 235, 244, 0.9) 50%, rgba(220, 229, 240, 0.6) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.6s infinite;
}

.skeleton-line {
  height: 12px;
  border-radius: 999px;
  background: linear-gradient(90deg, rgba(220, 229, 240, 0.6) 0%, rgba(230, 236, 245, 0.9) 50%, rgba(220, 229, 240, 0.6) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.6s infinite;
}

.skeleton-button {
  width: 72px;
  height: 28px;
  border-radius: 999px;
  background: linear-gradient(90deg, rgba(220, 229, 240, 0.6) 0%, rgba(230, 236, 245, 0.9) 50%, rgba(220, 229, 240, 0.6) 100%);
  background-size: 200% 100%;
  animation: shimmer 1.6s infinite;
}

@keyframes shimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

.storefront-sidebar {
  position: sticky;
  top: 2rem;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 767.98px) {
  .hero-title {
    font-size: 2.25rem;
  }

  .storefront-main {
    margin-top: 0;
  }

  .storefront-sidebar {
    position: static;
  }
}
</style>

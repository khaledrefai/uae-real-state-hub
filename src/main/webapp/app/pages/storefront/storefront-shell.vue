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
  <div v-else class="storefront">
    <header class="storefront-hero">
      <div class="hero-container">
        <!-- Top Bar: Logo and Name on Left, Contact Actions on Right -->
        <div class="hero-top-bar">
          <div class="hero-brand">
            <img v-if="site?.logoUrl" :src="site.logoUrl" alt="Agent logo" class="brand-logo" />
            <h1 class="brand-name">{{ site?.displayName }}</h1>
          </div>
          <div class="contact-actions">
            <a v-if="site?.contactWhatsapp" :href="whatsappUrl" class="btn btn-whatsapp" target="_blank" rel="noopener">
              <i class="fa fa-whatsapp"></i> WhatsApp
            </a>
            <a v-if="site?.contactEmail" :href="`mailto:${site.contactEmail}`" class="btn btn-email">
              <i class="fa fa-envelope"></i> Email
            </a>
            <a v-if="site?.contactPhone" :href="`tel:${site.contactPhone}`" class="btn btn-phone"> <i class="fa fa-phone"></i> Call </a>
          </div>
        </div>

        <!-- Metrics Row -->
        <div class="hero-metrics">
          <div class="hero-metric">
            <span class="hero-metric__value">{{ formattedTotalItems }}</span>
            <span class="hero-metric__label">Active Listings</span>
          </div>
          <div v-if="cityCount" class="hero-metric">
            <span class="hero-metric__value">{{ cityCount }}</span>
            <span class="hero-metric__label">Featured Cities</span>
          </div>
          <div v-if="averagePrice" class="hero-metric">
            <span class="hero-metric__value">{{ averagePrice }}</span>
            <span class="hero-metric__label">Avg Starting Price</span>
          </div>
        </div>
      </div>
    </header>

    <!-- Filters Section - Moved to top, after hero -->
    <section class="filters-section">
      <div class="filters-container">
        <div class="filters-header">
          <h2>Find Your Property</h2>
          <button type="button" class="btn-toggle-filters" @click="toggleAdvancedFilters">
            {{ showAdvancedFilters ? 'Hide Filters' : 'Show More Filters' }}
          </button>
        </div>

        <form class="filter-grid" @submit.prevent>
          <div class="form-group">
            <label for="listingType">Listing Type</label>
            <select id="listingType" v-model="selectedListingType" class="form-control">
              <option value="">All Types</option>
              <option v-for="option in listingTypeOptions" :key="option.value" :value="option.value">
                {{ option.label }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="status">Status</label>
            <select id="status" v-model="selectedStatus" class="form-control">
              <option value="">Any Status</option>
              <option v-for="option in statusOptions" :key="String(option.value)" :value="option.value">
                {{ option.label }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="minPrice">Min Price (AED)</label>
            <input id="minPrice" v-model="minPrice" type="number" min="0" step="1000" class="form-control" placeholder="500,000" />
          </div>
          <div class="form-group">
            <label for="maxPrice">Max Price (AED)</label>
            <input id="maxPrice" v-model="maxPrice" type="number" min="0" step="1000" class="form-control" placeholder="5,000,000" />
          </div>
        </form>

        <transition name="fade">
          <div v-if="showAdvancedFilters" class="advanced-filter-grid">
            <div class="form-group">
              <label for="area">Area / Neighborhood</label>
              <input
                id="area"
                v-model="selectedArea"
                type="text"
                list="area-suggestions"
                class="form-control"
                placeholder="Type to search..."
              />
              <datalist id="area-suggestions">
                <option v-for="areaOption in propertyStore.areas" :key="areaOption" :value="areaOption">{{ areaOption }}</option>
              </datalist>
            </div>
            <div class="form-group">
              <label for="developer">Developer</label>
              <input id="developer" v-model="selectedDeveloper" type="text" class="form-control" placeholder="Emaar, DAMAC..." />
            </div>
            <div class="form-group">
              <label for="dateFrom">Added From</label>
              <input id="dateFrom" v-model="dateFrom" type="date" class="form-control" />
            </div>
            <div class="form-group">
              <label for="dateTo">Added To</label>
              <input id="dateTo" v-model="dateTo" type="date" class="form-control" />
            </div>
            <div class="form-group">
              <label for="minArea">Min Area (sq.ft)</label>
              <input id="minArea" v-model="minArea" type="number" min="0" step="50" class="form-control" placeholder="800" />
            </div>
          </div>
        </transition>

        <div class="filters-footer">
          <small class="text-muted">{{ resultSummary }}</small>
          <div class="filters-actions">
            <label for="pageSize">Show:</label>
            <select id="pageSize" v-model.number="pageSizeSelection" class="form-control-sm" :disabled="propertyStore.loading">
              <option v-for="size in pageSizeOptions" :key="size" :value="size">{{ size }}</option>
            </select>
            <button type="button" class="btn btn-clear" @click="clearFilters" :disabled="propertyStore.loading">Clear Filters</button>
          </div>
        </div>
      </div>
    </section>

    <main class="storefront-main">
      <div class="main-container">
        <!-- Featured Carousels - Each in separate card -->
        <section class="carousels-section">
          <div class="carousel-card">
            <PropertyCarousel
              title="Recently Added"
              subtitle="Discover the latest properties added to our portfolio"
              :properties="recentCarouselItems"
              :loading="loadingCarousels"
              :link-base="propertyLinkBase"
            />
          </div>

          <div class="carousel-card">
            <PropertyCarousel
              title="Best Value"
              subtitle="Most affordable properties to fit your budget"
              :properties="valueCarouselItems"
              :loading="loadingCarousels"
              :link-base="propertyLinkBase"
            />
          </div>

          <div class="carousel-card">
            <PropertyCarousel
              title="Completing Soon"
              subtitle="Properties with the nearest completion dates"
              :properties="completionCarouselItems"
              :loading="loadingCarousels"
              :link-base="propertyLinkBase"
            />
          </div>
        </section>

        <!-- Property Grid -->
        <section class="properties-grid" aria-live="polite">
          <div v-if="propertyStore.loading" class="property-cards-grid">
            <div v-for="index in skeletonCardCount" :key="`skeleton-${index}`" class="property-card property-card--skeleton">
              <div class="property-card__media skeleton-block"></div>
              <div class="property-card__body">
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

          <div v-else-if="displayProperties.length" class="property-cards-grid">
            <div
              v-for="property in displayProperties"
              :key="property.id"
              class="property-card"
              :class="{ 'property-card--highlight': highlightedPropertyId === property.id }"
              @mouseenter="highlightedPropertyId = property.id ?? null"
              @mouseleave="highlightedPropertyId = null"
            >
              <div class="property-card__media" :style="coverImage(property.coverUrl)">
                <span v-if="property.listingType" class="property-card__badge">
                  {{ listingTypeLabel(property.listingType) }}
                </span>
              </div>
              <div class="property-card__body">
                <div class="d-flex justify-content-between align-items-start mb-2">
                  <h3 class="property-card__title">{{ property.name }}</h3>
                  <span v-if="property.status" class="badge badge-primary">
                    {{ statusLabel(property.status) }}
                  </span>
                </div>
                <p class="property-card__location">
                  <span v-if="property.city">{{ property.city }}</span>
                  <span v-if="property.city && property.country">, </span>
                  <span v-if="property.country">{{ property.country }}</span>
                </p>
                <p class="property-card__excerpt">
                  {{ propertyExcerpt(property) }}
                </p>
                <div class="property-card__footer">
                  <div>
                    <div class="property-card__price">{{ priceLabel(property) }}</div>
                    <small v-if="property.minArea" class="property-card__area">
                      From {{ property.minArea.toLocaleString() }} {{ property.areaUnit || 'sq.ft' }}
                    </small>
                  </div>
                  <router-link :to="propertyLink(property)" class="btn btn-primary btn-sm">View Details</router-link>
                </div>
              </div>
            </div>
          </div>

          <div v-else class="no-results">
            <h2>No listings match your filters</h2>
            <p>Try adjusting your filters or clearing them to browse all available properties.</p>
            <button class="btn btn-primary" type="button" @click="clearFilters" :disabled="propertyStore.loading">Reset Filters</button>
          </div>
        </section>

        <!-- Pagination -->
        <nav v-if="totalPages > 1" class="pagination-nav">
          <ul class="pagination">
            <li :class="{ disabled: propertyStore.activePage === 0 || propertyStore.loading }">
              <button type="button" @click="previousPage" :disabled="propertyStore.activePage === 0 || propertyStore.loading">
                Previous
              </button>
            </li>
            <li v-for="page in paginationRange" :key="page" :class="{ active: page === propertyStore.activePage }">
              <button type="button" @click="goToPage(page)" :disabled="propertyStore.loading">
                {{ page + 1 }}
              </button>
            </li>
            <li :class="{ disabled: propertyStore.activePage >= totalPages - 1 || propertyStore.loading }">
              <button type="button" @click="nextPage" :disabled="propertyStore.activePage >= totalPages - 1 || propertyStore.loading">
                Next
              </button>
            </li>
          </ul>
        </nav>
      </div>
    </main>
    <ai-chat-widget />
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch, watchEffect } from 'vue';
import { useRoute } from 'vue-router';

import AiChatWidget from '@/components/ai-chat-widget.vue';
import { useAgentSiteStore, usePropertyCatalogStore } from '@/store';
import MapView from '@/pages/storefront/map-view.vue';
import PropertyCarousel from '@/pages/storefront/property-carousel.vue';
import { useAlertService } from '@/shared/alert/alert.service';
import type { IProperty } from '@/shared/model/property.model';

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
const selectedArea = ref('');
const selectedListingType = ref('');
const selectedStatus = ref('');
const selectedDeveloper = ref('');
const minPrice = ref('');
const maxPrice = ref('');
const minArea = ref('');
const dateFrom = ref('');
const dateTo = ref('');

const showAdvancedFilters = ref(false);
const highlightedPropertyId = ref<number | null>(null);

type DateLike = string | Date | null | undefined;
const CAROUSEL_LIMIT = 5;
const catalogItems = computed(() => propertyStore.filteredItems);

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

// Helper function to darken colors for hover states
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

// Watch for site changes and update CSS variables for dynamic theming
watch(
  site,
  value => {
    if (value?.primaryColor) {
      document.documentElement.style.setProperty('--primary-color', value.primaryColor);
      const primaryDark = adjustColorBrightness(value.primaryColor, -20);
      document.documentElement.style.setProperty('--primary-color-dark', primaryDark);
    }
    if (value?.secondaryColor) {
      document.documentElement.style.setProperty('--secondary-color', value.secondaryColor);
    }
  },
  { immediate: true },
);

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

const toTimestamp = (value: DateLike): number => {
  if (!value) {
    return 0;
  }
  if (value instanceof Date) {
    const time = value.getTime();
    return Number.isFinite(time) ? time : 0;
  }
  if (typeof value === 'string') {
    const parsed = Date.parse(value);
    return Number.isFinite(parsed) ? parsed : 0;
  }
  return 0;
};

const coerceNumber = (value: unknown): number | undefined => {
  if (typeof value === 'number') {
    return Number.isFinite(value) ? value : undefined;
  }
  if (typeof value === 'string') {
    const normalized = value.trim().replace(/[, ]+/g, '');
    if (!normalized.length) {
      return undefined;
    }
    const parsed = Number(normalized);
    return Number.isFinite(parsed) ? parsed : undefined;
  }
  return undefined;
};

const comparablePrice = (property: IProperty): number | undefined => {
  const candidates: unknown[] = [property.minPrice, property.minPriceAed, property.maxPrice];
  for (const candidate of candidates) {
    const numeric = coerceNumber(candidate);
    if (numeric !== undefined) {
      return numeric;
    }
  }
  return undefined;
};

const dedupeProperties = (list: IProperty[]): IProperty[] => {
  const seenNumeric = new Set<number>();
  const seenFallback = new Set<string>();
  const result: IProperty[] = [];
  for (const property of list) {
    const numericKey = typeof property.id === 'number' ? property.id : typeof property.extId === 'number' ? property.extId : undefined;
    if (numericKey !== undefined) {
      if (seenNumeric.has(numericKey)) {
        continue;
      }
      seenNumeric.add(numericKey);
      result.push(property);
      continue;
    }
    const fallbackKey = property.name ?? '';
    if (fallbackKey && seenFallback.has(fallbackKey)) {
      continue;
    }
    if (fallbackKey) {
      seenFallback.add(fallbackKey);
    }
    result.push(property);
  }
  return result;
};

const getPublishedTimestamp = (property: IProperty): number =>
  toTimestamp(property.updatedAt as DateLike) || toTimestamp(property.publishedAt as DateLike);

const recentCarouselItems = ref<IProperty[]>([]);
const valueCarouselItems = ref<IProperty[]>([]);
const completionCarouselItems = ref<IProperty[]>([]);

watchEffect(() => {
  const items = catalogItems.value ?? [];
  console.log('[CAROUSEL WATCHEFFECT] catalogItems:', items.length);

  const recent = dedupeProperties([...items].sort((a, b) => getPublishedTimestamp(b) - getPublishedTimestamp(a))).slice(0, CAROUSEL_LIMIT);

  const rankedByPrice = items
    .map(property => {
      const price = comparablePrice(property);
      return price !== undefined ? { property, price } : null;
    })
    .filter((entry): entry is { property: IProperty; price: number } => entry !== null)
    .sort((a, b) => a.price - b.price)
    .map(entry => entry.property);

  const bestValue =
    rankedByPrice.length >= CAROUSEL_LIMIT
      ? rankedByPrice.slice(0, CAROUSEL_LIMIT)
      : dedupeProperties([...rankedByPrice, ...recent, ...items]).slice(0, CAROUSEL_LIMIT);

  const now = Date.now();
  const completionRanked = items
    .map(property => {
      const completion = toTimestamp(property.completionDateTime as DateLike);
      return completion > 0 ? { property, completion } : null;
    })
    .filter((entry): entry is { property: IProperty; completion: number } => entry !== null)
    .sort((a, b) => a.completion - b.completion);

  const upcoming = completionRanked.filter(entry => entry.completion >= now).map(entry => entry.property);
  const completionFallback = completionRanked.map(entry => entry.property);
  const completion = dedupeProperties([...upcoming, ...completionFallback, ...recent]).slice(0, CAROUSEL_LIMIT);

  recentCarouselItems.value = recent;
  valueCarouselItems.value = bestValue;
  completionCarouselItems.value = completion;

  console.log('[CAROUSEL WATCHEFFECT] Updated carousel data:', {
    recent: recent.length,
    value: bestValue.length,
    completion: completion.length,
  });
});

const loadingCarousels = computed(() => propertyStore.loading && catalogItems.value.length === 0);

// Debug watches for carousel data
watch(
  () => recentCarouselItems.value,
  val => {
    console.log('[WATCH] recentCarouselItems changed:', val.length);
  },
  { deep: true },
);

watch(
  () => valueCarouselItems.value,
  val => {
    console.log('[WATCH] valueCarouselItems changed:', val.length);
  },
  { deep: true },
);

watch(
  () => completionCarouselItems.value,
  val => {
    console.log('[WATCH] completionCarouselItems changed:', val.length);
  },
  { deep: true },
);

const formattedTotalItems = computed(() => propertyStore.totalItems.toLocaleString());
const displayProperties = computed(() => catalogItems.value);
const cityCount = computed(() => propertyStore.cities.length);

const averagePriceValue = computed(() => {
  const prices = catalogItems.value.map(property => property.minPrice).filter((value): value is number => typeof value === 'number');
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
  const times: number[] = [];

  for (const property of catalogItems.value) {
    const date = property.updatedAt ?? property.publishedAt;
    if (date !== null && date !== undefined) {
      const timestamp = new Date(date).getTime();
      if (Number.isFinite(timestamp)) {
        times.push(timestamp);
      }
    }
  }

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
      area: selectedArea.value || undefined,
      listingType: selectedListingType.value || undefined,
      status: selectedStatus.value || undefined,
      developer: selectedDeveloper.value || undefined,
      minPrice: toNumberOrUndefined(minPrice.value),
      maxPrice: toNumberOrUndefined(maxPrice.value),
      minArea: toNumberOrUndefined(minArea.value),
      dateFrom: dateFrom.value || undefined,
      dateTo: dateTo.value || undefined,
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
  filterUpdateHandle = setTimeout(() => {
    filterUpdateHandle = undefined;
    updateFilters();
  }, 250);
};

watch(
  [
    searchTerm,
    selectedCountry,
    selectedCity,
    selectedArea,
    selectedListingType,
    selectedStatus,
    selectedDeveloper,
    minPrice,
    maxPrice,
    minArea,
    dateFrom,
    dateTo,
  ],
  () => {
    scheduleFilterUpdate();
  },
);

const clearFilters = async () => {
  suppressFilterWatch = true;
  searchTerm.value = '';
  selectedCountry.value = '';
  selectedCity.value = '';
  selectedArea.value = '';
  selectedListingType.value = '';
  selectedStatus.value = '';
  selectedDeveloper.value = '';
  minPrice.value = '';
  maxPrice.value = '';
  minArea.value = '';
  dateFrom.value = '';
  dateTo.value = '';
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
/* Clean Professional Storefront - Full Width, No Padding */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.storefront {
  min-height: 100vh;
  background: var(--bg-color, #ffffff);
  color: var(--text-color, #1a202c);
}

/* Dark mode support */
@media (prefers-color-scheme: dark) {
  :root {
    --bg-color: #1a202c;
    --bg-secondary: #2d3748;
    --text-color: #f7fafc;
    --text-muted: #a0aec0;
    --border-color: #4a5568;
  }
}

@media (prefers-color-scheme: light) {
  :root {
    --bg-color: #ffffff;
    --bg-secondary: #f7fafc;
    --text-color: #1a202c;
    --text-muted: #718096;
    --border-color: #e2e8f0;
  }
}

/* Loading State */
.storefront-loading {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: var(--primary-color, #3182ce);
}

.storefront-loading .spinner-lg {
  width: 3rem;
  height: 3rem;
  border-width: 0.3rem;
  color: white;
}

.storefront-loading p {
  color: white;
  font-weight: 600;
  font-size: 1rem;
}

/* Hero Section - Uses Agent Primary Color */
.storefront-hero {
  background: var(--primary-color, #3182ce);
  color: white;
  padding: 0;
}

.hero-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1.5rem 2rem;
}

.hero-top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.hero-brand {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.brand-logo {
  height: 60px;
  width: 60px;
  object-fit: contain;
  border-radius: 0.5rem;
  background: white;
  padding: 0.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.brand-name {
  font-size: 1.75rem;
  font-weight: 700;
  margin: 0;
  color: white;
}

/* Enhanced Contact Actions */
.contact-actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.contact-actions .btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.625rem 1.25rem;
  border-radius: 0.5rem;
  font-weight: 600;
  font-size: 0.875rem;
  text-decoration: none;
  transition: all 0.2s ease;
  border: none;
}

.btn-whatsapp {
  background: #25d366;
  color: white;
}

.btn-whatsapp:hover {
  background: #20ba5a;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(37, 211, 102, 0.3);
}

.btn-email {
  background: white;
  color: var(--primary-color, #3182ce);
}

.btn-email:hover {
  background: #f7fafc;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 255, 255, 0.3);
}

.btn-phone {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.btn-phone:hover {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.5);
}

/* Hero Metrics */
.hero-metrics {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
}

.hero-metric {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.hero-metric__value {
  font-size: 2rem;
  font-weight: 700;
  color: white;
  line-height: 1;
}

.hero-metric__label {
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.9);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

/* Filters Section */
.filters-section {
  background: var(--bg-secondary, #f7fafc);
  border-bottom: 1px solid var(--border-color, #e2e8f0);
  padding: 1.5rem 0;
}

.filters-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
}

.filters-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.filters-header h2 {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-color, #1a202c);
  margin: 0;
}

.btn-toggle-filters {
  background: none;
  border: none;
  color: var(--primary-color, #3182ce);
  font-weight: 600;
  cursor: pointer;
  font-size: 0.875rem;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  transition: all 0.2s ease;
}

.btn-toggle-filters:hover {
  background: rgba(49, 130, 206, 0.1);
}

.filter-grid,
.advanced-filter-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-color, #2d3748);
  margin-bottom: 0.375rem;
}

.form-control,
.form-control-sm {
  padding: 0.625rem 0.875rem;
  border: 1px solid var(--border-color, #cbd5e0);
  border-radius: 0.375rem;
  font-size: 0.875rem;
  background: var(--bg-color, white);
  color: var(--text-color, #1a202c);
  transition: all 0.2s ease;
}

.form-control:focus,
.form-control-sm:focus {
  outline: none;
  border-color: var(--primary-color, #3182ce);
  box-shadow: 0 0 0 3px rgba(49, 130, 206, 0.1);
}

.filters-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color, #e2e8f0);
}

.filters-actions {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.filters-actions label {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--text-muted, #718096);
  margin: 0;
}

.btn-clear {
  padding: 0.5rem 1rem;
  background: transparent;
  border: 1px solid var(--border-color, #cbd5e0);
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-color, #4a5568);
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-clear:hover:not(:disabled) {
  background: var(--bg-secondary, #f7fafc);
  border-color: var(--primary-color, #3182ce);
  color: var(--primary-color, #3182ce);
}

.btn-clear:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Main Content */
.storefront-main {
  background: var(--bg-color, #ffffff);
  padding: 2rem 0;
}

.main-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
}

/* Carousels Section - Each in separate card */
.carousels-section {
  margin-bottom: 3rem;
}

.carousel-card {
  background: var(--bg-secondary, #f7fafc);
  border: 1px solid var(--border-color, #e2e8f0);
  border-radius: 0.75rem;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* Property Cards Grid */
.properties-grid {
  margin-bottom: 2rem;
}

.property-cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1.5rem;
}

.property-card {
  background: var(--bg-color, white);
  border: 1px solid var(--border-color, #e2e8f0);
  border-radius: 0.75rem;
  overflow: hidden;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.property-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  border-color: var(--primary-color, #3182ce);
}

.property-card--highlight {
  border-color: var(--primary-color, #3182ce);
  box-shadow: 0 4px 12px rgba(49, 130, 206, 0.2);
}

.property-card__media {
  height: 220px;
  background-size: cover;
  background-position: center;
  background-color: var(--bg-secondary, #f7fafc);
  position: relative;
}

.property-card__badge {
  position: absolute;
  top: 1rem;
  left: 1rem;
  background: white;
  color: var(--primary-color, #3182ce);
  padding: 0.375rem 0.75rem;
  border-radius: 0.375rem;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.property-card__body {
  padding: 1.25rem;
}

.property-card__title {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--text-color, #1a202c);
  margin: 0 0 0.5rem 0;
}

.property-card__location {
  font-size: 0.875rem;
  color: var(--text-muted, #718096);
  margin: 0 0 0.75rem 0;
}

.property-card__excerpt {
  font-size: 0.875rem;
  color: var(--text-muted, #4a5568);
  line-height: 1.5;
  margin: 0 0 1rem 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-clamp: 2;
  overflow: hidden;
}

.property-card__footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color, #e2e8f0);
}

.property-card__price {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--primary-color, #3182ce);
  margin-bottom: 0.25rem;
}

.property-card__area {
  font-size: 0.75rem;
  color: var(--text-muted, #718096);
}

/* Skeleton Loaders */
.property-card--skeleton {
  background: var(--bg-secondary, #f7fafc);
  pointer-events: none;
}

.skeleton-block {
  height: 220px;
  background: linear-gradient(90deg, #e2e8f0 0%, #edf2f7 50%, #e2e8f0 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s ease-in-out infinite;
}

.skeleton-line {
  height: 12px;
  border-radius: 0.25rem;
  background: linear-gradient(90deg, #e2e8f0 0%, #edf2f7 50%, #e2e8f0 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s ease-in-out infinite;
  margin-bottom: 0.75rem;
}

.skeleton-button {
  width: 80px;
  height: 32px;
  border-radius: 0.375rem;
  background: linear-gradient(90deg, #e2e8f0 0%, #edf2f7 50%, #e2e8f0 100%);
  background-size: 200% 100%;
  animation: shimmer 1.5s ease-in-out infinite;
}

@keyframes shimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

/* No Results */
.no-results {
  text-align: center;
  padding: 4rem 2rem;
  background: var(--bg-secondary, #f7fafc);
  border: 1px solid var(--border-color, #e2e8f0);
  border-radius: 0.75rem;
}

.no-results h2 {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-color, #1a202c);
  margin-bottom: 0.75rem;
}

.no-results p {
  font-size: 1rem;
  color: var(--text-muted, #718096);
  margin-bottom: 1.5rem;
}

/* Pagination */
.pagination-nav {
  margin-top: 3rem;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  list-style: none;
  padding: 0;
  margin: 0;
}

.pagination li {
  display: flex;
}

.pagination button {
  padding: 0.625rem 1rem;
  background: var(--bg-color, white);
  border: 1px solid var(--border-color, #cbd5e0);
  border-radius: 0.375rem;
  color: var(--text-color, #4a5568);
  font-weight: 600;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.pagination button:hover:not(:disabled) {
  background: var(--primary-color, #3182ce);
  color: white;
  border-color: var(--primary-color, #3182ce);
}

.pagination button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.pagination li.active button {
  background: var(--primary-color, #3182ce);
  color: white;
  border-color: var(--primary-color, #3182ce);
}

/* Buttons */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.625rem 1.25rem;
  border-radius: 0.375rem;
  font-weight: 600;
  font-size: 0.875rem;
  text-decoration: none;
  transition: all 0.2s ease;
  cursor: pointer;
}

.btn-primary {
  background: var(--primary-color, #3182ce);
  color: white;
  border: none;
}

.btn-primary:hover {
  background: var(--primary-color-dark, #2c5aa0);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(49, 130, 206, 0.3);
}

.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.8125rem;
}

.badge {
  padding: 0.25rem 0.625rem;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.badge-primary {
  background: var(--primary-color, #3182ce);
  color: white;
}

/* Fade Transition */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Responsive Design */
@media (max-width: 768px) {
  .hero-container,
  .filters-container,
  .main-container {
    padding: 1rem;
  }

  .hero-top-bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .contact-actions {
    width: 100%;
  }

  .contact-actions .btn {
    flex: 1;
    justify-content: center;
  }

  .hero-metrics {
    flex-direction: column;
    gap: 1rem;
  }

  .brand-name {
    font-size: 1.25rem;
  }

  .property-cards-grid {
    grid-template-columns: 1fr;
  }

  .filter-grid,
  .advanced-filter-grid {
    grid-template-columns: 1fr;
  }

  .filters-footer {
    flex-direction: column;
    align-items: stretch;
  }

  .filters-actions {
    flex-wrap: wrap;
  }
}

/* Dynamic Color Variables */
:root {
  --primary-color: #3182ce;
  --primary-color-dark: #2c5aa0;
  --secondary-color: #48bb78;
}
</style>

<template>
  <div class="property-carousel">
    <div class="carousel-header mb-3">
      <h2 class="h4 mb-1">{{ title }}</h2>
      <p v-if="subtitle" class="text-muted small mb-0">{{ subtitle }}</p>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="carousel-container">
      <div class="carousel-track">
        <div v-for="index in 5" :key="`skeleton-${index}`" class="carousel-item">
          <div class="property-card property-card--skeleton card border-0 shadow-sm">
            <div class="property-card__media skeleton-block"></div>
            <div class="card-body">
              <div class="skeleton-line w-75 mb-2"></div>
              <div class="skeleton-line w-50 mb-2"></div>
              <div class="skeleton-line w-100 mb-3"></div>
              <div class="d-flex justify-content-between">
                <div class="skeleton-line w-50"></div>
                <div class="skeleton-button"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Content State -->
    <div v-else-if="itemsToRender.length > 0" class="carousel-container">
      <button v-if="canScrollLeft" class="carousel-nav carousel-nav--prev" @click="scrollLeft" aria-label="Previous">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="15 18 9 12 15 6"></polyline>
        </svg>
      </button>

      <div ref="carouselTrack" class="carousel-track" @scroll="updateScrollState">
        <!-- Manual rendering without v-for since v-for refuses to work in this context -->
        <div v-if="itemsToRender[0]" style="flex: 0 0 280px; max-width: 280px">
          <div class="card h-100 shadow-sm border-0" style="border-radius: 1rem; overflow: hidden">
            <div
              style="height: 160px; background-size: cover; background-position: center; position: relative"
              :style="coverImage(itemsToRender[0].coverUrl)"
            >
              <span
                v-if="itemsToRender[0].listingType"
                class="badge badge-light"
                style="
                  position: absolute;
                  top: 0.75rem;
                  left: 0.75rem;
                  font-weight: 600;
                  text-transform: uppercase;
                  letter-spacing: 0.08em;
                  font-size: 0.65rem;
                "
              >
                {{ listingTypeLabel(itemsToRender[0].listingType) }}
              </span>
            </div>
            <div class="card-body d-flex flex-column">
              <div class="d-flex justify-content-between align-items-start mb-2">
                <h3 class="h6 mb-0">{{ itemsToRender[0].name || 'No Name' }}</h3>
                <span v-if="itemsToRender[0].status" class="badge badge-pill badge-primary text-uppercase small">
                  {{ statusLabel(itemsToRender[0].status) }}
                </span>
              </div>
              <p class="text-muted small mb-2">
                <span v-if="itemsToRender[0].city">{{ itemsToRender[0].city }}</span>
                <span v-if="itemsToRender[0].city && itemsToRender[0].country">, </span>
                <span v-if="itemsToRender[0].country">{{ itemsToRender[0].country }}</span>
              </p>
              <p
                class="text-muted small mb-2"
                style="
                  flex-grow: 1;
                  line-height: 1.4;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  display: -webkit-box;
                  -webkit-line-clamp: 2;
                  -webkit-box-orient: vertical;
                "
              >
                {{ propertyExcerpt(itemsToRender[0]) }}
              </p>
              <div class="mt-auto pt-2">
                <div class="font-weight-bold mb-2">{{ priceLabel(itemsToRender[0]) }}</div>
                <div class="d-flex justify-content-between align-items-center">
                  <small v-if="itemsToRender[0].minArea" class="text-muted">
                    {{ itemsToRender[0].minArea.toLocaleString() }} {{ itemsToRender[0].areaUnit || 'sq.ft' }}
                  </small>
                  <router-link :to="propertyLink(itemsToRender[0])" class="btn btn-outline-primary btn-sm ml-auto"> View </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="itemsToRender[1]" style="flex: 0 0 280px; max-width: 280px">
          <div class="card h-100 shadow-sm border-0" style="border-radius: 1rem; overflow: hidden">
            <div
              style="height: 160px; background-size: cover; background-position: center; position: relative"
              :style="coverImage(itemsToRender[1].coverUrl)"
            >
              <span
                v-if="itemsToRender[1].listingType"
                class="badge badge-light"
                style="
                  position: absolute;
                  top: 0.75rem;
                  left: 0.75rem;
                  font-weight: 600;
                  text-transform: uppercase;
                  letter-spacing: 0.08em;
                  font-size: 0.65rem;
                "
              >
                {{ listingTypeLabel(itemsToRender[1].listingType) }}
              </span>
            </div>
            <div class="card-body d-flex flex-column">
              <div class="d-flex justify-content-between align-items-start mb-2">
                <h3 class="h6 mb-0">{{ itemsToRender[1].name || 'No Name' }}</h3>
                <span v-if="itemsToRender[1].status" class="badge badge-pill badge-primary text-uppercase small">
                  {{ statusLabel(itemsToRender[1].status) }}
                </span>
              </div>
              <p class="text-muted small mb-2">
                <span v-if="itemsToRender[1].city">{{ itemsToRender[1].city }}</span>
                <span v-if="itemsToRender[1].city && itemsToRender[1].country">, </span>
                <span v-if="itemsToRender[1].country">{{ itemsToRender[1].country }}</span>
              </p>
              <p
                class="text-muted small mb-2"
                style="
                  flex-grow: 1;
                  line-height: 1.4;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  display: -webkit-box;
                  -webkit-line-clamp: 2;
                  -webkit-box-orient: vertical;
                "
              >
                {{ propertyExcerpt(itemsToRender[1]) }}
              </p>
              <div class="mt-auto pt-2">
                <div class="font-weight-bold mb-2">{{ priceLabel(itemsToRender[1]) }}</div>
                <div class="d-flex justify-content-between align-items-center">
                  <small v-if="itemsToRender[1].minArea" class="text-muted">
                    {{ itemsToRender[1].minArea.toLocaleString() }} {{ itemsToRender[1].areaUnit || 'sq.ft' }}
                  </small>
                  <router-link :to="propertyLink(itemsToRender[1])" class="btn btn-outline-primary btn-sm ml-auto"> View </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="itemsToRender[2]" style="flex: 0 0 280px; max-width: 280px">
          <div class="card h-100 shadow-sm border-0" style="border-radius: 1rem; overflow: hidden">
            <div
              style="height: 160px; background-size: cover; background-position: center; position: relative"
              :style="coverImage(itemsToRender[2].coverUrl)"
            >
              <span
                v-if="itemsToRender[2].listingType"
                class="badge badge-light"
                style="
                  position: absolute;
                  top: 0.75rem;
                  left: 0.75rem;
                  font-weight: 600;
                  text-transform: uppercase;
                  letter-spacing: 0.08em;
                  font-size: 0.65rem;
                "
              >
                {{ listingTypeLabel(itemsToRender[2].listingType) }}
              </span>
            </div>
            <div class="card-body d-flex flex-column">
              <div class="d-flex justify-content-between align-items-start mb-2">
                <h3 class="h6 mb-0">{{ itemsToRender[2].name || 'No Name' }}</h3>
                <span v-if="itemsToRender[2].status" class="badge badge-pill badge-primary text-uppercase small">
                  {{ statusLabel(itemsToRender[2].status) }}
                </span>
              </div>
              <p class="text-muted small mb-2">
                <span v-if="itemsToRender[2].city">{{ itemsToRender[2].city }}</span>
                <span v-if="itemsToRender[2].city && itemsToRender[2].country">, </span>
                <span v-if="itemsToRender[2].country">{{ itemsToRender[2].country }}</span>
              </p>
              <p
                class="text-muted small mb-2"
                style="
                  flex-grow: 1;
                  line-height: 1.4;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  display: -webkit-box;
                  -webkit-line-clamp: 2;
                  -webkit-box-orient: vertical;
                "
              >
                {{ propertyExcerpt(itemsToRender[2]) }}
              </p>
              <div class="mt-auto pt-2">
                <div class="font-weight-bold mb-2">{{ priceLabel(itemsToRender[2]) }}</div>
                <div class="d-flex justify-content-between align-items-center">
                  <small v-if="itemsToRender[2].minArea" class="text-muted">
                    {{ itemsToRender[2].minArea.toLocaleString() }} {{ itemsToRender[2].areaUnit || 'sq.ft' }}
                  </small>
                  <router-link :to="propertyLink(itemsToRender[2])" class="btn btn-outline-primary btn-sm ml-auto"> View </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="itemsToRender[3]" style="flex: 0 0 280px; max-width: 280px">
          <div class="card h-100 shadow-sm border-0" style="border-radius: 1rem; overflow: hidden">
            <div
              style="height: 160px; background-size: cover; background-position: center; position: relative"
              :style="coverImage(itemsToRender[3].coverUrl)"
            >
              <span
                v-if="itemsToRender[3].listingType"
                class="badge badge-light"
                style="
                  position: absolute;
                  top: 0.75rem;
                  left: 0.75rem;
                  font-weight: 600;
                  text-transform: uppercase;
                  letter-spacing: 0.08em;
                  font-size: 0.65rem;
                "
              >
                {{ listingTypeLabel(itemsToRender[3].listingType) }}
              </span>
            </div>
            <div class="card-body d-flex flex-column">
              <div class="d-flex justify-content-between align-items-start mb-2">
                <h3 class="h6 mb-0">{{ itemsToRender[3].name || 'No Name' }}</h3>
                <span v-if="itemsToRender[3].status" class="badge badge-pill badge-primary text-uppercase small">
                  {{ statusLabel(itemsToRender[3].status) }}
                </span>
              </div>
              <p class="text-muted small mb-2">
                <span v-if="itemsToRender[3].city">{{ itemsToRender[3].city }}</span>
                <span v-if="itemsToRender[3].city && itemsToRender[3].country">, </span>
                <span v-if="itemsToRender[3].country">{{ itemsToRender[3].country }}</span>
              </p>
              <p
                class="text-muted small mb-2"
                style="
                  flex-grow: 1;
                  line-height: 1.4;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  display: -webkit-box;
                  -webkit-line-clamp: 2;
                  -webkit-box-orient: vertical;
                "
              >
                {{ propertyExcerpt(itemsToRender[3]) }}
              </p>
              <div class="mt-auto pt-2">
                <div class="font-weight-bold mb-2">{{ priceLabel(itemsToRender[3]) }}</div>
                <div class="d-flex justify-content-between align-items-center">
                  <small v-if="itemsToRender[3].minArea" class="text-muted">
                    {{ itemsToRender[3].minArea.toLocaleString() }} {{ itemsToRender[3].areaUnit || 'sq.ft' }}
                  </small>
                  <router-link :to="propertyLink(itemsToRender[3])" class="btn btn-outline-primary btn-sm ml-auto"> View </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="itemsToRender[4]" style="flex: 0 0 280px; max-width: 280px">
          <div class="card h-100 shadow-sm border-0" style="border-radius: 1rem; overflow: hidden">
            <div
              style="height: 160px; background-size: cover; background-position: center; position: relative"
              :style="coverImage(itemsToRender[4].coverUrl)"
            >
              <span
                v-if="itemsToRender[4].listingType"
                class="badge badge-light"
                style="
                  position: absolute;
                  top: 0.75rem;
                  left: 0.75rem;
                  font-weight: 600;
                  text-transform: uppercase;
                  letter-spacing: 0.08em;
                  font-size: 0.65rem;
                "
              >
                {{ listingTypeLabel(itemsToRender[4].listingType) }}
              </span>
            </div>
            <div class="card-body d-flex flex-column">
              <div class="d-flex justify-content-between align-items-start mb-2">
                <h3 class="h6 mb-0">{{ itemsToRender[4].name || 'No Name' }}</h3>
                <span v-if="itemsToRender[4].status" class="badge badge-pill badge-primary text-uppercase small">
                  {{ statusLabel(itemsToRender[4].status) }}
                </span>
              </div>
              <p class="text-muted small mb-2">
                <span v-if="itemsToRender[4].city">{{ itemsToRender[4].city }}</span>
                <span v-if="itemsToRender[4].city && itemsToRender[4].country">, </span>
                <span v-if="itemsToRender[4].country">{{ itemsToRender[4].country }}</span>
              </p>
              <p
                class="text-muted small mb-2"
                style="
                  flex-grow: 1;
                  line-height: 1.4;
                  overflow: hidden;
                  text-overflow: ellipsis;
                  display: -webkit-box;
                  -webkit-line-clamp: 2;
                  -webkit-box-orient: vertical;
                "
              >
                {{ propertyExcerpt(itemsToRender[4]) }}
              </p>
              <div class="mt-auto pt-2">
                <div class="font-weight-bold mb-2">{{ priceLabel(itemsToRender[4]) }}</div>
                <div class="d-flex justify-content-between align-items-center">
                  <small v-if="itemsToRender[4].minArea" class="text-muted">
                    {{ itemsToRender[4].minArea.toLocaleString() }} {{ itemsToRender[4].areaUnit || 'sq.ft' }}
                  </small>
                  <router-link :to="propertyLink(itemsToRender[4])" class="btn btn-outline-primary btn-sm ml-auto"> View </router-link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <button v-if="canScrollRight" class="carousel-nav carousel-nav--next" @click="scrollRight" aria-label="Next">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="9 18 15 12 9 6"></polyline>
        </svg>
      </button>
    </div>

    <!-- Empty State -->
    <div v-else class="text-center py-4">
      <p class="text-muted mb-0">No properties available at the moment.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted, onBeforeUnmount, watch, nextTick, unref } from 'vue';
import type { IProperty } from '@/shared/model/property.model';

interface Props {
  title: string;
  subtitle?: string;
  properties?: IProperty[] | null;
  loading?: boolean;
  linkBase?: string;
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  linkBase: '',
  subtitle: '',
  properties: () => [] as IProperty[],
});

const carouselTrack = ref<HTMLElement | null>(null);
const canScrollLeft = ref(false);
const canScrollRight = ref(false);

// Create a computed property for properties
const propertyList = computed(() => {
  const resolved = unref(props.properties);
  const result = (resolved ?? []) as IProperty[];
  console.log(`[CAROUSEL ${props.title}] propertyList computed:`, result.length, 'items', result);
  console.log(`[CAROUSEL ${props.title}] loading:`, props.loading);
  console.log(`[CAROUSEL ${props.title}] condition check:`, {
    loading: props.loading,
    hasItems: result.length > 0,
    shouldShowContent: !props.loading && result.length > 0,
  });
  return result;
});

// Create a plain ref array as workaround for v-for issue
const itemsToRender = ref<IProperty[]>([]);

// Watch and update the plain array
watch(
  () => props.properties,
  newVal => {
    itemsToRender.value = (newVal ?? []) as IProperty[];
    console.log(`[CAROUSEL ${props.title}] itemsToRender updated:`, itemsToRender.value.length);
  },
  { immediate: true, deep: true },
);

const updateScrollState = () => {
  if (!carouselTrack.value) return;
  const { scrollLeft, scrollWidth, clientWidth } = carouselTrack.value;
  canScrollLeft.value = scrollLeft > 0;
  canScrollRight.value = scrollLeft < scrollWidth - clientWidth - 10;
};

const scrollLeft = () => {
  if (!carouselTrack.value) return;
  carouselTrack.value.scrollBy({ left: -320, behavior: 'smooth' });
  setTimeout(updateScrollState, 300);
};

const scrollRight = () => {
  if (!carouselTrack.value) return;
  carouselTrack.value.scrollBy({ left: 320, behavior: 'smooth' });
  setTimeout(updateScrollState, 300);
};

const propertyLink = (property: IProperty) => {
  if (!property.id) return '#';
  return `${props.linkBase}/${property.id}`;
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
  if (!value) return '';
  return listingTypeDictionary[value] ?? value.replace(/_/g, ' ').replace(/\b\w/g, letter => letter.toUpperCase());
};

const statusLabel = (value?: string | null) => {
  if (!value) return '';
  return value
    .toString()
    .split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

const priceLabel = (property: IProperty) => {
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

const propertyExcerpt = (property: IProperty) => {
  if (!property.overviewMd) {
    return 'Request more information to learn about this development.';
  }
  const clean = property.overviewMd.replace(/[#*`]/g, '').replace(/\s+/g, ' ').trim();
  if (clean.length <= 100) return clean;
  return `${clean.slice(0, 97)}...`;
};

watch(
  () => propertyList.value.length,
  () => {
    void nextTick(() => {
      updateScrollState();
    });
  },
);

onMounted(() => {
  setTimeout(updateScrollState, 100);
  window.addEventListener('resize', updateScrollState);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateScrollState);
});
</script>

<style scoped>
/* Modern Property Carousel */
.property-carousel {
  position: relative;
  margin-bottom: 3rem;
  padding: 0.5rem 0;
}

.carousel-header {
  margin-bottom: 1.5rem;
}

.carousel-header h2 {
  color: #2d3748;
  font-weight: 900;
  font-size: 1.75rem;
  margin: 0;
  letter-spacing: -0.5px;
}

.carousel-header .text-muted {
  color: #718096 !important;
  font-weight: 600;
  font-size: 0.95rem;
}

.carousel-container {
  position: relative;
  padding: 0 3rem;
}

.carousel-track {
  display: flex;
  gap: 1.5rem;
  overflow-x: auto;
  scroll-behavior: smooth;
  scrollbar-width: thin;
  scrollbar-color: rgba(102, 126, 234, 0.3) transparent;
  padding: 1rem 0 1.5rem;
  min-height: 400px;
  -webkit-overflow-scrolling: touch;
}

.carousel-track::-webkit-scrollbar {
  height: 10px;
}

.carousel-track::-webkit-scrollbar-track {
  background: rgba(102, 126, 234, 0.05);
  border-radius: 10px;
  margin: 0 1rem;
}

.carousel-track::-webkit-scrollbar-thumb {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  transition: all 0.3s ease;
}

.carousel-track::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(90deg, #764ba2 0%, #667eea 100%);
}

.carousel-item {
  flex: 0 0 300px;
  max-width: 300px;
  min-height: 380px;
}

/* Navigation Buttons */
.carousel-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 20;
  background: white;
  border: 2px solid rgba(102, 126, 234, 0.2);
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.2);
  color: #667eea;
}

.carousel-nav:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
  transform: translateY(-50%) scale(1.15);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.4);
}

.carousel-nav svg {
  width: 24px;
  height: 24px;
  stroke-width: 3;
}

.carousel-nav--prev {
  left: -8px;
}

.carousel-nav--next {
  right: -8px;
}

/* Property Cards */
.property-card {
  border-radius: 1.75rem;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  height: 100%;
  display: flex;
  flex-direction: column;
  min-height: 360px;
  background: white;
  border: 2px solid rgba(102, 126, 234, 0.1);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
}

.property-card:hover {
  transform: translateY(-12px) scale(1.03);
  box-shadow: 0 25px 50px rgba(102, 126, 234, 0.25);
  border-color: #667eea;
}

.property-card__media {
  height: 180px;
  min-height: 180px;
  background-size: cover;
  background-position: center;
  position: relative;
  flex-shrink: 0;
  overflow: hidden;
}

.property-card__media::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.4), transparent);
}

.property-card__badge {
  position: absolute;
  top: 1rem;
  left: 1rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  font-size: 0.7rem;
  z-index: 2;
  padding: 0.5rem 0.875rem;
  border-radius: 0.75rem;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.card-body {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.property-card h3 {
  font-size: 1.05rem;
  font-weight: 800;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  color: #2d3748;
  margin: 0;
}

.property-card__excerpt {
  flex-grow: 1;
  line-height: 1.6;
  font-size: 0.875rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  color: #718096;
  font-weight: 500;
}

.property-card .badge {
  padding: 0.35rem 0.75rem;
  border-radius: 0.5rem;
  font-weight: 700;
  font-size: 0.7rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.badge-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.property-card .font-weight-bold {
  font-size: 1.25rem;
  font-weight: 900;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.property-card .btn {
  border-radius: 0.75rem;
  padding: 0.5rem 1.25rem;
  font-weight: 700;
  font-size: 0.8rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  transition: all 0.3s ease;
  border: 2px solid #667eea;
  color: #667eea;
  background: transparent;
}

.property-card .btn:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

/* Skeleton Loaders */
.property-card--skeleton {
  background: linear-gradient(135deg, rgba(245, 247, 250, 0.95), rgba(235, 240, 250, 0.95));
  border: 2px solid rgba(102, 126, 234, 0.05);
}

.skeleton-block {
  height: 180px;
  background: linear-gradient(90deg, rgba(220, 229, 240, 0.6) 0%, rgba(229, 235, 244, 0.9) 50%, rgba(220, 229, 240, 0.6) 100%);
  background-size: 200% 100%;
  animation: shimmer 2s ease-in-out infinite;
}

.skeleton-line {
  height: 14px;
  border-radius: 999px;
  background: linear-gradient(90deg, rgba(220, 229, 240, 0.6) 0%, rgba(230, 236, 245, 0.9) 50%, rgba(220, 229, 240, 0.6) 100%);
  background-size: 200% 100%;
  animation: shimmer 2s ease-in-out infinite;
}

.skeleton-line.w-75 {
  width: 75% !important;
}

.skeleton-line.w-50 {
  width: 50% !important;
}

.skeleton-line.w-100 {
  width: 100% !important;
}

.skeleton-button {
  width: 80px;
  height: 32px;
  border-radius: 999px;
  background: linear-gradient(90deg, rgba(220, 229, 240, 0.6) 0%, rgba(230, 236, 245, 0.9) 50%, rgba(220, 229, 240, 0.6) 100%);
  background-size: 200% 100%;
  animation: shimmer 2s ease-in-out infinite;
}

@keyframes shimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

/* Responsive Design */
@media (max-width: 767.98px) {
  .carousel-container {
    padding: 0 1rem;
  }

  .carousel-item {
    flex: 0 0 260px;
    max-width: 260px;
  }

  .carousel-nav {
    width: 42px;
    height: 42px;
  }

  .carousel-nav--prev {
    left: -4px;
  }

  .carousel-nav--next {
    right: -4px;
  }

  .property-card__media {
    height: 160px;
  }
}

@media (max-width: 575.98px) {
  .carousel-container {
    padding: 0;
  }

  .carousel-nav {
    display: none;
  }

  .carousel-item {
    flex: 0 0 240px;
    max-width: 240px;
  }

  .carousel-header h2 {
    font-size: 1.5rem;
  }
}

/* Card Utilities */
.card {
  background: white;
  border-radius: 1.75rem;
  overflow: hidden;
}

.card.h-100 {
  height: 100% !important;
}

.card.border-0 {
  border: none !important;
}

.card.shadow-sm {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08) !important;
}

.d-flex {
  display: flex !important;
}

.flex-column {
  flex-direction: column !important;
}

.justify-content-between {
  justify-content: space-between !important;
}

.align-items-start {
  align-items: flex-start !important;
}

.align-items-center {
  align-items: center !important;
}

.mt-auto {
  margin-top: auto !important;
}

.pt-2 {
  padding-top: 0.5rem !important;
}

.mb-0 {
  margin-bottom: 0 !important;
}

.mb-2 {
  margin-bottom: 0.5rem !important;
}

.mb-3 {
  margin-bottom: 1rem !important;
}

.ml-auto {
  margin-left: auto !important;
}

.text-muted {
  color: #718096 !important;
}

.small {
  font-size: 0.875rem !important;
}

.text-uppercase {
  text-transform: uppercase !important;
}
</style>

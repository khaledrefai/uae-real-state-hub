import axios from 'axios';
import { computed, ref } from 'vue';
import { defineStore } from 'pinia';

import type { IProperty } from '@/shared/model/property.model';
import type { IPropertyMarker } from '@/shared/model/property-marker.model';

export interface PropertyFilters {
  search?: string;
  city?: string;
  country?: string;
  listingType?: string;
  status?: string;
  minPrice?: number;
  maxPrice?: number;
  developer?: string;
  minArea?: number;
  maxArea?: number;
}

interface PaginationOptions {
  page?: number;
  size?: number;
  sort?: string[];
}

export interface PropertyListResponse {
  items: IProperty[];
  totalItems: number;
}

const PROPERTY_API = 'api/properties';
const DEFAULT_PAGE_SIZE = 12;

export const usePropertyCatalogStore = defineStore('propertyCatalog', () => {
  const apiBase = ref(PROPERTY_API);

  const items = ref<IProperty[]>([]);
  const loading = ref(false);
  const totalItems = ref(0);
  const activePage = ref(0);
  const pageSize = ref(DEFAULT_PAGE_SIZE);
  const filters = ref<PropertyFilters>({});
  const sort = ref<string[]>(['publishedAt,desc']);
  const customFilter = ref<((property: IProperty) => boolean) | null>(null);

  const filteredItems = computed(() => (customFilter.value ? items.value.filter(customFilter.value) : items.value));

  const markers = computed(() =>
    filteredItems.value
      .map(property => {
        const fallbackLatitude = typeof property.latitude === 'number' ? property.latitude : null;
        const fallbackLongitude = typeof property.longitude === 'number' ? property.longitude : null;
        const markerSource = property.marker ?? {};
        const latitude = typeof markerSource.latitude === 'number' ? markerSource.latitude : fallbackLatitude;
        const longitude = typeof markerSource.longitude === 'number' ? markerSource.longitude : fallbackLongitude;

        if (typeof latitude !== 'number' || typeof longitude !== 'number') {
          return null;
        }

        const cleanedOverview = property.overviewMd ? property.overviewMd.replace(/[#*`]/g, '').replace(/\s+/g, ' ').trim() : null;
        const summary = cleanedOverview ? cleanedOverview.slice(0, 160) : (markerSource.summary ?? null);

        const inferredCurrency = markerSource.priceCurrency ?? property.priceCurrency ?? 'AED';

        return {
          ...markerSource,
          latitude,
          longitude,
          propertyId: property.id,
          name: markerSource.name ?? property.name ?? 'Property',
          developer: markerSource.developer ?? property.developer ?? null,
          status: markerSource.status ?? property.status ?? null,
          listingType: markerSource.listingType ?? property.listingType ?? null,
          minPrice: markerSource.minPrice ?? property.minPrice ?? null,
          priceCurrency: inferredCurrency,
          coverUrl: markerSource.coverUrl ?? property.coverUrl ?? null,
          city: markerSource.city ?? property.city ?? null,
          country: markerSource.country ?? property.country ?? null,
          summary,
        } as IPropertyMarker;
      })
      .filter((marker): marker is IPropertyMarker => Boolean(marker)),
  );

  const cities = computed(() => Array.from(new Set(items.value.map(item => item.city).filter((city): city is string => Boolean(city)))));
  const countries = computed(() =>
    Array.from(new Set(items.value.map(item => item.country).filter((country): country is string => Boolean(country)))),
  );
  const developers = computed(() =>
    Array.from(new Set(items.value.map(item => item.developer).filter((developer): developer is string => Boolean(developer)))),
  );
  const statuses = computed(() =>
    Array.from(new Set(items.value.map(item => item.status).filter((status): status is string => Boolean(status)))),
  );
  const listingTypes = computed(() =>
    Array.from(new Set(items.value.map(item => item.listingType).filter((type): type is string => Boolean(type)))),
  );

  const buildParams = (pagination: PaginationOptions = {}): Record<string, unknown> => {
    const params: Record<string, unknown> = {
      page: pagination.page ?? activePage.value,
      size: pagination.size ?? pageSize.value,
      sort: pagination.sort ?? sort.value,
    };

    if (filters.value.search) {
      params['name.contains'] = filters.value.search;
    }
    if (filters.value.country) {
      params['country.equals'] = filters.value.country;
    }
    if (filters.value.city) {
      params['city.equals'] = filters.value.city;
    }
    if (filters.value.listingType) {
      params['listingType.equals'] = filters.value.listingType;
    }
    if (filters.value.status) {
      params['status.equals'] = filters.value.status;
    }
    if (typeof filters.value.minPrice === 'number') {
      params['minPrice.greaterThanOrEqual'] = filters.value.minPrice;
    }
    if (typeof filters.value.maxPrice === 'number') {
      params['maxPrice.lessThanOrEqual'] = filters.value.maxPrice;
    }
    if (filters.value.developer) {
      params['developer.contains'] = filters.value.developer;
    }
    if (typeof filters.value.minArea === 'number') {
      params['minArea.greaterThanOrEqual'] = filters.value.minArea;
    }
    if (typeof filters.value.maxArea === 'number') {
      params['maxArea.lessThanOrEqual'] = filters.value.maxArea;
    }

    return params;
  };

  const fetchPage = async (pagination: PaginationOptions = {}): Promise<PropertyListResponse> => {
    loading.value = true;
    try {
      const response = await axios.get<IProperty[]>(apiBase.value, {
        params: buildParams(pagination),
        paramsSerializer: {
          indexes: null,
        },
      });

      const headerTotal = Number(response.headers?.['x-total-count']);
      items.value = response.data;
      activePage.value = pagination.page ?? activePage.value;
      pageSize.value = pagination.size ?? pageSize.value;

      const derivedItems = filteredItems.value;
      totalItems.value = Number.isFinite(headerTotal) ? headerTotal : derivedItems.length;

      return {
        items: derivedItems,
        totalItems: totalItems.value,
      };
    } finally {
      loading.value = false;
    }
  };

  const setFilters = (nextFilters: Partial<PropertyFilters>, triggerFetch = true): Promise<PropertyListResponse> => {
    filters.value = {
      ...filters.value,
      ...nextFilters,
    };

    if (triggerFetch) {
      return fetchPage({ page: 0 });
    }

    return Promise.resolve({ items: filteredItems.value, totalItems: totalItems.value });
  };

  const resetFilters = () =>
    setFilters(
      {
        search: undefined,
        city: undefined,
        country: undefined,
        listingType: undefined,
        status: undefined,
        minPrice: undefined,
        maxPrice: undefined,
        developer: undefined,
        minArea: undefined,
        maxArea: undefined,
      },
      true,
    );

  const setCustomFilter = (fn: ((property: IProperty) => boolean) | null) => {
    customFilter.value = fn;
  };

  const setApiBase = (nextApi: string) => {
    apiBase.value = nextApi;
  };

  return {
    // state
    apiBase,
    items,
    loading,
    totalItems,
    activePage,
    pageSize,
    filters,
    sort,
    customFilter,
    // getters
    filteredItems,
    markers,
    cities,
    countries,
    developers,
    statuses,
    listingTypes,
    // actions
    fetchPage,
    setFilters,
    resetFilters,
    setCustomFilter,
    setApiBase,
  };
});

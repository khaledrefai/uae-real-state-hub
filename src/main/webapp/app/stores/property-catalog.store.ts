import axios from 'axios';
import { computed, ref } from 'vue';
import { defineStore } from 'pinia';

import type { IProperty } from '@/shared/model/property.model';
import type { IPropertyMarker } from '@/shared/model/property-marker.model';

export interface PropertyFilters {
  search?: string;
  city?: string;
  country?: string;
  area?: string;
  listingType?: string;
  status?: string;
  minPrice?: number;
  maxPrice?: number;
  developer?: string;
  minArea?: number;
  maxArea?: number;
  dateFrom?: string;
  dateTo?: string;
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
  const areas = computed(() => Array.from(new Set(items.value.map(item => item.area).filter((area): area is string => Boolean(area)))));
  const developers = computed(() =>
    Array.from(new Set(items.value.map(item => item.developer).filter((developer): developer is string => Boolean(developer)))),
  );
  const statuses = computed(() =>
    Array.from(new Set(items.value.map(item => item.status).filter((status): status is NonNullable<typeof status> => Boolean(status)))),
  );
  const listingTypes = computed(() =>
    Array.from(new Set(items.value.map(item => item.listingType).filter((type): type is NonNullable<typeof type> => Boolean(type)))),
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
    if (filters.value.area) {
      params['area.contains'] = filters.value.area;
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
    if (filters.value.dateFrom) {
      params['publishedAt.greaterThanOrEqual'] = filters.value.dateFrom;
    }
    if (filters.value.dateTo) {
      params['publishedAt.lessThanOrEqual'] = filters.value.dateTo;
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
        area: undefined,
        listingType: undefined,
        status: undefined,
        minPrice: undefined,
        maxPrice: undefined,
        developer: undefined,
        minArea: undefined,
        maxArea: undefined,
        dateFrom: undefined,
        dateTo: undefined,
      },
      true,
    );

  const setCustomFilter = (fn: ((property: IProperty) => boolean) | null) => {
    customFilter.value = fn;
  };

  const setApiBase = (nextApi: string) => {
    apiBase.value = nextApi;
  };

  const fetchRecentProperties = async (limit = 5): Promise<IProperty[]> => {
    try {
      const response = await axios.get<IProperty[]>(apiBase.value, {
        params: {
          page: 0,
          size: limit,
          sort: ['publishedAt,desc'],
        },
        paramsSerializer: {
          indexes: null,
        },
      });
      return response.data;
    } catch (error) {
      console.error('Failed to fetch recent properties', error);
      return [];
    }
  };

  const fetchCheapestProperties = async (limit = 5): Promise<IProperty[]> => {
    try {
      const response = await axios.get<IProperty[]>(apiBase.value, {
        params: {
          page: 0,
          size: limit * 3,
          sort: ['minPrice,asc'],
        },
        paramsSerializer: {
          indexes: null,
        },
      });

      // Try to get properties with valid prices
      let filtered = response.data.filter(property => {
        const hasMinPrice = typeof property.minPrice === 'number' && property.minPrice > 0;
        const hasMinPriceAed = typeof property.minPriceAed === 'number' && property.minPriceAed > 0;
        return hasMinPrice || hasMinPriceAed;
      });

      // If we don't have enough, return all fetched (already sorted by backend)
      if (filtered.length < limit) {
        filtered = response.data;
      }

      return filtered.slice(0, limit);
    } catch (error) {
      console.error('Failed to fetch cheapest properties', error);
      return [];
    }
  };

  const fetchCompletionSoonProperties = async (limit = 5): Promise<IProperty[]> => {
    try {
      const response = await axios.get<IProperty[]>(apiBase.value, {
        params: {
          page: 0,
          size: limit * 3,
          sort: ['completionDateTime,asc'],
        },
        paramsSerializer: {
          indexes: null,
        },
      });

      // Filter properties with future completion dates
      const now = new Date();
      const futureProperties = response.data
        .filter(property => {
          if (!property.completionDateTime) return false;
          const completionDate = new Date(property.completionDateTime);
          return completionDate > now;
        })
        .slice(0, limit);

      // If no future properties, return properties with any completion date
      if (futureProperties.length === 0) {
        const withCompletionDate = response.data
          .filter(property => property.completionDateTime !== null && property.completionDateTime !== undefined)
          .slice(0, limit);

        // If still none, return all fetched properties
        if (withCompletionDate.length === 0) {
          return response.data.slice(0, limit);
        }

        return withCompletionDate;
      }

      return futureProperties;
    } catch (error) {
      console.error('Failed to fetch completion soon properties', error);
      return [];
    }
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
    areas,
    developers,
    statuses,
    listingTypes,
    // actions
    fetchPage,
    setFilters,
    resetFilters,
    setCustomFilter,
    setApiBase,
    fetchRecentProperties,
    fetchCheapestProperties,
    fetchCompletionSoonProperties,
  };
});

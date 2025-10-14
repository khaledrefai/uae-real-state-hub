<template>
  <div ref="mapContainer" class="storefront-map"></div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';
import L, { type LayerGroup, type LatLngExpression, type Map as LeafletMap, type Marker as LeafletMarker } from 'leaflet';
import 'leaflet/dist/leaflet.css';

import type { IPropertyMarker } from '@/shared/model/property-marker.model';
import { MAP_ATTRIBUTION, MAP_TILE_URL } from '@/constants';

type MarkerMeta = IPropertyMarker & { propertyId?: number | null };

const props = defineProps<{
  markers: MarkerMeta[];
  highlightId?: number | null;
  linkBase?: string | null;
}>();

const mapContainer = ref<HTMLDivElement | null>(null);
const mapInstance = ref<LeafletMap | null>(null);
const markerLayer = ref<LayerGroup | null>(null);
const markerLookup = new Map<number | string, LeafletMarker>();

const escapeHtml = (value: string) =>
  value.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');

const buildMarkerIcon = (highlighted = false) =>
  L.divIcon({
    className: highlighted ? 'property-marker property-marker--highlight' : 'property-marker',
    html: '<span class="property-marker__pin"></span>',
    iconSize: [24, 24],
    iconAnchor: [12, 24],
    popupAnchor: [0, -24],
  });

const initializeMap = () => {
  if (!mapContainer.value) {
    return;
  }
  mapInstance.value = L.map(mapContainer.value, {
    zoomControl: true,
    scrollWheelZoom: false,
  }).setView([25.2048, 55.2708], 10);

  L.tileLayer(MAP_TILE_URL, {
    attribution: MAP_ATTRIBUTION,
  }).addTo(mapInstance.value);

  markerLayer.value = L.layerGroup().addTo(mapInstance.value);
  updateMarkers();
  window.setTimeout(() => mapInstance.value?.invalidateSize(), 50);
};

const updateMarkers = () => {
  if (!mapInstance.value || !markerLayer.value) {
    return;
  }

  markerLayer.value.clearLayers();
  markerLookup.clear();

  if (!props.markers?.length) {
    return;
  }

  const group: LatLngExpression[] = [];

  props.markers.forEach((marker, index) => {
    if (typeof marker.latitude !== 'number' || typeof marker.longitude !== 'number') {
      return;
    }
    const latLng: LatLngExpression = [marker.latitude, marker.longitude];
    group.push(latLng);

    const markerId = marker.propertyId ?? marker.id ?? marker.extId ?? `${index}-${marker.latitude}-${marker.longitude}`;
    const isHighlighted = typeof props.highlightId === 'number' && marker.propertyId === props.highlightId;

    const leafletMarker = L.marker(latLng, { icon: buildMarkerIcon(isHighlighted) }).addTo(markerLayer.value);

    const name = escapeHtml(marker.name ?? 'Property');
    const locationLabel = escapeHtml([marker.city, marker.country].filter(Boolean).join(', '));
    const metaFragments: string[] = [];
    if (marker.listingType) {
      metaFragments.push(escapeHtml(marker.listingType.toString().replace(/_/g, ' ')));
    }
    if (marker.status) {
      metaFragments.push(escapeHtml(marker.status.toString().replace(/_/g, ' ')));
    }
    if (marker.developer) {
      metaFragments.push(escapeHtml(marker.developer));
    }
    const currency = marker.priceCurrency ?? 'AED';
    const price = typeof marker.minPrice === 'number' ? `${currency} ${marker.minPrice.toLocaleString()}` : 'Price on request';
    const summary = marker.summary ? escapeHtml(marker.summary) : '';

    let popupHtml = `<div class="map-popup"><h3 class="map-popup__title">${name}</h3>`;
    if (locationLabel.length) {
      popupHtml += `<p class="map-popup__location">${locationLabel}</p>`;
    }
    popupHtml += `<p class="map-popup__price">${price}</p>`;
    if (metaFragments.length) {
      popupHtml += `<p class="map-popup__meta">${metaFragments.join(' &bull; ')}</p>`;
    }
    if (summary.length) {
      popupHtml += `<p class="map-popup__summary">${summary}</p>`;
    }
    if (props.linkBase && marker.propertyId) {
      popupHtml += `<a class="map-popup__cta" href="${props.linkBase}/${marker.propertyId}" target="_blank" rel="noopener">View details</a>`;
    }
    popupHtml += '</div>';

    leafletMarker.bindPopup(popupHtml, { minWidth: 200 });
    markerLookup.set(markerId, leafletMarker);
  });

  if (group.length === 1) {
    mapInstance.value.setView(group[0], 14);
  } else if (group.length > 1) {
    mapInstance.value.fitBounds(group, { padding: [20, 20] });
  }
};

const updateHighlightState = () => {
  markerLookup.forEach((marker, key) => {
    const isHighlighted = typeof props.highlightId === 'number' && key === props.highlightId;
    marker.setIcon(buildMarkerIcon(isHighlighted));
    if (isHighlighted) {
      marker.openPopup();
    } else {
      marker.closePopup();
    }
  });
};

watch(
  () => props.highlightId,
  () => {
    updateHighlightState();
  },
);

watch(
  () => props.linkBase,
  () => {
    updateMarkers();
  },
);

watch(
  () => props.markers,
  () => {
    updateMarkers();
    updateHighlightState();
  },
  { deep: true },
);

onMounted(() => {
  initializeMap();
});

onBeforeUnmount(() => {
  markerLookup.clear();
  mapInstance.value?.remove();
});
</script>

<style scoped>
.storefront-map {
  width: 100%;
  min-height: 320px;
  border-radius: 1rem;
  overflow: hidden;
  box-shadow: inset 0 0 0 1px rgba(13, 35, 70, 0.08);
}

.property-marker {
  border: 0;
  background: transparent;
}

.property-marker__pin {
  display: block;
  width: 18px;
  height: 18px;
  border-radius: 50% 50% 50% 0;
  background: #2b7cff;
  transform: rotate(-45deg);
  box-shadow: 0 0 0 4px rgba(43, 124, 255, 0.18);
}

.property-marker--highlight .property-marker__pin {
  background: #ff7043;
  box-shadow: 0 0 0 6px rgba(255, 112, 67, 0.22);
}

.map-popup {
  font-size: 0.875rem;
  color: #12243c;
}

.map-popup__title {
  font-size: 1rem;
  margin-bottom: 0.25rem;
}

.map-popup__location {
  margin-bottom: 0.25rem;
  color: #51617d;
  font-size: 0.85rem;
}

.map-popup__price {
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.map-popup__meta {
  margin-bottom: 0.35rem;
  color: #1a2f55;
  font-size: 0.85rem;
}

.map-popup__summary {
  margin-bottom: 0.5rem;
  color: #4a5a75;
}

.map-popup__cta {
  display: inline-block;
  font-weight: 600;
  color: #1b4de4;
}

.map-popup__cta:hover {
  text-decoration: underline;
}
</style>

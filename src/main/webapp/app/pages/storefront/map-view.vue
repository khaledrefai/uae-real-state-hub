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
/* Modern Map View Styling */
.storefront-map {
  width: 100%;
  min-height: 400px;
  border-radius: 1.75rem;
  overflow: hidden;
  box-shadow:
    0 10px 40px rgba(0, 0, 0, 0.12),
    inset 0 0 0 2px rgba(102, 126, 234, 0.1);
  border: 2px solid rgba(102, 126, 234, 0.15);
  transition: all 0.3s ease;
}

.storefront-map:hover {
  box-shadow:
    0 15px 50px rgba(0, 0, 0, 0.18),
    inset 0 0 0 2px rgba(102, 126, 234, 0.2);
}

/* Modern Property Markers */
.property-marker {
  border: 0;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.property-marker__pin {
  display: block;
  width: 22px;
  height: 22px;
  border-radius: 50% 50% 50% 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transform: rotate(-45deg);
  box-shadow:
    0 0 0 4px rgba(102, 126, 234, 0.25),
    0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
  position: relative;
}

.property-marker__pin::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) rotate(45deg);
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
}

.property-marker:hover .property-marker__pin {
  width: 26px;
  height: 26px;
  box-shadow:
    0 0 0 6px rgba(102, 126, 234, 0.35),
    0 6px 20px rgba(102, 126, 234, 0.5);
}

.property-marker--highlight .property-marker__pin {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  box-shadow:
    0 0 0 6px rgba(240, 147, 251, 0.3),
    0 6px 25px rgba(245, 87, 108, 0.5);
  animation: markerPulse 2s ease-in-out infinite;
}

@keyframes markerPulse {
  0%,
  100% {
    transform: rotate(-45deg) scale(1);
  }
  50% {
    transform: rotate(-45deg) scale(1.15);
  }
}

/* Modern Map Popup */
.map-popup {
  font-size: 0.9rem;
  color: #2d3748;
  font-family: inherit;
  max-width: 300px;
}

.map-popup__title {
  font-size: 1.15rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
  color: #2d3748;
  line-height: 1.3;
}

.map-popup__location {
  margin-bottom: 0.5rem;
  color: #718096;
  font-size: 0.875rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.35rem;
}

.map-popup__location::before {
  content: '📍';
  font-size: 0.9rem;
}

.map-popup__price {
  font-weight: 900;
  margin-bottom: 0.5rem;
  font-size: 1.25rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.map-popup__meta {
  margin-bottom: 0.5rem;
  color: #4a5568;
  font-size: 0.875rem;
  font-weight: 600;
  padding: 0.5rem;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  border-radius: 0.5rem;
  border-left: 3px solid #667eea;
}

.map-popup__summary {
  margin-bottom: 0.75rem;
  color: #718096;
  line-height: 1.6;
  font-weight: 500;
}

.map-popup__cta {
  display: inline-block;
  font-weight: 700;
  color: white;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0.5rem 1.25rem;
  border-radius: 0.75rem;
  text-decoration: none;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-size: 0.8rem;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.map-popup__cta:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  text-decoration: none;
}

/* Map Controls Styling (if using default Leaflet controls) */
:global(.leaflet-control-zoom a) {
  background: white !important;
  border: 2px solid rgba(102, 126, 234, 0.2) !important;
  color: #667eea !important;
  font-weight: 700 !important;
  transition: all 0.3s ease !important;
  border-radius: 0.5rem !important;
}

:global(.leaflet-control-zoom a:hover) {
  background: #667eea !important;
  color: white !important;
  border-color: #667eea !important;
}

:global(.leaflet-popup-content-wrapper) {
  background: rgba(255, 255, 255, 0.98) !important;
  backdrop-filter: blur(20px) !important;
  -webkit-backdrop-filter: blur(20px) !important;
  border-radius: 1.25rem !important;
  box-shadow: 0 15px 45px rgba(0, 0, 0, 0.2) !important;
  border: 2px solid rgba(102, 126, 234, 0.1) !important;
  padding: 0.5rem !important;
}

:global(.leaflet-popup-tip) {
  background: rgba(255, 255, 255, 0.98) !important;
  border: 2px solid rgba(102, 126, 234, 0.1) !important;
}
</style>

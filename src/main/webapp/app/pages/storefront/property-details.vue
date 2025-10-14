<template>
  <div v-if="loading" class="text-center py-5">
    <div class="spinner-border text-primary" role="status"></div>
    <p class="mt-3">Loading property...</p>
  </div>
  <div v-else-if="!property" class="container py-5 text-center">
    <h1 class="display-4 mb-3">Property not found</h1>
    <router-link class="btn btn-outline-primary" :to="`/store/${site?.slug}`">Back to storefront</router-link>
  </div>
  <div v-else class="property-view">
    <section class="property-hero" :style="coverStyle">
      <div class="hero-overlay"></div>
      <div class="container py-5 position-relative text-light">
        <router-link class="btn btn-outline-light btn-sm mb-3" :to="`/store/${site?.slug}`">&larr; Back to listings</router-link>
        <div class="hero-badges mb-3">
          <span v-if="property.listingType" class="badge badge-pill badge-light mr-2">{{ listingTypeLabel(property.listingType) }}</span>
          <span v-if="property.status" class="badge badge-pill badge-success mr-2">{{ statusLabel(property.status) }}</span>
          <span v-if="property.saleStatus" class="badge badge-pill badge-info">{{ statusLabel(property.saleStatus) }}</span>
        </div>
        <h1 class="display-4 font-weight-bold mb-3">{{ property.name }}</h1>
        <p class="lead mb-4">
          <span v-if="property.city">{{ property.city }}</span>
          <span v-if="property.city && property.country">, </span>
          <span v-if="property.country">{{ property.country }}</span>
        </p>
        <div class="hero-price">
          <div class="h2 mb-1">{{ priceLabel }}</div>
          <small v-if="areaRange !== '-'" class="text-light">Available from {{ areaRange }}</small>
        </div>
      </div>
    </section>

    <main class="container py-5">
      <section class="card border-0 shadow-sm mb-4">
        <div class="card-body">
          <div class="row">
            <div class="col-lg-8 mb-4 mb-lg-0">
              <h2 class="h5 mb-3">Overview</h2>
              <p v-if="!overviewSections.length" class="text-muted mb-0">
                An overview is being prepared. Contact us to receive the latest brochure and availability.
              </p>
              <div v-else>
                <div
                  v-for="(section, sectionIndex) in overviewSections"
                  :key="(section.heading || 'section') + '-' + sectionIndex"
                  class="overview-block"
                >
                  <h3 v-if="section.heading" :class="['overview-heading', { 'overview-heading--highlight': section.highlight }]">
                    {{ section.heading }}
                  </h3>
                  <p
                    v-for="(paragraph, paragraphIndex) in section.content"
                    :key="sectionIndex + '-' + paragraphIndex + '-' + paragraph"
                    class="text-muted mb-2"
                  >
                    {{ paragraph }}
                  </p>
                </div>
              </div>
            </div>
            <div class="col-lg-4">
              <div class="sticky-aside">
                <div class="card border-0 shadow-sm mb-4 mb-lg-3">
                  <div class="card-body">
                    <h2 class="h5 mb-3">Talk to {{ site?.displayName }}</h2>
                    <p class="text-muted">Request availability, payment plans, and viewing schedules.</p>
                    <ul class="list-unstyled small mb-4">
                      <li v-if="site?.contactPhone" class="mb-2">
                        <strong>Phone:</strong> <a :href="`tel:${site.contactPhone}`">{{ site.contactPhone }}</a>
                      </li>
                      <li v-if="site?.contactEmail" class="mb-2">
                        <strong>Email:</strong> <a :href="`mailto:${site.contactEmail}`">{{ site.contactEmail }}</a>
                      </li>
                      <li v-if="site?.contactWhatsapp" class="mb-2">
                        <strong>WhatsApp:</strong> <a :href="whatsappUrl" target="_blank" rel="noopener">Chat now</a>
                      </li>
                    </ul>
                    <form class="lead-form" @submit.prevent="submitLead">
                      <div class="form-group">
                        <label for="leadName">Your name</label>
                        <input
                          id="leadName"
                          v-model.trim="leadForm.name"
                          type="text"
                          class="form-control"
                          required
                          placeholder="Your full name"
                        />
                      </div>
                      <div class="form-group">
                        <label for="leadEmail">Email</label>
                        <input
                          id="leadEmail"
                          v-model.trim="leadForm.email"
                          type="email"
                          class="form-control"
                          placeholder="you@example.com"
                        />
                      </div>
                      <div class="form-group">
                        <label for="leadPhone">Phone or WhatsApp</label>
                        <input
                          id="leadPhone"
                          v-model.trim="leadForm.phone"
                          type="tel"
                          class="form-control"
                          placeholder="+971 50 000 0000"
                        />
                      </div>
                      <div class="form-group">
                        <label for="leadMessage">Message</label>
                        <textarea
                          id="leadMessage"
                          v-model.trim="leadForm.message"
                          rows="3"
                          class="form-control"
                          placeholder="Tell us what you are looking for"
                        ></textarea>
                      </div>
                      <button class="btn btn-primary btn-block" type="submit" :disabled="submittingLead || !leadIsValid">
                        <span v-if="submittingLead">Submitting...</span>
                        <span v-else>Request a call back</span>
                      </button>
                    </form>
                    <small class="text-muted d-block mt-2">We respond within one business hour.</small>
                  </div>
                </div>

                <div class="card border-0 shadow-sm" v-if="downloads.length">
                  <div class="card-body">
                    <h2 class="h6 text-uppercase text-muted mb-3">Downloads</h2>
                    <div class="d-flex flex-column">
                      <a
                        v-for="item in downloads"
                        :key="item.label"
                        :href="item.url"
                        class="btn btn-outline-primary btn-sm mb-2"
                        target="_blank"
                        rel="noopener"
                      >
                        {{ item.label }}
                      </a>
                    </div>
                  </div>
                </div>

                <div class="card border-0 shadow-sm">
                  <div class="card-body">
                    <h2 class="h6 text-uppercase text-muted mb-3">At a glance</h2>
                    <ul class="list-unstyled mb-0 small">
                      <li class="mb-2"><strong>Completion:</strong> {{ completion }}</li>
                      <li class="mb-2"><strong>Developer:</strong> {{ property.developer || '-' }}</li>
                      <li class="mb-2"><strong>Readiness:</strong> {{ property.readiness || '-' }}</li>
                      <li class="mb-0"><strong>Updated:</strong> {{ lastUpdated }}</li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <div class="row">
        <div class="col-12">
          <section class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h2 class="h5 mb-3">Key facts</h2>
              <div class="row text-center text-md-left">
                <div class="col-md-4 mb-3" v-for="fact in keyFacts" :key="fact.label">
                  <div class="fact-label text-uppercase small text-muted">{{ fact.label }}</div>
                  <div class="fact-value h5 mb-0">{{ fact.value }}</div>
                </div>
              </div>
            </div>
          </section>

          <section class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h2 class="h5 mb-3">Highlights</h2>
              <dl class="row mb-0">
                <template v-for="highlight in highlights" :key="highlight.label">
                  <dt class="col-sm-4 text-muted">{{ highlight.label }}</dt>
                  <dd class="col-sm-8 mb-3 mb-sm-2">{{ highlight.value }}</dd>
                </template>
              </dl>
            </div>
          </section>

          <section v-if="architectureSlides.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <div class="section-heading d-flex justify-content-between align-items-center mb-3">
                <h2 class="h5 mb-0">Architecture</h2>
                <div class="carousel-controls" v-if="architectureSlides.length > 1">
                  <button class="carousel-button" type="button" @click="advanceArchitecture(-1)" aria-label="Previous architecture image">
                    &lsaquo;
                  </button>
                  <span class="carousel-indicator">{{ architectureIndex + 1 }} / {{ architectureSlides.length }}</span>
                  <button class="carousel-button" type="button" @click="advanceArchitecture(1)" aria-label="Next architecture image">
                    &rsaquo;
                  </button>
                </div>
              </div>
              <div class="media-carousel">
                <div
                  v-for="(slide, slideIndex) in architectureSlides"
                  :key="slide.url"
                  class="carousel-slide"
                  v-show="architectureIndex === slideIndex"
                >
                  <img :src="slide.url" :alt="slide.alt || 'Architecture rendering'" class="media-carousel__image" loading="lazy" />
                </div>
              </div>
            </div>
          </section>

          <section v-if="coverSlides.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <div class="section-heading d-flex justify-content-between align-items-center mb-3">
                <h2 class="h5 mb-0">Cover</h2>
                <div class="carousel-controls" v-if="coverSlides.length > 1">
                  <button class="carousel-button" type="button" @click="advanceCover(-1)" aria-label="Previous cover image">
                    &lsaquo;
                  </button>
                  <span class="carousel-indicator">{{ coverIndex + 1 }} / {{ coverSlides.length }}</span>
                  <button class="carousel-button" type="button" @click="advanceCover(1)" aria-label="Next cover image">&rsaquo;</button>
                </div>
              </div>
              <div class="media-carousel">
                <div v-for="(slide, slideIndex) in coverSlides" :key="slide.url" class="carousel-slide" v-show="coverIndex === slideIndex">
                  <img :src="slide.url" :alt="slide.alt || 'Project cover'" class="media-carousel__image" loading="lazy" />
                </div>
              </div>
            </div>
          </section>

          <section v-if="buildingSlides.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <div class="section-heading d-flex justify-content-between align-items-center mb-3">
                <h2 class="h5 mb-0">Buildings</h2>
                <div class="carousel-controls" v-if="buildingSlides.length > 1">
                  <button class="carousel-button" type="button" @click="advanceBuildings(-1)" aria-label="Previous building slide">
                    &lsaquo;
                  </button>
                  <span class="carousel-indicator">{{ buildingIndex + 1 }} / {{ buildingSlides.length }}</span>
                  <button class="carousel-button" type="button" @click="advanceBuildings(1)" aria-label="Next building slide">
                    &rsaquo;
                  </button>
                </div>
              </div>
              <div class="media-carousel">
                <div
                  v-for="(slide, slideIndex) in buildingSlides"
                  :key="slide.url"
                  class="carousel-slide"
                  v-show="buildingIndex === slideIndex"
                >
                  <img
                    :src="slide.url"
                    :alt="slide.alt || slide.caption || 'Building rendering'"
                    class="media-carousel__image"
                    loading="lazy"
                  />
                  <div v-if="slide.caption || slide.description" class="carousel-slide__overlay">
                    <h3 v-if="slide.caption" class="carousel-slide__overlay-title">{{ slide.caption }}</h3>
                    <p v-if="slide.description" class="carousel-slide__overlay-text">
                      {{ slide.description }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section v-if="facilitySlides.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <div class="section-heading d-flex justify-content-between align-items-center mb-3">
                <h2 class="h5 mb-0">Facilities</h2>
                <div class="carousel-controls" v-if="facilitySlides.length > 1">
                  <button class="carousel-button" type="button" @click="advanceFacilities(-1)" aria-label="Previous facility">
                    &lsaquo;
                  </button>
                  <span class="carousel-indicator">{{ facilityIndex + 1 }} / {{ facilitySlides.length }}</span>
                  <button class="carousel-button" type="button" @click="advanceFacilities(1)" aria-label="Next facility">&rsaquo;</button>
                </div>
              </div>
              <div class="media-carousel">
                <div
                  v-for="(slide, slideIndex) in facilitySlides"
                  :key="slide.url"
                  class="carousel-slide"
                  v-show="facilityIndex === slideIndex"
                >
                  <img
                    :src="slide.url"
                    :alt="slide.alt || slide.caption || 'Community facility'"
                    class="media-carousel__image"
                    loading="lazy"
                  />
                  <div v-if="slide.caption || slide.description" class="carousel-slide__overlay">
                    <h3 v-if="slide.caption" class="carousel-slide__overlay-title">{{ slide.caption }}</h3>
                    <p v-if="slide.description" class="carousel-slide__overlay-text">
                      {{ slide.description }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section v-if="interiorSlides.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <div class="section-heading d-flex justify-content-between align-items-center mb-3">
                <h2 class="h5 mb-0">Interior</h2>
                <div class="carousel-controls" v-if="interiorSlides.length > 1">
                  <button class="carousel-button" type="button" @click="advanceInterior(-1)" aria-label="Previous interior rendering">
                    &lsaquo;
                  </button>
                  <span class="carousel-indicator">{{ interiorIndex + 1 }} / {{ interiorSlides.length }}</span>
                  <button class="carousel-button" type="button" @click="advanceInterior(1)" aria-label="Next interior rendering">
                    &rsaquo;
                  </button>
                </div>
              </div>
              <div class="media-carousel">
                <div
                  v-for="(slide, slideIndex) in interiorSlides"
                  :key="slide.url"
                  class="carousel-slide"
                  v-show="interiorIndex === slideIndex"
                >
                  <img :src="slide.url" :alt="slide.alt || 'Interior rendering'" class="media-carousel__image" loading="lazy" />
                </div>
              </div>
            </div>
          </section>

          <section v-if="lobbySlides.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <div class="section-heading d-flex justify-content-between align-items-center mb-3">
                <h2 class="h5 mb-0">Lobby</h2>
                <div class="carousel-controls" v-if="lobbySlides.length > 1">
                  <button class="carousel-button" type="button" @click="advanceLobby(-1)" aria-label="Previous lobby">&lsaquo;</button>
                  <span class="carousel-indicator">{{ lobbyIndex + 1 }} / {{ lobbySlides.length }}</span>
                  <button class="carousel-button" type="button" @click="advanceLobby(1)" aria-label="Next lobby">&rsaquo;</button>
                </div>
              </div>
              <div class="media-carousel">
                <div v-for="(slide, slideIndex) in lobbySlides" :key="slide.url" class="carousel-slide" v-show="lobbyIndex === slideIndex">
                  <img :src="slide.url" :alt="slide.alt || 'Lobby rendering'" class="media-carousel__image" loading="lazy" />
                </div>
              </div>
            </div>
          </section>

          <section v-if="specificationSections.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h2 class="h5 mb-3">Specifications</h2>
              <div
                v-for="(section, sectionIndex) in specificationSections"
                :key="section.title + '-' + sectionIndex"
                :class="['spec-section', { 'spec-section--highlight': section.highlight }]"
              >
                <span class="spec-section__title">{{ section.title }}</span>
                <ul class="list-unstyled spec-section__list mb-0">
                  <li
                    v-for="(entry, entryIndex) in section.entries"
                    :key="section.title + '-' + entry.label + '-' + entryIndex"
                    class="spec-entry"
                  >
                    <div v-if="entry.label" class="spec-entry__label">{{ entry.label }}</div>
                    <div :class="['spec-entry__content', { 'spec-entry__content--full': !entry.label }]">
                      <template v-if="entry.valueType === 'image' && entry.url">
                        <img :src="entry.url" :alt="entry.alt || entry.label || section.title" class="spec-entry__image" loading="lazy" />
                      </template>
                      <template v-else-if="entry.valueType === 'link' && entry.url">
                        <a :href="entry.url" class="spec-entry__link" target="_blank" rel="noopener">
                          {{ entry.value || entry.label || 'View link' }}
                        </a>
                      </template>
                      <template v-else-if="entry.valueType === 'download' && entry.url">
                        <a :href="entry.url" class="spec-entry__link spec-entry__link--download" target="_blank" rel="noopener" download>
                          {{ entry.value || entry.label || 'Download file' }}
                        </a>
                      </template>
                      <template v-else>
                        <span class="spec-entry__value">{{ entry.value }}</span>
                      </template>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </section>

          <section v-if="paymentPlanDetails.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h2 class="h5 mb-3">Payment plans</h2>
              <div v-for="plan in paymentPlanDetails" :key="plan.planName" class="payment-plan mb-4">
                <div class="d-flex justify-content-between align-items-center mb-2">
                  <h3 class="h6 text-uppercase text-muted mb-0">{{ plan.planName }}</h3>
                  <span
                    v-if="typeof plan.monthsAfterHandover === 'number' && plan.monthsAfterHandover > 0"
                    class="badge badge-light text-uppercase"
                  >
                    +{{ plan.monthsAfterHandover }} months post-handover
                  </span>
                </div>
                <div class="table-responsive">
                  <table class="table table-sm payment-table mb-0">
                    <thead>
                      <tr>
                        <th scope="col">Stage</th>
                        <th scope="col">Timing</th>
                        <th scope="col">Percent</th>
                        <th scope="col">Amount</th>
                        <th scope="col">Notes</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(row, rowIndex) in plan.schedule" :key="plan.planName + '-' + rowIndex">
                        <th scope="row">{{ row.order ?? rowIndex + 1 }}</th>
                        <td>{{ row.time || '—' }}</td>
                        <td>{{ row.percent || '—' }}</td>
                        <td>{{ row.amount || '—' }}</td>
                        <td>{{ row.note || '—' }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </section>

          <section v-if="unitBlockRows.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h2 class="h5 mb-3">Unit blocks</h2>
              <div v-for="(row, rowIndex) in unitBlockRows" :key="`unit-block-${rowIndex}`" class="unit-block mb-4">
                <h3 class="unit-block__title">
                  Unit block {{ rowIndex + 1 }}
                  <span v-if="row.id" class="unit-block__subtitle">#{{ formatIdentifier(row.id) }}</span>
                </h3>
                <div class="table-responsive">
                  <table class="table table-sm unit-block-table mb-3">
                    <tbody>
                      <tr>
                        <th scope="row">Area unit</th>
                        <td>{{ row.areaUnit || 'N/A' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Area (sq.ft)</th>
                        <td>{{ formatAreaRange(row.areaFrom, row.areaTo, row.areaUnit) }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Area from (m²)</th>
                        <td>{{ formatAreaValue(row.areaFromM2, 'm²') }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Area to (m²)</th>
                        <td>{{ formatAreaValue(row.areaToM2, 'm²') }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Bedrooms</th>
                        <td>{{ row.bedrooms || 'N/A' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Bedrooms amount</th>
                        <td>{{ row.bedroomsAmount || 'N/A' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Normalized type</th>
                        <td>{{ row.normalizedType || 'N/A' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Unit type</th>
                        <td>{{ row.unitType || row.normalizedType || 'N/A' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Units amount</th>
                        <td>{{ formatCount(row.unitsAmount) }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Price currency</th>
                        <td>{{ row.priceCurrency || property.value?.priceCurrency || 'N/A' }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Price from</th>
                        <td>{{ formatPriceValue(row.priceFrom, row.priceCurrency) }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Price to</th>
                        <td>{{ formatPriceValue(row.priceTo, row.priceCurrency) }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Price from (AED)</th>
                        <td>{{ formatPriceValue(row.priceFromAed, 'AED') }}</td>
                      </tr>
                      <tr>
                        <th scope="row">Price to (AED)</th>
                        <td>{{ formatPriceValue(row.priceToAed, 'AED') }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </section>

          <section v-if="unitAvailabilityRows.length" class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h2 class="h5 mb-3">Unit availability</h2>
              <div class="table-responsive">
                <table class="table table-sm unit-table mb-0">
                  <thead>
                    <tr>
                      <th scope="col">Building</th>
                      <th scope="col">Bedrooms</th>
                      <th scope="col">Area from</th>
                      <th scope="col">Price range</th>
                      <th scope="col">Units</th>
                      <th scope="col">Updated</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(row, rowIndex) in unitAvailabilityRows" :key="rowIndex">
                      <td>{{ row.buildingName || '—' }}</td>
                      <td>{{ row.bedroomType || '—' }}</td>
                      <td>{{ formatArea(row.areaFrom, row.areaUnit) }}</td>
                      <td>{{ formatPriceRange(row.priceFrom, row.priceTo, row.currency) }}</td>
                      <td>{{ row.unitsAvailable ?? '—' }}</td>
                      <td>{{ row.lastUpdated || '—' }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </section>

          <section class="card border-0 shadow-sm">
            <div class="card-body">
              <h2 class="h5 mb-3">Location</h2>
              <map-view
                v-if="markers.length"
                :markers="markers"
                :highlight-id="property.id ?? null"
                :link-base="propertyLinkBase"
              ></map-view>
              <p v-else class="text-muted mb-0">Location details will be available soon.</p>
              <ul v-if="mapPointDisplayDetails.length" class="map-points list-unstyled mt-3 mb-0">
                <li v-for="point in mapPointDisplayDetails" :key="point.name" class="map-points__item">
                  <span class="map-points__name">{{ point.name }}</span>
                  <span v-if="point.distanceLabel" class="map-points__distance">
                    {{ point.distanceLabel }}
                  </span>
                </li>
              </ul>
            </div>
          </section>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import dayjs from 'dayjs';
import { computed, onMounted, reactive, ref, watch, type ComputedRef } from 'vue';
import { useRoute } from 'vue-router';

import MapView from '@/pages/storefront/map-view.vue';
import { useAgentSiteStore } from '@/store';
import { useAlertService } from '@/shared/alert/alert.service';
import type { IProperty } from '@/shared/model/property.model';
import type { IPropertyMarker } from '@/shared/model/property-marker.model';

const route = useRoute();
const agentSiteStore = useAgentSiteStore();
const alertService = useAlertService();
const leadForm = reactive({ name: '', email: '', phone: '', message: '' });
const submittingLead = ref(false);
const leadIsValid = computed(() => Boolean(leadForm.name && (leadForm.email || leadForm.phone)));

const loading = ref(true);
const property = ref<IProperty | null>(null);

const site = computed(() => agentSiteStore.site);

const listingTypeLabel = (value?: string | null) => {
  if (!value) {
    return VALUE_NA;
  }
  const dictionary: Record<string, string> = {
    SALE: 'For sale',
    RENT: 'For rent',
    OFF_PLAN: 'Off-plan',
  };
  return dictionary[value] ?? value.replace(/_/g, ' ').replace(/\b\w/g, letter => letter.toUpperCase());
};

const statusLabel = (value?: string | null) => {
  if (!value) {
    return VALUE_NA;
  }
  return value
    .toString()
    .split('_')
    .map(word => word.charAt(0) + word.slice(1).toLowerCase())
    .join(' ');
};

const propertyLinkBase = computed(() => (site.value?.slug ? `/store/${site.value.slug}/properties` : ''));

const markers = computed<IPropertyMarker[]>(() => {
  if (!property.value) {
    return [];
  }
  const baseMarker = property.value.marker ?? {
    latitude: property.value.latitude ?? undefined,
    longitude: property.value.longitude ?? undefined,
  };
  if (typeof baseMarker.latitude !== 'number' || typeof baseMarker.longitude !== 'number') {
    return [];
  }
  const currency = baseMarker.priceCurrency ?? property.value.priceCurrency ?? 'AED';
  const price = property.value.minPrice ?? baseMarker.minPrice ?? null;
  const summarySource = property.value.overviewMd
    ? property.value.overviewMd.replace(/[#*`]/g, '').replace(/\s+/g, ' ').trim().slice(0, 160)
    : (baseMarker.summary ?? null);

  return [
    {
      ...baseMarker,
      propertyId: property.value.id,
      name: baseMarker.name ?? property.value.name ?? 'Property',
      minPrice: price,
      priceCurrency: currency,
      summary: summarySource ?? null,
      city: baseMarker.city ?? property.value.city ?? null,
      country: baseMarker.country ?? property.value.country ?? null,
    },
  ];
});

const whatsappUrl = computed(() => {
  if (!site.value?.contactWhatsapp) {
    return '#';
  }
  const digits = site.value.contactWhatsapp.replace(/[^0-9]+/g, '');
  return `https://wa.me/${digits}`;
});

const coverStyle = computed(() => ({
  backgroundImage: property.value?.coverUrl ? `url(${property.value.coverUrl})` : undefined,
}));

interface OverviewSection {
  heading?: string;
  content: string[];
  highlight: boolean;
}

const HIGHLIGHTED_OVERVIEW_HEADINGS = new Set(
  ['Project general facts', 'Finishing and materials', 'Kitchen and appliances', 'Furnishing', 'Location description and benefits'].map(
    item => item.toLowerCase(),
  ),
);

const overviewSections = computed<OverviewSection[]>(() => {
  const source = property.value?.overviewMd;
  if (!source) {
    return [];
  }

  const sections: OverviewSection[] = [];
  const lines = source.replace(/\r\n/g, '\n').split('\n');

  let current: OverviewSection | null = null;

  const commitCurrent = () => {
    if (current && (current.heading || current.content.length)) {
      current.content = current.content.filter(Boolean);
      if (current.content.length || current.heading) {
        sections.push(current);
      }
    }
    current = null;
  };

  lines.forEach(rawLine => {
    const line = rawLine.trim();
    if (!line.length) {
      return;
    }

    const headingMatch = line.match(/^#{2,6}\s*(.+)$/);
    if (headingMatch) {
      commitCurrent();
      const heading = headingMatch[1].replace(/[*`]/g, '').trim();
      current = {
        heading,
        content: [],
        highlight: HIGHLIGHTED_OVERVIEW_HEADINGS.has(heading.toLowerCase()),
      };
      return;
    }

    if (!current) {
      current = { heading: undefined, content: [], highlight: false };
    }

    current.content.push(line.replace(/[*`]/g, '').trim());
  });

  commitCurrent();

  return sections;
});

const areaRange = computed(() => {
  const min = property.value?.minArea;
  const max = property.value?.maxArea;
  if (!min && !max) {
    return VALUE_NA;
  }
  const unit = property.value?.areaUnit || 'sq.ft';
  if (min && max && min !== max) {
    return `${min.toLocaleString()} - ${max.toLocaleString()} ${unit}`;
  }
  return `${(min ?? max)?.toLocaleString()} ${unit}`;
});

const priceLabel = computed(() => {
  if (!property.value) {
    return 'Price on request';
  }
  const currency = property.value.priceCurrency ?? 'AED';
  const min = property.value.minPrice;
  const max = property.value.maxPrice;
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
});

const completion = computed(() => {
  if (!property.value?.completionDateTime) {
    return 'To be confirmed';
  }
  return dayjs(property.value.completionDateTime).format('MMM YYYY');
});

const lastUpdated = computed(() => {
  const timestamp = property.value?.updatedAt ?? property.value?.publishedAt;
  if (!timestamp) {
    return 'Today';
  }
  const parsed = dayjs(timestamp);
  if (!parsed.isValid()) {
    return 'Today';
  }
  if (parsed.isSame(dayjs(), 'day')) {
    return 'Today';
  }
  return parsed.format('MMM DD, YYYY');
});

const keyFacts = computed(() => [
  { label: 'Listing type', value: listingTypeLabel(property.value?.listingType) },
  { label: 'Status', value: statusLabel(property.value?.status) },
  { label: 'Completion', value: completion.value },
]);

const highlights = computed(() => [
  { label: 'Developer', value: property.value?.developer || '-' },
  { label: 'Service charge', value: property.value?.serviceCharge || 'On request' },
  { label: 'Furnishing', value: property.value?.furnishing || '-' },
  { label: 'Escrow account', value: property.value?.hasEscrow ? 'Yes' : 'No' },
  { label: 'Post handover', value: property.value?.postHandover ? 'Available' : 'No' },
  { label: 'Readiness', value: property.value?.readiness || '-' },
]);

type SpecificationValueType = 'text' | 'image' | 'link' | 'download';

type SpecificationEntry = {
  label: string;
  value: string;
  valueType: SpecificationValueType;
  url?: string;
  alt?: string;
};

type SpecificationSection = {
  title: string;
  entries: SpecificationEntry[];
  highlight: boolean;
};

interface ImageSlide {
  url: string;
  alt?: string;
  caption?: string;
}

interface BuildingSlide extends ImageSlide {
  description?: string;
}

interface FacilitySlide extends ImageSlide {
  description?: string;
}

interface MapPointDetail {
  name: string;
  distanceKm?: number | null;
}

interface PaymentScheduleRow {
  order?: number | null;
  time?: string | null;
  percent?: string | null;
  amount?: string | number | null;
  note?: string | null;
}

interface PaymentPlanDetail {
  planName: string;
  monthsAfterHandover?: number | null;
  schedule: PaymentScheduleRow[];
}

interface UnitAvailabilityRow {
  buildingName?: string | null;
  bedroomType?: string | null;
  areaFrom?: number | null;
  areaUnit?: string | null;
  priceFrom?: number | null;
  priceTo?: number | null;
  currency?: string | null;
  unitsAvailable?: number | null;
  lastUpdated?: string | null;
}

interface UnitBlockRow {
  id?: string | number | null;
  bedrooms?: string | null;
  bedroomsAmount?: string | null;
  normalizedType?: string | null;
  unitType?: string | null;
  areaUnit?: string | null;
  areaFrom?: number | null;
  areaTo?: number | null;
  areaFromM2?: number | null;
  areaToM2?: number | null;
  priceCurrency?: string | null;
  priceFrom?: number | null;
  priceTo?: number | null;
  priceFromAed?: number | null;
  priceToAed?: number | null;
  unitsAmount?: number | null;
}

interface BuildOptions {
  sectionTitle: string;
  seenImages: Set<string>;
  downloads: { label: string; url: string }[];
  downloadUrls: Set<string>;
}

const HIGHLIGHTED_SPECIFICATION_TITLES = new Set(
  ['Project general facts', 'Finishing and materials', 'Kitchen and appliances', 'Furnishing', 'Location description and benefits'].map(
    item => item.toLowerCase(),
  ),
);

const IMAGE_KEY_HINTS = ['image', 'cover', 'render', 'logo', 'thumbnail', 'interior', 'architecture', 'facility', 'plan'];
const DOWNLOAD_KEY_HINTS = ['pdf', 'plan', 'layout', 'master'];

const formatTitle = (input: string): string => {
  const normalized = input.toString().replace(/[_-]+/g, ' ').replace(/\s+/g, ' ').trim();

  if (!normalized.length) {
    return 'Detail';
  }

  return normalized
    .split(' ')
    .map(part => part.charAt(0).toUpperCase() + part.slice(1))
    .join(' ');
};

const parseMaybeJson = (value: unknown): unknown => {
  if (typeof value !== 'string') {
    return value;
  }
  const trimmed = value.trim();
  if (!trimmed.length) {
    return trimmed;
  }
  if ((trimmed.startsWith('{') && trimmed.endsWith('}')) || (trimmed.startsWith('[') && trimmed.endsWith(']'))) {
    try {
      return JSON.parse(trimmed);
    } catch (error) {
      return trimmed;
    }
  }
  return trimmed;
};

const looksLikeUrl = (value: string) => /^https?:\/\//i.test(value);

const looksLikeImageUrl = (value: string, keyHint?: string | null, sectionTitle?: string) => {
  const loweredValue = value.toLowerCase();
  if (!looksLikeUrl(value)) {
    return false;
  }
  if (/\.(png|jpe?g|webp|gif|bmp|svg)(\?.*)?$/.test(loweredValue)) {
    return true;
  }
  const hint = `${keyHint ?? ''} ${sectionTitle ?? ''}`.toLowerCase();
  return IMAGE_KEY_HINTS.some(fragment => hint.includes(fragment));
};

const looksLikePdfUrl = (value: string, keyHint?: string | null) => {
  if (!looksLikeUrl(value)) {
    return false;
  }
  if (value.toLowerCase().includes('.pdf')) {
    return true;
  }
  const hint = keyHint?.toLowerCase() ?? '';
  return DOWNLOAD_KEY_HINTS.some(fragment => hint.includes(fragment));
};

const extractDateOnly = (value: string): string | null => {
  const isoDateMatch = value.match(/^(\d{4}-\d{2}-\d{2})(?:[T\s].*)?$/);
  if (isoDateMatch) {
    return isoDateMatch[1];
  }
  const parsed = dayjs(value);
  if (parsed.isValid()) {
    return parsed.format('YYYY-MM-DD');
  }
  return null;
};

const extractImageUrl = (payload: unknown): string | null => {
  if (!payload) {
    return null;
  }
  if (typeof payload === 'string') {
    const trimmed = payload.trim();
    if (!trimmed.length) {
      return null;
    }
    const parsed = parseMaybeJson(trimmed);
    if (typeof parsed === 'string') {
      return looksLikeUrl(parsed) ? parsed : null;
    }
    return extractImageUrl(parsed);
  }
  if (Array.isArray(payload)) {
    for (const item of payload) {
      const url = extractImageUrl(item);
      if (url) {
        return url;
      }
    }
    return null;
  }
  if (typeof payload === 'object') {
    const obj = payload as Record<string, unknown>;
    if (typeof obj.url === 'string') {
      return obj.url;
    }
    if (obj.image) {
      return extractImageUrl(obj.image);
    }
    if (obj.media) {
      return extractImageUrl(obj.media);
    }
  }
  return null;
};

const createPrimitiveEntry = (label: string, rawValue: unknown, key: string | null, options: BuildOptions): SpecificationEntry | null => {
  const parsedValue = parseMaybeJson(rawValue);

  if (parsedValue === null || parsedValue === undefined) {
    return null;
  }

  const normalizedKey = key ? key.toLowerCase() : '';
  let displayLabel = label || (normalizedKey ? formatTitle(normalizedKey) : '');

  if (typeof parsedValue === 'string') {
    const trimmed = parsedValue.trim();
    if (!trimmed.length) {
      return null;
    }

    const maybeDate = extractDateOnly(trimmed);
    if (maybeDate) {
      return {
        label: displayLabel,
        value: maybeDate,
        valueType: 'text',
      };
    }

    const normalizedUrlCandidate = trimmed.replace(/\\\//g, '/');

    if (looksLikeImageUrl(normalizedUrlCandidate, normalizedKey, options.sectionTitle)) {
      if (options.seenImages.has(normalizedUrlCandidate)) {
        return null;
      }
      options.seenImages.add(normalizedUrlCandidate);
      return {
        label: displayLabel,
        value: normalizedUrlCandidate,
        valueType: 'image',
        url: normalizedUrlCandidate,
        alt: displayLabel || formatTitle(options.sectionTitle),
      };
    }

    if (looksLikePdfUrl(normalizedUrlCandidate, normalizedKey)) {
      let downloadLabel = displayLabel || (normalizedKey ? formatTitle(normalizedKey) : 'Download');
      if (normalizedKey.includes('layout')) {
        downloadLabel = 'Layouts PDF';
      } else if (normalizedKey.includes('master')) {
        downloadLabel = 'Master plan';
      }
      if (downloadLabel.toLowerCase().includes('brochure')) {
        return null;
      }
      if (!options.downloadUrls.has(normalizedUrlCandidate)) {
        options.downloadUrls.add(normalizedUrlCandidate);
        options.downloads.push({ label: downloadLabel, url: normalizedUrlCandidate });
      }
      return {
        label: downloadLabel,
        value: downloadLabel,
        valueType: 'download',
        url: normalizedUrlCandidate,
      };
    }

    if (looksLikeUrl(normalizedUrlCandidate)) {
      const linkLabel = displayLabel || formatTitle(normalizedKey || 'Link');
      return {
        label: linkLabel,
        value: linkLabel,
        valueType: 'link',
        url: normalizedUrlCandidate,
      };
    }

    return {
      label: displayLabel,
      value: trimmed,
      valueType: 'text',
    };
  }

  if (typeof parsedValue === 'number') {
    return {
      label: displayLabel,
      value: Number.isFinite(parsedValue) ? parsedValue.toLocaleString() : '',
      valueType: 'text',
    };
  }

  if (typeof parsedValue === 'boolean') {
    return {
      label: displayLabel,
      value: parsedValue ? 'Yes' : 'No',
      valueType: 'text',
    };
  }

  return null;
};

const buildEntriesFromValue = (label: string, rawValue: unknown, key: string | null, options: BuildOptions): SpecificationEntry[] => {
  const parsedValue = parseMaybeJson(rawValue);

  if (Array.isArray(parsedValue)) {
    return entriesFromArray(parsedValue, key, label, options);
  }

  if (parsedValue && typeof parsedValue === 'object') {
    return entriesFromObject(parsedValue as Record<string, unknown>, key, label, options);
  }

  const entry = createPrimitiveEntry(label, parsedValue, key, options);
  return entry ? [entry] : [];
};

const entriesFromArray = (items: unknown[], key: string | null, parentLabel: string, options: BuildOptions): SpecificationEntry[] => {
  const entries: SpecificationEntry[] = [];
  items.forEach((item, index) => {
    const fallbackLabel = parentLabel || formatTitle(key ?? options.sectionTitle);
    const derivedLabel = items.length > 1 ? `${fallbackLabel} ${index + 1}` : fallbackLabel;
    entries.push(...buildEntriesFromValue(derivedLabel, item, key, options));
  });
  return entries;
};

const entriesFromObject = (
  source: Record<string, unknown>,
  key: string | null,
  parentLabel: string,
  options: BuildOptions,
): SpecificationEntry[] => {
  const entries: SpecificationEntry[] = [];
  const labelKeys = ['name', 'title', 'label', 'heading'];
  let baseLabel = parentLabel;

  labelKeys.forEach(labelKey => {
    const candidate = source[labelKey];
    if (typeof candidate === 'string' && candidate.trim().length) {
      baseLabel = candidate.trim();
    }
  });

  if (Array.isArray((source as Record<string, unknown>).options)) {
    entries.push(
      ...entriesFromArray((source as Record<string, unknown>).options as unknown[], 'options', baseLabel || parentLabel, options),
    );
  }

  Object.entries(source).forEach(([entryKey, entryValue]) => {
    if (entryValue === null || entryValue === undefined) {
      return;
    }
    if (labelKeys.includes(entryKey) || entryKey === 'options') {
      return;
    }

    if (entryKey.toLowerCase() === 'typical_unit_image_url') {
      return;
    }

    const childLabel = formatTitle(entryKey);
    const derivedLabel =
      (entryKey.toLowerCase().includes('url') || entryKey.toLowerCase().includes('image')) && baseLabel ? baseLabel : childLabel;

    entries.push(...buildEntriesFromValue(derivedLabel, entryValue, entryKey, options));
  });

  return entries;
};

const buildSectionFromValue = (title: string, value: unknown, baseOptions: BuildOptions): SpecificationSection | null => {
  const sectionOptions: BuildOptions = {
    ...baseOptions,
    sectionTitle: title,
  };

  let entries: SpecificationEntry[] = [];

  if (Array.isArray(value)) {
    entries = entriesFromArray(value, null, formatTitle(title), sectionOptions);
  } else if (value && typeof value === 'object') {
    entries = entriesFromObject(value as Record<string, unknown>, null, formatTitle(title), sectionOptions);
  } else {
    const entry = createPrimitiveEntry('', value, null, sectionOptions);
    if (entry) {
      entries = [entry];
    }
  }

  entries = entries.filter(entry => entry.value?.toString().length);

  if (!entries.length) {
    return null;
  }

  return {
    title: formatTitle(title),
    entries,
    highlight: HIGHLIGHTED_SPECIFICATION_TITLES.has(title.toLowerCase()),
  };
};

const specificationData = computed(() => {
  const raw = property.value?.raw;
  if (!raw || typeof raw !== 'string') {
    return {
      sections: [] as SpecificationSection[],
      downloads: [] as { label: string; url: string }[],
      architecture: [] as ImageSlide[],
      buildings: [] as BuildingSlide[],
      cover: [] as ImageSlide[],
      facilities: [] as FacilitySlide[],
      interior: [] as ImageSlide[],
      lobby: [] as ImageSlide[],
      mapPoints: [] as MapPointDetail[],
      paymentPlans: [] as PaymentPlanDetail[],
      unitAvailability: [] as UnitAvailabilityRow[],
      unitBlocks: [] as UnitBlockRow[],
    };
  }

  const trimmed = raw.trim();
  if (!trimmed.startsWith('{') && !trimmed.startsWith('[')) {
    return {
      sections: [] as SpecificationSection[],
      downloads: [] as { label: string; url: string }[],
      architecture: [] as ImageSlide[],
      buildings: [] as BuildingSlide[],
      cover: [] as ImageSlide[],
      facilities: [] as FacilitySlide[],
      interior: [] as ImageSlide[],
      lobby: [] as ImageSlide[],
      mapPoints: [] as MapPointDetail[],
      paymentPlans: [] as PaymentPlanDetail[],
      unitAvailability: [] as UnitAvailabilityRow[],
      unitBlocks: [] as UnitBlockRow[],
    };
  }

  let parsed: unknown;
  try {
    parsed = JSON.parse(trimmed);
  } catch (error) {
    return {
      sections: [] as SpecificationSection[],
      downloads: [] as { label: string; url: string }[],
      architecture: [] as ImageSlide[],
      buildings: [] as BuildingSlide[],
      cover: [] as ImageSlide[],
      facilities: [] as FacilitySlide[],
      interior: [] as ImageSlide[],
      lobby: [] as ImageSlide[],
      mapPoints: [] as MapPointDetail[],
      paymentPlans: [] as PaymentPlanDetail[],
      unitAvailability: [] as UnitAvailabilityRow[],
      unitBlocks: [] as UnitBlockRow[],
    };
  }

  const seenImages = new Set<string>();
  const heroImage = property.value?.coverUrl?.trim();
  if (heroImage) {
    seenImages.add(heroImage);
  }

  const downloads: { label: string; url: string }[] = [];
  const downloadUrls = new Set<string>();
  const sections: SpecificationSection[] = [];
  const architectureSlides: ImageSlide[] = [];
  const buildingSlides: BuildingSlide[] = [];
  const coverSlides: ImageSlide[] = [];
  const facilitySlides: FacilitySlide[] = [];
  const interiorSlides: ImageSlide[] = [];
  const lobbySlides: ImageSlide[] = [];
  const unitBlockRows: UnitBlockRow[] = [];
  const mapPointDetails: MapPointDetail[] = [];
  const paymentPlanDetails: PaymentPlanDetail[] = [];
  const unitAvailabilityRows: UnitAvailabilityRow[] = [];

  const makeOptions = (sectionTitle: string): BuildOptions => ({
    sectionTitle,
    seenImages,
    downloads,
    downloadUrls,
  });

  const pushSection = (section: SpecificationSection | null) => {
    if (section) {
      sections.push(section);
    }
  };

  const addImageSlide = (collection: ImageSlide[], url: string | null, alt?: string | null, caption?: string | null) => {
    if (!url) {
      return;
    }
    if (collection.some(slide => slide.url === url)) {
      return;
    }
    collection.push({
      url,
      alt: alt ?? undefined,
      caption: caption ?? undefined,
    });
  };

  const readFirstString = (source: Record<string, unknown>, keys: string[]): string | undefined => {
    for (const key of keys) {
      const value = source[key];
      if (typeof value === 'string' && value.trim().length) {
        return value.trim();
      }
    }
    return undefined;
  };

  const ensureDownload = (label: string, url?: string | null) => {
    if (!url) {
      return;
    }
    if (downloadUrls.has(url)) {
      return;
    }
    downloadUrls.add(url);
    downloads.push({ label, url });
  };
  const parseNumber = (value: unknown): number | null => {
    if (typeof value === 'number') {
      return Number.isFinite(value) ? value : null;
    }
    if (typeof value === 'string' && value.trim().length && !Number.isNaN(Number(value))) {
      return Number(value);
    }
    return null;
  };

  const processObject = (record: Record<string, unknown>) => {
    const architectureRaw = record.architecture;
    if (Array.isArray(architectureRaw)) {
      architectureRaw.forEach(item => {
        if (item && typeof item === 'object') {
          const obj = item as Record<string, unknown>;
          const url = extractImageUrl(obj);
          const name = readFirstString(obj, ['name', 'title', 'label']);
          addImageSlide(architectureSlides, url, name ?? 'Architecture rendering', name ?? undefined);
        } else {
          addImageSlide(architectureSlides, extractImageUrl(item), 'Architecture rendering');
        }
      });
    }

    const coverRaw = record.cover ?? record.cover_image_url;
    const coverUrl = extractImageUrl(coverRaw);
    addImageSlide(coverSlides, coverUrl, property.value?.name ?? 'Project cover');

    const interiorRaw = record.interior;
    if (Array.isArray(interiorRaw)) {
      interiorRaw.forEach(item => {
        if (item && typeof item === 'object') {
          const obj = item as Record<string, unknown>;
          const url = extractImageUrl(obj);
          const name = readFirstString(obj, ['name', 'title', 'label']);
          addImageSlide(interiorSlides, url, name ?? 'Interior rendering', name ?? undefined);
        } else {
          addImageSlide(interiorSlides, extractImageUrl(item), 'Interior rendering');
        }
      });
    }

    const lobbyRaw = record.lobby;
    if (Array.isArray(lobbyRaw)) {
      lobbyRaw.forEach(item => {
        if (item && typeof item === 'object') {
          const obj = item as Record<string, unknown>;
          const url = extractImageUrl(obj);
          const name = readFirstString(obj, ['name', 'title', 'label']);
          addImageSlide(lobbySlides, url, name ?? 'Lobby rendering', name ?? undefined);
        } else {
          addImageSlide(lobbySlides, extractImageUrl(item), 'Lobby rendering');
        }
      });
    }

    const facilitiesRaw = record.facilities;
    if (Array.isArray(facilitiesRaw)) {
      facilitiesRaw.forEach(item => {
        if (item && typeof item === 'object') {
          const obj = item as Record<string, unknown>;
          const name = readFirstString(obj, ['name', 'title', 'label']);
          const description = readFirstString(obj, ['description', 'details', 'image_source']);
          const url = extractImageUrl(obj.image ?? obj);
          if (url) {
            facilitySlides.push({
              url,
              alt: name ?? 'Facility',
              caption: name ?? undefined,
              description,
            });
          }
        }
      });
    }

    const buildingsRaw = record.buildings;
    if (Array.isArray(buildingsRaw)) {
      const flatten = (value: unknown): unknown[] => (Array.isArray(value) ? value.flatMap(item => flatten(item)) : [value]);
      flatten(buildingsRaw).forEach(item => {
        if (item && typeof item === 'object') {
          const obj = item as Record<string, unknown>;
          const name = readFirstString(obj, ['name', 'Name', 'title']);
          const description = readFirstString(obj, ['description', 'Description']);
          const url = extractImageUrl(obj.Building_image ?? obj.image ?? obj.media);
          if (url) {
            buildingSlides.push({
              url,
              alt: name ?? 'Building',
              caption: name ?? undefined,
              description,
            });
          }
        }
      });
    }

    const mapPointsRaw = record.map_points;
    if (Array.isArray(mapPointsRaw)) {
      mapPointsRaw.forEach(item => {
        if (item && typeof item === 'object') {
          const obj = item as Record<string, unknown>;
          const name = readFirstString(obj, ['name', 'title', 'label']);
          if (!name) {
            return;
          }
          const distanceValue = obj.distance_km ?? obj.distanceKm ?? obj.distance;
          let distance: number | null = null;
          if (typeof distanceValue === 'number') {
            distance = distanceValue;
          } else if (typeof distanceValue === 'string' && distanceValue.trim().length) {
            const parsedDistance = Number(distanceValue);
            if (!Number.isNaN(parsedDistance)) {
              distance = parsedDistance;
            }
          }
          mapPointDetails.push({ name, distanceKm: distance });
        }
      });
    }

    const paymentPlansRaw = record.payment_plans;
    if (Array.isArray(paymentPlansRaw)) {
      paymentPlansRaw.forEach(plan => {
        if (!plan || typeof plan !== 'object') {
          return;
        }
        const planRecord = plan as Record<string, unknown>;
        const planName = readFirstString(planRecord, ['Plan_name', 'plan_name', 'name', 'title']) ?? 'Payment plan';
        const monthsAfter = planRecord.months_after_handover;
        const schedule: PaymentScheduleRow[] = [];
        const paymentsRaw = planRecord.Payments ?? planRecord.payments;
        const paymentsArray = Array.isArray(paymentsRaw) ? paymentsRaw : [];
        paymentsArray.forEach((entry, entryIndex) => {
          const list = Array.isArray(entry) ? entry : [entry];
          list.forEach(item => {
            if (!item || typeof item !== 'object') {
              return;
            }
            const itemRecord = item as Record<string, unknown>;
            const orderRaw = itemRecord.Order ?? itemRecord.order ?? entryIndex + 1;
            const order =
              typeof orderRaw === 'number'
                ? orderRaw
                : typeof orderRaw === 'string' && orderRaw.trim().length && !Number.isNaN(Number(orderRaw))
                  ? Number(orderRaw)
                  : null;
            const time = readFirstString(itemRecord, ['Payment_time', 'payment_time', 'time', 'stage', 'Stage']) ?? null;
            const percentRaw = itemRecord.Percent_of_payment ?? itemRecord.percent_of_payment ?? itemRecord.Percent ?? itemRecord.percent;
            let percent: string | null = null;
            if (percentRaw !== undefined && percentRaw !== null) {
              if (typeof percentRaw === 'number') {
                percent = `${percentRaw}%`;
              } else if (typeof percentRaw === 'string' && percentRaw.trim().length) {
                percent = percentRaw.includes('%') ? percentRaw : `${percentRaw}%`;
              }
            }
            const amountRaw = itemRecord.Amount ?? itemRecord.amount ?? null;
            const amount =
              typeof amountRaw === 'number' ? amountRaw.toLocaleString() : typeof amountRaw === 'string' ? amountRaw.trim() : null;
            const note = readFirstString(itemRecord, ['Note', 'note', 'remarks', 'description']) ?? null;
            schedule.push({
              order,
              time,
              percent,
              amount,
              note,
            });
          });
        });
        if (schedule.length) {
          paymentPlanDetails.push({
            planName,
            monthsAfterHandover:
              typeof monthsAfter === 'number'
                ? monthsAfter
                : typeof monthsAfter === 'string' && monthsAfter.trim().length && !Number.isNaN(Number(monthsAfter))
                  ? Number(monthsAfter)
                  : null,
            schedule,
          });
        }
      });
    }

    const unitBlocksRaw = record.unit_blocks ?? record.unitBlocks;
    if (Array.isArray(unitBlocksRaw)) {
      unitBlocksRaw.forEach(block => {
        if (!block || typeof block !== 'object') {
          return;
        }
        const blockRecord = block as Record<string, unknown>;
        const rawId = blockRecord.id ?? blockRecord.Id ?? blockRecord.ID ?? blockRecord.block_id ?? blockRecord.blockId ?? null;
        const idValue = typeof rawId === 'number' || typeof rawId === 'string' ? rawId : null;
        const bedroomsAmount = readFirstString(blockRecord, ['bedrooms_amount', 'Bedrooms_amount', 'bedroomsAmount']);
        const unitBedrooms = readFirstString(blockRecord, ['unit_bedrooms', 'unitBedrooms']);
        const fallbackBedrooms = readFirstString(blockRecord, ['bedrooms']);
        const normalizedType = readFirstString(blockRecord, ['normalized_type', 'Normalized_type']);
        const unitType = readFirstString(blockRecord, ['unit_type', 'Unit_type']);
        const unitsAmount = parseNumber(blockRecord.units_amount ?? blockRecord.unitsAmount ?? blockRecord.count);

        let areaUnit =
          readFirstString(blockRecord, ['area_unit', 'units_area_unit']) ??
          readFirstString(record, ['area_unit']) ??
          property.value?.areaUnit ??
          null;

        let areaFrom = parseNumber(blockRecord.units_area_from ?? blockRecord.area_from ?? blockRecord.areaFrom);
        let areaTo = parseNumber(blockRecord.units_area_to ?? blockRecord.area_to ?? blockRecord.areaTo);
        let areaFromM2 = parseNumber(
          blockRecord.units_area_from_m2 ?? blockRecord.units_area_from_m ?? blockRecord.area_from_m2 ?? blockRecord.area_from_m,
        );
        let areaToM2 = parseNumber(
          blockRecord.units_area_to_m2 ?? blockRecord.units_area_to_m ?? blockRecord.area_to_m2 ?? blockRecord.area_to_m,
        );

        const SQM_TO_SQFT = 10.76391041671;

        if (!areaUnit && (areaFromM2 !== null || areaToM2 !== null)) {
          areaUnit = 'm²';
        }

        const areaUnitLower = (areaUnit ?? '').toLowerCase();
        const unitPrefersSqMeters = areaUnitLower.includes('m');

        if (areaFrom === null && areaFromM2 !== null) {
          areaFrom = unitPrefersSqMeters ? areaFromM2 : areaFromM2 * SQM_TO_SQFT;
        }
        if (areaTo === null && areaToM2 !== null) {
          areaTo = unitPrefersSqMeters ? areaToM2 : areaToM2 * SQM_TO_SQFT;
        }

        if (unitPrefersSqMeters && areaFromM2 === null && areaFrom !== null) {
          areaFromM2 = areaFrom;
        }
        if (!unitPrefersSqMeters && areaFromM2 === null && areaFrom !== null) {
          areaFromM2 = areaFrom / SQM_TO_SQFT;
        }
        if (unitPrefersSqMeters && areaToM2 === null && areaTo !== null) {
          areaToM2 = areaTo;
        }
        if (!unitPrefersSqMeters && areaToM2 === null && areaTo !== null) {
          areaToM2 = areaTo / SQM_TO_SQFT;
        }

        let priceFrom = parseNumber(blockRecord.units_price_from ?? blockRecord.price_from ?? blockRecord.priceFrom);
        let priceTo = parseNumber(blockRecord.units_price_to ?? blockRecord.price_to ?? blockRecord.priceTo);
        const priceFromAed = parseNumber(blockRecord.units_price_from_aed ?? blockRecord.price_from_aed);
        const priceToAed = parseNumber(blockRecord.units_price_to_aed ?? blockRecord.price_to_aed);
        const priceCurrency =
          readFirstString(blockRecord, ['price_currency', 'units_price_currency']) ?? property.value?.priceCurrency ?? null;

        if (priceFrom === null && priceFromAed !== null && (priceCurrency ?? '').toUpperCase() === 'AED') {
          priceFrom = priceFromAed;
        }
        if (priceTo === null && priceToAed !== null && (priceCurrency ?? '').toUpperCase() === 'AED') {
          priceTo = priceToAed;
        }

        unitBlockRows.push({
          id: idValue,
          bedrooms: unitBedrooms ?? bedroomsAmount ?? fallbackBedrooms ?? null,
          bedroomsAmount: bedroomsAmount ?? null,
          normalizedType: normalizedType ?? null,
          unitType: unitType ?? null,
          areaUnit,
          areaFrom,
          areaTo,
          areaFromM2,
          areaToM2,
          priceCurrency,
          priceFrom,
          priceTo,
          priceFromAed,
          priceToAed,
          unitsAmount,
        });
      });
    }

    const unitAvailabilityRaw = record.unit_availability;
    if (Array.isArray(unitAvailabilityRaw)) {
      unitAvailabilityRaw.forEach(entry => {
        if (!entry || typeof entry !== 'object') {
          return;
        }
        const entryRecord = entry as Record<string, unknown>;
        const buildingName = readFirstString(entryRecord, ['building_name', 'name', 'title']);
        const units = Array.isArray(entryRecord.units) ? entryRecord.units : [];
        units.forEach(unitItem => {
          if (!unitItem || typeof unitItem !== 'object') {
            return;
          }
          const unitRecord = unitItem as Record<string, unknown>;
          const areaFromRaw = unitRecord.area_from ?? unitRecord.areaFrom;
          const priceFromRaw = unitRecord.price_from ?? unitRecord.priceFrom;
          const priceToRaw = unitRecord.price_to ?? unitRecord.priceTo;
          const lastUpdatedRaw = unitRecord.last_updated ?? unitRecord.lastUpdated;

          let lastUpdated: string | null = null;

          if (typeof lastUpdatedRaw === 'string' && lastUpdatedRaw.trim().length) {
            const normalized = extractDateOnly(lastUpdatedRaw);
            lastUpdated = (normalized ?? dayjs(lastUpdatedRaw).isValid()) ? dayjs(lastUpdatedRaw).format('YYYY-MM-DD') : null;
          }

          unitAvailabilityRows.push({
            buildingName,
            bedroomType: readFirstString(unitRecord, ['bedroom_type', 'bedrooms', 'bedroom']),
            areaFrom: parseNumber(areaFromRaw),
            areaUnit: readFirstString(unitRecord, ['area_unit', 'areaUnit']),
            priceFrom: parseNumber(priceFromRaw),
            priceTo: parseNumber(priceToRaw),
            currency: readFirstString(unitRecord, ['price_currency', 'currency']),
            unitsAvailable: parseNumber(unitRecord.units_available ?? unitRecord.unitsAvailable),
            lastUpdated,
          });
        });
      });
    }

    const masterPlanRaw = record.master_plan;
    if (Array.isArray(masterPlanRaw)) {
      masterPlanRaw.forEach(item => {
        const url = extractImageUrl(item);
        ensureDownload('Master plan', url);
      });
    }

    const layoutsPdfRaw = record.layouts_pdf ?? record.layoutsPdf;
    if (typeof layoutsPdfRaw === 'string' && layoutsPdfRaw.trim().length) {
      ensureDownload('Layouts PDF', layoutsPdfRaw.trim());
    }

    const parseBedroomsValue = (value?: string | null) => {
      if (!value) {
        return Number.POSITIVE_INFINITY;
      }
      const match = value.match(/\d+/);
      return match ? Number(match[0]) : Number.POSITIVE_INFINITY;
    };

    unitBlockRows.sort((a, b) => {
      const bedroomDiff = parseBedroomsValue(a.bedrooms) - parseBedroomsValue(b.bedrooms);
      if (bedroomDiff !== 0) {
        return bedroomDiff;
      }
      const typeA = (a.normalizedType ?? a.unitType ?? '').toLowerCase();
      const typeB = (b.normalizedType ?? b.unitType ?? '').toLowerCase();
      if (typeA && typeB) {
        return typeA.localeCompare(typeB);
      }
      return 0;
    });

    mapPointDetails.sort((a, b) => {
      const distanceA = typeof a.distanceKm === 'number' ? a.distanceKm : Number.POSITIVE_INFINITY;
      const distanceB = typeof b.distanceKm === 'number' ? b.distanceKm : Number.POSITIVE_INFINITY;
      return distanceA - distanceB;
    });
  };

  const skipKeys = new Set(
    [
      'architecture',
      'buildings',
      'cover',
      'cover_image',
      'cover_image_url',
      'facilities',
      'interior',
      'lobby',
      'map_points',
      'payment_plans',
      'unit_availability',
      'master_plan',
      'layouts_pdf',
      'layouts',
    ].map(key => key.toLowerCase()),
  );

  if (Array.isArray(parsed)) {
    parsed.forEach((item, index) => {
      if (item && typeof item === 'object' && !Array.isArray(item)) {
        processObject(item as Record<string, unknown>);
        const record = item as Record<string, unknown>;
        const titleCandidate =
          ['title', 'name', 'label', 'category'].map(key => record[key]).find(value => typeof value === 'string' && value.trim().length) ||
          `Section ${index + 1}`;
        const sanitized = { ...record } as Record<string, unknown>;
        ['title', 'name', 'label', 'category'].forEach(key => {
          delete sanitized[key];
        });
        pushSection(buildSectionFromValue(titleCandidate, sanitized, makeOptions(titleCandidate)));
      } else {
        pushSection(buildSectionFromValue(`Item ${index + 1}`, item, makeOptions(`Item ${index + 1}`)));
      }
    });
  } else if (parsed && typeof parsed === 'object') {
    const record = parsed as Record<string, unknown>;
    processObject(record);
    Object.entries(record).forEach(([key, value]) => {
      if (skipKeys.has(key.toLowerCase())) {
        return;
      }
      pushSection(buildSectionFromValue(key, value, makeOptions(key)));
    });
  }

  sections.sort((a, b) => Number(b.highlight) - Number(a.highlight));

  return {
    sections,
    downloads,
    architecture: architectureSlides,
    buildings: buildingSlides,
    cover: coverSlides,
    facilities: facilitySlides,
    interior: interiorSlides,
    lobby: lobbySlides,
    mapPoints: mapPointDetails,
    paymentPlans: paymentPlanDetails,
    unitAvailability: unitAvailabilityRows,
    unitBlocks: unitBlockRows,
  };
});

const specificationSections = computed(() => specificationData.value.sections);
const specificationDownloads = computed(() => specificationData.value.downloads);
const architectureSlides = computed(() => specificationData.value.architecture);
const buildingSlides = computed(() => specificationData.value.buildings);
const coverSlides = computed(() => specificationData.value.cover);
const facilitySlides = computed(() => specificationData.value.facilities);
const interiorSlides = computed(() => specificationData.value.interior);
const lobbySlides = computed(() => specificationData.value.lobby);
const mapPointDetails = computed(() => specificationData.value.mapPoints);
const paymentPlanDetails = computed(() => specificationData.value.paymentPlans);
const unitAvailabilityRows = computed(() => specificationData.value.unitAvailability);
const unitBlockRows = computed(() => specificationData.value.unitBlocks);

const createCarousel = <T,>(slides: ComputedRef<T[]>) => {
  const index = ref(0);
  const go = (delta: number) => {
    const list = slides.value;
    if (!list.length) {
      index.value = 0;
      return;
    }
    index.value = (index.value + delta + list.length) % list.length;
  };
  watch(
    slides,
    list => {
      if (!list.length) {
        index.value = 0;
      } else if (index.value >= list.length) {
        index.value = 0;
      }
    },
    { immediate: true },
  );
  return { index, go };
};

const { index: architectureIndex, go: advanceArchitecture } = createCarousel(architectureSlides);
const { index: buildingIndex, go: advanceBuildings } = createCarousel(buildingSlides);
const { index: coverIndex, go: advanceCover } = createCarousel(coverSlides);
const { index: facilityIndex, go: advanceFacilities } = createCarousel(facilitySlides);
const { index: interiorIndex, go: advanceInterior } = createCarousel(interiorSlides);
const { index: lobbyIndex, go: advanceLobby } = createCarousel(lobbySlides);

const VALUE_NA = 'N/A';

const formatArea = (value: number | null | undefined, unit?: string | null) => {
  if (typeof value !== 'number' || !Number.isFinite(value)) {
    return VALUE_NA;
  }
  const formatted =
    value >= 100
      ? value.toLocaleString(undefined, { maximumFractionDigits: 0 })
      : value.toLocaleString(undefined, { maximumFractionDigits: 1 });
  return unit ? `${formatted} ${unit}` : formatted;
};

const formatAreaRange = (from: number | null | undefined, to: number | null | undefined, unit?: string | null) => {
  const fromLabel = formatArea(from ?? null, unit);
  const toLabel = formatArea(to ?? null, unit);
  if (fromLabel === VALUE_NA && toLabel === VALUE_NA) {
    return VALUE_NA;
  }
  if (fromLabel !== VALUE_NA && toLabel !== VALUE_NA && fromLabel !== toLabel) {
    return `${fromLabel} – ${toLabel}`;
  }
  return fromLabel !== VALUE_NA ? fromLabel : toLabel;
};

const formatAreaM2Range = (from: number | null | undefined, to: number | null | undefined) =>
  formatAreaRange(from ?? null, to ?? null, 'm²');

const formatAreaValue = (value: number | null | undefined, unit?: string | null) => formatArea(value ?? null, unit);

const formatPriceValue = (value: number | null | undefined, currency?: string | null) => {
  if (typeof value === 'number' && Number.isFinite(value)) {
    const formattedCurrency = currency ?? property.value?.priceCurrency ?? 'AED';
    return `${formattedCurrency} ${value.toLocaleString()}`;
  }
  return VALUE_NA;
};
const formatPriceRange = (from: number | null | undefined, to: number | null | undefined, currency?: string | null) => {
  const formattedCurrency = currency ?? property.value?.priceCurrency ?? 'AED';
  const formatNumber = (value: number | null | undefined) =>
    typeof value === 'number' && Number.isFinite(value) ? `${formattedCurrency} ${value.toLocaleString()}` : null;
  const fromLabel = formatNumber(from);
  const toLabel = formatNumber(to);
  if (fromLabel && toLabel && fromLabel !== toLabel) {
    return `${fromLabel} – ${toLabel}`;
  }
  if (fromLabel || toLabel) {
    return fromLabel ?? toLabel ?? '—';
  }
  return VALUE_NA;
};

const formatDistanceKm = (value: number | null | undefined) => {
  if (typeof value !== 'number' || !Number.isFinite(value)) {
    return '';
  }
  if (value >= 1) {
    const formatted = value.toFixed(1).replace(/\.0$/, '');
    return `${formatted} km`;
  }
  const meters = Math.round(value * 1000);
  return meters > 0 ? `${meters} m` : '';
};

const mapPointDisplayDetails = computed(() =>
  mapPointDetails.value.map(point => ({
    ...point,
    distanceLabel: formatDistanceKm(point.distanceKm),
  })),
);

const formatCount = (value: number | null | undefined) =>
  typeof value === 'number' && Number.isFinite(value) ? value.toLocaleString() : VALUE_NA;

const formatIdentifier = (value: string | number | null | undefined) => {
  if (value === null || value === undefined) {
    return VALUE_NA;
  }
  if (typeof value === 'number') {
    return value.toLocaleString();
  }
  return String(value);
};

const downloads = computed(() => {
  if (!property.value) {
    return [] as { label: string; url: string }[];
  }

  const items: { label: string; url: string }[] = [];
  const added = new Set<string>();

  const addItem = (label: string, url?: string | null) => {
    if (!url) {
      return;
    }
    if (added.has(url)) {
      return;
    }
    added.add(url);
    items.push({ label, url });
  };

  if (property.value.videoUrl) {
    addItem('Watch video tour', property.value.videoUrl);
  }

  const specificationLayout = specificationDownloads.value.find(item => item.label.toLowerCase().includes('layout'));
  addItem('Layouts PDF', property.value.layoutsPdfUrl ?? specificationLayout?.url ?? null);

  specificationDownloads.value.forEach(item => {
    const labelLower = item.label.toLowerCase();
    if (labelLower.includes('brochure')) {
      return;
    }
    if (labelLower.includes('layout')) {
      addItem('Layouts PDF', item.url);
      return;
    }
    if (labelLower.includes('master plan') || labelLower.includes('masterplan')) {
      addItem('Master plan', item.url);
      return;
    }
    if (labelLower.includes('video')) {
      addItem(item.label, item.url);
      return;
    }
    addItem(item.label, item.url);
  });

  return items;
});

const submitLead = async () => {
  if (!leadIsValid.value || submittingLead.value) {
    return;
  }

  submittingLead.value = true;
  try {
    await axios.post('api/contact-leads', {
      name: leadForm.name,
      email: leadForm.email || null,
      phone: leadForm.phone || null,
      message: leadForm.message || null,
      source: 'STORE_FRONT',
      site: site.value?.id ? { id: site.value.id } : null,
      property: property.value?.id ? { id: property.value.id } : null,
      status: 'NEW',
    });
    alertService.showSuccess('Thanks! We will reach out shortly.');
    leadForm.name = '';
    leadForm.email = '';
    leadForm.phone = '';
    leadForm.message = '';
  } catch (error) {
    if (error?.response) {
      alertService.showHttpError(error.response);
    } else {
      alertService.showError('Unable to submit your request right now.');
    }
  } finally {
    submittingLead.value = false;
  }
};

onMounted(async () => {
  const slug = route.params.slug as string;
  const propertyId = Number(route.params.propertyId);

  try {
    if (!site.value || site.value.slug !== slug) {
      await agentSiteStore.fetchSite({ slug });
    }

    const response = await axios.get<IProperty>(`api/public/properties/${propertyId}`);
    property.value = response.data;
  } catch (error) {
    console.error('Failed to load property', error);
    property.value = null;
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.property-hero {
  background-size: cover;
  background-position: center;
  position: relative;
  min-height: 320px;
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(7, 21, 40, 0.75), rgba(7, 21, 40, 0.55));
}

.property-hero .container {
  position: relative;
  z-index: 1;
}

.hero-badges .badge {
  letter-spacing: 0.08em;
}

.hero-price {
  backdrop-filter: blur(6px);
  background: rgba(7, 21, 40, 0.35);
  display: inline-block;
  padding: 0.75rem 1.5rem;
  border-radius: 1.5rem;
}

.fact-label {
  letter-spacing: 0.08em;
}

.fact-value {
  font-weight: 600;
}

.property-view main {
  background: #f5f7fa;
}

.card {
  border-radius: 1.25rem;
}

.lead-form .form-control {
  border-radius: 0.75rem;
}

.lead-form button {
  border-radius: 0.75rem;
}

.section-heading {
  gap: 1rem;
}

.sticky-aside {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.carousel-controls {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.carousel-button {
  border: 0;
  background: rgba(24, 64, 152, 0.1);
  color: #184098;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  font-size: 1rem;
  line-height: 1;
  cursor: pointer;
  transition: background 0.2s ease;
}

.carousel-button:hover {
  background: rgba(24, 64, 152, 0.2);
}

.carousel-indicator {
  font-size: 0.85rem;
  color: #4a5a75;
}

.media-carousel {
  position: relative;
}

.carousel-slide {
  position: relative;
  border-radius: 1.25rem;
  overflow: hidden;
  box-shadow: 0 16px 28px rgba(9, 32, 68, 0.18);
}

.media-carousel__image {
  display: block;
  width: 100%;
  height: 320px;
  object-fit: cover;
}

.carousel-slide__overlay {
  position: absolute;
  top: 1rem;
  left: 1rem;
  background: rgba(7, 21, 40, 0.72);
  color: #fff;
  padding: 0.75rem 1rem;
  border-radius: 0.85rem;
  max-width: 70%;
  box-shadow: 0 10px 20px rgba(7, 21, 40, 0.25);
}

.carousel-slide__overlay-title {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.carousel-slide__overlay-text {
  font-size: 0.85rem;
  margin: 0;
  line-height: 1.35;
  opacity: 0.9;
}

.overview-block {
  margin-bottom: 1.5rem;
}

.overview-block:last-of-type {
  margin-bottom: 0;
}

.overview-heading {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 0.75rem;
  color: #0a1f3f;
}

.overview-heading--highlight {
  display: inline-flex;
  align-items: center;
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  background: rgba(27, 77, 228, 0.12);
}

.spec-section {
  padding-bottom: 1rem;
  margin-bottom: 1rem;
  border-bottom: 1px solid rgba(13, 35, 70, 0.08);
}

.spec-section:last-of-type {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: 0;
}

.spec-section__title {
  display: inline-flex;
  align-items: center;
  background: rgba(27, 77, 228, 0.12);
  color: #0a1f3f;
  font-weight: 600;
  border-radius: 999px;
  padding: 0.25rem 0.75rem;
  margin-bottom: 0.75rem;
}

.spec-section--highlight .spec-section__title {
  background: rgba(255, 171, 64, 0.18);
  color: #5a3300;
}

.spec-section__list {
  margin: 0;
}

.spec-entry {
  display: grid;
  grid-template-columns: minmax(140px, 220px) 1fr;
  column-gap: 1rem;
  row-gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.spec-entry:last-of-type {
  margin-bottom: 0;
}

.spec-entry__label {
  font-weight: 600;
  color: #12243c;
  word-break: break-word;
}

.spec-entry__value {
  color: #4a5a75;
}

.spec-entry__content {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.spec-entry__content--full {
  grid-column: 1 / -1;
}

.spec-entry__image {
  width: 100%;
  max-width: 360px;
  border-radius: 1rem;
  object-fit: cover;
  box-shadow: 0 8px 16px rgba(14, 34, 56, 0.12);
}

.spec-entry__link {
  display: inline-flex;
  align-items: center;
  font-weight: 600;
  color: #1b4de4;
  text-decoration: none;
  gap: 0.4rem;
}

.spec-entry__link:hover {
  text-decoration: underline;
}

.spec-entry__link--download::after {
  content: '\2193';
  font-size: 0.8rem;
}

.payment-table th,
.payment-table td,
.unit-table th,
.unit-table td {
  vertical-align: middle;
  white-space: nowrap;
}

.payment-plan + .payment-plan {
  border-top: 1px solid rgba(13, 35, 70, 0.08);
  padding-top: 1rem;
}

.map-points {
  display: grid;
  gap: 0.5rem;
}

.map-points__item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  padding: 0.5rem 0;
  border-bottom: 1px solid rgba(13, 35, 70, 0.08);
}

.map-points__item:last-of-type {
  border-bottom: 0;
}

.map-points__name {
  font-weight: 600;
  color: #12243c;
}

.map-points__distance {
  font-size: 0.85rem;
  color: #4a5a75;
}

@media (max-width: 767.98px) {
  .hero-price {
    display: block;
  }

  .media-carousel__image {
    height: 240px;
  }

  .spec-entry {
    grid-template-columns: 1fr;
  }

  .spec-entry__image {
    max-width: 100%;
  }
}

@media (max-width: 575.98px) {
  .media-carousel__image {
    height: 200px;
  }

  .carousel-controls {
    gap: 0.35rem;
  }

  .carousel-button {
    width: 26px;
    height: 26px;
  }

  .carousel-indicator {
    font-size: 0.8rem;
  }
}
</style>

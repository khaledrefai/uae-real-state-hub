import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const AgentProfile = () => import('@/entities/agent-profile/agent-profile.vue');
const AgentProfileUpdate = () => import('@/entities/agent-profile/agent-profile-update.vue');
const AgentProfileDetails = () => import('@/entities/agent-profile/agent-profile-details.vue');

const AgentSite = () => import('@/entities/agent-site/agent-site.vue');
const AgentSiteUpdate = () => import('@/entities/agent-site/agent-site-update.vue');
const AgentSiteDetails = () => import('@/entities/agent-site/agent-site-details.vue');

const SubscriptionPlan = () => import('@/entities/subscription-plan/subscription-plan.vue');
const SubscriptionPlanUpdate = () => import('@/entities/subscription-plan/subscription-plan-update.vue');
const SubscriptionPlanDetails = () => import('@/entities/subscription-plan/subscription-plan-details.vue');

const BillingSubscription = () => import('@/entities/billing-subscription/billing-subscription.vue');
const BillingSubscriptionUpdate = () => import('@/entities/billing-subscription/billing-subscription-update.vue');
const BillingSubscriptionDetails = () => import('@/entities/billing-subscription/billing-subscription-details.vue');

const Property = () => import('@/entities/property/property.vue');
const PropertyUpdate = () => import('@/entities/property/property-update.vue');
const PropertyDetails = () => import('@/entities/property/property-details.vue');

const PropertyMarker = () => import('@/entities/property-marker/property-marker.vue');
const PropertyMarkerUpdate = () => import('@/entities/property-marker/property-marker-update.vue');
const PropertyMarkerDetails = () => import('@/entities/property-marker/property-marker-details.vue');

const MediaAsset = () => import('@/entities/media-asset/media-asset.vue');
const MediaAssetUpdate = () => import('@/entities/media-asset/media-asset-update.vue');
const MediaAssetDetails = () => import('@/entities/media-asset/media-asset-details.vue');

const Facility = () => import('@/entities/facility/facility.vue');
const FacilityUpdate = () => import('@/entities/facility/facility-update.vue');
const FacilityDetails = () => import('@/entities/facility/facility-details.vue');

const MapPoint = () => import('@/entities/map-point/map-point.vue');
const MapPointUpdate = () => import('@/entities/map-point/map-point-update.vue');
const MapPointDetails = () => import('@/entities/map-point/map-point-details.vue');

const PaymentPlan = () => import('@/entities/payment-plan/payment-plan.vue');
const PaymentPlanUpdate = () => import('@/entities/payment-plan/payment-plan-update.vue');
const PaymentPlanDetails = () => import('@/entities/payment-plan/payment-plan-details.vue');

const PaymentPlanItem = () => import('@/entities/payment-plan-item/payment-plan-item.vue');
const PaymentPlanItemUpdate = () => import('@/entities/payment-plan-item/payment-plan-item-update.vue');
const PaymentPlanItemDetails = () => import('@/entities/payment-plan-item/payment-plan-item-details.vue');

const UnitAvailability = () => import('@/entities/unit-availability/unit-availability.vue');
const UnitAvailabilityUpdate = () => import('@/entities/unit-availability/unit-availability-update.vue');
const UnitAvailabilityDetails = () => import('@/entities/unit-availability/unit-availability-details.vue');

const UnitBlock = () => import('@/entities/unit-block/unit-block.vue');
const UnitBlockUpdate = () => import('@/entities/unit-block/unit-block-update.vue');
const UnitBlockDetails = () => import('@/entities/unit-block/unit-block-details.vue');

const ContactLead = () => import('@/entities/contact-lead/contact-lead.vue');
const ContactLeadUpdate = () => import('@/entities/contact-lead/contact-lead-update.vue');
const ContactLeadDetails = () => import('@/entities/contact-lead/contact-lead-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'agent-profile',
      name: 'AgentProfile',
      component: AgentProfile,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'agent-profile/new',
      name: 'AgentProfileCreate',
      component: AgentProfileUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'agent-profile/:agentProfileId/edit',
      name: 'AgentProfileEdit',
      component: AgentProfileUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'agent-profile/:agentProfileId/view',
      name: 'AgentProfileView',
      component: AgentProfileDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'agent-site',
      name: 'AgentSite',
      component: AgentSite,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'agent-site/new',
      name: 'AgentSiteCreate',
      component: AgentSiteUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'agent-site/:agentSiteId/edit',
      name: 'AgentSiteEdit',
      component: AgentSiteUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'agent-site/:agentSiteId/view',
      name: 'AgentSiteView',
      component: AgentSiteDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'subscription-plan',
      name: 'SubscriptionPlan',
      component: SubscriptionPlan,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'subscription-plan/new',
      name: 'SubscriptionPlanCreate',
      component: SubscriptionPlanUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'subscription-plan/:subscriptionPlanId/edit',
      name: 'SubscriptionPlanEdit',
      component: SubscriptionPlanUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'subscription-plan/:subscriptionPlanId/view',
      name: 'SubscriptionPlanView',
      component: SubscriptionPlanDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'billing-subscription',
      name: 'BillingSubscription',
      component: BillingSubscription,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'billing-subscription/new',
      name: 'BillingSubscriptionCreate',
      component: BillingSubscriptionUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'billing-subscription/:billingSubscriptionId/edit',
      name: 'BillingSubscriptionEdit',
      component: BillingSubscriptionUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'billing-subscription/:billingSubscriptionId/view',
      name: 'BillingSubscriptionView',
      component: BillingSubscriptionDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property',
      name: 'Property',
      component: Property,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property/new',
      name: 'PropertyCreate',
      component: PropertyUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property/:propertyId/edit',
      name: 'PropertyEdit',
      component: PropertyUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property/:propertyId/view',
      name: 'PropertyView',
      component: PropertyDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property-marker',
      name: 'PropertyMarker',
      component: PropertyMarker,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property-marker/new',
      name: 'PropertyMarkerCreate',
      component: PropertyMarkerUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property-marker/:propertyMarkerId/edit',
      name: 'PropertyMarkerEdit',
      component: PropertyMarkerUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'property-marker/:propertyMarkerId/view',
      name: 'PropertyMarkerView',
      component: PropertyMarkerDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'media-asset',
      name: 'MediaAsset',
      component: MediaAsset,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'media-asset/new',
      name: 'MediaAssetCreate',
      component: MediaAssetUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'media-asset/:mediaAssetId/edit',
      name: 'MediaAssetEdit',
      component: MediaAssetUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'media-asset/:mediaAssetId/view',
      name: 'MediaAssetView',
      component: MediaAssetDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'facility',
      name: 'Facility',
      component: Facility,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'facility/new',
      name: 'FacilityCreate',
      component: FacilityUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'facility/:facilityId/edit',
      name: 'FacilityEdit',
      component: FacilityUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'facility/:facilityId/view',
      name: 'FacilityView',
      component: FacilityDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'map-point',
      name: 'MapPoint',
      component: MapPoint,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'map-point/new',
      name: 'MapPointCreate',
      component: MapPointUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'map-point/:mapPointId/edit',
      name: 'MapPointEdit',
      component: MapPointUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'map-point/:mapPointId/view',
      name: 'MapPointView',
      component: MapPointDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan',
      name: 'PaymentPlan',
      component: PaymentPlan,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan/new',
      name: 'PaymentPlanCreate',
      component: PaymentPlanUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan/:paymentPlanId/edit',
      name: 'PaymentPlanEdit',
      component: PaymentPlanUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan/:paymentPlanId/view',
      name: 'PaymentPlanView',
      component: PaymentPlanDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan-item',
      name: 'PaymentPlanItem',
      component: PaymentPlanItem,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan-item/new',
      name: 'PaymentPlanItemCreate',
      component: PaymentPlanItemUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan-item/:paymentPlanItemId/edit',
      name: 'PaymentPlanItemEdit',
      component: PaymentPlanItemUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'payment-plan-item/:paymentPlanItemId/view',
      name: 'PaymentPlanItemView',
      component: PaymentPlanItemDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-availability',
      name: 'UnitAvailability',
      component: UnitAvailability,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-availability/new',
      name: 'UnitAvailabilityCreate',
      component: UnitAvailabilityUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-availability/:unitAvailabilityId/edit',
      name: 'UnitAvailabilityEdit',
      component: UnitAvailabilityUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-availability/:unitAvailabilityId/view',
      name: 'UnitAvailabilityView',
      component: UnitAvailabilityDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-block',
      name: 'UnitBlock',
      component: UnitBlock,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-block/new',
      name: 'UnitBlockCreate',
      component: UnitBlockUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-block/:unitBlockId/edit',
      name: 'UnitBlockEdit',
      component: UnitBlockUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'unit-block/:unitBlockId/view',
      name: 'UnitBlockView',
      component: UnitBlockDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'contact-lead',
      name: 'ContactLead',
      component: ContactLead,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'contact-lead/new',
      name: 'ContactLeadCreate',
      component: ContactLeadUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'contact-lead/:contactLeadId/edit',
      name: 'ContactLeadEdit',
      component: ContactLeadUpdate,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    {
      path: 'contact-lead/:contactLeadId/view',
      name: 'ContactLeadView',
      component: ContactLeadDetails,
      meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

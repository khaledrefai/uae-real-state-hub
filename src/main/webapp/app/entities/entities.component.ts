import { defineComponent, provide } from 'vue';

import AgentProfileService from './agent-profile/agent-profile.service';
import AgentSiteService from './agent-site/agent-site.service';
import SubscriptionPlanService from './subscription-plan/subscription-plan.service';
import BillingSubscriptionService from './billing-subscription/billing-subscription.service';
import PropertyService from './property/property.service';
import PropertyMarkerService from './property-marker/property-marker.service';
import MediaAssetService from './media-asset/media-asset.service';
import FacilityService from './facility/facility.service';
import MapPointService from './map-point/map-point.service';
import PaymentPlanService from './payment-plan/payment-plan.service';
import PaymentPlanItemService from './payment-plan-item/payment-plan-item.service';
import UnitAvailabilityService from './unit-availability/unit-availability.service';
import UnitBlockService from './unit-block/unit-block.service';
import ContactLeadService from './contact-lead/contact-lead.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('agentProfileService', () => new AgentProfileService());
    provide('agentSiteService', () => new AgentSiteService());
    provide('subscriptionPlanService', () => new SubscriptionPlanService());
    provide('billingSubscriptionService', () => new BillingSubscriptionService());
    provide('propertyService', () => new PropertyService());
    provide('propertyMarkerService', () => new PropertyMarkerService());
    provide('mediaAssetService', () => new MediaAssetService());
    provide('facilityService', () => new FacilityService());
    provide('mapPointService', () => new MapPointService());
    provide('paymentPlanService', () => new PaymentPlanService());
    provide('paymentPlanItemService', () => new PaymentPlanItemService());
    provide('unitAvailabilityService', () => new UnitAvailabilityService());
    provide('unitBlockService', () => new UnitBlockService());
    provide('contactLeadService', () => new ContactLeadService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});

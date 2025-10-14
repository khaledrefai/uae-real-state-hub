import { Authority } from '@/shared/security/authority';

const AgentOnboarding = () => import('@/pages/agent-onboarding/agent-onboarding.vue');
const AgentDashboard = () => import('@/pages/agent-dashboard/agent-dashboard.vue');
const AdminDashboard = () => import('@/pages/admin-dashboard/admin-dashboard.vue');
const ReelyAdminImport = () => import('@/pages/admin-dashboard/admin-import.vue');
const StorefrontShell = () => import('@/pages/storefront/storefront-shell.vue');
const PropertyDetails = () => import('@/pages/storefront/property-details.vue');

export default [
  {
    path: '/agent/register',
    name: 'AgentOnboarding',
    component: AgentOnboarding,
  },
  {
    path: '/agent/dashboard',
    name: 'AgentDashboard',
    component: AgentDashboard,
    meta: { authorities: [Authority.AGENT, Authority.ADMIN] },
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/import',
    name: 'AdminImport',
    component: ReelyAdminImport,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/store/:slug',
    name: 'StorefrontShell',
    component: StorefrontShell,
  },
  {
    path: '/store/:slug/properties/:propertyId',
    name: 'PropertyDetails',
    component: PropertyDetails,
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];

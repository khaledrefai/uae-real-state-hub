import { type ComputedRef, computed, defineComponent, inject } from 'vue';
import { useI18n } from 'vue-i18n';

import { useLoginModal } from '@/account/login-modal';
import { MARKETING_HERO_IMAGE } from '@/constants';

const valuePropositions = [
  {
    title: 'Centralized inventory',
    description: 'Sync the daily Reely property feed into a single workspace with editorial controls before it hits your storefront.',
    icon: 'layers',
  },
  {
    title: 'Branded storefronts',
    description: 'Launch a polished B2C microsite with your colours, logo, vanity URL, and always-on lead capture widgets.',
    icon: 'palette',
  },
  {
    title: 'Agent productivity',
    description: 'Collaborate on listings, publish to clients, and answer leads from a unified agent dashboard.',
    icon: 'briefcase',
  },
];

const operatingPlaybook = [
  {
    title: 'Automated data ingestion',
    detail: 'We pull the Reely feed every morning, deduplicate, and surface suggested updates for your team to approve.',
  },
  {
    title: 'Human curation',
    detail: 'Marketing-ready copy, hero media, and geo tagging live right in the admin experience—no spreadsheets required.',
  },
  {
    title: 'Omni-channel publishing',
    detail: 'Push listings to your public site, marketing page, and agent roster instantly once you hit publish.',
  },
];

const integrationBadges = [
  {
    label: 'Stripe subscriptions',
    caption: 'Automated billing & upgrades',
  },
  {
    label: 'Supabase storage',
    caption: 'Secure media & brand assets',
  },
  {
    label: 'Map markers',
    caption: 'Geo-personalized browsing',
  },
];

const growthMetrics = [
  {
    value: '10k+',
    label: 'daily synced listings',
  },
  {
    value: '4x',
    label: 'faster go-live for new agents',
  },
  {
    value: '24/7',
    label: 'lead routing with WhatsApp',
  },
];

export default defineComponent({
  compatConfig: { MODE: 3 },
  setup() {
    const { showLogin } = useLoginModal();
    const authenticated = inject<ComputedRef<boolean>>('authenticated');
    const username = inject<ComputedRef<string>>('currentUsername');

    const heroImage = MARKETING_HERO_IMAGE;
    const primaryCtaLabel = computed(() => (authenticated?.value ? 'Open your dashboard' : 'Launch your storefront'));
    const primaryCtaTarget = computed(() => (authenticated?.value ? '/agent/dashboard' : '/agent/register'));

    return {
      authenticated,
      username,
      showLogin,
      heroImage,
      valuePropositions,
      operatingPlaybook,
      integrationBadges,
      growthMetrics,
      primaryCtaLabel,
      primaryCtaTarget,
      t$: useI18n().t,
    };
  },
});

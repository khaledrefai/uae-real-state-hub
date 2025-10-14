import { useAccountStore as useStore } from '@/shared/config/store/account-store';
export type AccountStore = ReturnType<typeof useStore>;
export { useStore };

import { useTranslationStore } from '@/shared/config/store/translation-store';
export { useTranslationStore };

export { useAgentSiteStore } from '@/stores/agent-site.store';
export { usePropertyCatalogStore } from '@/stores/property-catalog.store';
export { useBillingStore } from '@/stores/billing.store';

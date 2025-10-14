import axios from 'axios';
import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { useRouter } from 'vue-router';
import { useLoginModal } from '@/account/login-modal';
import type AccountService from '@/account/account.service';
import languages from '@/shared/config/languages';
import EntitiesMenu from '@/entities/entities-menu.vue';

import { useAlertService } from '@/shared/alert/alert.service';
import { useStore } from '@/store';

type PropertyRefreshResult = {
  propertiesCreated: number;
  propertiesUpdated: number;
  propertiesSkipped: number;
  markersCreated: number;
  markersUpdated: number;
};

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'JhiNavbar',
  components: {
    'entities-menu': EntitiesMenu,
  },
  setup() {
    const { showLogin } = useLoginModal();
    const accountService = inject<AccountService>('accountService');
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);
    const changeLanguage = inject<(string) => Promise<void>>('changeLanguage');

    const isActiveLanguage = (key: string) => {
      return key === currentLanguage.value;
    };

    const router = useRouter();
    const store = useStore();
    const alertService = useAlertService();

    const version = `v${APP_VERSION}`;
    const hasAnyAuthorityValues: Ref<any> = ref({});

    const openAPIEnabled = computed(() => store.activeProfiles.indexOf('api-docs') > -1);
    const inProduction = computed(() => store.activeProfiles.indexOf('prod') > -1);
    const authenticated = computed(() => store.authenticated);
    const reelyImporting = ref(false);

    const subIsActive = (input: string | string[]) => {
      const paths = Array.isArray(input) ? input : [input];
      return paths.some(path => {
        return router.currentRoute.value.path.indexOf(path) === 0; // current path starts with this path string
      });
    };

    const logout = async () => {
      localStorage.removeItem('jhi-authenticationToken');
      sessionStorage.removeItem('jhi-authenticationToken');
      store.logout();
      if (router.currentRoute.value.path !== '/') {
        router.push('/');
      }
    };

    const triggerReelyImport = async () => {
      if (reelyImporting.value) {
        return;
      }

      reelyImporting.value = true;
      try {
        const { data } = await axios.post<PropertyRefreshResult>('api/admin/reely/import');
        alertService.showSuccess(
          `Reely import finished. ${data.propertiesCreated} created, ${data.propertiesUpdated} updated, ${data.propertiesSkipped} skipped.`,
        );
      } catch (error: any) {
        if (error?.response) {
          alertService.showHttpError(error.response);
        } else {
          alertService.showError('Unable to trigger the Reely import. Please try again.');
        }
      } finally {
        reelyImporting.value = false;
      }
    };

    return {
      logout,
      subIsActive,
      accountService,
      showLogin,
      changeLanguage,
      languages: languages(),
      isActiveLanguage,
      version,
      currentLanguage,
      hasAnyAuthorityValues,
      openAPIEnabled,
      inProduction,
      authenticated,
      triggerReelyImport,
      reelyImporting,
      t$: useI18n().t,
    };
  },
  methods: {
    hasAnyAuthority(authorities: any): boolean {
      this.accountService.hasAnyAuthorityAndCheckAuth(authorities).then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
      return this.hasAnyAuthorityValues[authorities] ?? false;
    },
  },
});

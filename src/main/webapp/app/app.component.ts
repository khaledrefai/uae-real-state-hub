import { computed, defineComponent, provide } from 'vue';
import { useI18n } from 'vue-i18n';
import { storeToRefs } from 'pinia';

import { useRoute, useRouter } from 'vue-router';
import { useLoginModal } from '@/account/login-modal';
import LoginForm from '@/account/login-form/login-form.vue';
import Ribbon from '@/core/ribbon/ribbon.vue';
import JhiFooter from '@/core/jhi-footer/jhi-footer.vue';
import JhiNavbar from '@/core/jhi-navbar/jhi-navbar.vue';
import { useAlertService } from '@/shared/alert/alert.service';
import { useStore } from '@/store';
import '@/shared/config/dayjs';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'App',
  components: {
    ribbon: Ribbon,
    'jhi-navbar': JhiNavbar,
    'login-form': LoginForm,
    'jhi-footer': JhiFooter,
  },
  setup() {
    provide('alertService', useAlertService());
    const loginModalStore = useLoginModal();
    const { loginModalOpen } = storeToRefs(loginModalStore);
    const { showLogin } = loginModalStore;
    const accountStore = useStore();
    const { account, authenticated } = storeToRefs(accountStore);
    const isAuthenticated = computed(() => Boolean(authenticated.value));
    const isAdmin = computed(() => Boolean(account.value?.authorities?.includes('ROLE_ADMIN')));
    const route = useRoute();
    const router = useRouter();
    const isStorefrontRoute = computed(() => route.path.startsWith('/store/'));
    const logout = () => {
      localStorage.removeItem('jhi-authenticationToken');
      sessionStorage.removeItem('jhi-authenticationToken');
      accountStore.logout();
      if (router.currentRoute.value.path !== '/') {
        router.push('/');
      }
    };

    return {
      loginModalOpen,
      showLogin,
      logout,
      isAdmin,
      isAuthenticated,
      isStorefrontRoute,
      t$: useI18n().t,
    };
  },
});

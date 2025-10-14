<template>
  <div class="agent-onboarding container py-5">
    <div class="row justify-content-center">
      <div class="col-xl-9">
        <div class="text-center mb-5">
          <h1 class="display-4 mb-3">Create your agent workspace</h1>
          <p class="lead text-muted">
            Three quick steps to launch your branded storefront, sync Reely listings, and enable Stripe billing.
          </p>
        </div>

        <div class="card shadow-sm mb-4">
          <div class="card-body py-4 px-4 px-md-5">
            <ol class="onboarding-steps list-unstyled d-flex flex-wrap justify-content-between mb-4">
              <li
                v-for="step in steps"
                :key="step.id"
                :class="['onboarding-step', { active: currentStep === step.id, completed: currentStep > step.id }]"
              >
                <div class="step-index">{{ step.id }}</div>
                <div>
                  <div class="step-title">{{ step.title }}</div>
                  <div class="step-description">{{ step.description }}</div>
                </div>
              </li>
            </ol>

            <form @submit.prevent>
              <section v-show="currentStep === 1">
                <h2 class="h4 mb-4">1. Account credentials</h2>
                <div class="form-row">
                  <div class="form-group col-md-6">
                    <label for="firstName">First name</label>
                    <input
                      id="firstName"
                      v-model.trim="accountForm.firstName"
                      type="text"
                      class="form-control"
                      required
                      placeholder="Aisha"
                    />
                  </div>
                  <div class="form-group col-md-6">
                    <label for="lastName">Last name</label>
                    <input
                      id="lastName"
                      v-model.trim="accountForm.lastName"
                      type="text"
                      class="form-control"
                      required
                      placeholder="Hassan"
                    />
                  </div>
                </div>
                <div class="form-row">
                  <div class="form-group col-md-6">
                    <label for="companyEmail">Work email</label>
                    <input
                      id="companyEmail"
                      v-model.trim="accountForm.companyEmail"
                      type="email"
                      class="form-control"
                      required
                      placeholder="aisha@agency.ae"
                    />
                  </div>
                  <div class="form-group col-md-6">
                    <label for="phone">Contact number</label>
                    <input id="phone" v-model.trim="accountForm.phone" type="tel" class="form-control" placeholder="+971 50 123 4567" />
                  </div>
                </div>
                <div class="form-row">
                  <div class="form-group col-md-6">
                    <label for="password">Password</label>
                    <input id="password" v-model="accountForm.password" type="password" class="form-control" required minlength="8" />
                    <small class="form-text text-muted">Minimum 8 characters.</small>
                  </div>
                  <div class="form-group col-md-6">
                    <label for="confirmPassword">Confirm password</label>
                    <input id="confirmPassword" v-model="accountForm.confirmPassword" type="password" class="form-control" required />
                    <small class="form-text text-danger" v-if="passwordMismatch">Passwords do not match.</small>
                  </div>
                </div>
              </section>

              <section v-show="currentStep === 2">
                <h2 class="h4 mb-4">2. Agency profile</h2>
                <div class="form-group">
                  <label for="companyName">Company name</label>
                  <input
                    id="companyName"
                    v-model.trim="profileForm.companyName"
                    type="text"
                    class="form-control"
                    required
                    placeholder="Skyline Realty"
                  />
                </div>
                <div class="form-group">
                  <label for="fullName">Primary agent</label>
                  <input
                    id="fullName"
                    v-model.trim="profileForm.fullName"
                    type="text"
                    class="form-control"
                    required
                    placeholder="Aisha Hassan"
                  />
                </div>
                <div class="form-row">
                  <div class="form-group col-md-6">
                    <label for="profileEmail">Sales email</label>
                    <input
                      id="profileEmail"
                      v-model.trim="profileForm.email"
                      type="email"
                      class="form-control"
                      required
                      placeholder="sales@agency.ae"
                    />
                  </div>
                  <div class="form-group col-md-6">
                    <label for="profileWhatsapp">WhatsApp number</label>
                    <input
                      id="profileWhatsapp"
                      v-model.trim="profileForm.whatsapp"
                      type="tel"
                      class="form-control"
                      placeholder="+971 55 987 6543"
                    />
                  </div>
                </div>
                <div class="form-row">
                  <div class="form-group col-md-6">
                    <label for="profileCountry">Country</label>
                    <input
                      id="profileCountry"
                      v-model.trim="profileForm.country"
                      type="text"
                      class="form-control"
                      placeholder="United Arab Emirates"
                    />
                  </div>
                  <div class="form-group col-md-6">
                    <label for="profileCity">City</label>
                    <input id="profileCity" v-model.trim="profileForm.city" type="text" class="form-control" placeholder="Dubai" />
                  </div>
                </div>
                <div class="form-group">
                  <label for="profileWebsite">Website (optional)</label>
                  <input
                    id="profileWebsite"
                    v-model.trim="profileForm.website"
                    type="url"
                    class="form-control"
                    placeholder="https://agency.ae"
                  />
                </div>
              </section>

              <section v-show="currentStep === 3">
                <h2 class="h4 mb-4">3. Brand your storefront</h2>
                <div class="form-group">
                  <label for="slug">Storefront URL</label>
                  <div class="input-group">
                    <div class="input-group-prepend">
                      <span class="input-group-text">/store/</span>
                    </div>
                    <input
                      id="slug"
                      v-model.trim="siteForm.slug"
                      type="text"
                      class="form-control"
                      required
                      minlength="3"
                      pattern="[a-z0-9-]+"
                    />
                  </div>
                  <small class="form-text text-muted">
                    This becomes the unique URL your clients will visit. Use letters, numbers, and hyphens only.
                  </small>
                  <small v-if="slugStatus === 'taken'" class="form-text text-danger">This URL is already taken. Try another.</small>
                </div>
                <div class="form-group">
                  <label for="displayName">Display name</label>
                  <input id="displayName" v-model.trim="siteForm.displayName" type="text" class="form-control" required />
                </div>
                <div class="form-row">
                  <div class="form-group col-md-6">
                    <label for="primaryColor">Primary colour</label>
                    <input id="primaryColor" v-model="siteForm.primaryColor" type="color" class="form-control" title="Primary colour" />
                  </div>
                  <div class="form-group col-md-6">
                    <label for="secondaryColor">Secondary colour</label>
                    <input
                      id="secondaryColor"
                      v-model="siteForm.secondaryColor"
                      type="color"
                      class="form-control"
                      title="Secondary colour"
                    />
                  </div>
                </div>
                <div class="form-group">
                  <label for="logo">Logo (optional)</label>
                  <input id="logo" type="file" accept="image/*" class="form-control-file" @change="onLogoSelected" />
                  <small class="form-text text-muted">Square PNG or SVG works best. Max 2MB.</small>
                </div>
                <div class="form-row">
                  <div class="form-group col-md-6">
                    <label for="contactEmail">Contact email</label>
                    <input id="contactEmail" v-model.trim="siteForm.contactEmail" type="email" class="form-control" required />
                  </div>
                  <div class="form-group col-md-6">
                    <label for="contactPhone">Contact phone</label>
                    <input id="contactPhone" v-model.trim="siteForm.contactPhone" type="tel" class="form-control" />
                  </div>
                </div>
                <div class="form-group">
                  <label for="contactWhatsapp">WhatsApp for storefront</label>
                  <input
                    id="contactWhatsapp"
                    v-model.trim="siteForm.contactWhatsapp"
                    type="tel"
                    class="form-control"
                    placeholder="+971 55 222 3344"
                  />
                </div>

                <div class="plan-selector">
                  <h3 class="h5 mb-3">Choose your subscription</h3>
                  <div class="row">
                    <div class="col-md-4 mb-3" v-for="plan in planOptions" :key="plan.id">
                      <div :class="['plan-card', { active: siteForm.planId === plan.id }]" @click="siteForm.planId = plan.id">
                        <div class="plan-name">{{ plan.name }}</div>
                        <div class="plan-price">AED {{ plan.price }}</div>
                        <p class="plan-description mb-0">{{ plan.description }}</p>
                      </div>
                    </div>
                  </div>
                  <small v-if="!planOptions.length" class="text-muted"
                    >No subscription plans found. You can add one later from the admin area.</small
                  >
                </div>
              </section>
            </form>
          </div>
        </div>

        <div class="d-flex justify-content-between align-items-center">
          <button class="btn btn-link" :disabled="currentStep === 1 || loading" @click="previousStep">Back</button>
          <div>
            <button v-if="currentStep < steps.length" class="btn btn-primary btn-lg" :disabled="!canProceed || loading" @click="nextStep">
              Continue
            </button>
            <button v-else class="btn btn-success btn-lg" :disabled="!canProceed || loading" @click="submit">
              <span v-if="loading || uploadingLogo">Creating workspace...</span>
              <span v-else>Launch workspace</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import { computed, inject, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';
import { useRouter } from 'vue-router';

import RegisterService from '@/account/register/register.service';
import type AccountService from '@/account/account.service';
import { useAgentSiteStore, useBillingStore, useStore as useAccountStore } from '@/store';
import type { ISubscriptionPlan } from '@/shared/model/subscription-plan.model';
import { useSupabaseStorage } from '@/shared/composables/use-supabase-storage';
import { useAlertService } from '@/shared/alert/alert.service';

const router = useRouter();
const alertService = useAlertService();
const accountStore = useAccountStore();
const accountService = inject<AccountService>('accountService');
const agentSiteStore = useAgentSiteStore();
const billingStore = useBillingStore();
const registerService = new RegisterService();
const { upload: uploadAsset, uploading: uploadingLogo } = useSupabaseStorage();

const steps = [
  { id: 1, title: 'Create account', description: 'Secure access to the admin workspace.' },
  { id: 2, title: 'Agency profile', description: 'Tell us about your brokerage.' },
  { id: 3, title: 'Brand & subscribe', description: 'Pick your theme and billing plan.' },
];

const currentStep = ref(1);
const loading = ref(false);

const accountForm = reactive({
  firstName: '',
  lastName: '',
  companyEmail: '',
  password: '',
  confirmPassword: '',
  phone: '',
});

const profileForm = reactive({
  companyName: '',
  fullName: '',
  email: '',
  phone: '',
  whatsapp: '',
  country: 'United Arab Emirates',
  city: 'Dubai',
  website: '',
});

const siteForm = reactive({
  slug: '',
  displayName: '',
  theme: 'modern',
  primaryColor: '#1E3C72',
  secondaryColor: '#3DB8C0',
  contactEmail: '',
  contactPhone: '',
  contactWhatsapp: '',
  planId: null as number | null,
});

const logoFile = ref<File | null>(null);
const slugStatus = ref<'idle' | 'checking' | 'available' | 'taken'>('idle');
let slugCheckTimer: ReturnType<typeof setTimeout> | null = null;

const planOptions = computed<ISubscriptionPlan[]>(() => agentSiteStore.availablePlans);

const passwordMismatch = computed(() => accountForm.password && accountForm.password !== accountForm.confirmPassword);

const canProceed = computed(() => {
  if (currentStep.value === 1) {
    return (
      Boolean(accountForm.firstName) &&
      Boolean(accountForm.lastName) &&
      Boolean(accountForm.companyEmail) &&
      accountForm.password.length >= 8 &&
      !passwordMismatch.value
    );
  }
  if (currentStep.value === 2) {
    return Boolean(profileForm.companyName && profileForm.fullName && profileForm.email);
  }
  if (currentStep.value === 3) {
    const slugValid = Boolean(siteForm.slug && slugStatus.value !== 'taken');
    const contactValid = Boolean(siteForm.contactEmail);
    return slugValid && contactValid && !uploadingLogo.value && (!planOptions.value.length || siteForm.planId !== null);
  }
  return true;
});

const sanitizeSlug = (value: string) =>
  value
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/(^-|-$)+/g, '')
    .slice(0, 48);

watch(
  () => profileForm.companyName,
  newValue => {
    if (!newValue) {
      return;
    }
    siteForm.displayName = siteForm.displayName || newValue;
    if (!siteForm.slug || siteForm.slug.startsWith('store-')) {
      siteForm.slug = sanitizeSlug(newValue);
    }
  },
);

watch(
  () => [accountForm.firstName, accountForm.lastName],
  ([first, last]) => {
    profileForm.fullName = [first, last].filter(Boolean).join(' ').trim();
  },
);

watch(
  () => accountForm.companyEmail,
  value => {
    if (!value) {
      return;
    }
    const lower = value.toLowerCase();
    profileForm.email = profileForm.email || lower;
    siteForm.contactEmail = siteForm.contactEmail || lower;
  },
);

watch(
  () => siteForm.slug,
  slug => {
    if (slugCheckTimer) {
      clearTimeout(slugCheckTimer);
    }

    if (!slug) {
      slugStatus.value = 'idle';
      return;
    }

    const sanitized = sanitizeSlug(slug);
    if (sanitized !== slug) {
      siteForm.slug = sanitized;
      return;
    }

    slugStatus.value = 'checking';
    slugCheckTimer = setTimeout(async () => {
      try {
        await axios.get(`api/public/agent-sites/${sanitized}`);
        slugStatus.value = 'taken';
      } catch (error: any) {
        if (error?.response?.status === 404) {
          slugStatus.value = 'available';
          return;
        }
        console.error('Failed to check slug availability', error);
        slugStatus.value = 'idle';
      }
    }, 350);
  },
);
onBeforeUnmount(() => {
  if (slugCheckTimer) {
    clearTimeout(slugCheckTimer);
  }
});

const onLogoSelected = (event: Event) => {
  const input = event.target as HTMLInputElement;
  if (input?.files?.length) {
    logoFile.value = input.files[0];
  }
};

const nextStep = () => {
  if (!canProceed.value || currentStep.value >= steps.length) {
    return;
  }
  currentStep.value += 1;
};

const previousStep = () => {
  if (currentStep.value > 1) {
    currentStep.value -= 1;
  }
};

const persistTokens = (authorizationHeader?: string) => {
  if (!authorizationHeader || !authorizationHeader.startsWith('Bearer ')) {
    return;
  }
  const jwt = authorizationHeader.slice(7);
  localStorage.setItem('jhi-authenticationToken', jwt);
  sessionStorage.removeItem('jhi-authenticationToken');
};

const authenticate = async (login: string, password: string) => {
  const response = await axios.post('api/authenticate', {
    username: login,
    password,
    rememberMe: true,
  });
  persistTokens(response.headers.authorization);
  await accountService?.retrieveAccount();
};

const submit = async () => {
  if (!canProceed.value || loading.value) {
    return;
  }

  loading.value = true;
  try {
    const login = accountForm.companyEmail.toLowerCase();

    await registerService.processRegistration({
      login,
      email: login,
      password: accountForm.password,
      firstName: accountForm.firstName,
      lastName: accountForm.lastName,
      langKey: 'en',
    });

    await authenticate(login, accountForm.password);

    const profileResponse = await axios.post('api/agent-profiles', {
      externalUserId: login,
      fullName: profileForm.fullName,
      companyName: profileForm.companyName,
      email: profileForm.email,
      phone: profileForm.phone,
      whatsapp: profileForm.whatsapp,
      country: profileForm.country,
      city: profileForm.city,
      website: profileForm.website,
      active: true,
    });

    agentSiteStore.setOwner(profileResponse.data);

    let logoUrl: string | null = null;
    if (logoFile.value) {
      const extension = logoFile.value.name.split('.').pop() ?? 'png';
      const path = `agent-sites/${profileResponse.data.id}/logo-${Date.now()}.${extension}`;
      logoUrl = await uploadAsset(path, logoFile.value, { contentType: logoFile.value.type });
    }

    const plan = siteForm.planId ? planOptions.value.find(candidate => candidate.id === siteForm.planId) : null;

    const createdSite = await agentSiteStore.upsertSite({
      slug: siteForm.slug,
      displayName: siteForm.displayName || profileForm.companyName,
      theme: siteForm.theme,
      primaryColor: siteForm.primaryColor,
      secondaryColor: siteForm.secondaryColor,
      logoUrl,
      contactEmail: siteForm.contactEmail,
      contactPhone: siteForm.contactPhone,
      contactWhatsapp: siteForm.contactWhatsapp,
      isActive: true,
      owner: { id: profileResponse.data.id },
      plan: plan ? { id: plan.id } : null,
    });

    alertService.showSuccess('Your workspace is ready. We will redirect you to billing in a moment.');

    if (plan) {
      try {
        const origin = window.location.origin;
        await billingStore.redirectToCheckout({
          planId: plan.id,
          billingCycle: 'monthly',
          agentSiteId: createdSite?.id,
          successUrl: `${origin}/agent/dashboard?onboarding=completed`,
          cancelUrl: `${origin}/agent/dashboard?onboarding=cancelled`,
        });
        return;
      } catch (error) {
        alertService.showError('Stripe checkout could not be started. You can subscribe later from the dashboard.');
      }
    }

    await router.push('/agent/dashboard');
  } catch (error: any) {
    console.error('Agent onboarding failed', error);
    if (error?.response) {
      alertService.showHttpError(error.response);
    } else {
      alertService.showError('Unable to complete onboarding. Please try again.');
    }
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  try {
    await agentSiteStore.fetchPlans();
    if (planOptions.value.length && !siteForm.planId) {
      siteForm.planId = planOptions.value[0].id;
    }
  } catch (error) {
    console.warn('Failed to load subscription plans', error);
  }
});
</script>

<style scoped>
.agent-onboarding {
  min-height: 100vh;
}

.onboarding-step {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  border-radius: 0.75rem;
  background: #f1f4f8;
  flex: 1 1 220px;
  margin: 0.5rem;
  transition: all 0.2s ease;
}

.onboarding-step.active {
  background: #244a87;
  color: #fff;
}

.onboarding-step.completed {
  border: 1px solid rgba(36, 74, 135, 0.5);
}

.step-index {
  font-size: 1.35rem;
  font-weight: 700;
  margin-right: 1rem;
}

.step-title {
  font-weight: 600;
}

.step-description {
  font-size: 0.85rem;
  opacity: 0.8;
}

.plan-selector .plan-card {
  border: 1px solid #d6deeb;
  border-radius: 1rem;
  padding: 1.5rem;
  cursor: pointer;
  height: 100%;
  transition: all 0.2s ease;
}

.plan-selector .plan-card:hover,
.plan-selector .plan-card.active {
  border-color: #244a87;
  box-shadow: 0 0 0 3px rgba(36, 74, 135, 0.2);
}

.plan-name {
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.plan-price {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
}

@media (max-width: 767.98px) {
  .onboarding-step {
    flex-direction: column;
    text-align: center;
  }
  .step-index {
    margin-right: 0;
    margin-bottom: 0.5rem;
  }
}
</style>

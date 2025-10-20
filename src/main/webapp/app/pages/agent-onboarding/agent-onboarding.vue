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
/* Modern Onboarding with Progress Flow */
.agent-onboarding {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  background-attachment: fixed;
  position: relative;
  padding: 3rem 0;
}

.agent-onboarding::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(circle at 25% 25%, rgba(252, 70, 107, 0.2), transparent 50%),
    radial-gradient(circle at 75% 75%, rgba(63, 94, 251, 0.2), transparent 50%),
    radial-gradient(circle at 50% 50%, rgba(240, 147, 251, 0.15), transparent 50%);
  pointer-events: none;
  animation: onboardingPulse 10s ease infinite alternate;
}

@keyframes onboardingPulse {
  0% {
    opacity: 0.8;
  }
  100% {
    opacity: 1;
  }
}

/* Typography */
.agent-onboarding h1 {
  color: white;
  font-weight: 900;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  letter-spacing: -1px;
}

.agent-onboarding .lead {
  color: rgba(255, 255, 255, 0.95);
  font-weight: 500;
  font-size: 1.15rem;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

/* Modern Card */
.card {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(30px);
  -webkit-backdrop-filter: blur(30px);
  border: 2px solid rgba(255, 255, 255, 0.4);
  border-radius: 2rem;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  position: relative;
}

.card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
}

.card-body {
  padding: 3rem 2.5rem;
}

/* Step Indicators */
.onboarding-steps {
  margin: 0;
  padding: 0;
  margin-bottom: 3rem;
}

.onboarding-step {
  display: flex;
  align-items: center;
  padding: 1.25rem 1.5rem;
  border-radius: 1.25rem;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  flex: 1 1 240px;
  margin: 0.75rem;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.onboarding-step::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  transform: scaleY(0);
  transition: transform 0.4s ease;
}

.onboarding-step:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.12) 0%, rgba(118, 75, 162, 0.12) 100%);
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
}

.onboarding-step.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: rgba(255, 255, 255, 0.3);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.4);
  transform: scale(1.05);
}

.onboarding-step.active::before {
  transform: scaleY(1);
}

.onboarding-step.completed {
  border: 2px solid rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.15) 0%, rgba(118, 75, 162, 0.15) 100%);
  position: relative;
}

.onboarding-step.completed::after {
  content: '✓';
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  width: 24px;
  height: 24px;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 900;
  font-size: 0.85rem;
  box-shadow: 0 4px 12px rgba(17, 153, 142, 0.4);
}

.step-index {
  font-size: 1.75rem;
  font-weight: 900;
  margin-right: 1.25rem;
  width: 45px;
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  flex-shrink: 0;
}

.onboarding-step.active .step-index {
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0 0 0 4px rgba(255, 255, 255, 0.2);
}

.step-title {
  font-weight: 700;
  font-size: 1.05rem;
  margin-bottom: 0.25rem;
}

.step-description {
  font-size: 0.85rem;
  opacity: 0.85;
  font-weight: 500;
}

/* Form Styling */
form h2 {
  color: #2d3748;
  font-weight: 800;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 3px solid rgba(102, 126, 234, 0.2);
}

.form-group label {
  font-weight: 700;
  color: #2d3748;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.5rem;
}

.form-control {
  border-radius: 0.875rem;
  border: 2px solid rgba(102, 126, 234, 0.2);
  padding: 0.875rem 1.25rem;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.95);
  font-weight: 500;
  font-size: 0.95rem;
}

.form-control:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
  outline: none;
  background: white;
  transform: translateY(-2px);
}

.form-control::placeholder {
  color: #a0aec0;
  font-weight: 400;
}

.form-text {
  font-size: 0.8rem;
  font-weight: 600;
  margin-top: 0.5rem;
}

.text-muted {
  color: #718096 !important;
}

.text-danger {
  color: #f56565 !important;
  font-weight: 700;
}

/* Plan Selector */
.plan-selector .plan-card {
  border: 2px solid rgba(102, 126, 234, 0.2);
  border-radius: 1.5rem;
  padding: 2rem 1.75rem;
  cursor: pointer;
  height: 100%;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  background: white;
  position: relative;
  overflow: hidden;
}

.plan-selector .plan-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  transform: scaleX(0);
  transition: transform 0.4s ease;
}

.plan-selector .plan-card:hover {
  transform: translateY(-8px);
  border-color: #667eea;
  box-shadow: 0 15px 45px rgba(102, 126, 234, 0.25);
}

.plan-selector .plan-card:hover::before {
  transform: scaleX(1);
}

.plan-selector .plan-card.active {
  border-color: #667eea;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08) 0%, rgba(118, 75, 162, 0.08) 100%);
  box-shadow: 0 12px 40px rgba(102, 126, 234, 0.3);
  transform: scale(1.05);
}

.plan-selector .plan-card.active::before {
  transform: scaleX(1);
}

.plan-selector .plan-card.active::after {
  content: '✓ Selected';
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
  padding: 0.35rem 0.75rem;
  border-radius: 0.75rem;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  box-shadow: 0 4px 12px rgba(17, 153, 142, 0.3);
}

.plan-name {
  font-weight: 800;
  font-size: 1.5rem;
  margin-bottom: 0.75rem;
  color: #2d3748;
}

.plan-price {
  font-size: 2rem;
  font-weight: 900;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0.5rem;
}

/* Buttons */
.btn {
  border-radius: 0.875rem;
  padding: 0.875rem 2rem;
  font-weight: 700;
  transition: all 0.3s ease;
  border: none;
  position: relative;
  overflow: hidden;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  font-size: 0.9rem;
}

.btn::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition:
    width 0.6s,
    height 0.6s;
}

.btn:hover::before {
  width: 350px;
  height: 350px;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.5);
}

.btn-secondary {
  background: linear-gradient(135deg, #a8a8a8 0%, #cccccc 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(168, 168, 168, 0.3);
}

.btn-secondary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(168, 168, 168, 0.4);
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 0.6;
  }
  50% {
    opacity: 0.8;
  }
}

/* Navigation Buttons */
.d-flex.justify-content-between {
  margin-top: 2.5rem;
  padding-top: 2rem;
  border-top: 2px solid rgba(102, 126, 234, 0.1);
}

/* Responsive Design */
@media (max-width: 767.98px) {
  .agent-onboarding {
    padding: 2rem 0;
  }

  .agent-onboarding h1 {
    font-size: 2rem;
  }

  .onboarding-step {
    flex-direction: column;
    text-align: center;
    padding: 1rem;
  }

  .step-index {
    margin-right: 0;
    margin-bottom: 0.75rem;
  }

  .card-body {
    padding: 2rem 1.5rem;
  }

  .onboarding-steps {
    flex-direction: column;
  }

  .onboarding-step {
    width: 100%;
    margin: 0.5rem 0;
  }

  .plan-selector .plan-card {
    margin-bottom: 1.5rem;
  }

  .btn {
    width: 100%;
    margin-bottom: 0.75rem;
  }
}

/* Loading States */
.spinner-border {
  width: 2.5rem;
  height: 2.5rem;
  border-width: 0.3rem;
  color: #667eea;
}

/* Selection */
::selection {
  background: rgba(102, 126, 234, 0.3);
  color: #2d3748;
}

/* Smooth Scrolling */
html {
  scroll-behavior: smooth;
}
</style>

<template>
  <div class="marketing-home">
    <section class="hero-section">
      <div class="container hero-container">
        <div class="row align-items-center">
          <div class="col-lg-6 mb-5 mb-lg-0">
            <h1 class="display-4 font-weight-bold mb-4">One workspace to run every property mandate</h1>
            <p class="lead mb-4">
              Launch a branded storefront, sync Reely listings each morning, and route every lead back to your team—without touching a
              spreadsheet.
            </p>
            <div class="hero-actions d-flex flex-wrap align-items-center mb-4">
              <router-link :to="primaryCtaTarget" class="btn btn-primary btn-lg mr-3 mb-3">{{ primaryCtaLabel }}</router-link>
              <button v-if="!authenticated?.value" class="btn btn-outline-light btn-lg mb-3" type="button" @click="showLogin()">
                Sign in
              </button>
            </div>
            <div class="hero-metrics d-flex flex-wrap">
              <div class="metric" v-for="metric in growthMetrics" :key="metric.label">
                <span class="metric-value">{{ metric.value }}</span>
                <span class="metric-label">{{ metric.label }}</span>
              </div>
            </div>
          </div>
          <div class="col-lg-6">
            <div class="hero-visual">
              <img :src="heroImage" alt="Dubai skyline with real estate agents collaborating" class="img-fluid rounded" />
              <div class="hero-visual-card">
                <span class="visual-card-label">Today</span>
                <strong>18 new leads</strong>
                <span>7 ready for follow-up</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section id="features" class="section section-white">
      <div class="container">
        <div class="section-heading text-center mb-5">
          <h2 class="h1 mb-3">Why top brokerages switch to Real Estate Hub</h2>
          <p class="text-muted lead">
            Automations, collaboration, and a polished storefront—packaged for the teams that sell the UAE lifestyle every single day.
          </p>
        </div>
        <div class="row">
          <div class="col-md-4 mb-4" v-for="feature in valuePropositions" :key="feature.title">
            <div class="card h-100 shadow-sm feature-card">
              <div class="card-body">
                <div class="icon-circle mb-3">
                  <span class="icon-letter">{{ feature.icon.charAt(0).toUpperCase() }}</span>
                </div>
                <h3 class="h5 mb-3">{{ feature.title }}</h3>
                <p class="text-muted mb-0">{{ feature.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="section section-muted">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-lg-6 mb-4 mb-lg-0">
            <h2 class="h1 mb-4">Your daily operating playbook</h2>
            <ul class="playbook list-unstyled">
              <li v-for="step in operatingPlaybook" :key="step.title" class="playbook-step">
                <h4 class="h5 mb-2">{{ step.title }}</h4>
                <p class="text-muted mb-0">{{ step.detail }}</p>
              </li>
            </ul>
          </div>
          <div class="col-lg-5 offset-lg-1">
            <div class="automation-panel">
              <div class="card-body">
                <h3 class="h5 text-uppercase text-muted mb-3">What you can automate</h3>
                <ul class="list-unstyled mb-0">
                  <li class="automation-item">Daily Reely syncs with diff previews</li>
                  <li class="automation-item">One-click publishing to your B2C site</li>
                  <li class="automation-item">Lead routing to email & WhatsApp</li>
                  <li class="automation-item">Stripe-powered billing for every agent</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="section section-white">
      <div class="container">
        <div class="row align-items-center">
          <div class="col-lg-5 mb-4 mb-lg-0">
            <h2 class="h1 mb-3">Plug & play integrations</h2>
            <p class="text-muted mb-4">
              We activate Stripe for billing, Supabase for secure file storage, and live map markers out of the box—so your agents stay
              focused on selling.
            </p>
          </div>
          <div class="col-lg-7">
            <div class="row">
              <div class="col-sm-4 mb-3" v-for="integration in integrationBadges" :key="integration.label">
                <div class="integration-badge h-100">
                  <span class="badge-title">{{ integration.label }}</span>
                  <span class="badge-caption">{{ integration.caption }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="section final-cta text-light">
      <div class="container text-center">
        <h2 class="display-4 mb-3">Own the client journey from first click to closed deal</h2>
        <p class="lead mb-4">
          Set up takes minutes. Your storefront, admin, billing, and agent dashboard are ready the moment you onboard.
        </p>
        <router-link class="btn btn-light btn-lg" to="/agent/register">Create your agent workspace</router-link>
      </div>
    </section>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>

<style scoped>
.marketing-home {
  background: #f7f4ed;
  color: #142433;
}

.hero-section {
  position: relative;
  overflow: hidden;
  background:
    linear-gradient(105deg, rgba(10, 31, 50, 0.96) 0%, rgba(10, 31, 50, 0.9) 48%, rgba(15, 118, 110, 0.84) 100%),
    url('/content/images/uae-skyline.svg') no-repeat right bottom / min(56rem, 70vw);
  color: #fff;
}

.hero-container {
  padding-top: clamp(4rem, 8vw, 7rem);
  padding-bottom: clamp(3.5rem, 7vw, 6rem);
}

.hero-section h1 {
  max-width: 12ch;
  font-size: clamp(2.5rem, 5.6vw, 5rem);
  line-height: 0.98;
  letter-spacing: 0;
}

.hero-section .lead {
  max-width: 36rem;
  color: rgba(255, 255, 255, 0.82);
  font-size: 1.18rem;
  line-height: 1.7;
}

.hero-actions .btn {
  min-width: 12rem;
  border-radius: 0.65rem;
  font-weight: 800;
  padding: 0.85rem 1.25rem;
}

.hero-actions .btn-primary {
  background: #0f766e;
  border-color: #0f766e;
  box-shadow: 0 16px 34px rgba(15, 118, 110, 0.28);
}

.hero-actions .btn-outline-light {
  border-color: rgba(255, 255, 255, 0.44);
}

.hero-metrics .metric {
  margin-right: 2rem;
  margin-bottom: 1rem;
  padding-right: 2rem;
  border-right: 1px solid rgba(255, 255, 255, 0.16);
}

.hero-metrics .metric:last-child {
  border-right: 0;
}

.hero-metrics .metric-value {
  display: block;
  font-size: 2.15rem;
  font-weight: 850;
  line-height: 1;
}

.hero-metrics .metric-label {
  text-transform: uppercase;
  font-size: 0.75rem;
  letter-spacing: 0.06em;
  opacity: 0.76;
}

.hero-visual {
  position: relative;
  border-radius: 1.2rem;
  padding: 0.65rem;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 28px 70px rgba(0, 0, 0, 0.25);
}

.hero-visual img {
  min-height: 25rem;
  max-height: 29rem;
  object-fit: cover;
  width: 100%;
  border-radius: 0.9rem !important;
  filter: saturate(0.96) contrast(1.03);
}

.hero-visual-card {
  position: absolute;
  left: 1.5rem;
  bottom: 1.5rem;
  display: grid;
  gap: 0.15rem;
  width: min(14rem, calc(100% - 3rem));
  padding: 1rem;
  border-radius: 0.8rem;
  background: rgba(255, 255, 255, 0.94);
  color: #142433;
  box-shadow: 0 18px 34px rgba(10, 31, 50, 0.18);
}

.visual-card-label {
  color: #0f766e;
  font-size: 0.76rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.hero-visual-card strong {
  font-size: 1.45rem;
  line-height: 1.1;
}

.section {
  padding: 4.5rem 0;
}

.section-white {
  background: #fffdf8;
}

.section-muted {
  background: #efe9dc;
}

.section-heading {
  max-width: 48rem;
  margin-left: auto;
  margin-right: auto;
}

.section h2 {
  font-weight: 850;
  letter-spacing: 0;
  color: #142433;
}

.feature-card {
  border: 1px solid rgba(20, 36, 51, 0.08);
  border-radius: 0.8rem;
  box-shadow: 0 16px 40px rgba(20, 36, 51, 0.06) !important;
  transition:
    transform 0.18s ease,
    box-shadow 0.18s ease;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 22px 48px rgba(20, 36, 51, 0.1) !important;
}

.icon-circle {
  width: 48px;
  height: 48px;
  border-radius: 0.75rem;
  background: rgba(15, 118, 110, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0f766e;
  font-weight: 800;
  font-size: 1.25rem;
}

.playbook-step + .playbook-step {
  margin-top: 1.5rem;
}

.playbook-step {
  position: relative;
  padding-left: 1.65rem;
}

.playbook-step::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0.35rem;
  width: 0.55rem;
  height: 0.55rem;
  border-radius: 999px;
  background: #c8a95a;
  box-shadow: 0 0 0 0.35rem rgba(200, 169, 90, 0.18);
}

.automation-panel {
  border-radius: 0.9rem;
  background: #fffdf8;
  border: 1px solid rgba(20, 36, 51, 0.08);
  box-shadow: 0 18px 44px rgba(20, 36, 51, 0.08);
}

.automation-item {
  position: relative;
  padding-left: 1.75rem;
  margin-bottom: 1rem;
}

.automation-item::before {
  content: '\2713';
  position: absolute;
  left: 0;
  top: 0;
  color: #0f766e;
  font-weight: bold;
}

.integration-badge {
  background: #f7f4ed;
  border: 1px solid rgba(20, 36, 51, 0.08);
  border-radius: 0.75rem;
  padding: 1.5rem 1.25rem;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.integration-badge .badge-title {
  font-weight: 600;
  font-size: 1rem;
  margin-bottom: 0.5rem;
}

.integration-badge .badge-caption {
  font-size: 0.85rem;
  color: rgba(30, 47, 65, 0.75);
}

.final-cta {
  background: #0a1f32;
}

.final-cta h2 {
  color: #ffffff;
}

.final-cta .btn {
  border-radius: 0.65rem;
  font-weight: 800;
}

@media (max-width: 767.98px) {
  .hero-section {
    text-align: center;
  }

  .hero-actions .btn {
    width: 100%;
  }

  .hero-metrics {
    justify-content: center;
  }

  .hero-metrics .metric {
    padding-right: 0;
    margin-right: 1.2rem;
    border-right: 0;
  }

  .hero-visual img {
    min-height: 18rem;
  }
}
</style>

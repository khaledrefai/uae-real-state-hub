<template>
  <div class="marketing-home">
    <section class="hero-section text-light">
      <div class="container py-5">
        <div class="row align-items-center">
          <div class="col-lg-6 mb-5 mb-lg-0">
            <span class="badge badge-pill badge-highlight mb-3">Built for UAE agencies</span>
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
            <div class="hero-visual shadow-lg">
              <img :src="heroImage" alt="Dubai skyline with real estate agents collaborating" class="img-fluid rounded" />
            </div>
          </div>
        </div>
      </div>
    </section>

    <section id="features" class="section bg-white">
      <div class="container">
        <div class="text-center mb-5">
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

    <section class="section bg-light">
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
            <div class="card shadow-sm">
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

    <section class="section bg-white">
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

    <section class="section bg-gradient text-light">
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
  background-color: #f8f9fa;
}

.hero-section {
  background: linear-gradient(135deg, #0d2346 0%, #274b8b 45%, #3db8c0 100%);
}

.badge-highlight {
  background: rgba(255, 255, 255, 0.15);
  color: #fff;
  font-weight: 600;
  letter-spacing: 0.08em;
}

.hero-actions .btn {
  min-width: 220px;
}

.hero-metrics .metric {
  margin-right: 2rem;
  margin-bottom: 1rem;
}

.hero-metrics .metric-value {
  display: block;
  font-size: 2rem;
  font-weight: 700;
}

.hero-metrics .metric-label {
  text-transform: uppercase;
  font-size: 0.75rem;
  letter-spacing: 0.08em;
  opacity: 0.8;
}

.hero-visual img {
  max-height: 420px;
  object-fit: cover;
  width: 100%;
}

.section {
  padding: 4.5rem 0;
}

.feature-card {
  border: 0;
  border-radius: 1.25rem;
}

.icon-circle {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(39, 75, 139, 0.12);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #274b8b;
  font-weight: 600;
  font-size: 1.25rem;
}

.playbook-step + .playbook-step {
  margin-top: 1.5rem;
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
  color: #28a745;
  font-weight: bold;
}

.integration-badge {
  background: linear-gradient(135deg, rgba(39, 75, 139, 0.12), rgba(61, 184, 192, 0.12));
  border-radius: 1rem;
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

.bg-gradient {
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
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
}
</style>

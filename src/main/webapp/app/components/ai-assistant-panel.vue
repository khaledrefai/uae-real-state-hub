<template>
  <section class="ai-assistant card shadow-sm">
    <header class="card-header d-flex justify-content-between align-items-center">
      <div>
        <h2 class="h5 mb-0">AI Property Assistant</h2>
        <small class="text-muted">Use catalogue insights to answer customer questions instantly.</small>
      </div>
      <button class="btn btn-outline-secondary btn-sm" type="button" @click="resetConversation" :disabled="loading">Reset</button>
    </header>

    <div class="chat-card-body">
      <div ref="scrollContainer" class="chat-thread" role="log" aria-live="polite">
        <div v-for="message in messages" :key="message.id" :class="['chat-item', message.role]">
          <div class="chat-bubble">
            <p v-for="(paragraph, index) in normalizeContent(message.content)" :key="`${message.id}-${index}`">
              {{ paragraph }}
            </p>
          </div>
        </div>
        <div v-if="loading" class="chat-item assistant">
          <div class="chat-bubble chat-loading">
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            <span class="ml-2">Consulting our listings…</span>
          </div>
        </div>
      </div>

      <aside v-if="contextEntries.length" class="chat-context">
        <h3 class="h6 text-uppercase text-muted">Referenced properties</h3>
        <ul class="list-unstyled mb-0">
          <li v-for="property in contextEntries" :key="property.extId ?? property.propertyId" class="context-row">
            <button class="btn btn-link p-0 text-left" type="button" @click="openProperty(property)">
              <strong>{{ property.name ?? 'Property' }}</strong>
            </button>
            <div class="text-muted small">
              {{ [property.area, property.city].filter(Boolean).join(', ') || 'Location TBD' }}
            </div>
            <div v-if="property.priceRange" class="small text-primary mt-1">{{ property.priceRange }}</div>
            <ul v-if="property.keyPoints?.length" class="small text-muted pl-3 mb-1">
              <li v-for="(bullet, bulletIndex) in property.keyPoints.slice(0, 2)" :key="bulletIndex">{{ bullet }}</li>
            </ul>
          </li>
        </ul>
      </aside>
    </div>

    <footer class="card-footer">
      <form class="chat-form" @submit.prevent="sendMessage">
        <div class="form-group mb-2">
          <label class="sr-only" for="agentChatInput">Ask the assistant</label>
          <textarea
            id="agentChatInput"
            ref="inputRef"
            v-model="draft"
            class="form-control"
            rows="3"
            placeholder="Try: Summarise latest availability for Downtown Dubai 2BR units"
            :disabled="loading"
            @keydown.enter.exact.prevent="sendMessage"
          ></textarea>
        </div>
        <div class="d-flex justify-content-between align-items-center">
          <span v-if="error" class="text-danger small">{{ error }}</span>
          <button class="btn btn-primary" type="submit" :disabled="loading || !readyToSend">Send</button>
        </div>
      </form>
    </footer>
  </section>
</template>

<script setup lang="ts">
import axios from 'axios';
import { computed, nextTick, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

import { useAgentSiteStore, usePropertyCatalogStore } from '@/store';

interface ChatMessage {
  id: number;
  role: 'assistant' | 'user';
  content: string;
}

interface BackendPropertyContext {
  propertyId?: number | null;
  extId?: number | null;
  name?: string | null;
  developer?: string | null;
  area?: string | null;
  city?: string | null;
  country?: string | null;
  status?: string | null;
  priceRange?: string | null;
  unitsRange?: string | null;
  score?: number | null;
  keyPoints?: string[];
}

interface AgentStatePayload {
  budgetMaxAed?: number | null;
  purpose?: string | null;
  plan?: string | null;
  interestConfirmed?: boolean | null;
  leadName?: string | null;
  leadPhone?: string | null;
  leadCaptured?: boolean;
  stage?: string | null;
  minArea?: number | null;
  maxArea?: number | null;
  completionYearFrom?: number | null;
  completionYearTo?: number | null;
  city?: string | null;
  area?: string | null;
}

interface BackendChatResponse {
  answer: string;
  context: BackendPropertyContext[];
  agentState?: AgentStatePayload;
  leadCreated?: boolean;
}

const router = useRouter();
const propertyStore = usePropertyCatalogStore();
const agentSiteStore = useAgentSiteStore();

const messages = ref<ChatMessage[]>([
  {
    id: 0,
    role: 'assistant',
    content: 'Need a quick customer reply? Ask about pricing, availability, or nearby amenities.',
  },
]);
const contextEntries = ref<BackendPropertyContext[]>([]);
const agentState = ref<AgentStatePayload | undefined>(undefined);
const draft = ref('');
const loading = ref(false);
const error = ref('');
const counter = ref(1);

const scrollContainer = ref<HTMLDivElement | null>(null);
const inputRef = ref<HTMLTextAreaElement | null>(null);

const readyToSend = computed(() => draft.value.trim().length > 0);
const siteSlug = computed(() => agentSiteStore.site?.slug ?? '');

const normalizeContent = (value: string): string[] =>
  value
    .split(/\n+/)
    .map(line => line.trim())
    .filter(Boolean);

const focusInput = () => {
  nextTick(() => {
    inputRef.value?.focus();
  });
};

onMounted(() => focusInput());

const scrollToBottom = () => {
  nextTick(() => {
    const container = scrollContainer.value;
    if (container) {
      container.scrollTop = container.scrollHeight;
    }
  });
};

const appendMessage = (message: Omit<ChatMessage, 'id'>) => {
  messages.value.push({ ...message, id: counter.value++ });
  scrollToBottom();
};

const sendMessage = async () => {
  if (!readyToSend.value || loading.value) {
    return;
  }
  const content = draft.value.trim();
  draft.value = '';
  error.value = '';

  appendMessage({ role: 'user', content });

  try {
    loading.value = true;
    const { data } = await axios.post<BackendChatResponse>('api/chat', {
      message: content,
      agentState: agentState.value,
    });
    appendMessage({ role: 'assistant', content: data?.answer ?? 'No response available.' });
    contextEntries.value = Array.isArray(data?.context) ? data.context : [];
    agentState.value = data?.agentState ?? agentState.value;
  } catch (e) {
    error.value = 'Unable to reach the assistant.';
    appendMessage({
      role: 'assistant',
      content: 'We could not retrieve insights right now. Please try again shortly.',
    });
    console.error('Agent AI chat failed', e);
  } finally {
    loading.value = false;
    focusInput();
  }
};

const openProperty = (property: BackendPropertyContext) => {
  const slug = siteSlug.value;
  if (!slug) {
    return;
  }
  const propertyId =
    property.propertyId ?? propertyStore.items.find(item => typeof item.extId === 'number' && item.extId === property.extId)?.id ?? null;

  if (propertyId) {
    router.push(`/store/${slug}/properties/${propertyId}`);
  }
};

const resetConversation = () => {
  messages.value = [
    {
      id: 0,
      role: 'assistant',
      content: 'Need a quick customer reply? Ask about pricing, availability, or nearby amenities.',
    },
  ];
  contextEntries.value = [];
  draft.value = '';
  error.value = '';
  counter.value = 1;
  focusInput();
};
</script>

<style scoped>
.ai-assistant {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  border: 1px solid rgba(148, 163, 184, 0.2);
  border-radius: 1rem;
  overflow: hidden;
  background: #ffffff;
}

.card-header {
  border-bottom: 1px solid rgba(148, 163, 184, 0.18);
  background:
    radial-gradient(circle at 0% 0%, rgba(37, 99, 235, 0.08), transparent 45%), linear-gradient(180deg, rgba(248, 250, 252, 0.9), #ffffff);
}

.card-footer {
  border-top: 1px solid rgba(148, 163, 184, 0.18);
  background: rgba(255, 255, 255, 0.96);
}

.chat-card-body {
  display: grid;
  grid-template-columns: minmax(0, 2fr) minmax(0, 1fr);
  gap: 1.5rem;
  padding: 1.5rem;
  align-items: start;
  min-height: min(62vh, 640px);
  background: linear-gradient(180deg, rgba(241, 245, 249, 0.42), rgba(255, 255, 255, 0.95) 16rem), #ffffff;
}

.chat-thread {
  max-height: min(52vh, 520px);
  min-height: 20rem;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem;
  border-radius: 1rem;
  border: 1px solid rgba(148, 163, 184, 0.18);
  background: rgba(255, 255, 255, 0.86);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.85);
}

.chat-item {
  display: flex;
}

.chat-item.user {
  justify-content: flex-end;
}

.chat-item.assistant {
  justify-content: flex-start;
}

.chat-bubble {
  max-width: 90%;
  padding: 0.9rem 1.1rem;
  border-radius: 0.9rem;
  border: 1px solid rgba(148, 163, 184, 0.35);
  background: rgba(248, 250, 252, 0.95);
  font-size: 0.95rem;
  line-height: 1.6;
  color: #0f172a;
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.04);
  overflow-wrap: anywhere;
}

.chat-bubble p {
  margin: 0;
}

.chat-bubble p + p {
  margin-top: 0.45rem;
}

.chat-item.user .chat-bubble {
  background: linear-gradient(135deg, rgba(37, 99, 235, 0.12), rgba(14, 165, 233, 0.08));
  border-color: rgba(37, 99, 235, 0.25);
}

.chat-loading {
  display: inline-flex;
  align-items: center;
}

.chat-context {
  border: 1px solid rgba(148, 163, 184, 0.18);
  border-radius: 1rem;
  padding: 1.1rem;
  background: rgba(255, 255, 255, 0.9);
  overflow-y: auto;
  max-height: min(58vh, 620px);
  min-height: 16rem;
}

.chat-context .h6 {
  margin-bottom: 0.85rem;
  font-size: 0.8rem;
  letter-spacing: 0.08em;
}

.chat-context .context-row {
  padding: 0.8rem;
  border-radius: 0.85rem;
  background: rgba(248, 250, 252, 0.95);
  border: 1px solid rgba(148, 163, 184, 0.18);
}

.chat-context .context-row .btn-link {
  font-size: 0.98rem;
  line-height: 1.35;
}

.chat-context .context-row .small {
  font-size: 0.84rem;
  line-height: 1.4;
}

.context-row + .context-row {
  margin-top: 0.8rem;
  padding-top: 0.8rem;
  border-top: none;
}

.chat-form {
  margin-bottom: 0;
}

.chat-form .form-control {
  border-radius: 0.85rem;
  border-color: rgba(148, 163, 184, 0.4);
  min-height: 3.2rem;
  max-height: 10rem;
  resize: vertical;
  box-shadow: none;
}

.chat-form .form-control:focus {
  border-color: rgba(37, 99, 235, 0.55);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.14);
}

.chat-thread::-webkit-scrollbar,
.chat-context::-webkit-scrollbar {
  width: 8px;
}

.chat-thread::-webkit-scrollbar-thumb,
.chat-context::-webkit-scrollbar-thumb {
  background: rgba(148, 163, 184, 0.5);
  border-radius: 999px;
}

.chat-thread::-webkit-scrollbar-track,
.chat-context::-webkit-scrollbar-track {
  background: transparent;
}

@media (max-width: 992px) {
  .chat-card-body {
    grid-template-columns: 1fr;
    gap: 1rem;
    min-height: auto;
  }

  .chat-context {
    max-height: 18rem;
    min-height: 0;
    padding: 0.9rem;
  }

  .chat-thread {
    max-height: min(42vh, 420px);
    min-height: 16rem;
  }
}

@media (max-width: 576px) {
  .chat-card-body {
    padding: 1rem;
  }

  .chat-thread {
    padding: 0.85rem;
    min-height: 14rem;
  }

  .chat-bubble {
    max-width: 96%;
  }

  .chat-context .context-row {
    padding: 0.7rem;
  }
}
</style>

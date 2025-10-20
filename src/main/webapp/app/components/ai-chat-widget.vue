<template>
  <div class="ai-chat-widget">
    <button class="ai-chat-toggle" type="button" @click="togglePanel" aria-label="Open AI assistant">
      <span class="toggle-icon">AI</span>
      <span class="toggle-label">Ask AI</span>
    </button>

    <transition name="chat-fade">
      <section v-if="isOpen" class="ai-chat-panel" role="dialog" aria-modal="true">
        <header class="chat-header">
          <div>
            <h2>AI Property Assistant</h2>
            <p class="chat-subtitle">Ask about developments, payment plans, or availability.</p>
          </div>
          <button class="close-btn" type="button" @click="togglePanel" aria-label="Close AI assistant">&times;</button>
        </header>

        <div ref="scrollContainer" class="chat-body">
          <div v-for="message in messages" :key="message.id" :class="['chat-message', message.role]">
            <div class="message-bubble">
              <p v-for="(paragraph, index) in normalizeContent(message.content)" :key="`${message.id}-${index}`">{{ paragraph }}</p>
            </div>
          </div>
          <div v-if="loading" class="chat-message assistant">
            <div class="message-bubble loading">
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="dot"></span>
            </div>
          </div>
        </div>

        <div v-if="contextEntries.length" class="chat-context">
          <h3>Referenced properties</h3>
          <ul>
            <li v-for="property in contextEntries" :key="property.extId ?? property.propertyId">
              <button type="button" class="context-link" @click="openProperty(property)">
                <strong>{{ property.name ?? 'Property' }}</strong>
                <span v-if="property.area || property.city" class="context-location">
                  {{ [property.area, property.city].filter(Boolean).join(', ') }}
                </span>
              </button>
              <small v-if="property.priceRange" class="context-info">Pricing: {{ property.priceRange }}</small>
              <small v-if="property.unitsRange" class="context-info">Units: {{ property.unitsRange }}</small>
              <ul v-if="property.keyPoints?.length" class="context-points">
                <li v-for="(bullet, bulletIndex) in property.keyPoints.slice(0, 3)" :key="bulletIndex">{{ bullet }}</li>
              </ul>
            </li>
          </ul>
        </div>

        <form class="chat-form" @submit.prevent="sendMessage">
          <textarea
            ref="inputRef"
            v-model="draft"
            class="chat-input"
            name="chat-question"
            rows="2"
            placeholder="e.g. Show me 2BR units in Dubai under 2M AED"
            :disabled="loading"
            @keydown.enter.exact.prevent="sendMessage"
          ></textarea>
          <div class="chat-actions">
            <span v-if="error" class="chat-error">{{ error }}</span>
            <button class="send-btn" type="submit" :disabled="loading || !readyToSend">Send</button>
          </div>
        </form>
      </section>
    </transition>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue';
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

interface BackendChatResponse {
  answer: string;
  context: BackendPropertyContext[];
}

const router = useRouter();
const propertyStore = usePropertyCatalogStore();
const agentSiteStore = useAgentSiteStore();

const isOpen = ref(false);
const loading = ref(false);
const error = ref('');
const draft = ref('');
const messageCounter = ref(1);
const messages = ref<ChatMessage[]>([
  {
    id: 0,
    role: 'assistant',
    content: 'Hi there! I can help you explore developments, availability, payment plans, and nearby amenities.',
  },
]);
const contextEntries = ref<BackendPropertyContext[]>([]);

const scrollContainer = ref<HTMLDivElement | null>(null);
const inputRef = ref<HTMLTextAreaElement | null>(null);
const siteSlug = computed(() => agentSiteStore.site?.slug ?? '');

const readyToSend = computed(() => draft.value.trim().length > 0);

const normalizeContent = (value: string): string[] =>
  value
    .split(/\n+/)
    .map(line => line.trim())
    .filter(Boolean);

const togglePanel = () => {
  isOpen.value = !isOpen.value;
};

const focusInput = () => {
  nextTick(() => {
    if (inputRef.value) {
      inputRef.value.focus();
    }
  });
};

watch(isOpen, value => {
  if (value) {
    focusInput();
    nextTick(scrollToBottom);
  }
});

const registerEscapeHandler = (event: KeyboardEvent) => {
  if (event.key === 'Escape') {
    isOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('keydown', registerEscapeHandler);
});

onBeforeUnmount(() => {
  document.removeEventListener('keydown', registerEscapeHandler);
});

const scrollToBottom = () => {
  nextTick(() => {
    const element = scrollContainer.value;
    if (element) {
      element.scrollTop = element.scrollHeight;
    }
  });
};

const appendMessage = (message: Omit<ChatMessage, 'id'>) => {
  messages.value.push({ ...message, id: messageCounter.value++ });
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
    const { data } = await axios.post<BackendChatResponse>('api/chat', { message: content });

    appendMessage({ role: 'assistant', content: data?.answer ?? "I'm sorry, I couldn't find that information." });
    contextEntries.value = Array.isArray(data?.context) ? data.context : [];
  } catch (e: unknown) {
    error.value = 'Unable to reach the assistant. Please try again.';
    appendMessage({
      role: 'assistant',
      content: "I couldn't retrieve that information right now. Please try again in a moment.",
    });
    console.error('AI chat failed', e);
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
    isOpen.value = false;
  }
};
</script>

<style scoped>
.ai-chat-widget {
  position: fixed;
  bottom: 1.5rem;
  right: 1.5rem;
  z-index: 1050;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.ai-chat-toggle {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.25rem;
  border-radius: 999px;
  border: none;
  background: var(--primary-color, #3182ce);
  color: #fff;
  font-weight: 600;
  box-shadow: 0 12px 32px rgba(49, 130, 206, 0.35);
  cursor: pointer;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.ai-chat-toggle:hover {
  transform: translateY(-2px);
  box-shadow: 0 18px 36px rgba(49, 130, 206, 0.45);
}

.toggle-icon {
  font-size: 1.25rem;
  line-height: 1;
}

.ai-chat-panel {
  margin-top: 1rem;
  width: min(360px, calc(100vw - 2rem));
  max-height: calc(100vh - 5rem);
  background: #ffffff;
  border-radius: 1rem;
  box-shadow: 0 16px 48px rgba(15, 23, 42, 0.25);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  padding: 1rem 1.25rem;
  background: linear-gradient(135deg, var(--primary-color, #3182ce), var(--secondary-color, #4fd1c5));
  color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 0.5rem;
}

.chat-header h2 {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 700;
}

.chat-subtitle {
  margin: 0.2rem 0 0;
  font-size: 0.85rem;
  opacity: 0.85;
}

.close-btn {
  border: none;
  background: transparent;
  color: inherit;
  font-size: 1.5rem;
  line-height: 1;
  cursor: pointer;
  padding: 0;
}

.chat-body {
  padding: 1rem;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.chat-message {
  display: flex;
}

.chat-message.user {
  justify-content: flex-end;
}

.chat-message.assistant {
  justify-content: flex-start;
}

.message-bubble {
  max-width: 85%;
  padding: 0.75rem 1rem;
  border-radius: 1rem;
  font-size: 0.95rem;
  line-height: 1.5;
  background: #eff6ff;
  color: #1e293b;
}

.chat-message.user .message-bubble {
  background: var(--primary-color, #3182ce);
  color: #ffffff;
}

.message-bubble.loading {
  display: inline-flex;
  gap: 0.3rem;
  padding: 0.75rem;
  background: rgba(15, 23, 42, 0.05);
}

.dot {
  width: 0.55rem;
  height: 0.55rem;
  border-radius: 50%;
  background: rgba(15, 23, 42, 0.5);
  animation: pulse 1.2s infinite ease-in-out;
}

.dot:nth-child(2) {
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes pulse {
  0%,
  80%,
  100% {
    transform: scale(0.75);
    opacity: 0.6;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.chat-context {
  padding: 0.75rem 1rem;
  border-top: 1px solid rgba(15, 23, 42, 0.08);
  background: rgba(15, 23, 42, 0.02);
}

.chat-context h3 {
  margin: 0 0 0.5rem;
  font-size: 0.9rem;
  font-weight: 600;
  color: #334155;
}

.chat-context ul {
  margin: 0;
  padding: 0;
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.context-link {
  display: flex;
  flex-direction: column;
  border: none;
  background: transparent;
  text-align: left;
  padding: 0;
  font-size: 0.95rem;
  color: var(--primary-color, #3182ce);
  cursor: pointer;
}

.context-location {
  font-size: 0.8rem;
  color: #64748b;
}

.context-info {
  display: block;
  font-size: 0.75rem;
  color: #475569;
  margin-top: 0.25rem;
}

.context-points {
  margin: 0.35rem 0 0;
  padding-left: 1rem;
  color: #475569;
  font-size: 0.8rem;
}

.chat-form {
  padding: 0.75rem 1rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.chat-input {
  width: 100%;
  border: 1px solid rgba(148, 163, 184, 0.6);
  border-radius: 0.75rem;
  padding: 0.75rem 1rem;
  font-size: 0.95rem;
  resize: none;
  outline: none;
  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.chat-input:focus {
  border-color: var(--primary-color, #3182ce);
  box-shadow: 0 0 0 3px rgba(49, 130, 206, 0.15);
}

.chat-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-error {
  color: #dc2626;
  font-size: 0.8rem;
}

.send-btn {
  border: none;
  border-radius: 999px;
  padding: 0.55rem 1.4rem;
  font-weight: 600;
  background: var(--primary-color, #3182ce);
  color: #fff;
  cursor: pointer;
  transition:
    background 0.2s ease,
    transform 0.2s ease;
}

.send-btn:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.send-btn:not(:disabled):hover {
  transform: translateY(-1px);
  background: var(--primary-color-dark, #2c5aa0);
}

.chat-fade-enter-active,
.chat-fade-leave-active {
  transition:
    opacity 0.2s ease,
    transform 0.2s ease;
}

.chat-fade-enter-from,
.chat-fade-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

@media (max-width: 640px) {
  .ai-chat-widget {
    right: 1rem;
    bottom: 1rem;
  }

  .ai-chat-panel {
    width: min(320px, calc(100vw - 1.5rem));
  }
}
</style>

<template>
  <div class="ai-chat-widget">
    <button class="ai-chat-toggle" type="button" @click="togglePanel" aria-label="Open property assistant">
      <span class="toggle-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" focusable="false" role="presentation">
          <path
            d="M12 2a1 1 0 0 1 1 1v1.06A7.01 7.01 0 0 1 19 11v3a4 4 0 0 1-4 4h-6a4 4 0 0 1-4-4v-3a7.01 7.01 0 0 1 6-6.94V3a1 1 0 0 1 1-1z"
            fill="currentColor"
            opacity="0.16"
          />
          <rect x="7" y="9.5" width="10" height="5" rx="2.5" fill="none" stroke="currentColor" stroke-width="1.5" />
          <circle cx="10" cy="12" r="1" fill="currentColor" />
          <circle cx="14" cy="12" r="1" fill="currentColor" />
          <path d="M9 16.5a3 3 0 0 0 6 0" stroke="currentColor" stroke-linecap="round" stroke-width="1.5" fill="none" />
        </svg>
      </span>
      <span class="toggle-label">Ask AI</span>
    </button>

    <transition name="chat-fade">
      <section v-if="isOpen" class="ai-chat-panel" role="dialog" aria-modal="true">
        <header class="chat-header">
          <div class="bot-branding">
            <span class="bot-avatar" aria-hidden="true">
              <svg viewBox="0 0 32 32" focusable="false" role="presentation">
                <rect x="6" y="10" width="20" height="14" rx="7" fill="none" stroke="currentColor" stroke-width="2" />
                <circle cx="13" cy="17" r="2" fill="currentColor" />
                <circle cx="19" cy="17" r="2" fill="currentColor" />
                <path d="M12 22h8" stroke="currentColor" stroke-linecap="round" stroke-width="2" />
                <path d="M16 6v3" stroke="currentColor" stroke-linecap="round" stroke-width="2" />
                <path d="M10 6h12" stroke="currentColor" stroke-linecap="round" stroke-width="2" />
              </svg>
            </span>
            <div class="bot-copy">
              <span class="bot-title">Property bot</span>
              <p class="chat-subtitle">Ask about developments, payment plans, or availability.</p>
            </div>
          </div>
          <div class="header-actions">
            <button class="ghost-btn" type="button" @click="resetConversation" :disabled="!canResetConversation">Clear</button>
            <button class="contact-btn" type="button" @click="startContactFlow">Contact me</button>
            <button class="close-btn" type="button" @click="togglePanel" aria-label="Close AI assistant">&times;</button>
          </div>
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

        <div v-if="showSuggestions" class="chat-suggestions">
          <p class="suggestions-label">Try asking:</p>
          <div class="suggestions-list">
            <button v-for="prompt in suggestionPrompts" :key="prompt" class="suggestion-chip" type="button" @click="useSuggestion(prompt)">
              {{ prompt }}
            </button>
          </div>
        </div>

        <transition name="chat-fade">
          <div v-if="showLeadForm" class="chat-lead-card">
            <h3>Share your details</h3>
            <p class="lead-subtitle">I'll connect you with an agent right away.</p>
            <form ref="leadFormRef" class="lead-form" @submit.prevent="submitLeadDetails">
              <label class="lead-label" for="leadName">Name</label>
              <input
                id="leadName"
                v-model.trim="leadForm.name"
                type="text"
                name="lead-name"
                autocomplete="name"
                required
                :disabled="submittingLead"
                placeholder="Your full name"
              />

              <label class="lead-label" for="leadEmail">Email</label>
              <input
                id="leadEmail"
                v-model.trim="leadForm.email"
                type="email"
                name="lead-email"
                autocomplete="email"
                :disabled="submittingLead"
                placeholder="you@example.com"
              />

              <label class="lead-label" for="leadPhone">Phone</label>
              <input
                id="leadPhone"
                v-model.trim="leadForm.phone"
                type="tel"
                name="lead-phone"
                autocomplete="tel"
                :disabled="submittingLead"
                placeholder="+971 50 123 4567"
              />

              <label class="lead-label" for="leadMessage">Notes</label>
              <textarea
                id="leadMessage"
                v-model.trim="leadForm.message"
                name="lead-message"
                rows="2"
                :disabled="submittingLead"
                placeholder="Optional — tell us what you're looking for"
              ></textarea>

              <p v-if="leadError" class="chat-error">{{ leadError }}</p>
              <p v-else class="lead-hint">Name and at least one contact detail are required.</p>

              <div class="lead-form-actions">
                <button class="ghost-btn" type="button" @click="cancelLeadFlow" :disabled="submittingLead">Cancel</button>
                <button class="contact-submit" type="submit" :disabled="!leadFormValid || submittingLead">
                  <span v-if="submittingLead">Sending...</span>
                  <span v-else>Send</span>
                </button>
              </div>
            </form>
          </div>
        </transition>

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
import { computed, nextTick, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';
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

interface BackendConversationHistoryMessage {
  role: 'assistant' | 'user';
  content: string;
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
  propertyType?: string | null;
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

const defaultAssistantGreeting = 'Hi there! I can help you explore developments, availability, payment plans, and nearby amenities.';
const maxBackendHistoryMessages = 6;
const suggestionPrompts = [
  'Show me waterfront properties launching soon.',
  'Which communities have 2BR units under 2M AED?',
  'List family-friendly villas with payment plans.',
  'What are the newest off-plan projects in Dubai Marina?',
] as const;

const isOpen = ref(false);
const loading = ref(false);
const error = ref('');
const draft = ref('');
const messageCounter = ref(1);
const messages = ref<ChatMessage[]>([
  {
    id: 0,
    role: 'assistant',
    content: defaultAssistantGreeting,
  },
]);
const contextEntries = ref<BackendPropertyContext[]>([]);
const agentState = ref<AgentStatePayload | undefined>(undefined);

const scrollContainer = ref<HTMLDivElement | null>(null);
const leadFormRef = ref<HTMLFormElement | null>(null);
const inputRef = ref<HTMLTextAreaElement | null>(null);
const siteSlug = computed(() => agentSiteStore.site?.slug ?? '');

const readyToSend = computed(() => draft.value.trim().length > 0);
const hasUserMessages = computed(() => messages.value.some(message => message.role === 'user'));
const showSuggestions = computed(() => !hasUserMessages.value && !showLeadForm.value);
const canResetConversation = computed(() => hasUserMessages.value || contextEntries.value.length > 0);

const leadForm = reactive({
  name: '',
  email: '',
  phone: '',
  message: '',
});
const showLeadForm = ref(false);
const leadError = ref('');
const submittingLead = ref(false);
const leadFormValid = computed(() => {
  const nameValid = leadForm.name.trim().length > 0;
  const hasContact = leadForm.email.trim().length > 0 || leadForm.phone.trim().length > 0;
  return nameValid && hasContact;
});
const contactPrompted = ref(false);

const useSuggestion = (prompt: string) => {
  if (loading.value) {
    return;
  }
  draft.value = prompt;
  sendMessage();
};

const normalizeContent = (value: string): string[] =>
  value
    .split(/\n+/)
    .map(line => line.trim())
    .filter(Boolean);

const buildConversationHistoryPayload = (): BackendConversationHistoryMessage[] =>
  messages.value
    .filter(message => message.id !== 0)
    .filter(message => message.role === 'assistant' || message.role === 'user')
    .map(message => ({
      role: message.role,
      content: message.content.trim(),
    }))
    .filter(message => message.content.length > 0)
    .slice(-maxBackendHistoryMessages);

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
  } else {
    cancelLeadFlow();
  }
});

watch(
  () => messages.value.length,
  () => {
    scrollToBottom();
  },
);

watch(showLeadForm, value => {
  if (value) {
    scrollToBottom();
    nextTick(() => {
      const element = leadFormRef.value?.querySelector<HTMLInputElement>('input');
      element?.focus();
    });
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

const resetLeadForm = () => {
  leadForm.name = '';
  leadForm.email = '';
  leadForm.phone = '';
  leadForm.message = '';
  leadError.value = '';
  submittingLead.value = false;
};

const resetConversation = () => {
  messages.value = [
    {
      id: 0,
      role: 'assistant',
      content: defaultAssistantGreeting,
    },
  ];
  messageCounter.value = 1;
  contextEntries.value = [];
  agentState.value = undefined;
  error.value = '';
  draft.value = '';
  cancelLeadFlow();
  focusInput();
};

const startContactFlow = () => {
  if (loading.value) {
    return;
  }
  showLeadForm.value = true;
  leadError.value = '';

  if (!contactPrompted.value) {
    appendMessage({
      role: 'assistant',
      content: 'Happy to help! Share your name and best contact info, and an advisor will reach out shortly.',
    });
    contactPrompted.value = true;
  }
};

const cancelLeadFlow = () => {
  if (showLeadForm.value) {
    showLeadForm.value = false;
    contactPrompted.value = false;
  }
  resetLeadForm();
};

const submitLeadDetails = async () => {
  if (!leadFormValid.value || submittingLead.value) {
    return;
  }

  submittingLead.value = true;
  leadError.value = '';

  try {
    await axios.post('api/contact-leads', {
      name: leadForm.name.trim(),
      email: leadForm.email.trim() || null,
      phone: leadForm.phone.trim() || null,
      message: leadForm.message.trim() || 'Chat widget enquiry',
      source: 'CHAT_WIDGET',
      site: agentSiteStore.site?.id ? { id: agentSiteStore.site.id } : null,
    });

    appendMessage({
      role: 'assistant',
      content: "Thanks! I've shared your details with the team — they'll reach out shortly.",
    });

    cancelLeadFlow();
  } catch (e) {
    leadError.value = 'Unable to save your details. Please try again.';
    console.error('Contact lead submission failed', e);
  } finally {
    submittingLead.value = false;
    focusInput();
  }
};

const sendMessage = async () => {
  if (!readyToSend.value || loading.value) {
    return;
  }
  const content = draft.value.trim();
  const conversationHistory = buildConversationHistoryPayload();
  draft.value = '';
  error.value = '';

  appendMessage({ role: 'user', content });

  try {
    loading.value = true;
    const { data } = await axios.post<BackendChatResponse>('api/chat', {
      message: content,
      agentState: agentState.value,
      conversationHistory,
    });

    appendMessage({ role: 'assistant', content: data?.answer ?? "I'm sorry, I couldn't find that information." });
    contextEntries.value = Array.isArray(data?.context) ? data.context : [];
    agentState.value = data?.agentState ?? agentState.value;
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
  width: 1.75rem;
  height: 1.75rem;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.toggle-icon svg {
  width: 100%;
  height: 100%;
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
  align-items: center;
  gap: 0.75rem;
}

.bot-branding {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.bot-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 2.75rem;
  height: 2.75rem;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.bot-avatar svg {
  width: 1.75rem;
  height: 1.75rem;
}

.bot-copy {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
}

.bot-title {
  font-size: 1rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.chat-subtitle {
  margin: 0;
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
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

.ghost-btn,
.contact-btn,
.contact-submit {
  border-radius: 999px;
  font-weight: 600;
  padding: 0.5rem 1.2rem;
  cursor: pointer;
  border: none;
  transition:
    transform 0.2s ease,
    opacity 0.2s ease,
    background 0.2s ease;
}

.ghost-btn {
  background: rgba(255, 255, 255, 0.15);
  color: inherit;
  border: 1px solid rgba(255, 255, 255, 0.35);
}

.ghost-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.ghost-btn:not(:disabled):hover {
  transform: translateY(-1px);
  background: rgba(255, 255, 255, 0.25);
}

.contact-btn {
  background: #ffffff;
  color: var(--primary-color, #3182ce);
}

.contact-btn:hover {
  transform: translateY(-1px);
}

.chat-suggestions {
  padding: 0 1rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.suggestions-label {
  margin: 0;
  font-size: 0.8rem;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #475569;
}

.suggestions-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.suggestion-chip {
  border-radius: 999px;
  border: 1px solid rgba(148, 163, 184, 0.6);
  padding: 0.35rem 0.9rem;
  background: #ffffff;
  font-size: 0.85rem;
  color: #1e293b;
  cursor: pointer;
  transition:
    border-color 0.2s ease,
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.suggestion-chip:hover {
  transform: translateY(-1px);
  border-color: var(--primary-color, #3182ce);
  box-shadow: 0 6px 16px rgba(49, 130, 206, 0.15);
}

.chat-lead-card {
  margin: 0 1rem 1rem;
  padding: 1rem;
  border: 1px solid rgba(148, 163, 184, 0.3);
  border-radius: 1rem;
  background: rgba(248, 250, 252, 0.85);
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.chat-lead-card h3 {
  margin: 0;
  font-size: 1rem;
  color: #1e293b;
}

.lead-subtitle {
  margin: 0;
  font-size: 0.85rem;
  color: #475569;
}

.lead-form {
  display: grid;
  gap: 0.5rem;
}

.lead-label {
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: #475569;
}

.lead-form input,
.lead-form textarea {
  border-radius: 0.75rem;
  border: 1px solid rgba(148, 163, 184, 0.6);
  padding: 0.6rem 0.75rem;
  font-size: 0.9rem;
  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.lead-form input:focus,
.lead-form textarea:focus {
  outline: none;
  border-color: var(--primary-color, #3182ce);
  box-shadow: 0 0 0 3px rgba(49, 130, 206, 0.15);
}

.lead-hint {
  margin: 0;
  font-size: 0.75rem;
  color: #64748b;
}

.lead-form-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.25rem;
}

.contact-submit {
  background: var(--primary-color, #3182ce);
  color: #ffffff;
  border: none;
}

.contact-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.contact-submit:not(:disabled):hover {
  transform: translateY(-1px);
  background: var(--primary-color-dark, #2c5aa0);
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

  .bot-branding {
    align-items: flex-start;
  }

  .header-actions {
    gap: 0.35rem;
  }

  .ghost-btn,
  .contact-btn {
    padding: 0.45rem 0.9rem;
    font-size: 0.8rem;
  }
}
</style>

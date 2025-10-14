// Errors
export const PROBLEM_BASE_URL = 'https://www.jhipster.tech/problem';
export const EMAIL_ALREADY_USED_TYPE = `${PROBLEM_BASE_URL}/email-already-used`;
export const LOGIN_ALREADY_USED_TYPE = `${PROBLEM_BASE_URL}/login-already-used`;

// External services
export const DEFAULT_SUPABASE_BUCKET = import.meta.env.VITE_SUPABASE_STORAGE_BUCKET ?? 'agent-assets';
export const MARKETING_HERO_IMAGE = import.meta.env.VITE_MARKETING_HERO_IMAGE ?? '/content/images/uae-skyline.svg';
export const MAP_TILE_URL = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
export const MAP_ATTRIBUTION =
  '&copy; <a href="https://www.openstreetmap.org/copyright" target="_blank" rel="noopener">OpenStreetMap</a> contributors';

import { createClient, type SupabaseClient } from '@supabase/supabase-js';

let supabaseClient: SupabaseClient | null = null;

const createSupabase = (): SupabaseClient => {
  const url = import.meta.env.VITE_SUPABASE_URL;
  const anonKey = import.meta.env.VITE_SUPABASE_ANON_KEY;

  if (!url || !anonKey) {
    throw new Error('Supabase credentials are not defined. Add VITE_SUPABASE_URL and VITE_SUPABASE_ANON_KEY to your environment');
  }

  return createClient(url, anonKey, {
    auth: {
      persistSession: true,
      storageKey: 'uae-real-state-hub-supabase',
    },
  });
};

export const getSupabaseClient = (): SupabaseClient => {
  if (!supabaseClient) {
    supabaseClient = createSupabase();
  }

  return supabaseClient;
};

export const uploadPublicAsset = async (
  bucket: string,
  path: string,
  file: File | Blob,
  options: { upsert?: boolean; contentType?: string } = {},
): Promise<{ path: string; publicUrl: string }> => {
  const client = getSupabaseClient();
  const { data, error } = await client.storage.from(bucket).upload(path, file, {
    ...options,
    upsert: options.upsert ?? true,
  });

  if (error || !data?.path) {
    throw error ?? new Error('Failed to upload file to Supabase');
  }

  const {
    data: { publicUrl },
  } = client.storage.from(bucket).getPublicUrl(data.path);

  return { path: data.path, publicUrl };
};

export const removePublicAsset = async (bucket: string, path: string): Promise<void> => {
  const client = getSupabaseClient();
  const { error } = await client.storage.from(bucket).remove([path]);

  if (error) {
    throw error;
  }
};

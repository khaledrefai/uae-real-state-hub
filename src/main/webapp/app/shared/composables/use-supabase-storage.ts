import { ref } from 'vue';

import { removePublicAsset, uploadPublicAsset } from '@/shared/services/supabase-client';

export interface UploadOptions {
  contentType?: string;
  upsert?: boolean;
}

export const useSupabaseStorage = (bucketOverride?: string) => {
  const uploading = ref(false);
  const lastUploadedUrl = ref<string | null>(null);
  const bucket = bucketOverride || import.meta.env.VITE_SUPABASE_STORAGE_BUCKET || 'agent-assets';

  const upload = async (path: string, file: File | Blob, options: UploadOptions = {}) => {
    uploading.value = true;
    try {
      const { publicUrl } = await uploadPublicAsset(bucket, path, file, options);
      lastUploadedUrl.value = publicUrl;
      return publicUrl;
    } finally {
      uploading.value = false;
    }
  };

  const remove = async (path: string) => removePublicAsset(bucket, path);

  return {
    uploading,
    lastUploadedUrl,
    upload,
    remove,
  };
};

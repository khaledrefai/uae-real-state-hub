import { type IProperty } from '@/shared/model/property.model';

import { type MediaKind } from '@/shared/model/enumerations/media-kind.model';
export interface IMediaAsset {
  id?: number;
  kind?: keyof typeof MediaKind;
  url?: string;
  mimeType?: string | null;
  width?: number | null;
  height?: number | null;
  mediaSize?: number | null;
  source?: string | null;
  title?: string | null;
  altText?: string | null;
  sortOrder?: number | null;
  property?: IProperty | null;
}

export class MediaAsset implements IMediaAsset {
  constructor(
    public id?: number,
    public kind?: keyof typeof MediaKind,
    public url?: string,
    public mimeType?: string | null,
    public width?: number | null,
    public height?: number | null,
    public mediaSize?: number | null,
    public source?: string | null,
    public title?: string | null,
    public altText?: string | null,
    public sortOrder?: number | null,
    public property?: IProperty | null,
  ) {}
}

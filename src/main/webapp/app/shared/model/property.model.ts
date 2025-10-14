import { type IPropertyMarker } from '@/shared/model/property-marker.model';

import { type ListingType } from '@/shared/model/enumerations/listing-type.model';
import { type PropertyStatus } from '@/shared/model/enumerations/property-status.model';
export interface IProperty {
  id?: number;
  extId?: number;
  name?: string;
  developer?: string | null;
  area?: string | null;
  city?: string | null;
  country?: string | null;
  listingType?: keyof typeof ListingType | null;
  status?: keyof typeof PropertyStatus | null;
  saleStatus?: string | null;
  readiness?: string | null;
  serviceCharge?: string | null;
  furnishing?: string | null;
  hasEscrow?: boolean | null;
  postHandover?: boolean | null;
  completionDateTime?: Date | null;
  minPrice?: number | null;
  maxPrice?: number | null;
  minPriceAed?: number | null;
  priceCurrency?: string | null;
  minArea?: number | null;
  maxArea?: number | null;
  areaUnit?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  coverUrl?: string | null;
  coverJson?: string | null;
  videoUrl?: string | null;
  brochureUrl?: string | null;
  layoutsPdfUrl?: string | null;
  website?: string | null;
  overviewMd?: string | null;
  raw?: string | null;
  publishedAt?: Date | null;
  updatedAt?: Date | null;
  marker?: IPropertyMarker | null;
}

export class Property implements IProperty {
  constructor(
    public id?: number,
    public extId?: number,
    public name?: string,
    public developer?: string | null,
    public area?: string | null,
    public city?: string | null,
    public country?: string | null,
    public listingType?: keyof typeof ListingType | null,
    public status?: keyof typeof PropertyStatus | null,
    public saleStatus?: string | null,
    public readiness?: string | null,
    public serviceCharge?: string | null,
    public furnishing?: string | null,
    public hasEscrow?: boolean | null,
    public postHandover?: boolean | null,
    public completionDateTime?: Date | null,
    public minPrice?: number | null,
    public maxPrice?: number | null,
    public minPriceAed?: number | null,
    public priceCurrency?: string | null,
    public minArea?: number | null,
    public maxArea?: number | null,
    public areaUnit?: string | null,
    public latitude?: number | null,
    public longitude?: number | null,
    public coverUrl?: string | null,
    public coverJson?: string | null,
    public videoUrl?: string | null,
    public brochureUrl?: string | null,
    public layoutsPdfUrl?: string | null,
    public website?: string | null,
    public overviewMd?: string | null,
    public raw?: string | null,
    public publishedAt?: Date | null,
    public updatedAt?: Date | null,
    public marker?: IPropertyMarker | null,
  ) {
    this.hasEscrow = this.hasEscrow ?? false;
    this.postHandover = this.postHandover ?? false;
  }
}

export interface IPropertyMarker {
  id?: number;
  extId?: number;
  area?: string | null;
  completionDate?: Date | null;
  latitude?: number;
  longitude?: number;
  name?: string;
  propertyId?: number;
  developer?: string | null;
  status?: string | null;
  saleStatus?: string | null;
  listingType?: string | null;
  minPrice?: number | null;
  priceCurrency?: string | null;
  coverUrl?: string | null;
  coverJson?: string | null;
  city?: string | null;
  country?: string | null;
  summary?: string | null;
}

export class PropertyMarker implements IPropertyMarker {
  constructor(
    public id?: number,
    public extId?: number,
    public area?: string | null,
    public completionDate?: Date | null,
    public latitude?: number,
    public longitude?: number,
    public name?: string,
    public propertyId?: number,
    public developer?: string | null,
    public status?: string | null,
    public saleStatus?: string | null,
    public listingType?: string | null,
    public minPrice?: number | null,
    public priceCurrency?: string | null,
    public coverUrl?: string | null,
    public coverJson?: string | null,
    public city?: string | null,
    public country?: string | null,
    public summary?: string | null,
  ) {}
}

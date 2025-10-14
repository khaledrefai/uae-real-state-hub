import { type IProperty } from '@/shared/model/property.model';

export interface IUnitBlock {
  id?: number;
  normalizedType?: string | null;
  unitType?: string | null;
  bedroomsAmount?: string | null;
  unitBedrooms?: string | null;
  areaUnit?: string | null;
  unitsAmount?: number | null;
  unitsAreaFrom?: number | null;
  unitsAreaTo?: number | null;
  unitsAreaFromM2?: string | null;
  unitsAreaToM2?: string | null;
  unitsPriceFrom?: number | null;
  unitsPriceTo?: number | null;
  priceCurrency?: string | null;
  typicalImageUrl?: string | null;
  property?: IProperty | null;
}

export class UnitBlock implements IUnitBlock {
  constructor(
    public id?: number,
    public normalizedType?: string | null,
    public unitType?: string | null,
    public bedroomsAmount?: string | null,
    public unitBedrooms?: string | null,
    public areaUnit?: string | null,
    public unitsAmount?: number | null,
    public unitsAreaFrom?: number | null,
    public unitsAreaTo?: number | null,
    public unitsAreaFromM2?: string | null,
    public unitsAreaToM2?: string | null,
    public unitsPriceFrom?: number | null,
    public unitsPriceTo?: number | null,
    public priceCurrency?: string | null,
    public typicalImageUrl?: string | null,
    public property?: IProperty | null,
  ) {}
}

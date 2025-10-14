import { type IProperty } from '@/shared/model/property.model';

export interface IUnitAvailability {
  id?: number;
  buildingName?: string;
  areaFrom?: number | null;
  areaUnit?: string | null;
  bedroomType?: string | null;
  lastUpdated?: Date | null;
  priceCurrency?: string | null;
  priceFrom?: number | null;
  priceTo?: number | null;
  unitsAvailable?: number | null;
  property?: IProperty | null;
}

export class UnitAvailability implements IUnitAvailability {
  constructor(
    public id?: number,
    public buildingName?: string,
    public areaFrom?: number | null,
    public areaUnit?: string | null,
    public bedroomType?: string | null,
    public lastUpdated?: Date | null,
    public priceCurrency?: string | null,
    public priceFrom?: number | null,
    public priceTo?: number | null,
    public unitsAvailable?: number | null,
    public property?: IProperty | null,
  ) {}
}

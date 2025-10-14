import { type IProperty } from '@/shared/model/property.model';

export interface IFacility {
  id?: number;
  name?: string;
  source?: string | null;
  imageUrl?: string | null;
  property?: IProperty | null;
}

export class Facility implements IFacility {
  constructor(
    public id?: number,
    public name?: string,
    public source?: string | null,
    public imageUrl?: string | null,
    public property?: IProperty | null,
  ) {}
}

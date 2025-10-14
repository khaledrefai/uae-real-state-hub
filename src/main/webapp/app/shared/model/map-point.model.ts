import { type IProperty } from '@/shared/model/property.model';

export interface IMapPoint {
  id?: number;
  name?: string;
  distanceKm?: number | null;
  property?: IProperty | null;
}

export class MapPoint implements IMapPoint {
  constructor(
    public id?: number,
    public name?: string,
    public distanceKm?: number | null,
    public property?: IProperty | null,
  ) {}
}

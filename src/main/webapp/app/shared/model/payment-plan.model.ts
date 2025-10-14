import { type IProperty } from '@/shared/model/property.model';

export interface IPaymentPlan {
  id?: number;
  name?: string;
  monthsAfterHandover?: number;
  property?: IProperty | null;
}

export class PaymentPlan implements IPaymentPlan {
  constructor(
    public id?: number,
    public name?: string,
    public monthsAfterHandover?: number,
    public property?: IProperty | null,
  ) {}
}

import { type IPaymentPlan } from '@/shared/model/payment-plan.model';

export interface IPaymentPlanItem {
  id?: number;
  orderNo?: number;
  paymentTime?: string;
  percentOfPayment?: string;
  plan?: IPaymentPlan | null;
}

export class PaymentPlanItem implements IPaymentPlanItem {
  constructor(
    public id?: number,
    public orderNo?: number,
    public paymentTime?: string,
    public percentOfPayment?: string,
    public plan?: IPaymentPlan | null,
  ) {}
}

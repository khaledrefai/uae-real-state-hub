import { type Currency } from '@/shared/model/enumerations/currency.model';
export interface ISubscriptionPlan {
  id?: number;
  code?: string;
  name?: string;
  priceMonthly?: number;
  priceYearly?: number | null;
  currency?: keyof typeof Currency;
  stripePriceMonthlyId?: string | null;
  stripePriceYearlyId?: string | null;
  maxListings?: number;
  features?: string | null;
}

export class SubscriptionPlan implements ISubscriptionPlan {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public priceMonthly?: number,
    public priceYearly?: number | null,
    public currency?: keyof typeof Currency,
    public stripePriceMonthlyId?: string | null,
    public stripePriceYearlyId?: string | null,
    public maxListings?: number,
    public features?: string | null,
  ) {}
}

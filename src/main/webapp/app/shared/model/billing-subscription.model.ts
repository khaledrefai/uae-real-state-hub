import { type IAgentSite } from '@/shared/model/agent-site.model';

export interface IBillingSubscription {
  id?: number;
  status?: string;
  startDate?: Date;
  endDate?: Date | null;
  stripeCustomerId?: string | null;
  stripeSubscriptionId?: string | null;
  cancelAtPeriodEnd?: boolean | null;
  isActive?: boolean;
  site?: IAgentSite | null;
}

export class BillingSubscription implements IBillingSubscription {
  constructor(
    public id?: number,
    public status?: string,
    public startDate?: Date,
    public endDate?: Date | null,
    public stripeCustomerId?: string | null,
    public stripeSubscriptionId?: string | null,
    public cancelAtPeriodEnd?: boolean | null,
    public isActive?: boolean,
    public site?: IAgentSite | null,
  ) {
    this.cancelAtPeriodEnd = this.cancelAtPeriodEnd ?? false;
    this.isActive = this.isActive ?? false;
  }
}

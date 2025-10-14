import { type IAgentProfile } from '@/shared/model/agent-profile.model';
import { type ISubscriptionPlan } from '@/shared/model/subscription-plan.model';

export interface IAgentSite {
  id?: number;
  slug?: string;
  displayName?: string;
  theme?: string;
  primaryColor?: string | null;
  secondaryColor?: string | null;
  logoUrl?: string | null;
  contactEmail?: string;
  contactPhone?: string | null;
  contactWhatsapp?: string | null;
  domain?: string | null;
  isActive?: boolean;
  owner?: IAgentProfile | null;
  plan?: ISubscriptionPlan | null;
}

export class AgentSite implements IAgentSite {
  constructor(
    public id?: number,
    public slug?: string,
    public displayName?: string,
    public theme?: string,
    public primaryColor?: string | null,
    public secondaryColor?: string | null,
    public logoUrl?: string | null,
    public contactEmail?: string,
    public contactPhone?: string | null,
    public contactWhatsapp?: string | null,
    public domain?: string | null,
    public isActive?: boolean,
    public owner?: IAgentProfile | null,
    public plan?: ISubscriptionPlan | null,
  ) {
    this.isActive = this.isActive ?? false;
  }
}

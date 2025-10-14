import { type IAgentSite } from '@/shared/model/agent-site.model';
import { type IProperty } from '@/shared/model/property.model';

import { type LeadStatus } from '@/shared/model/enumerations/lead-status.model';
export interface IContactLead {
  id?: number;
  name?: string;
  email?: string | null;
  phone?: string | null;
  whatsapp?: string | null;
  message?: string | null;
  source?: string | null;
  utm?: string | null;
  createdAt?: Date;
  status?: keyof typeof LeadStatus;
  site?: IAgentSite | null;
  property?: IProperty | null;
}

export class ContactLead implements IContactLead {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string | null,
    public phone?: string | null,
    public whatsapp?: string | null,
    public message?: string | null,
    public source?: string | null,
    public utm?: string | null,
    public createdAt?: Date,
    public status?: keyof typeof LeadStatus,
    public site?: IAgentSite | null,
    public property?: IProperty | null,
  ) {}
}

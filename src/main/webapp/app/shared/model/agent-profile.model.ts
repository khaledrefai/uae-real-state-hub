export interface IAgentProfile {
  id?: number;
  externalUserId?: string;
  fullName?: string;
  companyName?: string;
  email?: string;
  phone?: string | null;
  whatsapp?: string | null;
  country?: string | null;
  city?: string | null;
  website?: string | null;
  active?: boolean;
}

export class AgentProfile implements IAgentProfile {
  constructor(
    public id?: number,
    public externalUserId?: string,
    public fullName?: string,
    public companyName?: string,
    public email?: string,
    public phone?: string | null,
    public whatsapp?: string | null,
    public country?: string | null,
    public city?: string | null,
    public website?: string | null,
    public active?: boolean,
  ) {
    this.active = this.active ?? false;
  }
}

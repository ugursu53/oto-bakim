export interface IParcaTipi {
  id?: number;
  ad?: string;
  kodu?: string;
  varsayilanFiyati?: number;
  aciklama?: string;
}

export class ParcaTipi implements IParcaTipi {
  constructor(public id?: number, public ad?: string, public kodu?: string, public varsayilanFiyati?: number, public aciklama?: string) {}
}

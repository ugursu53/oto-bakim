export interface IPersonel {
  id?: number;
  ad?: string;
  soyad?: string;
  gorevi?: string;
}

export class Personel implements IPersonel {
  constructor(public id?: number, public ad?: string, public soyad?: string, public gorevi?: string) {}
}

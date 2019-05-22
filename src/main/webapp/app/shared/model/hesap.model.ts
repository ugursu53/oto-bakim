export interface IHesap {
  id?: number;
  banka?: string;
  sube?: string;
  hesapNo?: string;
  iban?: string;
}

export class Hesap implements IHesap {
  constructor(public id?: number, public banka?: string, public sube?: string, public hesapNo?: string, public iban?: string) {}
}

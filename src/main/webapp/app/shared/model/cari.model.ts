import { IHesap } from 'app/shared/model/hesap.model';

export const enum CariTipi {
  MUSTERI = 'MUSTERI',
  TEDARIKCI = 'TEDARIKCI'
}

export const enum KisiTipi {
  GERCEK = 'GERCEK',
  TUZEL = 'TUZEL'
}

export const enum IsEmriTipi {
  SIGORTA = 'SIGORTA',
  BAKIM = 'BAKIM',
  HASAR = 'HASAR'
}

export interface ICari {
  id?: number;
  tipi?: CariTipi;
  kisiTipi?: KisiTipi;
  aktif?: boolean;
  ad?: string;
  adres?: string;
  telefon?: string;
  tcNo?: string;
  vergiNo?: string;
  yetkili?: string;
  fax?: string;
  eposta?: string;
  webAdresi?: string;
  iskonto?: number;
  efaturaKullanimi?: boolean;
  aciklama?: string;
  varsayilanIsEmriTipi?: IsEmriTipi;
  hesap?: IHesap;
}

export class Cari implements ICari {
  constructor(
    public id?: number,
    public tipi?: CariTipi,
    public kisiTipi?: KisiTipi,
    public aktif?: boolean,
    public ad?: string,
    public adres?: string,
    public telefon?: string,
    public tcNo?: string,
    public vergiNo?: string,
    public yetkili?: string,
    public fax?: string,
    public eposta?: string,
    public webAdresi?: string,
    public iskonto?: number,
    public efaturaKullanimi?: boolean,
    public aciklama?: string,
    public varsayilanIsEmriTipi?: IsEmriTipi,
    public hesap?: IHesap
  ) {
    this.aktif = this.aktif || false;
    this.efaturaKullanimi = this.efaturaKullanimi || false;
  }
}

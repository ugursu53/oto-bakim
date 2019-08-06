import { IModel } from 'app/shared/model/model.model';
import { ICari } from 'app/shared/model/cari.model';

export const enum YakitTuru {
  BENZIN = 'BENZIN',
  DIZEL = 'DIZEL',
  LPG = 'LPG',
  HYBRID = 'HYBRID',
  ELEKTRIK = 'ELEKTRIK'
}

export const enum VitesTuru {
  MANUEL = 'MANUEL',
  YARI_OTOMATIK = 'YARI_OTOMATIK',
  OTOMATIK = 'OTOMATIK'
}

export const enum KullanimSekli {
  HUSISI = 'HUSISI',
  SIRKET = 'SIRKET'
}

export const enum AracTipi {
  HATCHBACK = 'HATCHBACK',
  SEDAN = 'SEDAN',
  STAION_WAGON = 'STAION_WAGON'
}

export interface IArac {
  id?: number;
  plakaNo?: string;
  modelYili?: number;
  rengi?: string;
  yakitTuru?: YakitTuru;
  vitesTuru?: VitesTuru;
  motorNo?: string;
  sasiNo?: string;
  kullanimSekli?: KullanimSekli;
  aracTipi?: AracTipi;
  aciklama?: string;
  aktifCari?: ICari;
  model?: IModel;
}

export class Arac implements IArac {
  constructor(
    public id?: number,
    public plakaNo?: string,
    public modelYili?: number,
    public rengi?: string,
    public yakitTuru?: YakitTuru,
    public vitesTuru?: VitesTuru,
    public motorNo?: string,
    public sasiNo?: string,
    public kullanimSekli?: KullanimSekli,
    public aracTipi?: AracTipi,
    public aciklama?: string,
    public model?: IModel
  ) {}
}

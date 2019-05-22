import { IIsEmri } from 'app/shared/model/is-emri.model';
import { IIscilikTipi } from 'app/shared/model/iscilik-tipi.model';
import { IPersonel } from 'app/shared/model/personel.model';

export interface IIscilik {
  id?: number;
  aciklama?: string;
  fiyat?: number;
  iskonto?: number;
  isEmri?: IIsEmri;
  tipi?: IIscilikTipi;
  personel?: IPersonel;
}

export class Iscilik implements IIscilik {
  constructor(
    public id?: number,
    public aciklama?: string,
    public fiyat?: number,
    public iskonto?: number,
    public isEmri?: IIsEmri,
    public tipi?: IIscilikTipi,
    public personel?: IPersonel
  ) {}
}

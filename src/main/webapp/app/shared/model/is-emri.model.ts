import { Moment } from 'moment';
import { IIscilik } from 'app/shared/model/iscilik.model';
import { IParca } from 'app/shared/model/parca.model';
import { IArac } from 'app/shared/model/arac.model';

export const enum IsEmriTipi {
  SIGORTA = 'SIGORTA',
  BAKIM = 'BAKIM',
  HASAR = 'HASAR'
}

export interface IIsEmri {
  id?: number;
  gelisZamani?: Moment;
  aciklama?: string;
  kabulTarihi?: Moment;
  tipi?: IsEmriTipi;
  isciliklers?: IIscilik[];
  parcalars?: IParca[];
  arac?: IArac;
}

export class IsEmri implements IIsEmri {
  constructor(
    public id?: number,
    public gelisZamani?: Moment,
    public aciklama?: string,
    public kabulTarihi?: Moment,
    public tipi?: IsEmriTipi,
    public isciliklers?: IIscilik[],
    public parcalars?: IParca[],
    public arac?: IArac
  ) {}
}

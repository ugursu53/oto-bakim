import { IIsEmri } from 'app/shared/model/is-emri.model';
import { IParcaTipi } from 'app/shared/model/parca-tipi.model';

export interface IParca {
  id?: number;
  alisFiyati?: number;
  fiyati?: number;
  iskonto?: number;
  nihaiFiyat?: number;
  isEmri?: IIsEmri;
  tipi?: IParcaTipi;
}

export class Parca implements IParca {
  constructor(public id?: number, public alisFiyati?: number, public fiyati?: number, public isEmri?: IIsEmri, public tipi?: IParcaTipi) {}
}

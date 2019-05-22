import { IArac } from 'app/shared/model/arac.model';
import { ICari } from 'app/shared/model/cari.model';

export interface IAracCarisi {
  id?: number;
  aktif?: boolean;
  arac?: IArac;
  cari?: ICari;
}

export class AracCarisi implements IAracCarisi {
  constructor(public id?: number, public aktif?: boolean, public arac?: IArac, public cari?: ICari) {
    this.aktif = this.aktif || false;
  }
}

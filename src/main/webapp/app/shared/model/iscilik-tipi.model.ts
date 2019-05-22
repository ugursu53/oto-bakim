import { IIscilikGrubu } from 'app/shared/model/iscilik-grubu.model';

export interface IIscilikTipi {
  id?: number;
  ad?: string;
  varsayilanFiyat?: number;
  grubu?: IIscilikGrubu;
}

export class IscilikTipi implements IIscilikTipi {
  constructor(public id?: number, public ad?: string, public varsayilanFiyat?: number, public grubu?: IIscilikGrubu) {}
}

import { IModel } from 'app/shared/model/model.model';

export interface IMarka {
  id?: number;
  ad?: string;
  modellers?: IModel[];
}

export class Marka implements IMarka {
  constructor(public id?: number, public ad?: string, public modellers?: IModel[]) {}
}

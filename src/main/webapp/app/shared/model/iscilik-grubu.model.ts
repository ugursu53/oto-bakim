export interface IIscilikGrubu {
  id?: number;
  ad?: string;
}

export class IscilikGrubu implements IIscilikGrubu {
  constructor(public id?: number, public ad?: string) {}
}

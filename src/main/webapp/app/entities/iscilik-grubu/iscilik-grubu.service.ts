import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIscilikGrubu } from 'app/shared/model/iscilik-grubu.model';

type EntityResponseType = HttpResponse<IIscilikGrubu>;
type EntityArrayResponseType = HttpResponse<IIscilikGrubu[]>;

@Injectable({ providedIn: 'root' })
export class IscilikGrubuService {
  public resourceUrl = SERVER_API_URL + 'api/iscilik-grubus';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/iscilik-grubus';

  constructor(protected http: HttpClient) {}

  create(iscilikGrubu: IIscilikGrubu): Observable<EntityResponseType> {
    return this.http.post<IIscilikGrubu>(this.resourceUrl, iscilikGrubu, { observe: 'response' });
  }

  update(iscilikGrubu: IIscilikGrubu): Observable<EntityResponseType> {
    return this.http.put<IIscilikGrubu>(this.resourceUrl, iscilikGrubu, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIscilikGrubu>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIscilikGrubu[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIscilikGrubu[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

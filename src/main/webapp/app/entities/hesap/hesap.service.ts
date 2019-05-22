import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHesap } from 'app/shared/model/hesap.model';

type EntityResponseType = HttpResponse<IHesap>;
type EntityArrayResponseType = HttpResponse<IHesap[]>;

@Injectable({ providedIn: 'root' })
export class HesapService {
  public resourceUrl = SERVER_API_URL + 'api/hesaps';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/hesaps';

  constructor(protected http: HttpClient) {}

  create(hesap: IHesap): Observable<EntityResponseType> {
    return this.http.post<IHesap>(this.resourceUrl, hesap, { observe: 'response' });
  }

  update(hesap: IHesap): Observable<EntityResponseType> {
    return this.http.put<IHesap>(this.resourceUrl, hesap, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHesap>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHesap[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHesap[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

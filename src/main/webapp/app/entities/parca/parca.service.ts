import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IParca } from 'app/shared/model/parca.model';

type EntityResponseType = HttpResponse<IParca>;
type EntityArrayResponseType = HttpResponse<IParca[]>;

@Injectable({ providedIn: 'root' })
export class ParcaService {
  public resourceUrl = SERVER_API_URL + 'api/parcas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/parcas';

  constructor(protected http: HttpClient) {}

  create(parca: IParca): Observable<EntityResponseType> {
    return this.http.post<IParca>(this.resourceUrl, parca, { observe: 'response' });
  }

  update(parca: IParca): Observable<EntityResponseType> {
    return this.http.put<IParca>(this.resourceUrl, parca, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParca>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParca[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParca[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

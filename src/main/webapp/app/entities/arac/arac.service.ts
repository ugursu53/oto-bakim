import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IArac } from 'app/shared/model/arac.model';

type EntityResponseType = HttpResponse<IArac>;
type EntityArrayResponseType = HttpResponse<IArac[]>;

@Injectable({ providedIn: 'root' })
export class AracService {
  public resourceUrl = SERVER_API_URL + 'api/aracs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/aracs';

  constructor(protected http: HttpClient) {}

  create(arac: IArac): Observable<EntityResponseType> {
    return this.http.post<IArac>(this.resourceUrl, arac, { observe: 'response' });
  }

  update(arac: IArac): Observable<EntityResponseType> {
    return this.http.put<IArac>(this.resourceUrl, arac, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IArac>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArac[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IArac[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

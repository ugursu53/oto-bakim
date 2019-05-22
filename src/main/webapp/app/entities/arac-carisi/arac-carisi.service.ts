import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAracCarisi } from 'app/shared/model/arac-carisi.model';

type EntityResponseType = HttpResponse<IAracCarisi>;
type EntityArrayResponseType = HttpResponse<IAracCarisi[]>;

@Injectable({ providedIn: 'root' })
export class AracCarisiService {
  public resourceUrl = SERVER_API_URL + 'api/arac-carisis';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/arac-carisis';

  constructor(protected http: HttpClient) {}

  create(aracCarisi: IAracCarisi): Observable<EntityResponseType> {
    return this.http.post<IAracCarisi>(this.resourceUrl, aracCarisi, { observe: 'response' });
  }

  update(aracCarisi: IAracCarisi): Observable<EntityResponseType> {
    return this.http.put<IAracCarisi>(this.resourceUrl, aracCarisi, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAracCarisi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAracCarisi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAracCarisi[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

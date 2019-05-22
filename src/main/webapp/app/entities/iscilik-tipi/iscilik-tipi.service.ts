import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIscilikTipi } from 'app/shared/model/iscilik-tipi.model';

type EntityResponseType = HttpResponse<IIscilikTipi>;
type EntityArrayResponseType = HttpResponse<IIscilikTipi[]>;

@Injectable({ providedIn: 'root' })
export class IscilikTipiService {
  public resourceUrl = SERVER_API_URL + 'api/iscilik-tipis';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/iscilik-tipis';

  constructor(protected http: HttpClient) {}

  create(iscilikTipi: IIscilikTipi): Observable<EntityResponseType> {
    return this.http.post<IIscilikTipi>(this.resourceUrl, iscilikTipi, { observe: 'response' });
  }

  update(iscilikTipi: IIscilikTipi): Observable<EntityResponseType> {
    return this.http.put<IIscilikTipi>(this.resourceUrl, iscilikTipi, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIscilikTipi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIscilikTipi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIscilikTipi[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

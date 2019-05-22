import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IParcaTipi } from 'app/shared/model/parca-tipi.model';

type EntityResponseType = HttpResponse<IParcaTipi>;
type EntityArrayResponseType = HttpResponse<IParcaTipi[]>;

@Injectable({ providedIn: 'root' })
export class ParcaTipiService {
  public resourceUrl = SERVER_API_URL + 'api/parca-tipis';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/parca-tipis';

  constructor(protected http: HttpClient) {}

  create(parcaTipi: IParcaTipi): Observable<EntityResponseType> {
    return this.http.post<IParcaTipi>(this.resourceUrl, parcaTipi, { observe: 'response' });
  }

  update(parcaTipi: IParcaTipi): Observable<EntityResponseType> {
    return this.http.put<IParcaTipi>(this.resourceUrl, parcaTipi, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParcaTipi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParcaTipi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParcaTipi[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}

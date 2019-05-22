import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIsEmri } from 'app/shared/model/is-emri.model';

type EntityResponseType = HttpResponse<IIsEmri>;
type EntityArrayResponseType = HttpResponse<IIsEmri[]>;

@Injectable({ providedIn: 'root' })
export class IsEmriService {
  public resourceUrl = SERVER_API_URL + 'api/is-emris';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/is-emris';

  constructor(protected http: HttpClient) {}

  create(isEmri: IIsEmri): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isEmri);
    return this.http
      .post<IIsEmri>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(isEmri: IIsEmri): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isEmri);
    return this.http
      .put<IIsEmri>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIsEmri>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIsEmri[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIsEmri[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(isEmri: IIsEmri): IIsEmri {
    const copy: IIsEmri = Object.assign({}, isEmri, {
      gelisZamani: isEmri.gelisZamani != null && isEmri.gelisZamani.isValid() ? isEmri.gelisZamani.toJSON() : null,
      kabulTarihi: isEmri.kabulTarihi != null && isEmri.kabulTarihi.isValid() ? isEmri.kabulTarihi.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.gelisZamani = res.body.gelisZamani != null ? moment(res.body.gelisZamani) : null;
      res.body.kabulTarihi = res.body.kabulTarihi != null ? moment(res.body.kabulTarihi) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((isEmri: IIsEmri) => {
        isEmri.gelisZamani = isEmri.gelisZamani != null ? moment(isEmri.gelisZamani) : null;
        isEmri.kabulTarihi = isEmri.kabulTarihi != null ? moment(isEmri.kabulTarihi) : null;
      });
    }
    return res;
  }
}

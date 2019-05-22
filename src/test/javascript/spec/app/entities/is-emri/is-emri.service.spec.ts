/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IsEmriService } from 'app/entities/is-emri/is-emri.service';
import { IIsEmri, IsEmri, IsEmriTipi } from 'app/shared/model/is-emri.model';

describe('Service Tests', () => {
  describe('IsEmri Service', () => {
    let injector: TestBed;
    let service: IsEmriService;
    let httpMock: HttpTestingController;
    let elemDefault: IIsEmri;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(IsEmriService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new IsEmri(0, currentDate, 'AAAAAAA', currentDate, IsEmriTipi.SIGORTA);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            gelisZamani: currentDate.format(DATE_TIME_FORMAT),
            kabulTarihi: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a IsEmri', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            gelisZamani: currentDate.format(DATE_TIME_FORMAT),
            kabulTarihi: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            gelisZamani: currentDate,
            kabulTarihi: currentDate
          },
          returnedFromService
        );
        service
          .create(new IsEmri(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a IsEmri', async () => {
        const returnedFromService = Object.assign(
          {
            gelisZamani: currentDate.format(DATE_TIME_FORMAT),
            aciklama: 'BBBBBB',
            kabulTarihi: currentDate.format(DATE_TIME_FORMAT),
            tipi: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            gelisZamani: currentDate,
            kabulTarihi: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of IsEmri', async () => {
        const returnedFromService = Object.assign(
          {
            gelisZamani: currentDate.format(DATE_TIME_FORMAT),
            aciklama: 'BBBBBB',
            kabulTarihi: currentDate.format(DATE_TIME_FORMAT),
            tipi: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            gelisZamani: currentDate,
            kabulTarihi: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a IsEmri', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

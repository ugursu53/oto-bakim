/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { AracService } from 'app/entities/arac/arac.service';
import { IArac, Arac, YakitTuru, VitesTuru, KullanimSekli, AracTipi } from 'app/shared/model/arac.model';

describe('Service Tests', () => {
  describe('Arac Service', () => {
    let injector: TestBed;
    let service: AracService;
    let httpMock: HttpTestingController;
    let elemDefault: IArac;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(AracService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Arac(
        0,
        'AAAAAAA',
        0,
        'AAAAAAA',
        YakitTuru.BENZIN,
        VitesTuru.MANUEL,
        'AAAAAAA',
        'AAAAAAA',
        KullanimSekli.HUSISI,
        AracTipi.HATCHBACK,
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Arac', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new Arac(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Arac', async () => {
        const returnedFromService = Object.assign(
          {
            plakaNo: 'BBBBBB',
            modelYili: 1,
            rengi: 'BBBBBB',
            yakitTuru: 'BBBBBB',
            vitesTuru: 'BBBBBB',
            motorNo: 'BBBBBB',
            sasiNo: 'BBBBBB',
            kullanimSekli: 'BBBBBB',
            aracTipi: 'BBBBBB',
            aciklama: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Arac', async () => {
        const returnedFromService = Object.assign(
          {
            plakaNo: 'BBBBBB',
            modelYili: 1,
            rengi: 'BBBBBB',
            yakitTuru: 'BBBBBB',
            vitesTuru: 'BBBBBB',
            motorNo: 'BBBBBB',
            sasiNo: 'BBBBBB',
            kullanimSekli: 'BBBBBB',
            aracTipi: 'BBBBBB',
            aciklama: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
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

      it('should delete a Arac', async () => {
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

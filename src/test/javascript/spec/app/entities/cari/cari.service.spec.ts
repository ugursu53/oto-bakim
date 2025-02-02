/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { CariService } from 'app/entities/cari/cari.service';
import { ICari, Cari, CariTipi, KisiTipi, IsEmriTipi } from 'app/shared/model/cari.model';

describe('Service Tests', () => {
  describe('Cari Service', () => {
    let injector: TestBed;
    let service: CariService;
    let httpMock: HttpTestingController;
    let elemDefault: ICari;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CariService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Cari(
        0,
        CariTipi.MUSTERI,
        KisiTipi.GERCEK,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        false,
        'AAAAAAA',
        IsEmriTipi.SIGORTA
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

      it('should create a Cari', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new Cari(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Cari', async () => {
        const returnedFromService = Object.assign(
          {
            tipi: 'BBBBBB',
            kisiTipi: 'BBBBBB',
            aktif: true,
            ad: 'BBBBBB',
            adres: 'BBBBBB',
            telefon: 'BBBBBB',
            tcNo: 'BBBBBB',
            vergiNo: 'BBBBBB',
            yetkili: 'BBBBBB',
            fax: 'BBBBBB',
            eposta: 'BBBBBB',
            webAdresi: 'BBBBBB',
            iskonto: 1,
            efaturaKullanimi: true,
            aciklama: 'BBBBBB',
            varsayilanIsEmriTipi: 'BBBBBB'
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

      it('should return a list of Cari', async () => {
        const returnedFromService = Object.assign(
          {
            tipi: 'BBBBBB',
            kisiTipi: 'BBBBBB',
            aktif: true,
            ad: 'BBBBBB',
            adres: 'BBBBBB',
            telefon: 'BBBBBB',
            tcNo: 'BBBBBB',
            vergiNo: 'BBBBBB',
            yetkili: 'BBBBBB',
            fax: 'BBBBBB',
            eposta: 'BBBBBB',
            webAdresi: 'BBBBBB',
            iskonto: 1,
            efaturaKullanimi: true,
            aciklama: 'BBBBBB',
            varsayilanIsEmriTipi: 'BBBBBB'
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

      it('should delete a Cari', async () => {
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

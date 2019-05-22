/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { IscilikTipiDetailComponent } from 'app/entities/iscilik-tipi/iscilik-tipi-detail.component';
import { IscilikTipi } from 'app/shared/model/iscilik-tipi.model';

describe('Component Tests', () => {
  describe('IscilikTipi Management Detail Component', () => {
    let comp: IscilikTipiDetailComponent;
    let fixture: ComponentFixture<IscilikTipiDetailComponent>;
    const route = ({ data: of({ iscilikTipi: new IscilikTipi(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IscilikTipiDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IscilikTipiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IscilikTipiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.iscilikTipi).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { CariDetailComponent } from 'app/entities/cari/cari-detail.component';
import { Cari } from 'app/shared/model/cari.model';

describe('Component Tests', () => {
  describe('Cari Management Detail Component', () => {
    let comp: CariDetailComponent;
    let fixture: ComponentFixture<CariDetailComponent>;
    const route = ({ data: of({ cari: new Cari(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [CariDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CariDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CariDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cari).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

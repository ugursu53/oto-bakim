/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { ParcaTipiDetailComponent } from 'app/entities/parca-tipi/parca-tipi-detail.component';
import { ParcaTipi } from 'app/shared/model/parca-tipi.model';

describe('Component Tests', () => {
  describe('ParcaTipi Management Detail Component', () => {
    let comp: ParcaTipiDetailComponent;
    let fixture: ComponentFixture<ParcaTipiDetailComponent>;
    const route = ({ data: of({ parcaTipi: new ParcaTipi(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [ParcaTipiDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ParcaTipiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParcaTipiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.parcaTipi).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { ParcaDetailComponent } from 'app/entities/parca/parca-detail.component';
import { Parca } from 'app/shared/model/parca.model';

describe('Component Tests', () => {
  describe('Parca Management Detail Component', () => {
    let comp: ParcaDetailComponent;
    let fixture: ComponentFixture<ParcaDetailComponent>;
    const route = ({ data: of({ parca: new Parca(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [ParcaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ParcaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParcaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.parca).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

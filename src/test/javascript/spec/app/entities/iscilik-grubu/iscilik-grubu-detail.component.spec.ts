/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { IscilikGrubuDetailComponent } from 'app/entities/iscilik-grubu/iscilik-grubu-detail.component';
import { IscilikGrubu } from 'app/shared/model/iscilik-grubu.model';

describe('Component Tests', () => {
  describe('IscilikGrubu Management Detail Component', () => {
    let comp: IscilikGrubuDetailComponent;
    let fixture: ComponentFixture<IscilikGrubuDetailComponent>;
    const route = ({ data: of({ iscilikGrubu: new IscilikGrubu(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IscilikGrubuDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IscilikGrubuDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IscilikGrubuDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.iscilikGrubu).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { IsEmriDetailComponent } from 'app/entities/is-emri/is-emri-detail.component';
import { IsEmri } from 'app/shared/model/is-emri.model';

describe('Component Tests', () => {
  describe('IsEmri Management Detail Component', () => {
    let comp: IsEmriDetailComponent;
    let fixture: ComponentFixture<IsEmriDetailComponent>;
    const route = ({ data: of({ isEmri: new IsEmri(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IsEmriDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IsEmriDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IsEmriDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.isEmri).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

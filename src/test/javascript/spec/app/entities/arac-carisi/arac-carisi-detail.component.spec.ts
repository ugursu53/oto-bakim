/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { AracCarisiDetailComponent } from 'app/entities/arac-carisi/arac-carisi-detail.component';
import { AracCarisi } from 'app/shared/model/arac-carisi.model';

describe('Component Tests', () => {
  describe('AracCarisi Management Detail Component', () => {
    let comp: AracCarisiDetailComponent;
    let fixture: ComponentFixture<AracCarisiDetailComponent>;
    const route = ({ data: of({ aracCarisi: new AracCarisi(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [AracCarisiDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AracCarisiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AracCarisiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aracCarisi).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

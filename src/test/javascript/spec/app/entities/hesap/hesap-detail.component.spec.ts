/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { HesapDetailComponent } from 'app/entities/hesap/hesap-detail.component';
import { Hesap } from 'app/shared/model/hesap.model';

describe('Component Tests', () => {
  describe('Hesap Management Detail Component', () => {
    let comp: HesapDetailComponent;
    let fixture: ComponentFixture<HesapDetailComponent>;
    const route = ({ data: of({ hesap: new Hesap(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [HesapDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HesapDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HesapDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.hesap).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OtoBakimTestModule } from '../../../test.module';
import { HesapComponent } from 'app/entities/hesap/hesap.component';
import { HesapService } from 'app/entities/hesap/hesap.service';
import { Hesap } from 'app/shared/model/hesap.model';

describe('Component Tests', () => {
  describe('Hesap Management Component', () => {
    let comp: HesapComponent;
    let fixture: ComponentFixture<HesapComponent>;
    let service: HesapService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [HesapComponent],
        providers: []
      })
        .overrideTemplate(HesapComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HesapComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HesapService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Hesap(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.hesaps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

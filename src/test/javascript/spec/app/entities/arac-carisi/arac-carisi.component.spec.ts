/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { OtoBakimTestModule } from '../../../test.module';
import { AracCarisiComponent } from 'app/entities/arac-carisi/arac-carisi.component';
import { AracCarisiService } from 'app/entities/arac-carisi/arac-carisi.service';
import { AracCarisi } from 'app/shared/model/arac-carisi.model';

describe('Component Tests', () => {
  describe('AracCarisi Management Component', () => {
    let comp: AracCarisiComponent;
    let fixture: ComponentFixture<AracCarisiComponent>;
    let service: AracCarisiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [AracCarisiComponent],
        providers: []
      })
        .overrideTemplate(AracCarisiComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AracCarisiComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AracCarisiService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new AracCarisi(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.aracCarisis[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

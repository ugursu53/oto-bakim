/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { IscilikTipiUpdateComponent } from 'app/entities/iscilik-tipi/iscilik-tipi-update.component';
import { IscilikTipiService } from 'app/entities/iscilik-tipi/iscilik-tipi.service';
import { IscilikTipi } from 'app/shared/model/iscilik-tipi.model';

describe('Component Tests', () => {
  describe('IscilikTipi Management Update Component', () => {
    let comp: IscilikTipiUpdateComponent;
    let fixture: ComponentFixture<IscilikTipiUpdateComponent>;
    let service: IscilikTipiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IscilikTipiUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IscilikTipiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IscilikTipiUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IscilikTipiService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IscilikTipi(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new IscilikTipi();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

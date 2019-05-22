/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { ParcaTipiUpdateComponent } from 'app/entities/parca-tipi/parca-tipi-update.component';
import { ParcaTipiService } from 'app/entities/parca-tipi/parca-tipi.service';
import { ParcaTipi } from 'app/shared/model/parca-tipi.model';

describe('Component Tests', () => {
  describe('ParcaTipi Management Update Component', () => {
    let comp: ParcaTipiUpdateComponent;
    let fixture: ComponentFixture<ParcaTipiUpdateComponent>;
    let service: ParcaTipiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [ParcaTipiUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ParcaTipiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParcaTipiUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParcaTipiService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ParcaTipi(123);
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
        const entity = new ParcaTipi();
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

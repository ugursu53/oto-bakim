/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { ParcaUpdateComponent } from 'app/entities/parca/parca-update.component';
import { ParcaService } from 'app/entities/parca/parca.service';
import { Parca } from 'app/shared/model/parca.model';

describe('Component Tests', () => {
  describe('Parca Management Update Component', () => {
    let comp: ParcaUpdateComponent;
    let fixture: ComponentFixture<ParcaUpdateComponent>;
    let service: ParcaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [ParcaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ParcaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParcaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParcaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Parca(123);
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
        const entity = new Parca();
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

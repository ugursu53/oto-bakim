/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { IsEmriUpdateComponent } from 'app/entities/is-emri/is-emri-update.component';
import { IsEmriService } from 'app/entities/is-emri/is-emri.service';
import { IsEmri } from 'app/shared/model/is-emri.model';

describe('Component Tests', () => {
  describe('IsEmri Management Update Component', () => {
    let comp: IsEmriUpdateComponent;
    let fixture: ComponentFixture<IsEmriUpdateComponent>;
    let service: IsEmriService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IsEmriUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IsEmriUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IsEmriUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IsEmriService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IsEmri(123);
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
        const entity = new IsEmri();
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

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { AracCarisiUpdateComponent } from 'app/entities/arac-carisi/arac-carisi-update.component';
import { AracCarisiService } from 'app/entities/arac-carisi/arac-carisi.service';
import { AracCarisi } from 'app/shared/model/arac-carisi.model';

describe('Component Tests', () => {
  describe('AracCarisi Management Update Component', () => {
    let comp: AracCarisiUpdateComponent;
    let fixture: ComponentFixture<AracCarisiUpdateComponent>;
    let service: AracCarisiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [AracCarisiUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AracCarisiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AracCarisiUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AracCarisiService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AracCarisi(123);
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
        const entity = new AracCarisi();
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

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { HesapUpdateComponent } from 'app/entities/hesap/hesap-update.component';
import { HesapService } from 'app/entities/hesap/hesap.service';
import { Hesap } from 'app/shared/model/hesap.model';

describe('Component Tests', () => {
  describe('Hesap Management Update Component', () => {
    let comp: HesapUpdateComponent;
    let fixture: ComponentFixture<HesapUpdateComponent>;
    let service: HesapService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [HesapUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(HesapUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(HesapUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HesapService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Hesap(123);
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
        const entity = new Hesap();
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

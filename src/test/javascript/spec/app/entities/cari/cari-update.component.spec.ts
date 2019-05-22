/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { CariUpdateComponent } from 'app/entities/cari/cari-update.component';
import { CariService } from 'app/entities/cari/cari.service';
import { Cari } from 'app/shared/model/cari.model';

describe('Component Tests', () => {
  describe('Cari Management Update Component', () => {
    let comp: CariUpdateComponent;
    let fixture: ComponentFixture<CariUpdateComponent>;
    let service: CariService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [CariUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CariUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CariUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CariService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Cari(123);
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
        const entity = new Cari();
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

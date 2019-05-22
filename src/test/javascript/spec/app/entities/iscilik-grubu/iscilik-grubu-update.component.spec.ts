/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { OtoBakimTestModule } from '../../../test.module';
import { IscilikGrubuUpdateComponent } from 'app/entities/iscilik-grubu/iscilik-grubu-update.component';
import { IscilikGrubuService } from 'app/entities/iscilik-grubu/iscilik-grubu.service';
import { IscilikGrubu } from 'app/shared/model/iscilik-grubu.model';

describe('Component Tests', () => {
  describe('IscilikGrubu Management Update Component', () => {
    let comp: IscilikGrubuUpdateComponent;
    let fixture: ComponentFixture<IscilikGrubuUpdateComponent>;
    let service: IscilikGrubuService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IscilikGrubuUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IscilikGrubuUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IscilikGrubuUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IscilikGrubuService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IscilikGrubu(123);
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
        const entity = new IscilikGrubu();
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

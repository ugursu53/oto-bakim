/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OtoBakimTestModule } from '../../../test.module';
import { IscilikGrubuDeleteDialogComponent } from 'app/entities/iscilik-grubu/iscilik-grubu-delete-dialog.component';
import { IscilikGrubuService } from 'app/entities/iscilik-grubu/iscilik-grubu.service';

describe('Component Tests', () => {
  describe('IscilikGrubu Management Delete Component', () => {
    let comp: IscilikGrubuDeleteDialogComponent;
    let fixture: ComponentFixture<IscilikGrubuDeleteDialogComponent>;
    let service: IscilikGrubuService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IscilikGrubuDeleteDialogComponent]
      })
        .overrideTemplate(IscilikGrubuDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IscilikGrubuDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IscilikGrubuService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});

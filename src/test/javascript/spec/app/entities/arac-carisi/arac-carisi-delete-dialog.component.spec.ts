/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OtoBakimTestModule } from '../../../test.module';
import { AracCarisiDeleteDialogComponent } from 'app/entities/arac-carisi/arac-carisi-delete-dialog.component';
import { AracCarisiService } from 'app/entities/arac-carisi/arac-carisi.service';

describe('Component Tests', () => {
  describe('AracCarisi Management Delete Component', () => {
    let comp: AracCarisiDeleteDialogComponent;
    let fixture: ComponentFixture<AracCarisiDeleteDialogComponent>;
    let service: AracCarisiService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [AracCarisiDeleteDialogComponent]
      })
        .overrideTemplate(AracCarisiDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AracCarisiDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AracCarisiService);
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

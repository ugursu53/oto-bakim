/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OtoBakimTestModule } from '../../../test.module';
import { HesapDeleteDialogComponent } from 'app/entities/hesap/hesap-delete-dialog.component';
import { HesapService } from 'app/entities/hesap/hesap.service';

describe('Component Tests', () => {
  describe('Hesap Management Delete Component', () => {
    let comp: HesapDeleteDialogComponent;
    let fixture: ComponentFixture<HesapDeleteDialogComponent>;
    let service: HesapService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [HesapDeleteDialogComponent]
      })
        .overrideTemplate(HesapDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HesapDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HesapService);
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

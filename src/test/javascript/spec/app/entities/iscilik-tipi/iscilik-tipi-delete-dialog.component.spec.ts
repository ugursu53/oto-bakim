/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OtoBakimTestModule } from '../../../test.module';
import { IscilikTipiDeleteDialogComponent } from 'app/entities/iscilik-tipi/iscilik-tipi-delete-dialog.component';
import { IscilikTipiService } from 'app/entities/iscilik-tipi/iscilik-tipi.service';

describe('Component Tests', () => {
  describe('IscilikTipi Management Delete Component', () => {
    let comp: IscilikTipiDeleteDialogComponent;
    let fixture: ComponentFixture<IscilikTipiDeleteDialogComponent>;
    let service: IscilikTipiService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [IscilikTipiDeleteDialogComponent]
      })
        .overrideTemplate(IscilikTipiDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IscilikTipiDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IscilikTipiService);
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

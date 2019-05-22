/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OtoBakimTestModule } from '../../../test.module';
import { CariDeleteDialogComponent } from 'app/entities/cari/cari-delete-dialog.component';
import { CariService } from 'app/entities/cari/cari.service';

describe('Component Tests', () => {
  describe('Cari Management Delete Component', () => {
    let comp: CariDeleteDialogComponent;
    let fixture: ComponentFixture<CariDeleteDialogComponent>;
    let service: CariService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [CariDeleteDialogComponent]
      })
        .overrideTemplate(CariDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CariDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CariService);
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

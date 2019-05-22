/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { OtoBakimTestModule } from '../../../test.module';
import { ParcaDeleteDialogComponent } from 'app/entities/parca/parca-delete-dialog.component';
import { ParcaService } from 'app/entities/parca/parca.service';

describe('Component Tests', () => {
  describe('Parca Management Delete Component', () => {
    let comp: ParcaDeleteDialogComponent;
    let fixture: ComponentFixture<ParcaDeleteDialogComponent>;
    let service: ParcaService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OtoBakimTestModule],
        declarations: [ParcaDeleteDialogComponent]
      })
        .overrideTemplate(ParcaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParcaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ParcaService);
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

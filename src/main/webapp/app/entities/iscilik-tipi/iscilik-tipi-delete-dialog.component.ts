import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIscilikTipi } from 'app/shared/model/iscilik-tipi.model';
import { IscilikTipiService } from './iscilik-tipi.service';

@Component({
  selector: 'jhi-iscilik-tipi-delete-dialog',
  templateUrl: './iscilik-tipi-delete-dialog.component.html'
})
export class IscilikTipiDeleteDialogComponent {
  iscilikTipi: IIscilikTipi;

  constructor(
    protected iscilikTipiService: IscilikTipiService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.iscilikTipiService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'iscilikTipiListModification',
        content: 'Deleted an iscilikTipi'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-iscilik-tipi-delete-popup',
  template: ''
})
export class IscilikTipiDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ iscilikTipi }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IscilikTipiDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.iscilikTipi = iscilikTipi;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/iscilik-tipi', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/iscilik-tipi', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICari } from 'app/shared/model/cari.model';
import { CariService } from './cari.service';

@Component({
  selector: 'jhi-cari-delete-dialog',
  templateUrl: './cari-delete-dialog.component.html'
})
export class CariDeleteDialogComponent {
  cari: ICari;

  constructor(protected cariService: CariService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.cariService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'cariListModification',
        content: 'Deleted an cari'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-cari-delete-popup',
  template: ''
})
export class CariDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cari }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CariDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.cari = cari;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/cari', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/cari', { outlets: { popup: null } }]);
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

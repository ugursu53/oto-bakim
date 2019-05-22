import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAracCarisi } from 'app/shared/model/arac-carisi.model';
import { AracCarisiService } from './arac-carisi.service';

@Component({
  selector: 'jhi-arac-carisi-delete-dialog',
  templateUrl: './arac-carisi-delete-dialog.component.html'
})
export class AracCarisiDeleteDialogComponent {
  aracCarisi: IAracCarisi;

  constructor(
    protected aracCarisiService: AracCarisiService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.aracCarisiService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'aracCarisiListModification',
        content: 'Deleted an aracCarisi'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-arac-carisi-delete-popup',
  template: ''
})
export class AracCarisiDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ aracCarisi }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AracCarisiDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.aracCarisi = aracCarisi;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/arac-carisi', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/arac-carisi', { outlets: { popup: null } }]);
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

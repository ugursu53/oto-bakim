import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIsEmri } from 'app/shared/model/is-emri.model';
import { IsEmriService } from './is-emri.service';

@Component({
  selector: 'jhi-is-emri-delete-dialog',
  templateUrl: './is-emri-delete-dialog.component.html'
})
export class IsEmriDeleteDialogComponent {
  isEmri: IIsEmri;

  constructor(protected isEmriService: IsEmriService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.isEmriService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'isEmriListModification',
        content: 'Deleted an isEmri'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-is-emri-delete-popup',
  template: ''
})
export class IsEmriDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ isEmri }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IsEmriDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.isEmri = isEmri;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/is-emri', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/is-emri', { outlets: { popup: null } }]);
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

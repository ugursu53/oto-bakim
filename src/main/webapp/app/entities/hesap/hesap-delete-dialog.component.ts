import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHesap } from 'app/shared/model/hesap.model';
import { HesapService } from './hesap.service';

@Component({
  selector: 'jhi-hesap-delete-dialog',
  templateUrl: './hesap-delete-dialog.component.html'
})
export class HesapDeleteDialogComponent {
  hesap: IHesap;

  constructor(protected hesapService: HesapService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.hesapService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'hesapListModification',
        content: 'Deleted an hesap'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-hesap-delete-popup',
  template: ''
})
export class HesapDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ hesap }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(HesapDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.hesap = hesap;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/hesap', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/hesap', { outlets: { popup: null } }]);
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

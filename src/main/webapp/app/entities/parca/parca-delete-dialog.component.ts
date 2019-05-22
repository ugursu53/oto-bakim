import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParca } from 'app/shared/model/parca.model';
import { ParcaService } from './parca.service';

@Component({
  selector: 'jhi-parca-delete-dialog',
  templateUrl: './parca-delete-dialog.component.html'
})
export class ParcaDeleteDialogComponent {
  parca: IParca;

  constructor(protected parcaService: ParcaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.parcaService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'parcaListModification',
        content: 'Deleted an parca'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-parca-delete-popup',
  template: ''
})
export class ParcaDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ parca }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ParcaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.parca = parca;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/parca', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/parca', { outlets: { popup: null } }]);
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

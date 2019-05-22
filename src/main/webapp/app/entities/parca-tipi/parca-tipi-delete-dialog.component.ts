import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParcaTipi } from 'app/shared/model/parca-tipi.model';
import { ParcaTipiService } from './parca-tipi.service';

@Component({
  selector: 'jhi-parca-tipi-delete-dialog',
  templateUrl: './parca-tipi-delete-dialog.component.html'
})
export class ParcaTipiDeleteDialogComponent {
  parcaTipi: IParcaTipi;

  constructor(protected parcaTipiService: ParcaTipiService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.parcaTipiService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'parcaTipiListModification',
        content: 'Deleted an parcaTipi'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-parca-tipi-delete-popup',
  template: ''
})
export class ParcaTipiDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ parcaTipi }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ParcaTipiDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.parcaTipi = parcaTipi;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/parca-tipi', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/parca-tipi', { outlets: { popup: null } }]);
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

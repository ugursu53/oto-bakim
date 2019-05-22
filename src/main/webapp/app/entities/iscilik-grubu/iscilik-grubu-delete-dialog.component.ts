import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIscilikGrubu } from 'app/shared/model/iscilik-grubu.model';
import { IscilikGrubuService } from './iscilik-grubu.service';

@Component({
  selector: 'jhi-iscilik-grubu-delete-dialog',
  templateUrl: './iscilik-grubu-delete-dialog.component.html'
})
export class IscilikGrubuDeleteDialogComponent {
  iscilikGrubu: IIscilikGrubu;

  constructor(
    protected iscilikGrubuService: IscilikGrubuService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.iscilikGrubuService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'iscilikGrubuListModification',
        content: 'Deleted an iscilikGrubu'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-iscilik-grubu-delete-popup',
  template: ''
})
export class IscilikGrubuDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ iscilikGrubu }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IscilikGrubuDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.iscilikGrubu = iscilikGrubu;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/iscilik-grubu', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/iscilik-grubu', { outlets: { popup: null } }]);
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

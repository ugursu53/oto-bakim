import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IIscilikTipi, IscilikTipi } from 'app/shared/model/iscilik-tipi.model';
import { IscilikTipiService } from './iscilik-tipi.service';
import { IIscilikGrubu } from 'app/shared/model/iscilik-grubu.model';
import { IscilikGrubuService } from 'app/entities/iscilik-grubu';

@Component({
  selector: 'jhi-iscilik-tipi-update',
  templateUrl: './iscilik-tipi-update.component.html'
})
export class IscilikTipiUpdateComponent implements OnInit {
  iscilikTipi: IIscilikTipi;
  isSaving: boolean;

  iscilikgrubus: IIscilikGrubu[];

  editForm = this.fb.group({
    id: [],
    ad: [],
    varsayilanFiyat: [],
    grubu: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected iscilikTipiService: IscilikTipiService,
    protected iscilikGrubuService: IscilikGrubuService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ iscilikTipi }) => {
      this.updateForm(iscilikTipi);
      this.iscilikTipi = iscilikTipi;
    });
    this.iscilikGrubuService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIscilikGrubu[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIscilikGrubu[]>) => response.body)
      )
      .subscribe((res: IIscilikGrubu[]) => (this.iscilikgrubus = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(iscilikTipi: IIscilikTipi) {
    this.editForm.patchValue({
      id: iscilikTipi.id,
      ad: iscilikTipi.ad,
      varsayilanFiyat: iscilikTipi.varsayilanFiyat,
      grubu: iscilikTipi.grubu
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const iscilikTipi = this.createFromForm();
    if (iscilikTipi.id !== undefined) {
      this.subscribeToSaveResponse(this.iscilikTipiService.update(iscilikTipi));
    } else {
      this.subscribeToSaveResponse(this.iscilikTipiService.create(iscilikTipi));
    }
  }

  private createFromForm(): IIscilikTipi {
    const entity = {
      ...new IscilikTipi(),
      id: this.editForm.get(['id']).value,
      ad: this.editForm.get(['ad']).value,
      varsayilanFiyat: this.editForm.get(['varsayilanFiyat']).value,
      grubu: this.editForm.get(['grubu']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIscilikTipi>>) {
    result.subscribe((res: HttpResponse<IIscilikTipi>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackIscilikGrubuById(index: number, item: IIscilikGrubu) {
    return item.id;
  }
}

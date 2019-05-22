import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IParca, Parca } from 'app/shared/model/parca.model';
import { ParcaService } from './parca.service';
import { IIsEmri } from 'app/shared/model/is-emri.model';
import { IsEmriService } from 'app/entities/is-emri';
import { IParcaTipi } from 'app/shared/model/parca-tipi.model';
import { ParcaTipiService } from 'app/entities/parca-tipi';

@Component({
  selector: 'jhi-parca-update',
  templateUrl: './parca-update.component.html'
})
export class ParcaUpdateComponent implements OnInit {
  parca: IParca;
  isSaving: boolean;

  isemris: IIsEmri[];

  parcatipis: IParcaTipi[];

  editForm = this.fb.group({
    id: [],
    fiyati: [],
    isEmri: [],
    tipi: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected parcaService: ParcaService,
    protected isEmriService: IsEmriService,
    protected parcaTipiService: ParcaTipiService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ parca }) => {
      this.updateForm(parca);
      this.parca = parca;
    });
    this.isEmriService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIsEmri[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIsEmri[]>) => response.body)
      )
      .subscribe((res: IIsEmri[]) => (this.isemris = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.parcaTipiService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IParcaTipi[]>) => mayBeOk.ok),
        map((response: HttpResponse<IParcaTipi[]>) => response.body)
      )
      .subscribe((res: IParcaTipi[]) => (this.parcatipis = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(parca: IParca) {
    this.editForm.patchValue({
      id: parca.id,
      fiyati: parca.fiyati,
      isEmri: parca.isEmri,
      tipi: parca.tipi
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const parca = this.createFromForm();
    if (parca.id !== undefined) {
      this.subscribeToSaveResponse(this.parcaService.update(parca));
    } else {
      this.subscribeToSaveResponse(this.parcaService.create(parca));
    }
  }

  private createFromForm(): IParca {
    const entity = {
      ...new Parca(),
      id: this.editForm.get(['id']).value,
      fiyati: this.editForm.get(['fiyati']).value,
      isEmri: this.editForm.get(['isEmri']).value,
      tipi: this.editForm.get(['tipi']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParca>>) {
    result.subscribe((res: HttpResponse<IParca>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackIsEmriById(index: number, item: IIsEmri) {
    return item.id;
  }

  trackParcaTipiById(index: number, item: IParcaTipi) {
    return item.id;
  }
}

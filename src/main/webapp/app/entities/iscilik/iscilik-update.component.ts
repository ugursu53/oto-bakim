import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IIscilik, Iscilik } from 'app/shared/model/iscilik.model';
import { IscilikService } from './iscilik.service';
import { IIsEmri } from 'app/shared/model/is-emri.model';
import { IsEmriService } from 'app/entities/is-emri';
import { IIscilikTipi } from 'app/shared/model/iscilik-tipi.model';
import { IscilikTipiService } from 'app/entities/iscilik-tipi';
import { IPersonel } from 'app/shared/model/personel.model';
import { PersonelService } from 'app/entities/personel';

@Component({
  selector: 'jhi-iscilik-update',
  templateUrl: './iscilik-update.component.html'
})
export class IscilikUpdateComponent implements OnInit {
  iscilik: IIscilik;
  isSaving: boolean;

  isemris: IIsEmri[];

  isciliktipis: IIscilikTipi[];

  personels: IPersonel[];

  editForm = this.fb.group({
    id: [],
    aciklama: [],
    fiyat: [],
    iskonto: [],
    isEmri: [],
    tipi: [],
    personel: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected iscilikService: IscilikService,
    protected isEmriService: IsEmriService,
    protected iscilikTipiService: IscilikTipiService,
    protected personelService: PersonelService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ iscilik }) => {
      this.updateForm(iscilik);
      this.iscilik = iscilik;
    });
    this.isEmriService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIsEmri[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIsEmri[]>) => response.body)
      )
      .subscribe((res: IIsEmri[]) => (this.isemris = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.iscilikTipiService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIscilikTipi[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIscilikTipi[]>) => response.body)
      )
      .subscribe((res: IIscilikTipi[]) => (this.isciliktipis = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.personelService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPersonel[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPersonel[]>) => response.body)
      )
      .subscribe((res: IPersonel[]) => (this.personels = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(iscilik: IIscilik) {
    this.editForm.patchValue({
      id: iscilik.id,
      aciklama: iscilik.aciklama,
      fiyat: iscilik.fiyat,
      iskonto: iscilik.iskonto,
      isEmri: iscilik.isEmri,
      tipi: iscilik.tipi,
      personel: iscilik.personel
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const iscilik = this.createFromForm();
    if (iscilik.id !== undefined) {
      this.subscribeToSaveResponse(this.iscilikService.update(iscilik));
    } else {
      this.subscribeToSaveResponse(this.iscilikService.create(iscilik));
    }
  }

  private createFromForm(): IIscilik {
    const entity = {
      ...new Iscilik(),
      id: this.editForm.get(['id']).value,
      aciklama: this.editForm.get(['aciklama']).value,
      fiyat: this.editForm.get(['fiyat']).value,
      iskonto: this.editForm.get(['iskonto']).value,
      isEmri: this.editForm.get(['isEmri']).value,
      tipi: this.editForm.get(['tipi']).value,
      personel: this.editForm.get(['personel']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIscilik>>) {
    result.subscribe((res: HttpResponse<IIscilik>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackIscilikTipiById(index: number, item: IIscilikTipi) {
    return item.id;
  }

  trackPersonelById(index: number, item: IPersonel) {
    return item.id;
  }
}

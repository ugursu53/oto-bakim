import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IIsEmri, IsEmri } from 'app/shared/model/is-emri.model';
import { IsEmriService } from './is-emri.service';
import { IArac } from 'app/shared/model/arac.model';
import { AracService } from 'app/entities/arac';

@Component({
  selector: 'jhi-is-emri-update',
  templateUrl: './is-emri-update.component.html'
})
export class IsEmriUpdateComponent implements OnInit {
  isSaving: boolean;
  @Input() arac: IArac;

  aracs: IArac[];

  editForm = this.fb.group({
    id: [],
    gelisZamani: [],
    aciklama: [null, [Validators.required]],
    kabulTarihi: [null, [Validators.required]],
    tipi: [null, [Validators.required]],
    arac: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected isEmriService: IsEmriService,
    protected aracService: AracService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    if (this.arac === undefined) {
      this.activatedRoute.data.subscribe(({ isEmri }) => {
        this.updateForm(isEmri);
      });
    } else {
      const isEmri = new IsEmri();
      isEmri.arac = this.arac;
      this.updateForm(isEmri);
    }

    this.aracService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IArac[]>) => mayBeOk.ok),
        map((response: HttpResponse<IArac[]>) => response.body)
      )
      .subscribe((res: IArac[]) => (this.aracs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(isEmri: IIsEmri) {
    this.editForm.patchValue({
      id: isEmri.id,
      gelisZamani: isEmri.gelisZamani != null ? isEmri.gelisZamani.format(DATE_TIME_FORMAT) : null,
      aciklama: isEmri.aciklama,
      kabulTarihi: isEmri.kabulTarihi != null ? isEmri.kabulTarihi.format(DATE_TIME_FORMAT) : null,
      tipi: isEmri.tipi ? isEmri.tipi : isEmri.arac && isEmri.arac.aktifCari ? isEmri.arac.aktifCari.varsayilanIsEmriTipi : null,
      arac: isEmri.arac
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const isEmri = this.createFromForm();
    if (isEmri.id !== undefined) {
      this.subscribeToSaveResponse(this.isEmriService.update(isEmri));
    } else {
      this.subscribeToSaveResponse(this.isEmriService.create(isEmri));
    }
  }

  private createFromForm(): IIsEmri {
    return {
      ...new IsEmri(),
      id: this.editForm.get(['id']).value,
      gelisZamani:
        this.editForm.get(['gelisZamani']).value != null ? moment(this.editForm.get(['gelisZamani']).value, DATE_TIME_FORMAT) : undefined,
      aciklama: this.editForm.get(['aciklama']).value,
      kabulTarihi:
        this.editForm.get(['kabulTarihi']).value != null ? moment(this.editForm.get(['kabulTarihi']).value, DATE_TIME_FORMAT) : undefined,
      tipi: this.editForm.get(['tipi']).value,
      arac: this.editForm.get(['arac']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsEmri>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
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

  trackAracById(index: number, item: IArac) {
    return item.id;
  }
}

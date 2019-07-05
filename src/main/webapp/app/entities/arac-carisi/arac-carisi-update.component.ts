import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IAracCarisi, AracCarisi } from 'app/shared/model/arac-carisi.model';
import { AracCarisiService } from './arac-carisi.service';
import { IArac } from 'app/shared/model/arac.model';
import { AracService } from 'app/entities/arac';
import { ICari } from 'app/shared/model/cari.model';
import { CariService } from 'app/entities/cari';

@Component({
  selector: 'jhi-arac-carisi-update',
  templateUrl: './arac-carisi-update.component.html'
})
export class AracCarisiUpdateComponent implements OnInit {
  isSaving: boolean;

  aracs: IArac[];

  caris: ICari[];

  editForm = this.fb.group({
    id: [],
    aktif: [null, [Validators.required]],
    arac: [],
    cari: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected aracCarisiService: AracCarisiService,
    protected aracService: AracService,
    protected cariService: CariService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ aracCarisi }) => {
      this.updateForm(aracCarisi);
    });
    this.aracService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IArac[]>) => mayBeOk.ok),
        map((response: HttpResponse<IArac[]>) => response.body)
      )
      .subscribe((res: IArac[]) => (this.aracs = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.cariService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICari[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICari[]>) => response.body)
      )
      .subscribe((res: ICari[]) => (this.caris = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(aracCarisi: IAracCarisi) {
    this.editForm.patchValue({
      id: aracCarisi.id,
      aktif: aracCarisi.aktif,
      arac: aracCarisi.arac,
      cari: aracCarisi.cari
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const aracCarisi = this.createFromForm();
    if (aracCarisi.id !== undefined) {
      this.subscribeToSaveResponse(this.aracCarisiService.update(aracCarisi));
    } else {
      this.subscribeToSaveResponse(this.aracCarisiService.create(aracCarisi));
    }
  }

  private createFromForm(): IAracCarisi {
    return {
      ...new AracCarisi(),
      id: this.editForm.get(['id']).value,
      aktif: this.editForm.get(['aktif']).value,
      arac: this.editForm.get(['arac']).value,
      cari: this.editForm.get(['cari']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAracCarisi>>) {
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

  trackCariById(index: number, item: ICari) {
    return item.id;
  }
}

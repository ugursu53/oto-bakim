import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IModel, Model } from 'app/shared/model/model.model';
import { ModelService } from './model.service';
import { IMarka } from 'app/shared/model/marka.model';
import { MarkaService } from 'app/entities/marka';

@Component({
  selector: 'jhi-model-update',
  templateUrl: './model-update.component.html'
})
export class ModelUpdateComponent implements OnInit {
  isSaving: boolean;
  @Input() isPopup: boolean;
  @Output() saved = new EventEmitter();

  markas: IMarka[];

  editForm = this.fb.group({
    id: [],
    ad: [null, [Validators.required]],
    marka: [null, [Validators.required]]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected modelService: ModelService,
    protected markaService: MarkaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  @Input() set modelAdi(modelAdi: string) {
    this.editForm.patchValue({
      id: null,
      ad: modelAdi,
      marka: []
    });
  }

  ngOnInit() {
    this.isSaving = false;
    if (!this.isPopup) {
      this.activatedRoute.data.subscribe(({ model }) => {
        this.updateForm(model);
      });
    }
    this.markaService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMarka[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMarka[]>) => response.body)
      )
      .subscribe((res: IMarka[]) => (this.markas = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(model: IModel) {
    this.editForm.patchValue({
      id: model.id,
      ad: model.ad,
      marka: model.marka
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const model = this.createFromForm();
    if (model.id !== undefined && model.id != null) {
      this.subscribeToSaveResponse(this.modelService.update(model));
    } else {
      this.subscribeToSaveResponse(this.modelService.create(model));
    }
  }

  private createFromForm(): IModel {
    return {
      ...new Model(),
      id: this.editForm.get(['id']).value,
      ad: this.editForm.get(['ad']).value,
      marka: this.editForm.get(['marka']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModel>>) {
    result.subscribe(res => this.onSaveSuccess(res.body), () => this.onSaveError());
  }

  protected onSaveSuccess(model: any) {
    this.isSaving = false;
    if (this.isPopup) {
      this.saved.emit(model);
    } else {
      this.previousState();
    }
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackMarkaById(index: number, item: IMarka) {
    return item.id;
  }
}

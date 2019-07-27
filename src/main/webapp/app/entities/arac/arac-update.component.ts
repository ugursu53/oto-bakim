import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IArac, Arac } from 'app/shared/model/arac.model';
import { AracService } from './arac.service';
import { IModel } from 'app/shared/model/model.model';
import { ModelService } from 'app/entities/model';

@Component({
  selector: 'jhi-arac-update',
  templateUrl: './arac-update.component.html'
})
export class AracUpdateComponent implements OnInit {
  isSaving: boolean;

  models: IModel[];
  filteredModels: any[];

  editForm = this.fb.group({
    id: [],
    plakaNo: [null, [Validators.required]],
    modelYili: [null, [Validators.required]],
    rengi: [],
    yakitTuru: [null, [Validators.required]],
    vitesTuru: [null, [Validators.required]],
    motorNo: [],
    sasiNo: [],
    kullanimSekli: [],
    aracTipi: [],
    aciklama: [],
    model: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected aracService: AracService,
    protected modelService: ModelService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ arac }) => {
      this.updateForm(arac);
    });
    this.modelService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IModel[]>) => mayBeOk.ok),
        map((response: HttpResponse<IModel[]>) => response.body)
      )
      .subscribe((res: IModel[]) => (this.models = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(arac: IArac) {
    this.editForm.patchValue({
      id: arac.id,
      plakaNo: arac.plakaNo,
      modelYili: arac.modelYili,
      rengi: arac.rengi,
      yakitTuru: arac.yakitTuru,
      vitesTuru: arac.vitesTuru,
      motorNo: arac.motorNo,
      sasiNo: arac.sasiNo,
      kullanimSekli: arac.kullanimSekli,
      aracTipi: arac.aracTipi,
      aciklama: arac.aciklama,
      model: arac.model
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const arac = this.createFromForm();
    if (arac.id !== undefined) {
      this.subscribeToSaveResponse(this.aracService.update(arac));
    } else {
      this.subscribeToSaveResponse(this.aracService.create(arac));
    }
  }

  private createFromForm(): IArac {
    return {
      ...new Arac(),
      id: this.editForm.get(['id']).value,
      plakaNo: this.editForm.get(['plakaNo']).value,
      modelYili: this.editForm.get(['modelYili']).value,
      rengi: this.editForm.get(['rengi']).value,
      yakitTuru: this.editForm.get(['yakitTuru']).value,
      vitesTuru: this.editForm.get(['vitesTuru']).value,
      motorNo: this.editForm.get(['motorNo']).value,
      sasiNo: this.editForm.get(['sasiNo']).value,
      kullanimSekli: this.editForm.get(['kullanimSekli']).value,
      aracTipi: this.editForm.get(['aracTipi']).value,
      aciklama: this.editForm.get(['aciklama']).value,
      model: this.editForm.get(['model']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArac>>) {
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

  trackModelById(index: number, item: IModel) {
    return item.id;
  }

  filterModels(event) {
    this.filteredModels = [];
    for (let i = 0; i < this.models.length; i++) {
      let model = this.models[i];
      if (
        model.ad.toLowerCase().indexOf(event.query.toLowerCase()) == 0 ||
        (model.marka != null && model.marka.ad.toLowerCase().indexOf(event.query.toLowerCase()) == 0)
      ) {
        this.filteredModels.push(model);
      }
    }
  }
}

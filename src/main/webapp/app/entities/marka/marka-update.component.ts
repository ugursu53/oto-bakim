import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IMarka, Marka } from 'app/shared/model/marka.model';
import { MarkaService } from './marka.service';

@Component({
  selector: 'jhi-marka-update',
  templateUrl: './marka-update.component.html'
})
export class MarkaUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    ad: []
  });

  constructor(protected markaService: MarkaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ marka }) => {
      this.updateForm(marka);
    });
  }

  updateForm(marka: IMarka) {
    this.editForm.patchValue({
      id: marka.id,
      ad: marka.ad
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const marka = this.createFromForm();
    if (marka.id !== undefined) {
      this.subscribeToSaveResponse(this.markaService.update(marka));
    } else {
      this.subscribeToSaveResponse(this.markaService.create(marka));
    }
  }

  private createFromForm(): IMarka {
    return {
      ...new Marka(),
      id: this.editForm.get(['id']).value,
      ad: this.editForm.get(['ad']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarka>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}

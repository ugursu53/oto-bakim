import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IIscilikGrubu, IscilikGrubu } from 'app/shared/model/iscilik-grubu.model';
import { IscilikGrubuService } from './iscilik-grubu.service';

@Component({
  selector: 'jhi-iscilik-grubu-update',
  templateUrl: './iscilik-grubu-update.component.html'
})
export class IscilikGrubuUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    ad: []
  });

  constructor(protected iscilikGrubuService: IscilikGrubuService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ iscilikGrubu }) => {
      this.updateForm(iscilikGrubu);
    });
  }

  updateForm(iscilikGrubu: IIscilikGrubu) {
    this.editForm.patchValue({
      id: iscilikGrubu.id,
      ad: iscilikGrubu.ad
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const iscilikGrubu = this.createFromForm();
    if (iscilikGrubu.id !== undefined) {
      this.subscribeToSaveResponse(this.iscilikGrubuService.update(iscilikGrubu));
    } else {
      this.subscribeToSaveResponse(this.iscilikGrubuService.create(iscilikGrubu));
    }
  }

  private createFromForm(): IIscilikGrubu {
    return {
      ...new IscilikGrubu(),
      id: this.editForm.get(['id']).value,
      ad: this.editForm.get(['ad']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIscilikGrubu>>) {
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

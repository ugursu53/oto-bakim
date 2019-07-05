import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IHesap, Hesap } from 'app/shared/model/hesap.model';
import { HesapService } from './hesap.service';

@Component({
  selector: 'jhi-hesap-update',
  templateUrl: './hesap-update.component.html'
})
export class HesapUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    banka: [],
    sube: [],
    hesapNo: [],
    iban: []
  });

  constructor(protected hesapService: HesapService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ hesap }) => {
      this.updateForm(hesap);
    });
  }

  updateForm(hesap: IHesap) {
    this.editForm.patchValue({
      id: hesap.id,
      banka: hesap.banka,
      sube: hesap.sube,
      hesapNo: hesap.hesapNo,
      iban: hesap.iban
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const hesap = this.createFromForm();
    if (hesap.id !== undefined) {
      this.subscribeToSaveResponse(this.hesapService.update(hesap));
    } else {
      this.subscribeToSaveResponse(this.hesapService.create(hesap));
    }
  }

  private createFromForm(): IHesap {
    return {
      ...new Hesap(),
      id: this.editForm.get(['id']).value,
      banka: this.editForm.get(['banka']).value,
      sube: this.editForm.get(['sube']).value,
      hesapNo: this.editForm.get(['hesapNo']).value,
      iban: this.editForm.get(['iban']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHesap>>) {
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

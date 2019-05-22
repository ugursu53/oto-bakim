import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPersonel, Personel } from 'app/shared/model/personel.model';
import { PersonelService } from './personel.service';

@Component({
  selector: 'jhi-personel-update',
  templateUrl: './personel-update.component.html'
})
export class PersonelUpdateComponent implements OnInit {
  personel: IPersonel;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    ad: [null, [Validators.required]],
    soyad: [null, [Validators.required]],
    gorevi: [null, [Validators.required]]
  });

  constructor(protected personelService: PersonelService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ personel }) => {
      this.updateForm(personel);
      this.personel = personel;
    });
  }

  updateForm(personel: IPersonel) {
    this.editForm.patchValue({
      id: personel.id,
      ad: personel.ad,
      soyad: personel.soyad,
      gorevi: personel.gorevi
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const personel = this.createFromForm();
    if (personel.id !== undefined) {
      this.subscribeToSaveResponse(this.personelService.update(personel));
    } else {
      this.subscribeToSaveResponse(this.personelService.create(personel));
    }
  }

  private createFromForm(): IPersonel {
    const entity = {
      ...new Personel(),
      id: this.editForm.get(['id']).value,
      ad: this.editForm.get(['ad']).value,
      soyad: this.editForm.get(['soyad']).value,
      gorevi: this.editForm.get(['gorevi']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonel>>) {
    result.subscribe((res: HttpResponse<IPersonel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}

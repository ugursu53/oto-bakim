import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IParcaTipi, ParcaTipi } from 'app/shared/model/parca-tipi.model';
import { ParcaTipiService } from './parca-tipi.service';

@Component({
  selector: 'jhi-parca-tipi-update',
  templateUrl: './parca-tipi-update.component.html'
})
export class ParcaTipiUpdateComponent implements OnInit {
  parcaTipi: IParcaTipi;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    ad: [null, [Validators.required]],
    kodu: [],
    varsayilanFiyati: [],
    aciklama: []
  });

  constructor(protected parcaTipiService: ParcaTipiService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ parcaTipi }) => {
      this.updateForm(parcaTipi);
      this.parcaTipi = parcaTipi;
    });
  }

  updateForm(parcaTipi: IParcaTipi) {
    this.editForm.patchValue({
      id: parcaTipi.id,
      ad: parcaTipi.ad,
      kodu: parcaTipi.kodu,
      varsayilanFiyati: parcaTipi.varsayilanFiyati,
      aciklama: parcaTipi.aciklama
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const parcaTipi = this.createFromForm();
    if (parcaTipi.id !== undefined) {
      this.subscribeToSaveResponse(this.parcaTipiService.update(parcaTipi));
    } else {
      this.subscribeToSaveResponse(this.parcaTipiService.create(parcaTipi));
    }
  }

  private createFromForm(): IParcaTipi {
    const entity = {
      ...new ParcaTipi(),
      id: this.editForm.get(['id']).value,
      ad: this.editForm.get(['ad']).value,
      kodu: this.editForm.get(['kodu']).value,
      varsayilanFiyati: this.editForm.get(['varsayilanFiyati']).value,
      aciklama: this.editForm.get(['aciklama']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParcaTipi>>) {
    result.subscribe((res: HttpResponse<IParcaTipi>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}

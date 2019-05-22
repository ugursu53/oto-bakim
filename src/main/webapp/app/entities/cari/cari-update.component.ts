import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICari, Cari } from 'app/shared/model/cari.model';
import { CariService } from './cari.service';
import { IHesap } from 'app/shared/model/hesap.model';
import { HesapService } from 'app/entities/hesap';

@Component({
  selector: 'jhi-cari-update',
  templateUrl: './cari-update.component.html'
})
export class CariUpdateComponent implements OnInit {
  cari: ICari;
  isSaving: boolean;

  hesaps: IHesap[];

  editForm = this.fb.group({
    id: [],
    tipi: [null, [Validators.required]],
    kisiTipi: [null, [Validators.required]],
    aktif: [null, [Validators.required]],
    ad: [null, [Validators.required]],
    adres: [],
    telefon: [],
    tcNo: [],
    vergiNo: [],
    yetkili: [],
    fax: [],
    eposta: [],
    webAdresi: [],
    iskonto: [],
    efaturaKullanimi: [],
    aciklama: [],
    varsayilanIsEmriTipi: [],
    hesap: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cariService: CariService,
    protected hesapService: HesapService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ cari }) => {
      this.updateForm(cari);
      this.cari = cari;
    });
    this.hesapService
      .query({ filter: 'cari-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IHesap[]>) => mayBeOk.ok),
        map((response: HttpResponse<IHesap[]>) => response.body)
      )
      .subscribe(
        (res: IHesap[]) => {
          if (!this.cari.hesap || !this.cari.hesap.id) {
            this.hesaps = res;
          } else {
            this.hesapService
              .find(this.cari.hesap.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IHesap>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IHesap>) => subResponse.body)
              )
              .subscribe(
                (subRes: IHesap) => (this.hesaps = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(cari: ICari) {
    this.editForm.patchValue({
      id: cari.id,
      tipi: cari.tipi,
      kisiTipi: cari.kisiTipi,
      aktif: cari.aktif,
      ad: cari.ad,
      adres: cari.adres,
      telefon: cari.telefon,
      tcNo: cari.tcNo,
      vergiNo: cari.vergiNo,
      yetkili: cari.yetkili,
      fax: cari.fax,
      eposta: cari.eposta,
      webAdresi: cari.webAdresi,
      iskonto: cari.iskonto,
      efaturaKullanimi: cari.efaturaKullanimi,
      aciklama: cari.aciklama,
      varsayilanIsEmriTipi: cari.varsayilanIsEmriTipi,
      hesap: cari.hesap
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const cari = this.createFromForm();
    if (cari.id !== undefined) {
      this.subscribeToSaveResponse(this.cariService.update(cari));
    } else {
      this.subscribeToSaveResponse(this.cariService.create(cari));
    }
  }

  private createFromForm(): ICari {
    const entity = {
      ...new Cari(),
      id: this.editForm.get(['id']).value,
      tipi: this.editForm.get(['tipi']).value,
      kisiTipi: this.editForm.get(['kisiTipi']).value,
      aktif: this.editForm.get(['aktif']).value,
      ad: this.editForm.get(['ad']).value,
      adres: this.editForm.get(['adres']).value,
      telefon: this.editForm.get(['telefon']).value,
      tcNo: this.editForm.get(['tcNo']).value,
      vergiNo: this.editForm.get(['vergiNo']).value,
      yetkili: this.editForm.get(['yetkili']).value,
      fax: this.editForm.get(['fax']).value,
      eposta: this.editForm.get(['eposta']).value,
      webAdresi: this.editForm.get(['webAdresi']).value,
      iskonto: this.editForm.get(['iskonto']).value,
      efaturaKullanimi: this.editForm.get(['efaturaKullanimi']).value,
      aciklama: this.editForm.get(['aciklama']).value,
      varsayilanIsEmriTipi: this.editForm.get(['varsayilanIsEmriTipi']).value,
      hesap: this.editForm.get(['hesap']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICari>>) {
    result.subscribe((res: HttpResponse<ICari>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackHesapById(index: number, item: IHesap) {
    return item.id;
  }
}

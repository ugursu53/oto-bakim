import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICari, Cari, CariTipi } from 'app/shared/model/cari.model';
import { CariService } from './cari.service';
import { Hesap, IHesap } from 'app/shared/model/hesap.model';

@Component({
  selector: 'jhi-cari-update',
  templateUrl: './cari-update.component.html'
})
export class CariUpdateComponent implements OnInit {
  isSaving: boolean;
  @Input() isAltComponent: boolean;
  @Output() saved = new EventEmitter();
  @Input() searchExist: boolean;
  currentSearch: string;
  caris: ICari[];

  editForm = this.fb.group<>({
    id: [],
    tipi: [null, [Validators.required]],
    kisiTipi: [null, [Validators.required]],
    aktif: [true, [Validators.required]],
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
    hesapId: [],
    banka: [],
    sube: [],
    hesapNo: [],
    iban: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected cariService: CariService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    if (this.isAltComponent) {
      const cari = new Cari();
      cari.tipi = CariTipi.MUSTERI;
      cari.aktif = true;
      this.updateForm(cari);
    } else {
      this.activatedRoute.data.subscribe(({ cari }) => {
        this.updateForm(cari);
      });
    }
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
      hesapId: cari.hesap === undefined ? null : cari.hesap.id,
      banka: cari.hesap === undefined ? null : cari.hesap.banka,
      sube: cari.hesap === undefined ? null : cari.hesap.sube,
      hesapNo: cari.hesap === undefined ? null : cari.hesap.hesapNo,
      iban: cari.hesap === undefined ? null : cari.hesap.iban
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
    return {
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
      hesap: this.createHesapFromForm()
    };
  }

  private createHesapFromForm(): IHesap {
    return {
      ...new Hesap(),
      id: this.editForm.get(['hesapId']).value,
      banka: this.editForm.get(['banka']).value,
      sube: this.editForm.get(['sube']).value,
      hesapNo: this.editForm.get(['hesapNo']).value,
      iban: this.editForm.get(['iban']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICari>>) {
    result.subscribe(res => this.onSaveSuccess(res.body), () => this.onSaveError());
  }

  protected onSaveSuccess(savedCari: ICari) {
    this.isSaving = false;
    if (this.isAltComponent) {
      this.editForm.patchValue({ id: savedCari.id });
      this.saved.emit(savedCari);
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

  trackHesapById(index: number, item: IHesap) {
    return item.id;
  }

  search(query) {
    this.currentSearch = query;
    if (this.currentSearch) {
      this.cariService
        .search({
          page: 0,
          query: this.currentSearch,
          size: 10
        })
        .subscribe(
          (res: HttpResponse<ICari[]>) => this.paginateCaris(res.body, res.headers),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
  }

  protected paginateCaris(data: ICari[], headers: HttpHeaders) {
    this.caris = data;
  }

  selected(cari: ICari) {
    this.updateForm(cari);
  }
}

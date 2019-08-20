import { Component, Input, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IIsEmri, IsEmri } from 'app/shared/model/is-emri.model';
import { IsEmriService } from './is-emri.service';
import { IArac } from 'app/shared/model/arac.model';
import { IParca } from 'app/shared/model/parca.model';
import { IIscilik } from 'app/shared/model/iscilik.model';
import { IModel } from 'app/shared/model/model.model';
import { IParcaTipi } from 'app/shared/model/parca-tipi.model';
import { ParcaTipiService } from 'app/entities/parca-tipi';
import { IscilikTipiService } from 'app/entities/iscilik-tipi';
import { IIscilikTipi } from 'app/shared/model/iscilik-tipi.model';
import { IPersonel } from 'app/shared/model/personel.model';
import { filter, map } from 'rxjs/operators';
import { PersonelService } from 'app/entities/personel';

@Component({
  selector: 'jhi-is-emri-update',
  templateUrl: './is-emri-update.component.html'
})
export class IsEmriUpdateComponent implements OnInit {
  isSaving: boolean;
  @Input() arac: IArac;

  quering: boolean;
  newOne: boolean;
  query: string;
  isciliks: IIscilik[];
  selectedIscilik: IIscilik;
  iscilik: IIscilik;
  filteredIscilikTipis: IIscilikTipi[];
  iscilikDialogDisplay: boolean;
  iscilikTipiDialogDisplay: boolean;
  personels: IPersonel[];

  parcas: IParca[];
  selectedParca: IParca;
  parca: IParca = {};
  filteredParcaTipis: IParcaTipi[];
  parcaDialogDisplay: boolean;
  parcaTipiDialogDisplay: boolean;

  editForm = this.fb.group({
    id: [],
    gelisZamani: [],
    aciklama: [null, [Validators.required]],
    kabulTarihi: [null, [Validators.required]],
    tipi: [null, [Validators.required]],
    arac: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected isEmriService: IsEmriService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private parcaTipiService: ParcaTipiService,
    private iscilikTipiService: IscilikTipiService,
    private personelService: PersonelService
  ) {}

  ngOnInit() {
    this.isSaving = false;
    if (this.arac === undefined) {
      this.activatedRoute.data.subscribe(({ isEmri }) => {
        this.updateForm(isEmri);
      });
    } else {
      const isEmri = new IsEmri();
      isEmri.arac = this.arac;
      this.updateForm(isEmri);
    }
    this.personelService
      .query({ size: 20, sort: ['ad,asc'] })
      .pipe(
        filter((mayBeOk: HttpResponse<IPersonel[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPersonel[]>) => response.body)
      )
      .subscribe((res: IPersonel[]) => (this.personels = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(isEmri: IIsEmri) {
    this.parcas = isEmri.parcas;
    this.isciliks = isEmri.isciliks;

    this.editForm.patchValue({
      id: isEmri.id,
      gelisZamani: isEmri.gelisZamani !== null ? isEmri.gelisZamani.format(DATE_TIME_FORMAT) : null,
      aciklama: isEmri.aciklama,
      kabulTarihi: isEmri.kabulTarihi !== null ? isEmri.kabulTarihi.format(DATE_TIME_FORMAT) : null,
      tipi: isEmri.tipi ? isEmri.tipi : isEmri.arac && isEmri.arac.aktifCari ? isEmri.arac.aktifCari.varsayilanIsEmriTipi : null,
      arac: isEmri.arac
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const isEmri = this.createFromForm();
    if (isEmri.id !== undefined) {
      this.subscribeToSaveResponse(this.isEmriService.update(isEmri));
    } else {
      this.subscribeToSaveResponse(this.isEmriService.create(isEmri));
    }
  }

  private createFromForm(): IIsEmri {
    return {
      ...new IsEmri(),
      id: this.editForm.get(['id']).value,
      gelisZamani:
        this.editForm.get(['gelisZamani']).value !== null ? moment(this.editForm.get(['gelisZamani']).value, DATE_TIME_FORMAT) : undefined,
      aciklama: this.editForm.get(['aciklama']).value,
      kabulTarihi:
        this.editForm.get(['kabulTarihi']).value !== null ? moment(this.editForm.get(['kabulTarihi']).value, DATE_TIME_FORMAT) : undefined,
      tipi: this.editForm.get(['tipi']).value,
      arac: this.editForm.get(['arac']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsEmri>>) {
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

  // parca işlem baş

  onParcaRowSelect(event) {
    this.newOne = false;
    this.parca = this.cloneParca(event.data);
    this.parcaDialogDisplay = true;
  }

  showParcaDialogToAdd() {
    this.newOne = true;
    this.parca = {};
    this.parcaDialogDisplay = true;
  }

  saveParca() {
    if (!this.parca.tipi || !this.parca.tipi.id) {
      this.onError('otoBakimApp.parca.error.tipi');
      return;
    }
    if (!this.parca.fiyati) {
      this.onError('otoBakimApp.parca.error.fiyati');
      return;
    }

    const parcas = [...this.parcas];
    if (this.newOne) {
      parcas.push(this.parca);
    } else {
      parcas[this.parcas.indexOf(this.selectedParca)] = this.parca;
    }

    this.parcas = parcas;
    this.parca = null;
    this.parcaDialogDisplay = false;
  }

  deleteParca() {
    const index = this.parcas.indexOf(this.selectedParca);
    this.parcas = this.parcas.filter((val, i) => i !== index);
    this.parca = null;
    this.parcaDialogDisplay = false;
  }

  cloneParca(a: any): any {
    const entity = {};
    for (const prop in a) {
      entity[prop] = a[prop];
    }
    return entity;
  }

  filterParcas(event) {
    this.quering = true;
    this.query = event.query;
    this.parcaTipiService
      .search({
        page: 0,
        query: event.query,
        size: 10
      })
      .subscribe(
        (res: HttpResponse<IModel[]>) => {
          this.filteredParcaTipis = res.body;
          this.quering = false;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  isNewParcaButtonRendered(parcaTipi: any): boolean {
    return parcaTipi && !parcaTipi.id && (!this.filteredParcaTipis || this.filteredParcaTipis.length === 0) && !this.quering;
  }

  showParcaTipiDialog() {
    this.parcaTipiDialogDisplay = true;
  }

  parcaTipiSaved(parcaTipi: IParcaTipi) {
    this.parca.tipi = parcaTipi;
    this.parcaTipiSelect(parcaTipi);
    this.parcaTipiDialogDisplay = false;
  }

  parcaTipiSelect(parcaTipi: IParcaTipi) {
    this.parca.fiyati = parcaTipi.varsayilanFiyati;
  }

  // parca işlem bitiş

  // iscilik Islem baş

  onIscilikRowSelect(event) {
    this.newOne = false;
    this.iscilik = this.cloneIscilik(event.data);
    this.iscilikDialogDisplay = true;
  }

  showIscilikDialogToAdd() {
    this.newOne = true;
    this.iscilik = {};
    this.iscilikDialogDisplay = true;
  }

  saveIscilik() {
    if (!this.iscilik.tipi || !this.iscilik.tipi.id) {
      this.onError('otoBakimApp.iscilik.error.tipi');
      return;
    }
    if (!this.iscilik.fiyat) {
      this.onError('otoBakimApp.iscilik.error.fiyati');
      return;
    }

    const isciliks = [...this.isciliks];
    if (this.newOne) {
      isciliks.push(this.iscilik);
    } else {
      isciliks[this.isciliks.indexOf(this.selectedIscilik)] = this.iscilik;
    }

    this.isciliks = isciliks;
    this.iscilik = null;
    this.iscilikDialogDisplay = false;
  }

  deleteIscilik() {
    const index = this.isciliks.indexOf(this.selectedIscilik);
    this.isciliks = this.isciliks.filter((val, i) => i !== index);
    this.iscilik = null;
    this.iscilikDialogDisplay = false;
  }

  cloneIscilik(a: any): any {
    const entity = {};
    for (const prop in a) {
      entity[prop] = a[prop];
    }
    return entity;
  }

  filterIsciliks(event) {
    this.quering = true;
    this.query = event.query;
    this.iscilikTipiService
      .search({
        page: 0,
        query: event.query,
        size: 10
      })
      .subscribe(
        (res: HttpResponse<IModel[]>) => {
          this.filteredIscilikTipis = res.body;
          this.quering = false;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  isNewIscilikButtonRendered(iscilikTipi: any): boolean {
    return iscilikTipi && !iscilikTipi.id && (!this.filteredIscilikTipis || this.filteredIscilikTipis.length === 0) && !this.quering;
  }

  showIscilikTipiDialog() {
    this.iscilikTipiDialogDisplay = true;
  }

  iscilikTipiSaved(iscilikTipi: IIscilikTipi) {
    this.iscilik.tipi = iscilikTipi;
    this.iscilikTipiSelect(iscilikTipi);
    this.iscilikTipiDialogDisplay = false;
  }

  iscilikTipiSelect(iscilikTipi: IIscilikTipi) {
    this.iscilik.fiyat = iscilikTipi.varsayilanFiyat;
  }
}

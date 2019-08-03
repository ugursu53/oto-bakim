import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { IArac } from 'app/shared/model/arac.model';
import { ICari } from 'app/shared/model/cari.model';
import { AracCarisiService } from 'app/entities/arac-carisi/arac-carisi.service';
import { AracCarisi, IAracCarisi } from 'app/shared/model/arac-carisi.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/index';

@Component({
  selector: 'jhi-arac-kabul',
  templateUrl: './arackabul.component.html'
})
export class ArackabulComponent implements OnInit {
  items: MenuItem[];
  activeIndex: number;
  arac: IArac;

  constructor(protected aracCarisiService: AracCarisiService) {}

  ngOnInit() {
    this.activeIndex = 0;
    this.items = [{ label: 'Araç' }, { label: 'Cari' }, { label: 'İş Emri' }];
  }

  aracSaved(arac: IArac) {
    this.activeIndex = 1;
    this.arac = arac;
  }

  cariSaved(cari: ICari) {
    const aracCarisi = {
      ...new AracCarisi(),
      id: null,
      aktif: true,
      arac: this.arac,
      cari: cari
    };
    this.subscribeToSaveResponse(this.aracCarisiService.create(aracCarisi));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAracCarisi>>) {
    result.subscribe(res => this.onSaveSuccess());
  }

  protected onSaveSuccess() {
    this.activeIndex = 2;
  }
}

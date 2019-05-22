import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICari } from 'app/shared/model/cari.model';

@Component({
  selector: 'jhi-cari-detail',
  templateUrl: './cari-detail.component.html'
})
export class CariDetailComponent implements OnInit {
  cari: ICari;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ cari }) => {
      this.cari = cari;
    });
  }

  previousState() {
    window.history.back();
  }
}

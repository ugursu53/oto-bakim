import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIscilikTipi } from 'app/shared/model/iscilik-tipi.model';

@Component({
  selector: 'jhi-iscilik-tipi-detail',
  templateUrl: './iscilik-tipi-detail.component.html'
})
export class IscilikTipiDetailComponent implements OnInit {
  iscilikTipi: IIscilikTipi;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ iscilikTipi }) => {
      this.iscilikTipi = iscilikTipi;
    });
  }

  previousState() {
    window.history.back();
  }
}

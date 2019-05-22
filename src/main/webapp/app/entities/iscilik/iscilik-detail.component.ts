import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIscilik } from 'app/shared/model/iscilik.model';

@Component({
  selector: 'jhi-iscilik-detail',
  templateUrl: './iscilik-detail.component.html'
})
export class IscilikDetailComponent implements OnInit {
  iscilik: IIscilik;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ iscilik }) => {
      this.iscilik = iscilik;
    });
  }

  previousState() {
    window.history.back();
  }
}

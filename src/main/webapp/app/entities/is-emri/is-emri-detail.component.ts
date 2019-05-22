import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsEmri } from 'app/shared/model/is-emri.model';

@Component({
  selector: 'jhi-is-emri-detail',
  templateUrl: './is-emri-detail.component.html'
})
export class IsEmriDetailComponent implements OnInit {
  isEmri: IIsEmri;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ isEmri }) => {
      this.isEmri = isEmri;
    });
  }

  previousState() {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAracCarisi } from 'app/shared/model/arac-carisi.model';

@Component({
  selector: 'jhi-arac-carisi-detail',
  templateUrl: './arac-carisi-detail.component.html'
})
export class AracCarisiDetailComponent implements OnInit {
  aracCarisi: IAracCarisi;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ aracCarisi }) => {
      this.aracCarisi = aracCarisi;
    });
  }

  previousState() {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParca } from 'app/shared/model/parca.model';

@Component({
  selector: 'jhi-parca-detail',
  templateUrl: './parca-detail.component.html'
})
export class ParcaDetailComponent implements OnInit {
  parca: IParca;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ parca }) => {
      this.parca = parca;
    });
  }

  previousState() {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParcaTipi } from 'app/shared/model/parca-tipi.model';

@Component({
  selector: 'jhi-parca-tipi-detail',
  templateUrl: './parca-tipi-detail.component.html'
})
export class ParcaTipiDetailComponent implements OnInit {
  parcaTipi: IParcaTipi;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ parcaTipi }) => {
      this.parcaTipi = parcaTipi;
    });
  }

  previousState() {
    window.history.back();
  }
}

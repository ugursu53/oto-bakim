import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHesap } from 'app/shared/model/hesap.model';

@Component({
  selector: 'jhi-hesap-detail',
  templateUrl: './hesap-detail.component.html'
})
export class HesapDetailComponent implements OnInit {
  hesap: IHesap;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ hesap }) => {
      this.hesap = hesap;
    });
  }

  previousState() {
    window.history.back();
  }
}

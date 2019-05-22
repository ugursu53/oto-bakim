import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonel } from 'app/shared/model/personel.model';

@Component({
  selector: 'jhi-personel-detail',
  templateUrl: './personel-detail.component.html'
})
export class PersonelDetailComponent implements OnInit {
  personel: IPersonel;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ personel }) => {
      this.personel = personel;
    });
  }

  previousState() {
    window.history.back();
  }
}

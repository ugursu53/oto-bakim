import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArac } from 'app/shared/model/arac.model';

@Component({
  selector: 'jhi-arac-detail',
  templateUrl: './arac-detail.component.html'
})
export class AracDetailComponent implements OnInit {
  arac: IArac;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ arac }) => {
      this.arac = arac;
    });
  }

  previousState() {
    window.history.back();
  }
}

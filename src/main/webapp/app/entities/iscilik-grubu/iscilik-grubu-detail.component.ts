import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIscilikGrubu } from 'app/shared/model/iscilik-grubu.model';

@Component({
  selector: 'jhi-iscilik-grubu-detail',
  templateUrl: './iscilik-grubu-detail.component.html'
})
export class IscilikGrubuDetailComponent implements OnInit {
  iscilikGrubu: IIscilikGrubu;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ iscilikGrubu }) => {
      this.iscilikGrubu = iscilikGrubu;
    });
  }

  previousState() {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { IArac } from 'app/shared/model/arac.model';

@Component({
  selector: 'jhi-arac-kabul',
  templateUrl: './arackabul.component.html'
})
export class ArackabulComponent implements OnInit {
  items: MenuItem[];
  activeIndex: number;
  arac: IArac;

  constructor() {}

  ngOnInit() {
    this.activeIndex = 0;
    this.items = [{ label: 'Araç' }, { label: 'Cari' }, { label: 'İş Emri' }];
  }

  aracSaved(arac: IArac) {
    this.activeIndex = 1;
    this.arac = arac;
  }
}

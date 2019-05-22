import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  ParcaComponent,
  ParcaDetailComponent,
  ParcaUpdateComponent,
  ParcaDeletePopupComponent,
  ParcaDeleteDialogComponent,
  parcaRoute,
  parcaPopupRoute
} from './';

const ENTITY_STATES = [...parcaRoute, ...parcaPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ParcaComponent, ParcaDetailComponent, ParcaUpdateComponent, ParcaDeleteDialogComponent, ParcaDeletePopupComponent],
  entryComponents: [ParcaComponent, ParcaUpdateComponent, ParcaDeleteDialogComponent, ParcaDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimParcaModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

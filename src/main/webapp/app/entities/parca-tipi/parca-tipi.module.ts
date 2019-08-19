import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  ParcaTipiComponent,
  ParcaTipiDetailComponent,
  ParcaTipiDeletePopupComponent,
  ParcaTipiDeleteDialogComponent,
  parcaTipiRoute,
  parcaTipiPopupRoute
} from './';

const ENTITY_STATES = [...parcaTipiRoute, ...parcaTipiPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ParcaTipiComponent, ParcaTipiDetailComponent, ParcaTipiDeleteDialogComponent, ParcaTipiDeletePopupComponent],
  entryComponents: [ParcaTipiComponent, ParcaTipiDeleteDialogComponent, ParcaTipiDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimParcaTipiModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

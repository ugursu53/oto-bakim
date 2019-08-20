import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  IscilikTipiComponent,
  IscilikTipiDetailComponent,
  IscilikTipiDeletePopupComponent,
  IscilikTipiDeleteDialogComponent,
  iscilikTipiRoute,
  iscilikTipiPopupRoute
} from './';

const ENTITY_STATES = [...iscilikTipiRoute, ...iscilikTipiPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [IscilikTipiComponent, IscilikTipiDetailComponent, IscilikTipiDeleteDialogComponent, IscilikTipiDeletePopupComponent],
  entryComponents: [IscilikTipiComponent, IscilikTipiDeleteDialogComponent, IscilikTipiDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimIscilikTipiModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

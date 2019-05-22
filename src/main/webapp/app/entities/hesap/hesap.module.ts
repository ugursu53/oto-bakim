import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  HesapComponent,
  HesapDetailComponent,
  HesapUpdateComponent,
  HesapDeletePopupComponent,
  HesapDeleteDialogComponent,
  hesapRoute,
  hesapPopupRoute
} from './';

const ENTITY_STATES = [...hesapRoute, ...hesapPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [HesapComponent, HesapDetailComponent, HesapUpdateComponent, HesapDeleteDialogComponent, HesapDeletePopupComponent],
  entryComponents: [HesapComponent, HesapUpdateComponent, HesapDeleteDialogComponent, HesapDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimHesapModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

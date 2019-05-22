import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  MarkaComponent,
  MarkaDetailComponent,
  MarkaUpdateComponent,
  MarkaDeletePopupComponent,
  MarkaDeleteDialogComponent,
  markaRoute,
  markaPopupRoute
} from './';

const ENTITY_STATES = [...markaRoute, ...markaPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [MarkaComponent, MarkaDetailComponent, MarkaUpdateComponent, MarkaDeleteDialogComponent, MarkaDeletePopupComponent],
  entryComponents: [MarkaComponent, MarkaUpdateComponent, MarkaDeleteDialogComponent, MarkaDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimMarkaModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

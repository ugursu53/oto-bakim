import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  IsEmriComponent,
  IsEmriDetailComponent,
  IsEmriDeletePopupComponent,
  IsEmriDeleteDialogComponent,
  isEmriRoute,
  isEmriPopupRoute
} from './';

const ENTITY_STATES = [...isEmriRoute, ...isEmriPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [IsEmriComponent, IsEmriDetailComponent, IsEmriDeleteDialogComponent, IsEmriDeletePopupComponent],
  entryComponents: [IsEmriComponent, IsEmriDeleteDialogComponent, IsEmriDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimIsEmriModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

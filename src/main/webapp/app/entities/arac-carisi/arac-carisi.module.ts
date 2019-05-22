import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  AracCarisiComponent,
  AracCarisiDetailComponent,
  AracCarisiUpdateComponent,
  AracCarisiDeletePopupComponent,
  AracCarisiDeleteDialogComponent,
  aracCarisiRoute,
  aracCarisiPopupRoute
} from './';

const ENTITY_STATES = [...aracCarisiRoute, ...aracCarisiPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    AracCarisiComponent,
    AracCarisiDetailComponent,
    AracCarisiUpdateComponent,
    AracCarisiDeleteDialogComponent,
    AracCarisiDeletePopupComponent
  ],
  entryComponents: [AracCarisiComponent, AracCarisiUpdateComponent, AracCarisiDeleteDialogComponent, AracCarisiDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimAracCarisiModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

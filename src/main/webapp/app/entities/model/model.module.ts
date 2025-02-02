import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  ModelComponent,
  ModelDetailComponent,
  ModelDeletePopupComponent,
  ModelDeleteDialogComponent,
  modelRoute,
  modelPopupRoute
} from './';

const ENTITY_STATES = [...modelRoute, ...modelPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ModelComponent, ModelDetailComponent, ModelDeleteDialogComponent, ModelDeletePopupComponent],
  entryComponents: [ModelComponent, ModelDeleteDialogComponent, ModelDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimModelModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

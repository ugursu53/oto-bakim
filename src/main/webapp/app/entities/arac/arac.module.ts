import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  AracComponent,
  AracDetailComponent,
  AracUpdateComponent,
  AracDeletePopupComponent,
  AracDeleteDialogComponent,
  aracRoute,
  aracPopupRoute
} from './';

const ENTITY_STATES = [...aracRoute, ...aracPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [AracComponent, AracDetailComponent, AracUpdateComponent, AracDeleteDialogComponent, AracDeletePopupComponent],
  entryComponents: [AracComponent, AracUpdateComponent, AracDeleteDialogComponent, AracDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimAracModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

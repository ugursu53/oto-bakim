import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  IscilikGrubuComponent,
  IscilikGrubuDetailComponent,
  IscilikGrubuUpdateComponent,
  IscilikGrubuDeletePopupComponent,
  IscilikGrubuDeleteDialogComponent,
  iscilikGrubuRoute,
  iscilikGrubuPopupRoute
} from './';

const ENTITY_STATES = [...iscilikGrubuRoute, ...iscilikGrubuPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IscilikGrubuComponent,
    IscilikGrubuDetailComponent,
    IscilikGrubuUpdateComponent,
    IscilikGrubuDeleteDialogComponent,
    IscilikGrubuDeletePopupComponent
  ],
  entryComponents: [
    IscilikGrubuComponent,
    IscilikGrubuUpdateComponent,
    IscilikGrubuDeleteDialogComponent,
    IscilikGrubuDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimIscilikGrubuModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

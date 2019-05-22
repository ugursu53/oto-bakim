import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { OtoBakimSharedModule } from 'app/shared';
import {
  IscilikComponent,
  IscilikDetailComponent,
  IscilikUpdateComponent,
  IscilikDeletePopupComponent,
  IscilikDeleteDialogComponent,
  iscilikRoute,
  iscilikPopupRoute
} from './';

const ENTITY_STATES = [...iscilikRoute, ...iscilikPopupRoute];

@NgModule({
  imports: [OtoBakimSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IscilikComponent,
    IscilikDetailComponent,
    IscilikUpdateComponent,
    IscilikDeleteDialogComponent,
    IscilikDeletePopupComponent
  ],
  entryComponents: [IscilikComponent, IscilikUpdateComponent, IscilikDeleteDialogComponent, IscilikDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimIscilikModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

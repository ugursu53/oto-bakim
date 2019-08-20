import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { OtoBakimSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { ModelUpdateComponent } from 'app/entities/model';
import { CariUpdateComponent } from 'app/entities/cari';
import { IsEmriUpdateComponent } from 'app/entities/is-emri';
import { ParcaTipiUpdateComponent } from 'app/entities/parca-tipi';
import { IscilikTipiUpdateComponent } from 'app/entities/iscilik-tipi';

@NgModule({
  imports: [OtoBakimSharedCommonModule],
  declarations: [
    JhiLoginModalComponent,
    HasAnyAuthorityDirective,
    ModelUpdateComponent,
    CariUpdateComponent,
    IsEmriUpdateComponent,
    ParcaTipiUpdateComponent,
    IscilikTipiUpdateComponent
  ],
  entryComponents: [JhiLoginModalComponent],
  exports: [
    OtoBakimSharedCommonModule,
    JhiLoginModalComponent,
    HasAnyAuthorityDirective,
    ModelUpdateComponent,
    CariUpdateComponent,
    IsEmriUpdateComponent,
    ParcaTipiUpdateComponent,
    IscilikTipiUpdateComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimSharedModule {
  static forRoot() {
    return {
      ngModule: OtoBakimSharedModule
    };
  }
}

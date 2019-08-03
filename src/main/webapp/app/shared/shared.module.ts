import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { OtoBakimSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { ModelUpdateComponent } from 'app/entities/model';
import { CariUpdateComponent } from 'app/entities/cari';

@NgModule({
  imports: [OtoBakimSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, ModelUpdateComponent, CariUpdateComponent],
  entryComponents: [JhiLoginModalComponent],
  exports: [OtoBakimSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, ModelUpdateComponent, CariUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimSharedModule {
  static forRoot() {
    return {
      ngModule: OtoBakimSharedModule
    };
  }
}

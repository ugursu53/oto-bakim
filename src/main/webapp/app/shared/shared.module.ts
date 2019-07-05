import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { OtoBakimSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [OtoBakimSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [OtoBakimSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimSharedModule {
  static forRoot() {
    return {
      ngModule: OtoBakimSharedModule
    };
  }
}

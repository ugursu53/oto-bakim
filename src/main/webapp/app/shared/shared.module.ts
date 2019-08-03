import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { OtoBakimSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { ModelUpdateComponent } from 'app/entities/model';

@NgModule({
  imports: [OtoBakimSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, ModelUpdateComponent],
  entryComponents: [JhiLoginModalComponent],
  exports: [OtoBakimSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, ModelUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimSharedModule {
  static forRoot() {
    return {
      ngModule: OtoBakimSharedModule
    };
  }
}

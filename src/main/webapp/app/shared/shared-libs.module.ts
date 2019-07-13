import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { CookieModule } from 'ngx-cookie';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FieldsetModule, CheckboxModule } from 'primeng/primeng';

@NgModule({
  imports: [NgbModule, InfiniteScrollModule, CookieModule.forRoot(), FontAwesomeModule, ReactiveFormsModule],
  exports: [
    FormsModule,
    CommonModule,
    NgbModule,
    NgJhipsterModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    FieldsetModule,
    CheckboxModule
  ]
})
export class OtoBakimSharedLibsModule {
  static forRoot() {
    return {
      ngModule: OtoBakimSharedLibsModule
    };
  }
}

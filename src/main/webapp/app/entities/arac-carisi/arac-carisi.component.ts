import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAracCarisi } from 'app/shared/model/arac-carisi.model';
import { AccountService } from 'app/core';
import { AracCarisiService } from './arac-carisi.service';

@Component({
  selector: 'jhi-arac-carisi',
  templateUrl: './arac-carisi.component.html'
})
export class AracCarisiComponent implements OnInit, OnDestroy {
  aracCarisis: IAracCarisi[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected aracCarisiService: AracCarisiService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.aracCarisiService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IAracCarisi[]>) => res.ok),
          map((res: HttpResponse<IAracCarisi[]>) => res.body)
        )
        .subscribe((res: IAracCarisi[]) => (this.aracCarisis = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.aracCarisiService
      .query()
      .pipe(
        filter((res: HttpResponse<IAracCarisi[]>) => res.ok),
        map((res: HttpResponse<IAracCarisi[]>) => res.body)
      )
      .subscribe(
        (res: IAracCarisi[]) => {
          this.aracCarisis = res;
          this.currentSearch = '';
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInAracCarisis();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IAracCarisi) {
    return item.id;
  }

  registerChangeInAracCarisis() {
    this.eventSubscriber = this.eventManager.subscribe('aracCarisiListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}

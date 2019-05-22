import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IHesap } from 'app/shared/model/hesap.model';
import { AccountService } from 'app/core';
import { HesapService } from './hesap.service';

@Component({
  selector: 'jhi-hesap',
  templateUrl: './hesap.component.html'
})
export class HesapComponent implements OnInit, OnDestroy {
  hesaps: IHesap[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected hesapService: HesapService,
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
      this.hesapService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IHesap[]>) => res.ok),
          map((res: HttpResponse<IHesap[]>) => res.body)
        )
        .subscribe((res: IHesap[]) => (this.hesaps = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.hesapService
      .query()
      .pipe(
        filter((res: HttpResponse<IHesap[]>) => res.ok),
        map((res: HttpResponse<IHesap[]>) => res.body)
      )
      .subscribe(
        (res: IHesap[]) => {
          this.hesaps = res;
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
    this.registerChangeInHesaps();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IHesap) {
    return item.id;
  }

  registerChangeInHesaps() {
    this.eventSubscriber = this.eventManager.subscribe('hesapListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}

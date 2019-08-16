import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Cari } from 'app/shared/model/cari.model';
import { CariService } from './cari.service';
import { CariComponent } from './cari.component';
import { CariDetailComponent } from './cari-detail.component';
import { CariUpdateComponent } from './cari-update.component';
import { CariDeletePopupComponent } from './cari-delete-dialog.component';
import { ICari } from 'app/shared/model/cari.model';

@Injectable({ providedIn: 'root' })
export class CariResolve implements Resolve<ICari> {
  constructor(private service: CariService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICari> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Cari>) => response.ok),
        map((cari: HttpResponse<Cari>) => cari.body)
      );
    }
    const cari = new Cari();
    cari.aktif = true;
    return of(cari);
  }
}

export const cariRoute: Routes = [
  {
    path: '',
    component: CariComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'lastModifiedDate,desc',
      pageTitle: 'otoBakimApp.cari.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CariDetailComponent,
    resolve: {
      cari: CariResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.cari.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CariUpdateComponent,
    resolve: {
      cari: CariResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.cari.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CariUpdateComponent,
    resolve: {
      cari: CariResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.cari.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cariPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CariDeletePopupComponent,
    resolve: {
      cari: CariResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.cari.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

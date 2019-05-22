import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IscilikTipi } from 'app/shared/model/iscilik-tipi.model';
import { IscilikTipiService } from './iscilik-tipi.service';
import { IscilikTipiComponent } from './iscilik-tipi.component';
import { IscilikTipiDetailComponent } from './iscilik-tipi-detail.component';
import { IscilikTipiUpdateComponent } from './iscilik-tipi-update.component';
import { IscilikTipiDeletePopupComponent } from './iscilik-tipi-delete-dialog.component';
import { IIscilikTipi } from 'app/shared/model/iscilik-tipi.model';

@Injectable({ providedIn: 'root' })
export class IscilikTipiResolve implements Resolve<IIscilikTipi> {
  constructor(private service: IscilikTipiService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIscilikTipi> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IscilikTipi>) => response.ok),
        map((iscilikTipi: HttpResponse<IscilikTipi>) => iscilikTipi.body)
      );
    }
    return of(new IscilikTipi());
  }
}

export const iscilikTipiRoute: Routes = [
  {
    path: '',
    component: IscilikTipiComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'otoBakimApp.iscilikTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IscilikTipiDetailComponent,
    resolve: {
      iscilikTipi: IscilikTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IscilikTipiUpdateComponent,
    resolve: {
      iscilikTipi: IscilikTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IscilikTipiUpdateComponent,
    resolve: {
      iscilikTipi: IscilikTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const iscilikTipiPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IscilikTipiDeletePopupComponent,
    resolve: {
      iscilikTipi: IscilikTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikTipi.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

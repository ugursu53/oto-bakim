import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IsEmri } from 'app/shared/model/is-emri.model';
import { IsEmriService } from './is-emri.service';
import { IsEmriComponent } from './is-emri.component';
import { IsEmriDetailComponent } from './is-emri-detail.component';
import { IsEmriUpdateComponent } from './is-emri-update.component';
import { IsEmriDeletePopupComponent } from './is-emri-delete-dialog.component';
import { IIsEmri } from 'app/shared/model/is-emri.model';
import { AracService } from '../arac/arac.service';
import { Arac, IArac } from 'app/shared/model/arac.model';

@Injectable({ providedIn: 'root' })
export class IsEmriResolve implements Resolve<IIsEmri> {
  constructor(private service: IsEmriService, private aracService: AracService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIsEmri> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IsEmri>) => response.ok),
        map((isEmri: HttpResponse<IsEmri>) => isEmri.body)
      );
    }

    const aracId = route.queryParams['aracId'];
    if (aracId) {
      return this.aracService.find(aracId).pipe(
        filter((response: HttpResponse<Arac>) => response.ok),
        map((arac: HttpResponse<Arac>) => {
          const isEmri = new IsEmri();
          isEmri.arac = arac.body;
          return isEmri;
        })
      );
    }

    return of(new IsEmri());
  }
}

export const isEmriRoute: Routes = [
  {
    path: '',
    component: IsEmriComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'otoBakimApp.isEmri.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IsEmriDetailComponent,
    resolve: {
      isEmri: IsEmriResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.isEmri.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IsEmriUpdateComponent,
    resolve: {
      isEmri: IsEmriResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.isEmri.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IsEmriUpdateComponent,
    resolve: {
      isEmri: IsEmriResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.isEmri.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const isEmriPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IsEmriDeletePopupComponent,
    resolve: {
      isEmri: IsEmriResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.isEmri.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

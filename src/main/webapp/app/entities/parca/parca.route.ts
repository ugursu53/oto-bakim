import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Parca } from 'app/shared/model/parca.model';
import { ParcaService } from './parca.service';
import { ParcaComponent } from './parca.component';
import { ParcaDetailComponent } from './parca-detail.component';
import { ParcaUpdateComponent } from './parca-update.component';
import { ParcaDeletePopupComponent } from './parca-delete-dialog.component';
import { IParca } from 'app/shared/model/parca.model';

@Injectable({ providedIn: 'root' })
export class ParcaResolve implements Resolve<IParca> {
  constructor(private service: ParcaService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IParca> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Parca>) => response.ok),
        map((parca: HttpResponse<Parca>) => parca.body)
      );
    }
    return of(new Parca());
  }
}

export const parcaRoute: Routes = [
  {
    path: '',
    component: ParcaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'otoBakimApp.parca.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ParcaDetailComponent,
    resolve: {
      parca: ParcaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parca.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ParcaUpdateComponent,
    resolve: {
      parca: ParcaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parca.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ParcaUpdateComponent,
    resolve: {
      parca: ParcaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parca.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const parcaPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ParcaDeletePopupComponent,
    resolve: {
      parca: ParcaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parca.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ParcaTipi } from 'app/shared/model/parca-tipi.model';
import { ParcaTipiService } from './parca-tipi.service';
import { ParcaTipiComponent } from './parca-tipi.component';
import { ParcaTipiDetailComponent } from './parca-tipi-detail.component';
import { ParcaTipiUpdateComponent } from './parca-tipi-update.component';
import { ParcaTipiDeletePopupComponent } from './parca-tipi-delete-dialog.component';
import { IParcaTipi } from 'app/shared/model/parca-tipi.model';

@Injectable({ providedIn: 'root' })
export class ParcaTipiResolve implements Resolve<IParcaTipi> {
  constructor(private service: ParcaTipiService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IParcaTipi> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ParcaTipi>) => response.ok),
        map((parcaTipi: HttpResponse<ParcaTipi>) => parcaTipi.body)
      );
    }
    return of(new ParcaTipi());
  }
}

export const parcaTipiRoute: Routes = [
  {
    path: '',
    component: ParcaTipiComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'otoBakimApp.parcaTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ParcaTipiDetailComponent,
    resolve: {
      parcaTipi: ParcaTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parcaTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ParcaTipiUpdateComponent,
    resolve: {
      parcaTipi: ParcaTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parcaTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ParcaTipiUpdateComponent,
    resolve: {
      parcaTipi: ParcaTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parcaTipi.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const parcaTipiPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ParcaTipiDeletePopupComponent,
    resolve: {
      parcaTipi: ParcaTipiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.parcaTipi.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IscilikGrubu } from 'app/shared/model/iscilik-grubu.model';
import { IscilikGrubuService } from './iscilik-grubu.service';
import { IscilikGrubuComponent } from './iscilik-grubu.component';
import { IscilikGrubuDetailComponent } from './iscilik-grubu-detail.component';
import { IscilikGrubuUpdateComponent } from './iscilik-grubu-update.component';
import { IscilikGrubuDeletePopupComponent } from './iscilik-grubu-delete-dialog.component';
import { IIscilikGrubu } from 'app/shared/model/iscilik-grubu.model';

@Injectable({ providedIn: 'root' })
export class IscilikGrubuResolve implements Resolve<IIscilikGrubu> {
  constructor(private service: IscilikGrubuService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIscilikGrubu> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IscilikGrubu>) => response.ok),
        map((iscilikGrubu: HttpResponse<IscilikGrubu>) => iscilikGrubu.body)
      );
    }
    return of(new IscilikGrubu());
  }
}

export const iscilikGrubuRoute: Routes = [
  {
    path: '',
    component: IscilikGrubuComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'otoBakimApp.iscilikGrubu.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IscilikGrubuDetailComponent,
    resolve: {
      iscilikGrubu: IscilikGrubuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikGrubu.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IscilikGrubuUpdateComponent,
    resolve: {
      iscilikGrubu: IscilikGrubuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikGrubu.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IscilikGrubuUpdateComponent,
    resolve: {
      iscilikGrubu: IscilikGrubuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikGrubu.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const iscilikGrubuPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IscilikGrubuDeletePopupComponent,
    resolve: {
      iscilikGrubu: IscilikGrubuResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.iscilikGrubu.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

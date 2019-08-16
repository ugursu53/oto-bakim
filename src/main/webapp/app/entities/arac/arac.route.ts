import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Arac } from 'app/shared/model/arac.model';
import { AracService } from './arac.service';
import { AracComponent } from './arac.component';
import { AracDetailComponent } from './arac-detail.component';
import { AracUpdateComponent } from './arac-update.component';
import { AracDeletePopupComponent } from './arac-delete-dialog.component';
import { IArac } from 'app/shared/model/arac.model';
import { ArackabulComponent } from 'app/entities/arac/arackabul.component';

@Injectable({ providedIn: 'root' })
export class AracResolve implements Resolve<IArac> {
  constructor(private service: AracService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IArac> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Arac>) => response.ok),
        map((arac: HttpResponse<Arac>) => arac.body)
      );
    }
    const arac = new Arac();
    const query = route.queryParams['query'] ? route.queryParams['query'] : null;
    arac.plakaNo = query;
    return of(arac);
  }
}

export const aracRoute: Routes = [
  {
    path: '',
    component: AracComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'lastModifiedDate,desc',
      pageTitle: 'otoBakimApp.arac.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AracDetailComponent,
    resolve: {
      arac: AracResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.arac.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AracUpdateComponent,
    resolve: {
      arac: AracResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.arac.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'arackabul',
    component: ArackabulComponent,
    resolve: {
      arac: AracResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.arac.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AracUpdateComponent,
    resolve: {
      arac: AracResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.arac.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const aracPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AracDeletePopupComponent,
    resolve: {
      arac: AracResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.arac.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

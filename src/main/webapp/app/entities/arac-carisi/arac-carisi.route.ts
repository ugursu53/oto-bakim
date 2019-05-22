import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { AracCarisi } from 'app/shared/model/arac-carisi.model';
import { AracCarisiService } from './arac-carisi.service';
import { AracCarisiComponent } from './arac-carisi.component';
import { AracCarisiDetailComponent } from './arac-carisi-detail.component';
import { AracCarisiUpdateComponent } from './arac-carisi-update.component';
import { AracCarisiDeletePopupComponent } from './arac-carisi-delete-dialog.component';
import { IAracCarisi } from 'app/shared/model/arac-carisi.model';

@Injectable({ providedIn: 'root' })
export class AracCarisiResolve implements Resolve<IAracCarisi> {
  constructor(private service: AracCarisiService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAracCarisi> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<AracCarisi>) => response.ok),
        map((aracCarisi: HttpResponse<AracCarisi>) => aracCarisi.body)
      );
    }
    return of(new AracCarisi());
  }
}

export const aracCarisiRoute: Routes = [
  {
    path: '',
    component: AracCarisiComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.aracCarisi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AracCarisiDetailComponent,
    resolve: {
      aracCarisi: AracCarisiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.aracCarisi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AracCarisiUpdateComponent,
    resolve: {
      aracCarisi: AracCarisiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.aracCarisi.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AracCarisiUpdateComponent,
    resolve: {
      aracCarisi: AracCarisiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.aracCarisi.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const aracCarisiPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AracCarisiDeletePopupComponent,
    resolve: {
      aracCarisi: AracCarisiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.aracCarisi.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

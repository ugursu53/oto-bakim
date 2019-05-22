import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Hesap } from 'app/shared/model/hesap.model';
import { HesapService } from './hesap.service';
import { HesapComponent } from './hesap.component';
import { HesapDetailComponent } from './hesap-detail.component';
import { HesapUpdateComponent } from './hesap-update.component';
import { HesapDeletePopupComponent } from './hesap-delete-dialog.component';
import { IHesap } from 'app/shared/model/hesap.model';

@Injectable({ providedIn: 'root' })
export class HesapResolve implements Resolve<IHesap> {
  constructor(private service: HesapService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHesap> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Hesap>) => response.ok),
        map((hesap: HttpResponse<Hesap>) => hesap.body)
      );
    }
    return of(new Hesap());
  }
}

export const hesapRoute: Routes = [
  {
    path: '',
    component: HesapComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.hesap.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: HesapDetailComponent,
    resolve: {
      hesap: HesapResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.hesap.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: HesapUpdateComponent,
    resolve: {
      hesap: HesapResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.hesap.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: HesapUpdateComponent,
    resolve: {
      hesap: HesapResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.hesap.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const hesapPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: HesapDeletePopupComponent,
    resolve: {
      hesap: HesapResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'otoBakimApp.hesap.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cari',
        loadChildren: './cari/cari.module#OtoBakimCariModule'
      },
      {
        path: 'hesap',
        loadChildren: './hesap/hesap.module#OtoBakimHesapModule'
      },
      {
        path: 'arac',
        loadChildren: './arac/arac.module#OtoBakimAracModule'
      },
      {
        path: 'arac-carisi',
        loadChildren: './arac-carisi/arac-carisi.module#OtoBakimAracCarisiModule'
      },
      {
        path: 'marka',
        loadChildren: './marka/marka.module#OtoBakimMarkaModule'
      },
      {
        path: 'model',
        loadChildren: './model/model.module#OtoBakimModelModule'
      },
      {
        path: 'is-emri',
        loadChildren: './is-emri/is-emri.module#OtoBakimIsEmriModule'
      },
      {
        path: 'iscilik',
        loadChildren: './iscilik/iscilik.module#OtoBakimIscilikModule'
      },
      {
        path: 'iscilik-grubu',
        loadChildren: './iscilik-grubu/iscilik-grubu.module#OtoBakimIscilikGrubuModule'
      },
      {
        path: 'iscilik-tipi',
        loadChildren: './iscilik-tipi/iscilik-tipi.module#OtoBakimIscilikTipiModule'
      },
      {
        path: 'personel',
        loadChildren: './personel/personel.module#OtoBakimPersonelModule'
      },
      {
        path: 'parca',
        loadChildren: './parca/parca.module#OtoBakimParcaModule'
      },
      {
        path: 'parca-tipi',
        loadChildren: './parca-tipi/parca-tipi.module#OtoBakimParcaTipiModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class OtoBakimEntityModule {}

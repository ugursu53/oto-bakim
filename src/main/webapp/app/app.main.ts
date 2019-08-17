import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { ProdConfig } from './blocks/config/prod.config';
import { OtoBakimAppModule } from './app.module';

ProdConfig();

if (module['hot']) {
}

platformBrowserDynamic()
  .bootstrapModule(OtoBakimAppModule, { preserveWhitespaces: true })
  .then(success => console.log(`Application started`))
  .catch(err => console.error(err));

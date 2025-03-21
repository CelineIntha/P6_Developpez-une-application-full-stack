import {enableProdMode, LOCALE_ID} from '@angular/core';
import {bootstrapApplication} from '@angular/platform-browser';
import {AppComponent} from './app/app.component';
import {environment} from './environments/environment';
import {appConfig} from './app/app.config';
import {registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';

if (environment.production) {
  enableProdMode();
}

registerLocaleData(localeFr);

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    ...(appConfig.providers || []),
    {provide: LOCALE_ID, useValue: 'fr-FR'}
  ]
}).catch(err => console.error(err));

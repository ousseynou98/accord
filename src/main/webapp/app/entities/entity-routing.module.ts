import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'accord',
        data: { pageTitle: 'accordApp.accord.home.title' },
        loadChildren: () => import('./accord/accord.module').then(m => m.AccordModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

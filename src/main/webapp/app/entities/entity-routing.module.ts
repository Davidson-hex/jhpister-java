import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'livro',
        data: { pageTitle: 'bibliotecaApp.livro.home.title' },
        loadChildren: () => import('./livro/livro.module').then(m => m.LivroModule),
      },
      {
        path: 'locar',
        data: { pageTitle: 'bibliotecaApp.locar.home.title' },
        loadChildren: () => import('./locar/locar.module').then(m => m.LocarModule),
      },
      {
        path: 'movimentos',
        data: { pageTitle: 'bibliotecaApp.movimentos.home.title' },
        loadChildren: () => import('./movimentos/movimentos.module').then(m => m.MovimentosModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

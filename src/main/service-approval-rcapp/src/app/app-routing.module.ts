import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from './view/login/login.component';
import {OfficerViewComponent} from './view/officer-view/officer-view.component';
import {ManagerViewComponent} from './view/manager-view/manager-view.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    component: LoginComponent,
    path: 'login'
  },
  {
    component: OfficerViewComponent,
    path: 'officer'
  },
  {
    component: ManagerViewComponent,
    path: 'manager'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

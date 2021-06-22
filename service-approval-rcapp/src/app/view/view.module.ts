import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {OfficerViewComponent} from './officer-view/officer-view.component';
import {ManagerViewComponent} from './manager-view/manager-view.component';
import {ReactiveFormsModule} from '@angular/forms';
import {ManagerService} from './service/manager.service';
import {OfficerService} from './service/officer.service';


@NgModule({
  declarations: [
    LoginComponent,
    OfficerViewComponent,
    ManagerViewComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [
    ManagerService,
    OfficerService
  ]
})
export class ViewModule {
}

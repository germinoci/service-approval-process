import { ManagerService } from './../service/manager.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-manager-view',
  templateUrl: './manager-view.component.html',
  styleUrls: ['./manager-view.component.scss']
})
export class ManagerViewComponent implements OnInit {

  constructor(private svc: ManagerService) { }

  ngOnInit(): void {
    this.svc.getApprovals().toPromise().then((response) => {
      console.log(response);
    })
  }

  public confirmAssessed(id: number): void {
    if (window.confirm("Naozaj chcete odobriť túto službu?")) {

    }
  }

  public denyAssessed(id: number): void {
    if (window.confirm("Naozaj chcete zamietnuť túto službu?")) {

    }
  }

}

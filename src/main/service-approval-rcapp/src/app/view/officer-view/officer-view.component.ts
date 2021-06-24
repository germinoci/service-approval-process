import { OfficerService } from './../service/officer.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-officer-view',
  templateUrl: './officer-view.component.html',
  styleUrls: ['./officer-view.component.scss']
})
export class OfficerViewComponent implements OnInit {

  constructor(private svc: OfficerService) { }

  ngOnInit(): void {
    this.svc.getApprovals().toPromise().then((response) => {
      console.log(response);
    })
  }

  public confirmAssessed(id: number): void {
    if (window.confirm("Naozaj chcete odobriť túto službu a odoslať ju na schválenie vedúcemu?")) {

    }
  }

  public denyAssessed(id: number): void {
    if (window.confirm("Naozaj chcete zamietnuť túto službu?")) {

    }
  }

  public publishService(id: number): void {
    if (window.confirm("Naozaj chcete zverejniť túto službu?")) {

    }
  }
}

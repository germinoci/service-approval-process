import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-manager-view',
  templateUrl: './manager-view.component.html',
  styleUrls: ['./manager-view.component.scss']
})
export class ManagerViewComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
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

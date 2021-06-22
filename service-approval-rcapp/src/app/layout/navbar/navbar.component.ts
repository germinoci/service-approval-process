import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  constructor(private router: Router) {
  }

  public logout(): void {
    if (window.confirm("Naozaj sa chcete odhlásiť?")) {
      sessionStorage.removeItem('login');
      this.router.navigateByUrl("login");
    }
  }

  public isNotLoggedIn(): boolean {
    return !sessionStorage.getItem('login');
  }

}

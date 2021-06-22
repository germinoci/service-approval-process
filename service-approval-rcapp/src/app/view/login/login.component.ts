import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  public loginForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {
    this.loginForm = fb.group({
      loginName: ['', [Validators.required]],
      password: ['', [Validators.required]]
    })
  }

  public login(): void {
    if (this.loginForm.valid) {
      switch (this.loginForm.get('loginName')?.value) {
        case 'veduci':
          this.router.navigateByUrl('manager');
          sessionStorage.setItem('login', 'manager');
          break;
        case 'referent':
          this.router.navigateByUrl('officer');
          sessionStorage.setItem('login', 'officer');
          break;
        default:
          window.alert('Nesprávne prihlasovacie údaje!');
          this.loginForm.reset();
          break;
      }
    }
  }

}

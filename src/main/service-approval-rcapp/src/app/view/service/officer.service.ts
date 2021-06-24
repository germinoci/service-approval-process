import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class OfficerService {

  constructor(private http: HttpClient) {
  }

  public getApprovals(): Observable<any> {
    return this.http.get('/approvals');
  }

}

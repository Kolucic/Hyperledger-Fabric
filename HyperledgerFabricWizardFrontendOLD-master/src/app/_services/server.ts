import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Network} from '../_models/network';
import {Observable} from 'rxjs';

@Injectable()
export class Server {
  readonly serverURL = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) {
  }

  send(network: Network): Observable<HttpResponse<Blob>> {
    return this.httpClient.post(`${this.serverURL}/submit`, network, {
      headers: {accept: 'application/zip'},
      responseType: 'blob',
      observe: 'response'
    });
  }
}

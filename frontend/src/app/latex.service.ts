import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export interface LatexRequest {
  latex: string;
}

export interface LatexResponse {
  text: string;
  base64PNG: string;
}

@Injectable({
  providedIn: 'root'
})
export class LatexService {

  constructor(private http: HttpClient) { }

  getLatex(latex: LatexRequest): Observable<LatexResponse> {
    return this.http.post<LatexResponse>('api/latex', latex);
  }
}

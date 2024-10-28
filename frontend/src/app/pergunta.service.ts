import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {LatexRequest, LatexResponse} from "./latex.service";

export interface OpcaoImg {
  id: number
  letra: string
  base64PNG: string
}

export interface PerguntaImgResponse {
  id: number
  disciplina: string
  base64PNG: string
  opcoes: OpcaoImg[]
}

@Injectable({
  providedIn: 'root'
})
export class PerguntaService {

  constructor(private http: HttpClient) { }

  get(id: number): Observable<PerguntaImgResponse> {
    return this.http.get<PerguntaImgResponse>(`api/pergunta?id=${id}`);
  }}

import { Injectable } from '@angular/core';
import {LatexResponse} from "./latex.service";
import {HttpClient} from "@angular/common/http";

export interface TesteQuestaoDto {
  id: number
  perguntaId: number
  opcaoId?: number
}

export interface TesteResponse {
  id: number
  perguntas: TesteQuestaoDto[]
  nota?: number
  status: string
  dataCriacao: string
  dataConclusao?: string
}

@Injectable({
  providedIn: 'root'
})
export class TesteService {

  constructor(private http: HttpClient) { }

  get() {
    return this.http.get<TesteResponse>('api/teste');
  }

}

import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export interface TesteQuestaoDto {
  id: number;
  perguntaId: number;
  opcaoId?: number;
  correto: boolean;
  explicacao: string;
}

export interface TesteResponse {
  id: number
  perguntas: TesteQuestaoDto[]
  nota?: number
  status: string
  dataCriacao: string
  dataConclusao?: string
}

export interface MarcarOpcaoRequest {
  questaoId: number
  opcaoId: number
}

export interface DesmarcarOpcaoRequest {
  questaoId: number
}

@Injectable({
  providedIn: 'root'
})
export class TesteService {

  constructor(private http: HttpClient) { }

  get() {
    return this.http.get<TesteResponse>('api/teste');
  }

  marcar_opcao(teste_id: number, questao_id: number, opcao_id: number): Observable<TesteQuestaoDto> {
    const marcarOpcaoRequest: MarcarOpcaoRequest = {
      questaoId: questao_id,
      opcaoId: opcao_id
    };
    return this.http.put<TesteQuestaoDto>(`api/teste/${teste_id}/marcar_opcao`, marcarOpcaoRequest)
  }

  desmarcar_opcao(teste_id: number, questao_id: number): Observable<TesteQuestaoDto> {
    const desmarcarOpcaoRequest: DesmarcarOpcaoRequest = {
      questaoId: questao_id
    };
    return this.http.put<TesteQuestaoDto>(`api/teste/${teste_id}/desmarcar_opcao`, desmarcarOpcaoRequest)
  }

  corrigir(teste_id: number): Observable<TesteResponse> {
    return this.http.get<TesteResponse>(`api/teste/${teste_id}/corrigir`)
  }
}

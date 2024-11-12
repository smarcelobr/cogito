import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export interface OpcaoImg {
  id: number;
  letra: string;
  base64PNG: string;
  correta: boolean;
  explicacaoBase64PNG: string;
}

export interface PerguntaImgResponse {
  id: number;
  disciplina: string;
  base64PNG: string;
  opcoes: OpcaoImg[];
}

export interface OpcaoDto {
  id: number;
  alternativaLatex: string;
  correta: boolean;
  explicacaoLatex: string;
}

export interface PerguntaDto {
  id: number;
  disciplina: string;
  enunciadoLatex: string;
  opcoes: OpcaoDto[];
}

export interface PerguntaUpdateRequest {
  disciplina: string;
  enunciadoLatex: string;
  opcoes: OpcaoDto[];
}

@Injectable({
  providedIn: 'root'
})
export class PerguntaService {

  constructor(private http: HttpClient) { }

  getImg(id: number, rndSeed:number): Observable<PerguntaImgResponse> {
    return this.http.get<PerguntaImgResponse>(`api/pergunta/${id}/img?rndSeed=${rndSeed}`);
  }

  get(id: number) {
    return this.http.get<PerguntaDto>(`api/pergunta/${id}`);
  }

  put(id: number, perguntaAlterada: PerguntaUpdateRequest) {
    return this.http.put<PerguntaDto>(`api/pergunta/${id}`, perguntaAlterada);
  }

  clone(id: number) {
    return this.http.post<PerguntaDto>(`api/pergunta/clone`, {
      origem: id
    });
  }
}

import {Component, inject, OnInit} from '@angular/core';
import {TesteService} from "../teste.service";
import {catchError, concatMap, from, map, switchMap, tap} from "rxjs";
import {OpcaoView, QuestaoView, TesteView} from "./tela-teste.model";
import {PerguntaImgResponse, PerguntaService} from "../pergunta.service";
import {NgForOf, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {MatProgressBar} from "@angular/material/progress-bar";
import {HttpErrorResponse} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-tela-teste',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    MatButton,
    MatDivider,
    MatProgressBar
  ],
  templateUrl: './tela-teste.component.html',
  styleUrl: './tela-teste.component.scss'
})
export class TelaTesteComponent implements OnInit {
  private _snackBar = inject(MatSnackBar);

  teste: TesteView = {
    id: 0,
    perguntas: [],
    dataCriacao: '',
    status: 'CANCELADO'
  }
  perguntaAtivaIdx = 0;

  constructor(private testeService: TesteService,
              private perguntaService: PerguntaService) {
  }

  ngOnInit(): void {

    this.testeService
      .get()
      .pipe(
        map(response => ({
          id: response.id,
          dataCriacao: response.dataCriacao,
          status: response.status,
          dataConclusao: response.dataConclusao,
          nota: response.nota,
          perguntas: response.perguntas.map(
            testeQuestaoDto => ({
              id: testeQuestaoDto.id,
              perguntaId: testeQuestaoDto.perguntaId,
              opcaoId: testeQuestaoDto.opcaoId,
              opcoes: [],
              disciplina: '',
              base64PNG: ''
            } as QuestaoView)
          ),
        })),
        switchMap(testeView => from(testeView.perguntas)
          .pipe(
            concatMap(questao =>
              this.perguntaService.get(questao.perguntaId).pipe(
                tap(perguntaImgResponse => this.combina(questao, perguntaImgResponse))
              )),
            map(questao => testeView)
          ))
      ).subscribe(testeView => this.teste = testeView);
  }

  private combina(questao: QuestaoView, perguntaImgResponse: PerguntaImgResponse) {
    if (questao.perguntaId !== perguntaImgResponse.id) {
      throw new Error(`Ids da questão (${questao.id}) e pergunta (${perguntaImgResponse.id}) não conferem!`);
    }
    questao.disciplina = perguntaImgResponse.disciplina;
    questao.base64PNG = `data:image/png;base64, ${perguntaImgResponse.base64PNG}`;

    perguntaImgResponse.opcoes.forEach(opcao => {
      questao.opcoes.push({
        id: opcao.id,
        letra: opcao.letra,
        base64PNG: `data:image/png;base64, ${opcao.base64PNG}`
      });
    })

  }


  anterior() {
    this.perguntaAtivaIdx--;
    if (this.perguntaAtivaIdx < 0) {
      this.perguntaAtivaIdx = 0;
    }
  }

  proxima() {
    this.perguntaAtivaIdx++;
    if (this.perguntaAtivaIdx >= this.teste.perguntas.length) {
      this.perguntaAtivaIdx = this.teste.perguntas.length - 1;
    }
  }

  marcar_opcao(pergunta: QuestaoView, opcao_clicada: OpcaoView) {

    let chamada;
    if (pergunta.opcaoId == opcao_clicada.id) {
      // desmarca
      chamada = this.testeService.desmarcar_opcao(this.teste.id, pergunta.id);
    } else {
      // marca
      chamada = this.testeService.marcar_opcao(this.teste.id, pergunta.id, opcao_clicada.id);
    }
    chamada
      .pipe(
        catchError((err: HttpErrorResponse) => {
          // console.log(err);
          this._snackBar.open(`Erro ${err.status}: ${err.error.detail}`, undefined, {
            duration: 1600
          });
          throw err;
        })
      ).subscribe(testeQuestaoDto => pergunta.opcaoId = testeQuestaoDto.opcaoId)

  }

  countRespondidas() {
    return this.teste.perguntas.reduce(
      (qtdResposta, pergunta) => qtdResposta + (pergunta.opcaoId ? 1 : 0),
      0
    )
  }

  enviar() {
    console.log("Enviar clicado!");

    this.teste.perguntas.forEach(pergunta => pergunta.opcaoId = undefined);
  }
}

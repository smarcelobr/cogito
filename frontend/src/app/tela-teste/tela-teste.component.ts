import {Component, OnInit} from '@angular/core';
import {TesteQuestaoDto, TesteService} from "../teste.service";
import {BehaviorSubject, Subject, map, tap, switchMap, from, concatMap} from "rxjs";
import {QuestaoView, TesteView} from "./tela-teste.model";
import {PerguntaImgResponse, PerguntaService} from "../pergunta.service";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-tela-teste',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './tela-teste.component.html',
  styleUrl: './tela-teste.component.scss'
})
export class TelaTesteComponent implements OnInit {

  teste: TesteView = {
    id: 0,
    perguntas: [],
    dataCriacao: '',
    status: 'CANCELADO'
  }

  constructor(private testeService: TesteService,
              private perguntaService: PerguntaService) {
  }

  ngOnInit(): void {

    this.testeService
          .get()
          .pipe(
            map(response=> ({
              id: response.id,
              dataCriacao: response.dataCriacao,
              status: response.status,
              dataConclusao: response.dataConclusao,
              nota: response.nota,
              perguntas: response.perguntas.map(
                testeQuestaoDto=> ({
                  id: testeQuestaoDto.id,
                  perguntaId: testeQuestaoDto.perguntaId,
                  opcaoId: testeQuestaoDto.opcaoId,
                  opcoes: [],
                  disciplina: '',
                  base64PNG: ''
                } as QuestaoView)
              ),
            })),
            switchMap(testeView=>from(testeView.perguntas)
              .pipe(
                concatMap(questao=>
                  this.perguntaService.get(questao.perguntaId).pipe(
                    tap(perguntaImgResponse=> this.combina(questao, perguntaImgResponse))
                  )),
                map(questao => testeView)
              ))
          ).subscribe(testeView=> this.teste=testeView);
    }

  private combina(questao: QuestaoView, perguntaImgResponse: PerguntaImgResponse) {
    if (questao.perguntaId !== perguntaImgResponse.id) {
      throw new Error(`Ids da questão (${questao.id}) e pergunta (${perguntaImgResponse.id}) não conferem!`);
    }
    questao.disciplina = perguntaImgResponse.disciplina;
    questao.base64PNG = `data:image/png;base64, ${perguntaImgResponse.base64PNG}`;

    perguntaImgResponse.opcoes.forEach(opcao=>{
      questao.opcoes.push({
        id: opcao.id,
        letra: opcao.letra,
        base64PNG: `data:image/png;base64, ${opcao.base64PNG}`
      });
    })

  }


}

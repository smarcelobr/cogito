import {TesteQuestaoDto} from "../teste.service";

export interface OpcaoView {
  id: number
  letra: string
  base64PNG: string
}

export interface QuestaoView {
  id: number
  perguntaId: number
  disciplina: string;
  base64PNG: string /* enunciado em base 64 PNG */
  opcoes: OpcaoView[]
  opcaoId?: number /* opcao marcada */
  correto?: boolean /* indica se a resposta está correta. retornado apenas quando teste estiver corrigido. */
  explicacao?: string /* base64PNG da explicacao do acerto ou erro. Retorna apenas se corrigido. */
}

export interface TesteView {
  id: number
  perguntas: QuestaoView[]
  nota?: number
  status: string
  dataCriacao: string
  dataConclusao?: string

}

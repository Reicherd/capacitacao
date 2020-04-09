import { ApiNegociacao, Negociacao } from "../models/index";

export class NegociacaoService {
    obterNegociacoes(handler: HandlerFunction): Promise<Negociacao[] | void> {

        return fetch('http://localhost:8080/dados')
            .then(res => handler(res))
            .then(res => res.json())
            .then((dados: ApiNegociacao[]) =>
                dados
                    .map(dado => new Negociacao(new Date(), dado.vezes, dado.montante))
            )
            .catch(err => console.log(err.message));
    }
}

export interface HandlerFunction {
    (res: Response): Response;
}
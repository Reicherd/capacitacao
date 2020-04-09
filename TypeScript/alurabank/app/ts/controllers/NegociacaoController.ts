import { MensagemView, NegociacoesView } from '../views/index'
import { Negociacoes, Negociacao, ApiNegociacao } from '../models/index'
import { domInject, throttle } from '../helpers/decorators/index'
import { NegociacaoService } from '../services/index'
import { imprime } from '../helpers/index';

export class NegociacaoController {
    @domInject('#data')
    private _inputData: JQuery;

    @domInject('#quantidade')
    private _inputQuantidade: JQuery;

    @domInject('#valor')
    private _inputValor: JQuery;
    private _negociacoes = new Negociacoes();
    private _negociacoesView = new NegociacoesView('#negociacoesView', true);
    private _mensagemView = new MensagemView('#mensagemView', true);

    private _negociacaoService = new NegociacaoService();

    constructor() {
        this._negociacoesView.update(this._negociacoes);
    }

    @throttle()
    adiciona() {
        let data = new Date(this._inputData.val().replace(/-/g, ','));

        if (this.nEhDiaUtil(data)) {
            this._mensagemView.update('Somente negociações em dias úteis ai grazadeus!!');
            return;
        }

        const negociacao = new Negociacao(
            data,
            parseInt(this._inputQuantidade.val()),
            parseFloat(this._inputValor.val())
            );

        this._negociacoes.adiciona(negociacao);

        imprime(negociacao, this._negociacoes);

        this._negociacoesView.update(this._negociacoes);
        this._mensagemView.update('Negociação adicionada com sucesso!!');        
    }

    private nEhDiaUtil(data: Date) {
        return data.getDay() == DiaDaSemana.Domingo || data.getDay() == DiaDaSemana.Sabado;
    }

    @throttle()
    importaDados() {

        this._negociacaoService.obterNegociacoes((res: Response) => {
            if (res.ok) {
                return res;
            } else {
                throw new Error(res.statusText);
            }
        })
        .then(negociacoes => {
                    const negociacoesImportadas = this._negociacoes.paraArray();

                    negociacoes ? negociacoes
                        .filter(negociacao => !negociacoesImportadas
                            .some(jaImportada => negociacao.ehIgual(jaImportada)))
                            .forEach(negociacao => this._negociacoes.adiciona(negociacao))
                        : null;


                    this._negociacoesView.update(this._negociacoes);
                }
            )
        
    }
}

enum DiaDaSemana {
    Domingo,
    Segunda,
    Terca,
    Quarta,
    Quinta,
    Sexta,
    Sabado
}
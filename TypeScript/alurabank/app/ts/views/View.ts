import { logarTempoDeExecucao } from '../helpers/decorators/index'

export abstract class View<T> {
    private _elemento: JQuery;
    private _escapar: boolean;

    constructor(seletor: string, escapar?: boolean) {
        this._elemento = $(seletor);
        this._escapar = escapar ? escapar : false;
    }

    @logarTempoDeExecucao(true)
    update(model: T): void {
        let template = this.template(model);
        if (this._escapar) {
            template = template.replace(/<script>[\s\S]*?<\/script>/, '');
        }

        this._elemento.html(template);
    }

    abstract template(model: T): string;
}

import { NegociacaoController } from './controllers/NegociacaoController';

const controller = new NegociacaoController();

$('.form').submit(controller.adiciona.bind(controller));

$('#importa').click(controller.importaDados.bind(controller));

package br.com.caelum.leilao.servico;

import java.util.Calendar;
import java.util.List;

import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.interfaces.RepositorioDeLeiloes;
import br.com.caelum.leilao.interfaces.RepositorioDePagamentos;

public class GeradorDePagamento {
	private RepositorioDeLeiloes leiloes;
	private Avaliador avaliador;
	private RepositorioDePagamentos pagamentos;

	public GeradorDePagamento(RepositorioDeLeiloes leiloes, RepositorioDePagamentos pagamentos, Avaliador avaliador) {
		this.leiloes = leiloes;
		this.avaliador= avaliador;
		this.pagamentos = pagamentos;
	}
	
	public void gera() {
		List<Leilao> leiloesEncerrados = this.leiloes.encerrados();
		
		for (Leilao leilao : leiloesEncerrados) {
			this.avaliador.avalia(leilao);
			
			Pagamento novoPagamento = new Pagamento(avaliador.getMaiorLance(), Calendar.getInstance());
			this.pagamentos.salva(novoPagamento);
					
		}
	}
}

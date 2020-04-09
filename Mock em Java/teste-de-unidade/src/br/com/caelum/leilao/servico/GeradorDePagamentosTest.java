package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.interfaces.RepositorioDeLeiloes;
import br.com.caelum.leilao.interfaces.RepositorioDePagamentos;

public class GeradorDePagamentosTest {
	@Test
	public void geraPagamentoEncerrado() {
		RepositorioDePagamentos pagamentos = Mockito.mock(RepositorioDePagamentos.class);
		RepositorioDeLeiloes leiloes = Mockito.mock(RepositorioDeLeiloes.class);
		Avaliador avaliador = Mockito.mock(Avaliador.class);
		
		Leilao leilao = new CriadorDeLeilao().para("Menes usados")
				.lance(new Usuario("Leozinho"), 4000.0)
				.lance(new Usuario("Xtronda"), 700.0)
				.constroi();
		
		Mockito.when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));
		Mockito.when(avaliador.getMaiorLance()).thenReturn(4000.0);
		
		GeradorDePagamento gerador = new GeradorDePagamento(leiloes, pagamentos, avaliador);
		gerador.gera();
		
		ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
		Mockito.verify(pagamentos).salva(argumento.capture());
		
		Pagamento pagamentoGerado = argumento.getValue();
		assertEquals(4000.0, pagamentoGerado.getValor(), 0.00001);
	}
}

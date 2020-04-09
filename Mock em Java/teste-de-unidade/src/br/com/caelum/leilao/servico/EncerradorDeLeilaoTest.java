package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.interfaces.EnviadorDeEmail;
import br.com.caelum.leilao.interfaces.RepositorioDeLeiloes;

public class EncerradorDeLeilaoTest{
	@Test
	public void deveEncerrar() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999,1, 20);
		
		Leilao leilao1 = new CriadorDeLeilao().para("TV de memes").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Gela de memes").naData(antiga).constroi();
		List<Leilao> leiloesAntigo = Arrays.asList(leilao1, leilao2);
		
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(leiloesAntigo);
		
		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
		encerrador.encerra();
		assertEquals(2, encerrador.getTotalEncerrados());
		assertTrue(leilao1.isEncerrado());
		assertTrue(leilao2.isEncerrado());
	}
	
	@Test
	public void nDeveEncerrar() {
		Calendar ontem = Calendar.getInstance();
		ontem.add(Calendar.DAY_OF_MONTH, -1);
		
		Leilao leilao1 = new CriadorDeLeilao().para("TV de memes").naData(ontem).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Gela de memes").naData(ontem).constroi();
		List<Leilao> leiloesAntigo = Arrays.asList(leilao1, leilao2);
		
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(leiloesAntigo);
		
		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
		encerrador.encerra();
		
		assertEquals(0, encerrador.getTotalEncerrados());
		assertFalse(leilao1.isEncerrado());
		assertFalse(leilao2.isEncerrado());
	}
	
	@Test
	public void zeroDeveEncerrar() {
		
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());
		
		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
		encerrador.encerra();
		
		assertEquals(0, encerrador.getTotalEncerrados());
	}
	
	@Test
	public void atualizaEncerrados() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999,1, 20);
		
		Leilao leilao1 = new CriadorDeLeilao().para("TV de memes").naData(antiga).constroi();
		
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));
		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
		encerrador.encerra();
		
		verify(daoFalso, times(1)).atualiza(leilao1);
	}
	
	@Test
    public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {

        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_MONTH, -1);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de memes")
            .naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Gela de memes")
            .naData(ontem).constroi();

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
        encerrador.encerra();

        assertEquals(0, encerrador.getTotalEncerrados());
        assertFalse(leilao1.isEncerrado());
        assertFalse(leilao2.isEncerrado());

        verify(daoFalso, never()).atualiza(leilao1);
        verify(daoFalso, never()).atualiza(leilao2);
    }
	
	@Test
    public void verificaRuntimeLeilao() {

        Calendar ontem = Calendar.getInstance();
        ontem.set(1999,1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de memes")
            .naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Gela de memes")
            .naData(ontem).constroi();

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);
        
        EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
		
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
        encerrador.encerra();

        verify(daoFalso).atualiza(leilao2);
        verify(carteiroFalso).envia(leilao2);
        
        verify(carteiroFalso, times(0)).envia(leilao1);
    }
	
	@Test
    public void verificaRuntimeCarteiro() {

        Calendar ontem = Calendar.getInstance();
        ontem.set(1999,1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de memes")
            .naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Gela de memes")
            .naData(ontem).constroi();

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);
        
        EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
        doThrow(new RuntimeException()).when(carteiroFalso).envia(leilao1);
        
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
        encerrador.encerra();

        verify(daoFalso).atualiza(leilao2);
        verify(carteiroFalso).envia(leilao2);
        
        verify(carteiroFalso, times(0)).envia(leilao1);
    }
	
	@Test
    public void verificaRuntimeTodos() {

        Calendar ontem = Calendar.getInstance();
        ontem.set(1999,1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de memes")
            .naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Gela de memes")
            .naData(ontem).constroi();

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
        doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);
        doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao2);
        
        EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
        
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
        encerrador.encerra();

        verify(carteiroFalso, never()).envia(any(Leilao.class));
    }

	public void envia(Leilao leilao) {
		// TODO Auto-generated method stub
		
	}
	
	@Test
    public void verificaEnviarOrdem() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma")
            .naData(antiga).constroi();

        RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));

        EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);
        EncerradorDeLeilao encerrador = 
            new EncerradorDeLeilao(daoFalso, carteiroFalso);

        encerrador.encerra();

        InOrder inOrder = inOrder(daoFalso, carteiroFalso);
        inOrder.verify(daoFalso, times(1)).atualiza(leilao1);    
        inOrder.verify(carteiroFalso, times(1)).envia(leilao1);    
    }
}

package br.com.caelum.leilao.dominio;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TestAvaliador
 */
public class TestAvaliador {

	private Avaliador leiloeiro;
	
	@Before
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
	}
	
	@After
	public void finaliza() {
	  System.out.println("fim");
	}
	
    @Test
    public void deveCalcularAMedia() {
        Usuario joao = new Usuario("Joao");
        Usuario jose = new Usuario("José");
        Usuario maria = new Usuario("Maria");

        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(maria,300.0));
        leilao.propoe(new Lance(joao,400.0));
        leilao.propoe(new Lance(jose,500.0));

    }
    
    @Test
    public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
        Usuario joao = new Usuario("Joao"); 
        Usuario maria = new Usuario("Maria"); 
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,200.0));
        leilao.propoe(new Lance(maria,450.0));
        leilao.propoe(new Lance(joao,120.0));
        leilao.propoe(new Lance(maria,700.0));
        leilao.propoe(new Lance(joao,630.0));
        leilao.propoe(new Lance(maria,230.0));

        
        leiloeiro.avalia(leilao);

        assertEquals(700.0, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(120.0, leiloeiro.getMenorLance(), 0.0001);
    }
    
    @Test
    public void deveEntenderLeilaoComApenasUmLance() {
        Usuario joao = new Usuario("Joao"); 
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,1000.0));

        
        leiloeiro.avalia(leilao);

        assertEquals(1000, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(1000, leiloeiro.getMenorLance(), 0.0001);
    }
    
    @Test
    public void deveEntenderLeilaoComLancesEmOrdemDecrescente() {
        Usuario joao = new Usuario("Joao"); 
        Usuario maria = new Usuario("Maria"); 
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,400.0));
        leilao.propoe(new Lance(maria,300.0));
        leilao.propoe(new Lance(joao,200.0));
        leilao.propoe(new Lance(maria,100.0));

        
        leiloeiro.avalia(leilao);

        assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
        assertThat(leiloeiro.getMenorLance(), equalTo(100.0));
    }
    
    @Test
    public void deveEncontrarOsTresMaioresLances() {
        Usuario joao = new Usuario("João");
        Usuario maria = new Usuario("Maria");
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 100.0));
        leilao.propoe(new Lance(maria, 200.0));
        leilao.propoe(new Lance(joao, 300.0));
        leilao.propoe(new Lance(maria, 400.0));

        
        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertEquals(3, maiores.size());
        assertEquals(400, maiores.get(0).getValor(), 0.00001);
        assertEquals(300, maiores.get(1).getValor(), 0.00001);
        assertEquals(200, maiores.get(2).getValor(), 0.00001);
    }
    
    @Test(expected=RuntimeException.class)
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
    	Leilao leilao = new Leilao("Playstation 3 Novo");

        leiloeiro.avalia(leilao);
    }

    @Test
    public void deveDevolverTodosLancesCasoNaoHajaNoMinimo3() {
        Usuario joao = new Usuario("João");
        Usuario maria = new Usuario("Maria");
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 100.0));
        leilao.propoe(new Lance(maria, 200.0));

        
        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();

        assertEquals(2, maiores.size());
        assertEquals(200, maiores.get(0).getValor(), 0.00001);
        assertEquals(100, maiores.get(1).getValor(), 0.00001);
    }
    
    @Test
    public void naoAceitaDoisLancesSeguidos() {
    	Leilao leilao = new Leilao("Memes do dia");
    	Usuario steveJobs = new Usuario("Steve Memes");
    	
    	leilao.propoe(new Lance(steveJobs, 2000.0));
    	leilao.propoe(new Lance(steveJobs, 3000.0));
    	
    	assertEquals(1, leilao.getLances().size());
    	assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
    }
    
    @Test
    public void naoAceitaCinco() {
		Leilao leilao = new Leilao("Memes atomicos");
		Usuario mene = new Usuario("Mene sensei");
		Usuario meme = new Usuario("Meme senpai");
		
		leilao.propoe(new Lance(mene, 2000.0));
		leilao.propoe(new Lance(meme, 3000.0));
		leilao.propoe(new Lance(mene, 2000.0));
		leilao.propoe(new Lance(meme, 3000.0));
		leilao.propoe(new Lance(mene, 2000.0));
		leilao.propoe(new Lance(meme, 3000.0));
		leilao.propoe(new Lance(mene, 2000.0));
		leilao.propoe(new Lance(meme, 3000.0));
		leilao.propoe(new Lance(mene, 2000.0));
		leilao.propoe(new Lance(meme, 6000.0));
		
		leilao.propoe(new Lance(mene, 2000.0));
		
		assertEquals(10, leilao.getLances().size());
		assertEquals(6000.0, leilao.getLances().get(leilao.getLances().size()-1).getValor(), 0.00001);
	}
    
    @Test
    public void deveDobrarOUltimoLanceDado() {
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve Jobs");
        Usuario billGates = new Usuario("Bill Gates");

        leilao.propoe(new Lance(steveJobs, 2000));
        leilao.propoe(new Lance(billGates, 3000));
        leilao.dobraLance(steveJobs);

        assertEquals(4000, leilao.getLances().get(2).getValor(), 0.00001);
    }
    
    @Test
    public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
        Leilao leilao = new Leilao("Macbook Pro 15");
        Usuario steveJobs = new Usuario("Steve Jobs");

        leilao.dobraLance(steveJobs);

        assertEquals(0, leilao.getLances().size());
    }
}
package br.com.caelum.leilao.interfaces;

import br.com.caelum.leilao.dominio.Leilao;

public interface EnviadorDeEmail {
    void envia(Leilao leilao);
}

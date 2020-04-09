package br.com.caelum.leilao.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leilao {

	private String descricao;
	private List<Lance> lances;
	
	public Leilao(String descricao) {
		this.descricao = descricao;
		this.lances = new ArrayList<Lance>();
	}
	
	public void propoe(Lance lance) {
		int total = qtdLances(lance.getUsuario());
		
		if (lances.isEmpty() || (!ultimoLance().getUsuario().equals(lance.getUsuario())) && total <5) {
			lances.add(lance);
		}
	}

	private int qtdLances(Usuario usuario) {
		int total = 0;
		for (Lance l : lances) {
			if (l.getUsuario().equals(usuario)) {
				total++;
			}
		}
		return total;
	}

	private Lance ultimoLance() {
		return lances.get(lances.size()-1);
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}
	
	public void dobraLance(Usuario usuario) {
        Lance ultimoLance = ultimoLanceDo(usuario);
        if(ultimoLance!=null) {
            propoe(new Lance(usuario, ultimoLance.getValor()*2));
        }
    }

    private Lance ultimoLanceDo(Usuario usuario) {
        Lance ultimo = null;
        for(Lance lance : lances) {
            if(lance.getUsuario().equals(usuario)) ultimo = lance;
        }

        return ultimo;
    }
}

package br.ct200.tarefa1.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AutomatoSemEpsilon {
	/** aut�mato com transi��es epsilon de entrada **/
	private Automato automato;
	
	/** map de id estado x fechos epsilon do estado **/
	private Map<Integer, List<Integer>> mapFechosEpsilonPorEstado;
	
	/** passos para a cria��o do aut�mato sem transi��es epsilon **/
	private List<Passo> passosAutomato = new ArrayList<Passo>();
	private Integer numeroPassoAutomato = 0;
	
	public AutomatoSemEpsilon(String expressaoRegular) {
		this.automato = new Automato(expressaoRegular);
		this.mapFechosEpsilonPorEstado = new HashMap<Integer, List<Integer>>();
		adicionaPasso("Iniciando remo��o de transi��es epsilon a partir da regex : " + expressaoRegular);
		this.geraAutomatoSemTransicaoEpsilon();
	}

	public Automato getAutomato() {
		return automato;
	}

	/**
	 * M�todo principal da Atividade 3, remove as &-transi��es de um aut�mato,
	 * (podendo resultar em m�ltiplos estados finais)
	 * 
	 * Algoritmo:
	 * I 	computa o &-fecho de cada estado
	 * II 	todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)
	 * III 	todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A
	 * IV 	X � estado final se algum Y no &-fecho(X) for final
	 * 
	 * @param args
	 */
	private void geraAutomatoSemTransicaoEpsilon() {
		calculaFechosEpsilon();
		removeTransicoesEpsilon();
		List<Arco> novosArcos = new ArrayList<Arco>();
		novosArcos.addAll(getNovosArcosPassoII());
		novosArcos.addAll(getNovosArcosPassoIII());
		for (Arco novoArco : novosArcos) {
			automato.criaNovoArco(novoArco);
		}
		adicionaPasso("Remo��o de transi��es epsilon do aut�mato conclu�da");
	}

	/**
	 * Remove as transi��es epsilon do aut�mato
	 */
	private void removeTransicoesEpsilon() {
		adicionaPasso("In�cio remo��o transi��es epsilon");
		for (Estado estado : automato.getTodosEstados()){
			List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
			if (arcosDoEstado != null){
				for (Iterator<Arco> iter = arcosDoEstado.iterator(); iter.hasNext();) {
					Arco arco = iter.next();
					if ("&".equals(arco.getExpressao())){
						adicionaPasso("Removendo arco : " + arco);
						iter.remove();
					}
				}
			}
		}
		adicionaPasso("Fim remo��o transi��es epsilon");
	}

	/**
	 * I e IV Calcula os fechos epsilon de todos os estados e 
	 * se X � estado final se algum Y no &-fecho(X) for final
	 */
	private void calculaFechosEpsilon() {
		adicionaPasso("In�cio computa��o fechos epsilon");
		for (Estado estado : automato.getTodosEstados()){
			List<Integer> fechosEpsilon = new ArrayList<Integer>();
			adicionaPasso("Computando fechos epsilon para estado : " + estado);
			setFechosEpsilonRecursivo(fechosEpsilon, automato.getArcosPorIdEstado(estado.getId()));
			if (fechosEpsilon != null && !fechosEpsilon.isEmpty()){
				mapFechosEpsilonPorEstado.put(estado.getId(), fechosEpsilon);
				for (Integer fecho : fechosEpsilon) {
					adicionaPasso("Estado " + automato.getEstadoPorId(fecho) + " � fecho epsilon de " + estado);
					if (TipoEstadoEnum.FINAL.equals(automato.getEstadoPorId(fecho).getTipo())){
						estado.setTipo(TipoEstadoEnum.FINAL);
					}
				}
			} else {
				adicionaPasso("Estado " + estado + " n�o tem fechos epsilon");
			}
		}
		adicionaPasso("Fim computa��o fechos epsilon");
	}

	/**
	 * Descobre recursivamente os fechos-epsilon de cada estado
	 * 
	 * @param retorno
	 * @param arcos
	 */
	private void setFechosEpsilonRecursivo(List<Integer> retorno, List<Arco> arcos) {
		if (arcos != null && !arcos.isEmpty()){
			for (Arco arco : arcos) {
				if ("&".equals(arco.getExpressao())){
					retorno.add(arco.getIdEstadoFinal());
					setFechosEpsilonRecursivo(retorno, automato.getArcosPorIdEstado(arco.getIdEstadoFinal()));
				}
			}
		}
	}

	/**
	 * II 	todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)
	 */
	private List<Arco> getNovosArcosPassoII() {
		adicionaPasso("In�cio passo II - todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)");
		List<Arco> retorno = new ArrayList<Arco>();
		Set<Integer> estadosComFechoEpsilon = mapFechosEpsilonPorEstado.keySet();
		for (Integer estadoComFechoEpsilon : estadosComFechoEpsilon){
			for (Estado estado : automato.getTodosEstados()){
				List<Arco> arcosDoEstado = automato.getArcosPorIdEstado(estado.getId());
				if (arcosDoEstado != null && !arcosDoEstado.isEmpty()){
					for (int contArco = 0; contArco < arcosDoEstado.size(); contArco ++) {
						Arco arco = arcosDoEstado.get(contArco);
						if (arco.getEstadoFinal().getId().equals(estadoComFechoEpsilon)){
							adicionaPasso("Estado destino do arco : " + arco + " � fecho epsilon");
							List<Integer> estadosFechoEpsilon = mapFechosEpsilonPorEstado.get(estadoComFechoEpsilon);
							for (Integer estadoFechoEpsilon : estadosFechoEpsilon) {
								Arco novoArco = new Arco(arco.getEstadoInicial(), automato.getEstadoPorId(estadoFechoEpsilon), arco.getExpressao());
								adicionaPasso("Cria novo arco : " + novoArco);
								retorno.add(novoArco);
							}
						}
					}
				}
			}
		}
		adicionaPasso("Fim passo II - todo arco de A em X gera um arco de A em Y para cada Y no &-fecho(X)");
		return retorno;
	}
	
	/**
	 * III 	todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A
	 */
	private List<Arco> getNovosArcosPassoIII() {
		adicionaPasso("In�cio passo III - todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A");
		List<Arco> retorno = new ArrayList<Arco>();
		Set<Integer> estadosComFechoEpsilon = mapFechosEpsilonPorEstado.keySet();
		for (Integer estadoComFechoEpsilon : estadosComFechoEpsilon){
//			System.out.println("\nEstado com fecho epsilon: " + estadoComFechoEpsilon);
			List<Integer> estadosFechoEpsilon = mapFechosEpsilonPorEstado.get(estadoComFechoEpsilon);
			for (Integer estadoFechoEpsilon : estadosFechoEpsilon) {
				List<Arco> arcosDoEstadoFechoEpsilon = automato.getArcosPorIdEstado(estadoFechoEpsilon);
				if (arcosDoEstadoFechoEpsilon != null && !arcosDoEstadoFechoEpsilon.isEmpty()){
					for (Arco arco : arcosDoEstadoFechoEpsilon) {
						Arco novoArco = new Arco(automato.getEstadoPorId(estadoComFechoEpsilon), arco.getEstadoFinal(), arco.getExpressao());
						adicionaPasso("Cria novo arco : " + novoArco);
						retorno.add(novoArco);
					}
				}
			}
		}
		adicionaPasso("Fim passo III - todo arco de Y em A para qualquer Y no &-fecho(X) gera um arco de X para A");
		return retorno;
	}
	
	/**
	 * Retorna o hist�rico de passos para a cria��o do aut�mato
	 * 
	 * @return
	 */
	public List<Passo> getPassosAutomato() {
		return passosAutomato;
	}
	
	/**
	 * Cria novo passo para guardar no hist�rico
	 * 
	 * @param descricao
	 */
	public void adicionaPasso(String descricao){
		this.passosAutomato.add(new Passo(numeroPassoAutomato++, descricao));
	}
}

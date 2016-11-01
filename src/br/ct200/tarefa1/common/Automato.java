package br.ct200.tarefa1.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ct200.tarefa1.processo.ProcessamentoCadeia;
import br.ct200.tarefa1.processo.ProcessamentoLinguagem;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemConcatenacao;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemKleene;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemParentese;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemUniao;
import br.ct200.tarefa1.util.ProcessamentoCadeiaUtil;
import br.ct200.tarefa1.util.ProcessamentoLinguagemUtil;

public class Automato {
	/** regex de entrada **/
	private String expressaoRegular;
	
	/** map de id estado x estado **/
	private HashMap<Integer, Estado> mapEstadosPorId;
	
	/** map de id estado x arcos do estado **/
	private Map<Integer, List<Arco>> mapArcosPorIdEstado;
	
	/** passos para a cria��o do aut�mato a partir da regex **/
	private List<Passo> passosAutomato = new ArrayList<Passo>();
	private Integer numeroPassoAutomato = 0;
	
	/** passos para a encontrar a regex do aut�mato **/
	private List<Passo> passosRegex = new ArrayList<Passo>();
	private Integer numeroPassoRegex = 0;
	
	/**
	 * Construtor que recebe express�o regular
	 * 
	 * @param expressaoRegular
	 */
	public Automato(String expressaoRegular){
		super();
		Estado.zeraId();
		adicionaPassoAutomato("Iniciando autom�to com regex : " + expressaoRegular);
		this.expressaoRegular = expressaoRegular;
		this.mapArcosPorIdEstado = new LinkedHashMap<Integer, List<Arco>>();
		this.mapEstadosPorId = new LinkedHashMap<Integer, Estado>();
		criaNovoArco(criaNovoEstado(TipoEstadoEnum.INICIAL), criaNovoEstado(TipoEstadoEnum.FINAL), expressaoRegular);
		this.processaAutomato();
	}
	
	/**
	 * Cria novo estado e guarda no mapa
	 * 
	 * @param tipo
	 * @return
	 */
	private Estado criaNovoEstado(TipoEstadoEnum tipo) {
		Estado retorno = new Estado(tipo);
		adicionaPassoAutomato("Cria novo estado : " + retorno);
		mapEstadosPorId.put(retorno.getId(), retorno);
		return retorno;
	}

	/** 
	 * M�todo que cria novo arco para o aut�mato 
	 */
	public void criaNovoArco(Estado estadoInicial, Estado estadoFinal,
			String expressao) {
		criaNovoArco(new Arco(estadoInicial, estadoFinal, expressao));
	}

	public void imprimeAutomatoTeste(){
		System.out.println("\n---- Resultado ----");
		for (Estado estado : mapEstadosPorId.values()){
			List<Arco> arcosDoEstado = mapArcosPorIdEstado.get(estado.getId());
			if (arcosDoEstado != null){
				System.out.println("Arcos do estado (" + estado.getId() + "): ");
				for (Arco arco : arcosDoEstado){
					System.out.println("\t" + arco);
				}
			}
		}
	}
	
	/** 
	 * M�todo que processa aut�mato at� que todos
	 * os arcos tenham apenas um s�mbolo
	 */
	private void processaAutomato() {
		Arco arcoParaProcessar = proximoArcoParaProcessar();
		while (arcoParaProcessar != null){
			processaArco(arcoParaProcessar);
			arcoParaProcessar = proximoArcoParaProcessar();
		}
		adicionaPassoAutomato("Constru��o de Aut�mato conclu�da");
	}

	/**
	 * M�todo que processa arco seguindo os passos:
	 * 
	 * I 	- Uni�o de linguagens a+b
	 * II 	- Concatena��o de linguagens ab, aa, a*b, a(a+b)*
	 * III 	- Fecho de Kleene a*, (ab)*, (a+b)*
	 * IV 	- Express�o entre par�nteses (a+b), ((a+b)*a)
	 * 
	 * @param arcoParaProcessar
	 */
	private void processaArco(Arco arcoParaProcessar) {
		while (arcoParaProcessar.getExpressao().length() > 1){
			ProcessamentoLinguagem processamentoArco = ProcessamentoLinguagemUtil.getTipoProcessamentoLinguagem(arcoParaProcessar);
			if (processamentoArco instanceof ProcessamentoLinguagemUniao){
				ProcessamentoLinguagemUniao uniao = (ProcessamentoLinguagemUniao) processamentoArco;
				adicionaPassoAutomato("Uni�o de linguagens em : " + arcoParaProcessar);
				arcoParaProcessar.setExpressao(uniao.getLinguagemInicial());
				adicionaPassoAutomato("Altera arco : " + arcoParaProcessar);
				criaNovoArco(arcoParaProcessar.getEstadoInicial(), arcoParaProcessar.getEstadoFinal(), uniao.getLinguagemFinal());
			} else if (processamentoArco instanceof ProcessamentoLinguagemConcatenacao){
				ProcessamentoLinguagemConcatenacao concatenacao = (ProcessamentoLinguagemConcatenacao) processamentoArco;
				adicionaPassoAutomato("Concatena��o de linguagens em : " + arcoParaProcessar);
				arcoParaProcessar.setExpressao(concatenacao.getLinguagemInicial());
				Estado estadoFinal = arcoParaProcessar.getEstadoFinal();
				Estado estadoIntermediario = criaNovoEstado(TipoEstadoEnum.COMUM);
				arcoParaProcessar.setEstadoFinal(estadoIntermediario);
				adicionaPassoAutomato("Altera arco : " + arcoParaProcessar);
				criaNovoArco(estadoIntermediario, estadoFinal, concatenacao.getLinguagemFinal());
			} else if (processamentoArco instanceof ProcessamentoLinguagemKleene){
				ProcessamentoLinguagemKleene kleene = (ProcessamentoLinguagemKleene) processamentoArco;
				adicionaPassoAutomato("Fecho de Kleene em : " + arcoParaProcessar);
				Estado estadoFinal = arcoParaProcessar.getEstadoFinal();
				Estado estadoKleene = criaNovoEstado(TipoEstadoEnum.COMUM);
				arcoParaProcessar.setExpressao("&");
				arcoParaProcessar.setEstadoFinal(estadoKleene);
				adicionaPassoAutomato("Altera arco : " + arcoParaProcessar);
				criaNovoArco(estadoKleene, estadoKleene, kleene.getLinguagem());
				criaNovoArco(estadoKleene, estadoFinal, "&");
			} else if (processamentoArco instanceof ProcessamentoLinguagemParentese){
				ProcessamentoLinguagemParentese parentese = (ProcessamentoLinguagemParentese) processamentoArco;
				adicionaPassoAutomato("Remo��o de par�nteses em : " + arcoParaProcessar);
				arcoParaProcessar.setExpressao(parentese.getLinguagem());
			}
		}
	}

	/**
	 * Retorna pr�ximo arco para ser processado, 
	 * i.e. expressao do arco tem mais de um char 
	 * em sua express�o
	 * 
	 * @return
	 */
	private Arco proximoArcoParaProcessar() {
		for (Estado estado : mapEstadosPorId.values()){
			List<Arco> arcosDoEstado = mapArcosPorIdEstado.get(estado.getId());
			if (arcosDoEstado != null){
				for (Arco arco : arcosDoEstado){
					if (arco.getExpressao().length() > 1){
						adicionaPassoAutomato("Arco para processar: " + arco);
						return arco;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Verifica se determinada cadeia � aceita pelo aut�mato
	 * al�m de retornar os poss�veis estados ap�s computa��o da cadeia
	 * 
	 * @param cadeiaParaVerificar
	 * @return
	 */
	public ProcessamentoCadeia processaCadeia(String cadeiaParaVerificar) {
		return ProcessamentoCadeiaUtil.processaCadeia(this, cadeiaParaVerificar);
	}
	
	/**
	 * M�todo que cria novo arco para o aut�mato
	 * 
	 * @param novoArco
	 */
	public void criaNovoArco(Arco novoArco) {
		adicionaPassoAutomato("Cria novo arco : " + novoArco);
		List<Arco> arcosDoEstadoInicial = mapArcosPorIdEstado.get(novoArco.getEstadoInicial().getId());
		if (arcosDoEstadoInicial == null){
			arcosDoEstadoInicial = new ArrayList<Arco>();
		}
		arcosDoEstadoInicial.add(novoArco);
		mapArcosPorIdEstado.put(novoArco.getIdEstadoInicial(), arcosDoEstadoInicial);
	}
	
	/**
	 * Retorna lista de arcos em que o idEstado seja o estado do in�cio do arco
	 * 
	 * @param idEstado
	 * @return
	 */
	public List<Arco> getArcosPorIdEstado(Integer idEstado) {
		return mapArcosPorIdEstado.get(idEstado);
	}
	
	/**
	 * Retorna objeto Estado pelo id
	 * 
	 * @param idEstado
	 * @return
	 */
	public Estado getEstadoPorId(Integer idEstado){
		return mapEstadosPorId.get(idEstado);
	}
	
	/**
	 * Retorna express�o regular inicial de entrada
	 * 
	 * @return
	 */
	public String getExpressaoRegular() {
		return expressaoRegular;
	}
	
	/**
	 * Retorna todos os estados criados para o aut�mato
	 * 
	 * @return
	 */
	public Collection<Estado> getTodosEstados() {
		return mapEstadosPorId.values();
	}

	/**
	 * M�todo que encontra express�o regular a partir do aut�mato &-AFN
	 * removendo estado por estado.
	 * 
	 * @return
	 */
	public String encontraExpressaoRegular() {
		String retorno = null;
		adicionaPassoRegex("Iniciando busca por express�o regular a partir do aut�mato");
		Estado estadoInicial = getEstadoInicial();
		
		adicionaPassoRegex("In�cio remo��o de estados com fecho de Kleene");
		removeFechoKleeneRecursivo(estadoInicial);
		adicionaPassoRegex("Fim remo��o de estados com fecho de Kleene");
		
		adicionaPassoRegex("In�cio remo��o de estados com concatena��o de linguagens");
		removeConcatenacaoRecursivo(estadoInicial);
		adicionaPassoRegex("Fim remo��o de estados com concatena��o de linguagens");
		
		adicionaPassoRegex("In�cio remo��o de estados com uni�o de linguagens");
		removeUniaoRecursivo(estadoInicial);
		adicionaPassoRegex("Fim remo��o de estados com uni�o de linguagens");
		
		retorno = getArcosPorIdEstado(0).get(0).getExpressao();
		adicionaPassoRegex("Somente um arco. Express�o regular encontrada: " + retorno);
		return retorno;
	}
	
	/**
	 * 
	 * @param estadoInicial
	 */
	private void removeUniaoRecursivo(Estado estadoInicial) {
		List<Arco> arcosDoEstadoOrigem = getArcosPorIdEstado(estadoInicial.getId());
		List<Arco> arcosUniaoTemp = new ArrayList<Arco>();
		for (Arco arcoEstadoOrigem : arcosDoEstadoOrigem) {
			if (!arcoEstadoOrigem.getExpressao().equals("&")){
				arcosUniaoTemp.add(arcoEstadoOrigem);
			}
		}
		if (arcosUniaoTemp != null && arcosUniaoTemp.size() > 1){
			Arco primeiroArco = arcosUniaoTemp.get(0);
			for (int contArco = 1; contArco < arcosUniaoTemp.size(); contArco++) {
				Arco proximoArco = arcosUniaoTemp.get(contArco);
				if (proximoArco.getEstadoFinal().equals(primeiroArco.getEstadoFinal())){
					adicionaPassoRegex("Detectada uni�o de linguagens : " + primeiroArco + " e " + proximoArco);
					primeiroArco.setExpressao(primeiroArco.getExpressao() + "+" + proximoArco.getExpressao());
					adicionaPassoRegex("Alterando arco para : " + primeiroArco);
					removeArcoDoEstado(estadoInicial.getId(), proximoArco);
					removeUniaoRecursivo(estadoInicial);
				}
			}
		} else {
			adicionaPassoRegex("N�o foi detectada uni�o de linguagens");
			
		}
	}

	/**
	 * Remove todas as concatena��es recursivametne
	 * 
	 * @param estadoInicial
	 */
	private void removeConcatenacaoRecursivo(Estado estadoInicial) {
		List<Arco> arcosDoEstadoOrigem = getArcosPorIdEstado(estadoInicial.getId());
		if (arcosDoEstadoOrigem != null && !arcosDoEstadoOrigem.isEmpty()){
			for (int contArcoEstadoOrigem = 0; contArcoEstadoOrigem < arcosDoEstadoOrigem.size(); contArcoEstadoOrigem++) {
				Arco arcoEstadoOrigem = arcosDoEstadoOrigem.get(contArcoEstadoOrigem);
				List<Arco> arcosDoEstadoDestino = getArcosPorIdEstado(arcoEstadoOrigem.getIdEstadoFinal());
				if (arcosDoEstadoDestino != null && !arcosDoEstadoDestino.isEmpty()){
					for (Arco arcoEstadoDestino : arcosDoEstadoDestino) {
						if (!arcoEstadoOrigem.getExpressao().equals("&")
						 && !arcoEstadoDestino.getExpressao().equals("&")
						 && !arcoEstadoOrigem.getIdEstadoFinal().equals(arcoEstadoOrigem.getIdEstadoInicial())
						 && !arcoEstadoDestino.getIdEstadoFinal().equals(arcoEstadoDestino.getIdEstadoInicial())){
							if (getArcosPorIdEstado(arcoEstadoDestino.getIdEstadoInicial()).size() > 1){
								removeUniaoRecursivo(arcoEstadoDestino.getEstadoInicial());
							}
							if (getArcosPorIdEstado(arcoEstadoOrigem.getIdEstadoInicial()).size() > 1){
								removeUniaoRecursivo(arcoEstadoOrigem.getEstadoInicial());
							}
							adicionaPassoRegex("Detectada concatena��o de linguagens : " + arcoEstadoOrigem + " e " + arcoEstadoDestino);
							arcoEstadoOrigem.setEstadoFinal(arcoEstadoDestino.getEstadoFinal());
							String primeiraParte = arcoEstadoOrigem.getExpressao();
							if (primeiraParte.contains("+") && !primeiraParte.startsWith("(")){
								primeiraParte = "(" + primeiraParte + ")";
							}
							String segundaParte = arcoEstadoDestino.getExpressao();
							if (segundaParte.contains("+") && !segundaParte.startsWith("(")){
								segundaParte = "(" + segundaParte + ")";
							}
							arcoEstadoOrigem.setExpressao(primeiraParte + segundaParte);
							adicionaPassoRegex("Alterando arco para : " + arcoEstadoOrigem);
							removeEstadoEArcos(arcoEstadoDestino.getIdEstadoInicial());
							removeConcatenacaoRecursivo(arcoEstadoOrigem.getEstadoInicial());
						}
					} 
				}
			}
		}
	}

	/**
	 * Remove todos os fechos de kleene recursivametne
	 * 
	 * @param estadoInicial
	 */
	private void removeFechoKleeneRecursivo(Estado estadoInicial) {
		List<Arco> arcosDoEstadoOrigem = getArcosPorIdEstado(estadoInicial.getId());
		if (arcosDoEstadoOrigem != null && !arcosDoEstadoOrigem.isEmpty()){
			for (Arco arcoEstadoOrigem : arcosDoEstadoOrigem) {
				List<Arco> arcosDoEstadoDestino = getArcosPorIdEstado(arcoEstadoOrigem.getIdEstadoFinal());
				if (arcosDoEstadoDestino != null && !arcosDoEstadoDestino.isEmpty()){
					for (int contArcoDestino = 0; contArcoDestino < arcosDoEstadoDestino.size(); contArcoDestino++) {
						Arco arcoEstadoDestino = arcosDoEstadoDestino.get(contArcoDestino);
						// kleene
						if (arcoEstadoOrigem.getExpressao().equals("&")
						 && arcoEstadoDestino.getExpressao().equals("&")){
							adicionaPassoRegex("Detectado fecho de Kleene nos arcos : " + arcoEstadoOrigem + " e " + arcoEstadoDestino);
							removeConcatenacaoRecursivo(arcoEstadoOrigem.getEstadoFinal());
							removeUniaoRecursivo(arcoEstadoOrigem.getEstadoFinal());
							List<Arco> arcosDoEstadoKleeneSemEpsilon = getArcosSemEpsilonPorIdEstado(arcoEstadoOrigem.getIdEstadoFinal());
							arcoEstadoOrigem.setEstadoFinal(arcoEstadoDestino.getEstadoFinal());
							if (arcosDoEstadoKleeneSemEpsilon.get(0).getExpressao().length() > 1){
								arcoEstadoOrigem.setExpressao("(" + arcosDoEstadoKleeneSemEpsilon.get(0).getExpressao() + ")*");
							} else {
								arcoEstadoOrigem.setExpressao(arcosDoEstadoKleeneSemEpsilon.get(0).getExpressao() + "*");
							}
							adicionaPassoRegex("Alterando arco para : " + arcoEstadoOrigem);
							removeEstadoEArcos(arcoEstadoDestino.getIdEstadoInicial());
						}
					}
					removeFechoKleeneRecursivo(arcoEstadoOrigem.getEstadoFinal());
				}
			}
		}
	}

	/**
	 * Retorna arcos desconsiderando transi��o epsilon de determinado estado
	 * @param idEstado
	 * @return
	 */
	private List<Arco> getArcosSemEpsilonPorIdEstado(Integer idEstado) {
		List<Arco> retorno = new ArrayList<Arco>();
		for (Arco arcoEstadoKleene : mapArcosPorIdEstado.get(idEstado)) {
			if (!arcoEstadoKleene.getExpressao().equals("&")){
				retorno.add(arcoEstadoKleene);
			}
		}
		return retorno;
	}

	/**
	 * M�todo que remove o estado e seus arcos
	 * 
	 * @param idEstadoInicial
	 */
	private void removeEstadoEArcos(Integer idEstado) {
		Estado estadoRemovido = mapEstadosPorId.remove(idEstado);
		adicionaPassoRegex("Removendo estado intermedi�rio : " + estadoRemovido);
		List<Arco> arcosRemovidos = mapArcosPorIdEstado.remove(idEstado);
		for (Arco arcoRemovido : arcosRemovidos) {
			adicionaPassoRegex("Removendo arco do estado intermedi�rio : " + arcoRemovido);
		}
	}
	
	/**
	 * M�todo que remove o estado e seus arcos
	 * 
	 * @param idEstadoInicial
	 */
	private void removeArcoDoEstado(Integer idEstado, Arco arco) {
		adicionaPassoRegex("Removendo arco : " + arco);
		mapArcosPorIdEstado.get(idEstado).remove(arco);
	}

	/**
	 * Retorna estado inicial do aut�mato
	 * 
	 * @return
	 */
	private Estado getEstadoInicial() {
		Estado retorno = null;
		for (Estado estado : getTodosEstados()){
			if (TipoEstadoEnum.INICIAL.equals(estado.getTipo())){
				adicionaPassoRegex("Partindo do estado inicial : " + estado);
				retorno = estado;
			}
		}
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
	 * Cria novo passo para guardar no hist�rico da cria��o do aut�mato
	 * 
	 * @param descricao
	 */
	public void adicionaPassoAutomato(String descricao){
		this.passosAutomato.add(new Passo(numeroPassoAutomato++, descricao));
	}
	
	/**
	 * Retorna o hist�rico de passos para a identifica��o da regex do aut�mato
	 * 
	 * @return
	 */
	public List<Passo> getPassosRegex() {
		return passosRegex;
	}
	
	/**
	 * Cria novo passo para guardar no hist�rico da identifica��o da regex
	 * 
	 * @param descricao
	 */
	public void adicionaPassoRegex(String descricao){
		this.passosRegex.add(new Passo(numeroPassoRegex++, descricao));
	}
}

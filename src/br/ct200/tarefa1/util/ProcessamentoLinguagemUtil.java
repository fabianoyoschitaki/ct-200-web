package br.ct200.tarefa1.util;

import br.ct200.tarefa1.common.Arco;
import br.ct200.tarefa1.common.TipoProcessamentoEnum;
import br.ct200.tarefa1.processo.ProcessamentoLinguagem;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemConcatenacao;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemKleene;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemParentese;
import br.ct200.tarefa1.processo.ProcessamentoLinguagemUniao;

/**
 * Classe que descobre e faz os processamentos da linguagem para os 4 tipos:
 * - uni�o de linguagens
 * - concatena��o de linguagens
 * - fecho de kleene
 * - parenteses 
 * 
 * @author Fabiano
 *
 */
public class ProcessamentoLinguagemUtil {
	
	/**
	 * M�todo que verifica tipo de processamento da linguagem na ordem
	 * de TipoProcessamentoEnum
	 * 
	 * @param linguagemParaProcessar
	 * @return
	 */
	public static ProcessamentoLinguagem getTipoProcessamentoLinguagem(Arco arco) {
		ProcessamentoLinguagem retorno = null;
		for (TipoProcessamentoEnum tipo : TipoProcessamentoEnum.values()){
			retorno = getProcessamentoLinguagem(tipo, arco.getExpressao());
			if (retorno != null){
				break;
			}
		}
		return retorno;
	}

	/**
	 * Aciona m�todo respons�vel de acordo com o tipo de processamento
	 * 
	 * @param tipo
	 * @param expressao
	 * @return
	 */
	private static ProcessamentoLinguagem getProcessamentoLinguagem(
			TipoProcessamentoEnum tipo, String expressao) {
		if (TipoProcessamentoEnum.UNIAO.equals(tipo)){
			return getProcessamentoLinguagemUniao(expressao);
		} else if (TipoProcessamentoEnum.CONCATENACAO.equals(tipo)){
			return getProcessamentoLinguagemConcatenacao(expressao);
		} else if (TipoProcessamentoEnum.KLEENE.equals(tipo)){
			return getProcessamentoLinguagemFechoKleene(expressao);
		} else if (TipoProcessamentoEnum.PARENTESE.equals(tipo)){
			return getProcessamentoLinguagemParentese(expressao);
		}
		return null;
	}

	private static ProcessamentoLinguagem getProcessamentoLinguagemParentese(
			String expressao) {
		ProcessamentoLinguagemParentese retorno = null;
		if (expressao.startsWith("(") && expressao.endsWith(")")){
			retorno = new ProcessamentoLinguagemParentese();
			retorno.setLinguagem(expressao.substring(1, expressao.length()-1));
		}
		return retorno;
	}

	private static ProcessamentoLinguagem getProcessamentoLinguagemFechoKleene(
			String expressao) {
		ProcessamentoLinguagemKleene retorno = null;
		if (expressao.endsWith("*")){
			retorno = new ProcessamentoLinguagemKleene();
			retorno.setLinguagem(expressao.substring(0, expressao.length()-1));
		};
		return retorno;
	}

	/**
	 * M�todo que processa concatena��o de linguagens.
	 * Verifica os seguintes casos:
	 * 
	 * a|a (letra + letra AND parenteses = 0)
	 * a|(b)* (letra + parentese AND parenteses = 0)
	 * (b)*|a ('*' + letra AND parenteses = 0)
	 * (a+b)|a (')' + letra AND parenteses = 0)
	 * (a+b)|(b+c) (')' + '(' AND parenteses = 0)
	 * (a+b)*|(a+c) ('*' + '(' AND parenteses = 0)
	 * 
	 * @param expressao
	 * @return
	 */
	private static ProcessamentoLinguagemConcatenacao getProcessamentoLinguagemConcatenacao(
			String expressao) {
		ProcessamentoLinguagemConcatenacao retorno = null;
		
		char arrayExpressao[] = expressao.toCharArray();
		
		int posicaoConcatenacao = -1;
		int contParenteses = 0;
		
		char charAnterior = 0;
		char charAtual = 0;
		for (int posicaoChar = 0; posicaoChar < arrayExpressao.length; posicaoChar++) {
			charAtual = arrayExpressao[posicaoChar];
			if (posicaoChar != 0){
				/** a|a (letra + letra AND parenteses = 0) **/
				if (Character.isLetter(charAnterior)
				 && Character.isLetter(charAtual)
				 && contParenteses == 0){
					posicaoConcatenacao = posicaoChar;
					break;
				} 
				/** a|(b)* (letra + parentese AND parenteses = 0) **/
				else if (Character.isLetter(charAnterior)
					&& charAtual == '('
					&& contParenteses == 0){
					posicaoConcatenacao = posicaoChar;
					break;
				} 
				/** (b)*|a ('*' + letra AND parenteses = 0) **/
				else if (charAnterior == '*'
					&& Character.isLetter(charAtual)
					&& contParenteses == 0){
					posicaoConcatenacao = posicaoChar;
					break;
				}
				/** (a+b)|a (')' + letra AND parenteses = 0) **/
				else if (charAnterior == ')'
					&& Character.isLetter(charAtual)
					&& contParenteses == 0){
					posicaoConcatenacao = posicaoChar;
					break;
				}
				/** (a+b)|(b+c) (')' + '(' AND parenteses = 0) **/
				else if (charAnterior == ')'
					&& charAtual == '('
					&& contParenteses == 0){
					posicaoConcatenacao = posicaoChar;
					break;
				}
				/** (a+b)*|(a+c) ('*' + '(' AND parenteses = 0) **/
				else if (charAnterior == '*'
					&& charAtual == '('
					&& contParenteses == 0){
					posicaoConcatenacao = posicaoChar;
					break;
				} 
			} 
			if (charAtual == '('){
				contParenteses++;
			} else if (charAtual == ')'){
				contParenteses--;
			}
			charAnterior = charAtual;
		}
		
		if (posicaoConcatenacao != -1){
			retorno = new ProcessamentoLinguagemConcatenacao();
			retorno.setLinguagemInicial(expressao.substring(0, posicaoConcatenacao));
			String linguagemFinal = expressao.substring(posicaoConcatenacao);
			retorno.setLinguagemFinal(linguagemFinal);
		};
		return retorno;
	}

	private static ProcessamentoLinguagemUniao getProcessamentoLinguagemUniao(
			String expressao) {
		ProcessamentoLinguagemUniao retorno = null;
		if (expressao.contains("+")){
			// pega apenas a primeira parte (a+b)*a |+| b(c+d)* e analisa se n�o est� dentro de parenteses
			char arrayExpressao[] = expressao.toCharArray();
			
			int posicaoUniao = -1;
			int parenteses = 0;
			for (int posicaoChar = 0; posicaoChar < arrayExpressao.length; posicaoChar++) {
				if (arrayExpressao[posicaoChar] == '('){
					parenteses++;
				} else if (arrayExpressao[posicaoChar] == ')'){
					parenteses--;
				} else if (arrayExpressao[posicaoChar] == '+' && parenteses == 0){
					posicaoUniao = posicaoChar;
					break;
				}
			}
			
			if (posicaoUniao != -1){
				retorno = new ProcessamentoLinguagemUniao();
				retorno.setLinguagemInicial(expressao.substring(0, posicaoUniao));
				String linguagemFinal = expressao.substring(posicaoUniao+1);
				retorno.setLinguagemFinal(linguagemFinal);
			};
		}
		return retorno;
	}

}

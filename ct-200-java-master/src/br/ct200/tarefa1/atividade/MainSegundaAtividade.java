package br.ct200.tarefa1.atividade;

import java.util.List;

import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.Estado;
import br.ct200.tarefa1.common.GraphvizParser;
import br.ct200.tarefa1.common.Passo;
import br.ct200.tarefa1.processo.ProcessamentoCadeia;

public class MainSegundaAtividade {

	/**
	 * Segunda Atividade, recebe uma express�o regular e uma cadeia de entrada e 
	 * retorna os poss�veis estados ap�s a computa��o da cadeia e se o estado final 1 
	 * pertence a esse conjunto de estados (cadeia aceita ou n�o pelo aut�mato)0 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
		String expressaoRegular = "a*b*c*";
		
		String cadeiaParaVerificar = "ab";
//		String cadeiaParaVerificar = "abb";
//		String cadeiaParaVerificar = "bba";
//		String cadeiaParaVerificar = "abba";
		
		Automato automato = new Automato(expressaoRegular);
		ProcessamentoCadeia resultado = automato.processaCadeia(cadeiaParaVerificar);
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(automato));
		List<Passo> passos = resultado.getPassosProcessamento();
		for (Passo passo : passos) {
			System.out.println(passo);
		}
		System.out.println("Cadeia:" + resultado.getCadeia());
		for (Estado estado : resultado.getEstadosPossiveis()) {
			System.out.println("Estado poss�vel: " + estado);
		}
		if (resultado.isCadeiaAceita()){
			System.out.println("Cadeia aceita.");
		} else {
			System.out.println("Cadeia n�o aceita.");
		}
	}
}

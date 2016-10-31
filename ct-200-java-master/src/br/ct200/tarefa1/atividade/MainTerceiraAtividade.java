package br.ct200.tarefa1.atividade;

import java.util.List;

import br.ct200.tarefa1.common.AutomatoSemEpsilon;
import br.ct200.tarefa1.common.GraphvizParser;
import br.ct200.tarefa1.common.Passo;

public class MainTerceiraAtividade {

	/**
	 * Terceira Atividade, remove as &-transi��es de um aut�mato,
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
	public static void main(String[] args) {
		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "a*b+b*a";
//		String expressaoRegular = "a*b*c*";
		
		System.out.println("Regex: " + expressaoRegular);
		AutomatoSemEpsilon automatoSemEpsilon = new AutomatoSemEpsilon(expressaoRegular);
		List<Passo> passos = automatoSemEpsilon.getPassosAutomato();
		for (Passo passo : passos) {
			System.out.println(passo);
		}
		System.out.println(GraphvizParser.traduzAutomatoParaGraphviz(automatoSemEpsilon.getAutomato()));
	}
}

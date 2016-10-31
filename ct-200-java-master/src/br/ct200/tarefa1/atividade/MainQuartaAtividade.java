package br.ct200.tarefa1.atividade;

import br.ct200.tarefa1.common.Automato;

public class MainQuartaAtividade {

	/**
	 * Quarta Atividade, Dado um &-AFN, encontre a express�o regular 
	 * da linguagem aceita pelo aut�mato. � recomendado criar um estado 
	 * inicial ligado por & ao estado inicial original 0 e sup�e-se que haja 
	 * apenas um estado final 1. 
	 * Implemente o algoritmo que remove estado por estado.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		String expressaoRegular = "(a+b)*";
		String expressaoRegular = "(a+b)*bb(b+a)*";
//		String expressaoRegular = "(a(b+c))*";
//		String expressaoRegular = "((b+c)ab+a)*";
//		String expressaoRegular = "a*b+b*a+a*b*";
//		String expressaoRegular = "a*b*c*";
//		String expressaoRegular = "a*b*c*d*e*ff";
		
		System.out.println("Regex: " + expressaoRegular);
		Automato automato = new Automato(expressaoRegular);
		System.out.println(automato.encontraExpressaoRegular());
	}
}

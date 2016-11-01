package br.ct200.automata.dto;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RetornoAutomatoSemEpsilonDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String graphviz;
	private String regex;
	private List<PassoDTO> passos;
	private List<PassoDTO> passosTransformacao;
	public String getGraphviz() {
		return graphviz;
	}
	public void setGraphviz(String graphviz) {
		this.graphviz = graphviz;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public List<PassoDTO> getPassos() {
		return passos;
	}
	public void setPassos(List<PassoDTO> passos) {
		this.passos = passos;
	}
	public List<PassoDTO> getPassosTransformacao() {
		return passosTransformacao;
	}
	public void setPassosTransformacao(List<PassoDTO> passosTransformacao) {
		this.passosTransformacao = passosTransformacao;
	}
}

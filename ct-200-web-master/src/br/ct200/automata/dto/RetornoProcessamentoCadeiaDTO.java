package br.ct200.automata.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RetornoProcessamentoCadeiaDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String regex;
	private String cadeia;
	private List<PassoDTO> passos;
	private List<EstadoDTO> estadosPossiveis;
	private boolean isCadeiaAceita;
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getCadeia() {
		return cadeia;
	}
	public void setCadeia(String cadeia) {
		this.cadeia = cadeia;
	}
	public List<PassoDTO> getPassos() {
		return passos;
	}
	public void setPassos(List<PassoDTO> passos) {
		this.passos = passos;
	}
	public List<EstadoDTO> getEstadosPossiveis() {
		return estadosPossiveis;
	}
	public void setEstadosPossiveis(List<EstadoDTO> estadosPossiveis) {
		this.estadosPossiveis = estadosPossiveis;
	}
	public boolean isCadeiaAceita() {
		return isCadeiaAceita;
	}
	public void setCadeiaAceita(boolean isCadeiaAceita) {
		this.isCadeiaAceita = isCadeiaAceita;
	}
}

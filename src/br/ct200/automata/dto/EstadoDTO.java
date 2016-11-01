package br.ct200.automata.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import br.ct200.tarefa1.common.TipoEstadoEnum;

@XmlRootElement
public class EstadoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private TipoEstadoEnum tipo;

	public Integer getId() {
		return id;
	}
	public TipoEstadoEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoEstadoEnum tipo) {
		this.tipo = tipo;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
}

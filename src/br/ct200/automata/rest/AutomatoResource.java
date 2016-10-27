package br.ct200.automata.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import br.ct200.automata.dto.PassoDTO;
import br.ct200.automata.dto.RetornoAutomatoDTO;
import br.ct200.tarefa1.common.Automato;
import br.ct200.tarefa1.common.GraphvizParser;
import br.ct200.tarefa1.common.Passo;

@Path("/automato")
@Produces(MediaType.APPLICATION_JSON)
public class AutomatoResource {

  // This method is called if TEXT_PLAIN is request
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "Hello Jersey";
  }

  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
  }

  // This method is called if HTML is request
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String sayHtmlHello() {
    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
  }
  
  @GET
  @Path("transicaoepsilon/{regex}")
  public String getAutomatoTransicoesEpsilon(@PathParam("regex") String regex){
	  RetornoAutomatoDTO retorno = new RetornoAutomatoDTO();
	  retorno.setRegex(regex);
	  Automato automato = new Automato(regex);
	  retorno.setGraphviz(GraphvizParser.traduzAutomatoParaGraphviz(automato));
	  List<PassoDTO> passos = new ArrayList<PassoDTO>();
	  for (Passo passo : automato.getPassosAutomato()){
		  PassoDTO p = new PassoDTO();
		  p.setNumero(passo.getNumero());
		  p.setDescricao(passo.getDescricao());
		  passos.add(p);
	  }
	  retorno.setPassos(passos);
	  return new Gson().toJson(retorno);
  }

} 
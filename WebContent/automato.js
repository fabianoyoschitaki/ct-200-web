$(function(){
    
	var regex_input = $('#regex_input'); // conteudo da regex
	var gerar_automato_btn = $('#gerar_automato_btn'); // botao que dispara geracao do automato
	var result_viz_div = $('#result_viz_div'); // div com a imagem resultante (automato computado a partir da regex)
	var atualizar_automato_viz_btn = $("#atualizar_automato_viz_btn");
//	var graphviz_data = $("#graphviz_data"); // texto viz no textarea
	var passos_div = $("#passos_div");
	
	/** 
	 * funcao que atualiza div com imagem do automato
	 * calculado a partir da regex 
	 **/
	function atualizaImagemAutomato(graphviz_data) {
	    try {
		    result_viz_div.html(Viz(graphviz_data, "svg"));
	    } catch (err){
	        alert("Erro ao gerar autômato: " + err);
	    }
	}
	
	/** 
	 * funcao que atualiza div com passos do automato
	 * calculado a partir da regex 
	 **/
	function atualizaPassos(passos) {
	    try {
	    	passos_div.empty();
	    	for (var passo = 0; passo < passos.length; passo++){
	    		passos_div.append("<p>" + passos[passo]['numero'] + " " + passos[passo]['descricao'] + "</p>");
	    	}
	    } catch (err){
	        alert("Erro ao gerar passos: " + err);
	    }
	}
	
	/** 
	 *  funcao que gera o automato a partir da regex e
	 *  atualiza a imagem e a notação graphviz 
	 */
	function geraAutomatoEAtualizaImagem() {
		$.ajax({url: 'rest/automato/transicaoepsilon/' + regex_input.val(), 
			success: function(result){
//				graphviz_data.val(result['graphviz']);
				atualizaImagemAutomato(result['graphviz']);
				atualizaPassos(result['passos']);
			}
		});
	}

    
//    /**
//     * funcao que gera os nos iniciais da regex
//     */
//    function geraTransicaoInicial(regex){
//        return criaTransicao(
//            {"id" : cont++, "tipo" : "I"},
//            {"id" : cont++, "tipo" : "F"},
//            regex);
//    }
    
//    /**
//     * funcao que cria transicao
//     */
//    function criaTransicao(noInicial, noFinal, valor){
//        var transicao = new Object();
//        transicao["noInicial"] = noInicial;
//        transicao["noFinal"] = noFinal;
//        transicao["valor"] = valor;
//        return transicao;
//    }
    
//    function parseTransicoes(arr){
//        var retorno = "digraph finite_state_machine {" + 
//	    "rankdir=LR;" + 
//	    "size=\"8,5\"";
//	   
//	    var nosFinaisStr = "node [shape = doublecircle]; ";
//	    var transicoesStr = "node [shape = circle];";
//	    
//	    for (var t = 0; t < arr.length; t++){
//	        if (arr[t]["noInicial"]["tipo"] == 'F'){
//	            nosFinaisStr += " " + arr[t]["noInicial"]["id"];
//	        }
//	        if (arr[t]["noFinal"]["tipo"] == 'F'){
//	            nosFinaisStr += " " + arr[t]["noFinal"]["id"];
//	        }
//	        transicoesStr += arr[t]["noInicial"]["id"] + " -> " + arr[t]["noFinal"]["id"] + " [ label = \"" + arr[t]["valor"] + "\" ];";
//	    }
//	    nosFinaisStr += ";"
//	    
//	    retorno += nosFinaisStr + transicoesStr + "}";
//	    return retorno;
//    }
    
	gerar_automato_btn.click(geraAutomatoEAtualizaImagem);
	atualizar_automato_viz_btn.click(atualizaImagemAutomato);
//	atualizar_automato_viz_btn.click();
});
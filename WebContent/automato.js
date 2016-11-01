$(function() {

	// conteudo da regex
	var regex_input = $('#regex_input'); 
	
	// conteudo da cadeia para testar na regex
	var cadeia_input = $('#cadeia_input');
	
	// botao que dispara geracao do automato
	var gerar_automato_btn = $('#gerar_automato_btn');
	
	// div com a imagem resultante (automato computado a partir da regex)
	var result_viz_div = $('#result_viz_div'); 

	// passos da geração do autômato ou transformação ou processamento de cadeia
	var passos_div = $("#passos_div");
	
	// botão para testar uma cadeia
	var testa_cadeia_btn = $("#testa_cadeia_btn");

	// valor da regex atual
	var regex_atual;
	
	// se o automato é sem transicao epsilon
	var is_sem_transicao_epsilon;
	
	/**
	 * funcao que atualiza div com imagem do automato calculado a partir da
	 * regex
	 */
	function atualizaImagemAutomato(graphviz_data) {
		try {
			result_viz_div.html(Viz(graphviz_data, "svg")).fadeIn("slow");
			result_viz_div.hide().fadeIn(1000);
		} catch (err) {
			alert("Erro ao gerar autômato: " + err);
		}
	}

	/**
	 * funcao que atualiza div com passos do automato calculado a partir da
	 * regex
	 */
	function atualizaPassos(passos) {
		try {
			var table = $("<table>").addClass("table table-striped table-bordered table-curved").appendTo(passos_div);
			table.append($("<thead>").append($("<tr>")
				.append($("<th>").text("Passo").width("10%"))
				.append($("<th>").text("Descrição").width("90%"))));
			
			var tbody = $("<tbody>").appendTo(table);
			for (var passo = 0; passo < passos.length; passo++) {
				tbody.append($("<tr>")
					.append($("<td>").addClass("center").text(passos[passo]['numero']))
					.append($("<td>").text(passos[passo]['descricao'])));
			}
		} catch (err) {
			alert("Erro ao gerar passos: " + err);
		}
	}
	
	/**
	 * funcao que atualiza div com passos do processamento de cadeia
	 */
	function atualizaResultadoCadeia(isCadeiaAceita, passos) {
		try {
			passos_div.empty();
			passos_div.append($("<h4>")
				.text("Cadeia " + (isCadeiaAceita? " foi " : " não foi ") + " aceita.")
				.addClass("center")
				.css('color', (isCadeiaAceita? "green" : "red")));
			passos_div.append($("<h3>").text("Passos do processamento da cadeia: \"" + cadeia_input.val() + "\"").addClass("center"));
			atualizaPassos(passos);
			passos_div.hide().fadeIn(500);
		} catch (err) {
			alert("Erro ao atualizaResultadoCadeia: " + err);
		}
	}
	
	/**
	 * funcao que atualiza div com passos do processamento do automato
	 */
	function atualizaResultadoAutomato(passos, graphviz) {
		try {
			passos_div.empty();
			passos_div.append($("<h3>").text("Passos da criação do Autômato: " + regex_atual).addClass("center"));
			atualizaPassos(passos);
			passos_div.hide().fadeIn(500);
			atualizaImagemAutomato(graphviz);
			$("#testa_cadeia_div").hide().fadeIn(1000);
		} catch (err) {
			alert("Erro ao atualizaResultadoAutomato: " + err);
		}
	}
	
	/**
	 * funcao que atualiza div com passos do processamento do automato sem Epsilon
	 */
	function atualizaResultadoAutomatoSemEpsilon(passos, passosTransformacao, graphviz) {
		try {
			passos_div.empty();
			passos_div.append($("<h3>").text("Passos da criação do Autômato: " + regex_atual).addClass("center"));
			atualizaPassos(passos);
			passos_div.append($("<h3>").text("Remoção das transições epsilon").addClass("center"));
			atualizaPassos(passosTransformacao);
			passos_div.hide().fadeIn(500);
			atualizaImagemAutomato(graphviz);
			$("#testa_cadeia_div").hide().fadeIn(500);
		} catch (err) {
			alert("Erro ao atualizaResultadoAutomato: " + err);
		}
	}

	/**
	 * funcao que gera o automato a partir da regex e atualiza a imagem e a
	 * notação graphviz
	 */
	function processaAutomato() {
		regex_atual = regex_input.val();
		is_sem_transicao_epsilon = $("#is_sem_transicao_epsilon").is(':checked');
		if (is_sem_transicao_epsilon){
			$.ajax({
				url : 'rest/automato/transicaosemepsilon/' + regex_atual,
				success : function(result) {
					atualizaResultadoAutomatoSemEpsilon(result['passos'], result['passosTransformacao'], result['graphviz']);
				}
			});
		} else {
			$.ajax({
				url : 'rest/automato/transicaoepsilon/' + regex_atual,
				success : function(result) {
					atualizaResultadoAutomato(result['passos'], result['graphviz']);
				}
			});
		}
	}
	
	/**
	 * funcao processa cadeia no automato
	 */
	function processaCadeia() {
		$.ajax({
			url : 'rest/automato/processacadeia/' + regex_atual + '/' + cadeia_input.val(),
			success : function(result) {
				atualizaResultadoCadeia(result['isCadeiaAceita'], result['passos']);
			}
		});
	}

	gerar_automato_btn.click(processaAutomato);
	testa_cadeia_btn.click(processaCadeia);
});
//////////////START Green-Turtle/////////////////////////////////////////////////////////////////////////////////////////////
console.log("RDFa Live start...");

primaryProcess = true;

RDFa.attach(document);
//document.addEventListener('rdfa.loaded', function() {if(r2d2==1)startRLE(); r2d2++}, false);
startRLE();

// recebe uma requisicao do background.js e responde com o array de triplas
chrome.runtime.onMessage.addListener(
	function(request, sender, sendResponse) {
		if (request.requestBG){
			primaryProcess = false;
			startRLE();
			sendResponse({triples: listaObjTriplas});
		}
	}
);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// variaveis globais
var listaSujeitosURI; // uma lista contendo todos as URIs de sujeitos da pagina
var listaSujeitosComp; // uma lista contendo todos os sujeito da pagina em formato de objeto javascript (o objeto possui o sujeito e os predicados e os objetos desse sujeito)
var listaPredicados = [[]]; // lista com as URIs de todos os perdicados da pagina
var listaObjetos = [[]]; // lista com todos os objetos da pagina em formato de objeto javascript 
var listaArrayTriplas = []; // uma lista com todas as triplas da pagina em formato de array de strings
var listaObjTriplas = []; // uma lista com todas as triplas da pagina em formato de objeto javascript

// usa a API do green turtle para iniciar a identificacao e recuperacao de triplas 
function startRLE(){
	listaSujeitosURI = document.data.getSubjects();
	if(listaSujeitosURI.length > 0){
		if (primaryProcess) console.log("RDFa Live on fire");
		listaSujeitosComp = new Array(listaSujeitosURI.length); // cria um array do tamanho da lista de URIs dos sujeitos. Ele vai os elementos para cada sujeito (predicados e objetos)
		listaPredicados = new Array();
		listaObjetos = new Array();
		listaArrayTriplas = new Array();
		listaObjTriplas = new Array();
		getTriples();
	}
	else{
		if (primaryProcess) console.log("RDFa Live found no triples");
	}
}

// monta as triplas
function getTriples(){
	var m=0; n=0; o=0;
	for(var i=0; i<listaSujeitosURI.length; i++){ // itera na lista de URIs dos sujeitos
		listaSujeitosComp[i] = document.data.getSubject(listaSujeitosURI[i]); // recebe a lista de objetos que representa cada sujeito (cada objeto contem predicado, objeto, elemento html de cada sujeito)
		listaPredicados[o] = getKeiesByObj(listaSujeitosComp[i].predicates); // recebe a lista de predicado para cada sujeito
		for(var j=0; j<listaPredicados[o].length; j++){ // itera na lista dos predicados de cada sujeito
			listaObjetos[n] = listaSujeitosComp[i].predicates[listaPredicados[o][j]].objects; // recebe a lista de objetos correspondentes a cada predicado de cada sujeito
			for(var l=0; l<listaObjetos[n].length; l++){ // itera na lista de objetos de cada predicado de cada sujeito
				//listaArrayTriplas[m] = listaSujeitosComp[i].id +"|;|"+ listaPredicados[o][j] +"|;|"+ listaObjetos[n][l].value; // preenche a lista de triplas (sujeito|;|predicado|;|objeto)
				listaObjTriplas[m] = new Object({tripleSubject:listaSujeitosComp[i].id, triplePredicate:listaPredicados[o][j], tripleObject: listaObjetos[n][l].value}); // preenche a lista de objetos com triplas
				m++;
			}
			n++;
		}
		o++;
	}
	
	// envia uma mensagem para background.js contendo a confirmacao da existencia de triplas
	chrome.extension.sendRequest({ harvestedTriples: true });
	
	//console.log(listaObjTriplas);
	if (primaryProcess) console.log("RDFa Live found "+listaObjTriplas.length+" triples");
}

// retorna um array contendo todas as chaves de um objeto javascript (apenas as chaves, nao os valores)
function getKeiesByObj(obj){
	var arrayKey = [];
	var i = 0;
	for(var key in obj){
		arrayKey[i] = key;
		i++;
	}
	return arrayKey;
}
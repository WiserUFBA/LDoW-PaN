var listaObjTriplasBG = [];

try {
  // recebe a mensagem do content script confirmando a presença de triplas e libera o icone da extensao na barra do browser
  chrome.extension.onRequest.addListener(
  function(request, sender, sendResponse) {
	if (request.harvestedTriples) {
		chrome.pageAction.show(sender.tab.id);
        sendResponse({});
    }
  });
  
  // quando o usuario clica no icone...
  chrome.pageAction.onClicked.addListener(function(pageTab) {
	var url = chrome.extension.getURL("index.html"); // cria uma URL para a pagina principal index.html
	
	// envia uma requisicao para o content script pedindo as triplas da pagina
	chrome.tabs.sendMessage(pageTab.id, {requestBG: true}, function(response) {
		listaObjTriplasBG = response.triples; // recebe a resposta do content script com as triplas
	});
	
	// cria a aba de resultado
	chrome.tabs.create({"url": url, "active": true}, function(newTab){ // cria a aba
		setTimeout(function(){ // espera a aba ser criada para poder enviar a mensagem
				chrome.tabs.sendMessage(newTab.id, {viewerInit: true, url: pageTab.url, id: pageTab.id, triples: listaObjTriplasBG});
		},1000);
	});
  });
} catch (ex) {
   console.log("Error setting up rdfa extension: "+ex);
}

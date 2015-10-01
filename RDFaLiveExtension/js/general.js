//////////////START/////////////////////////////////////////////////////////////////////////////////////////////
$(document).ready(function () {
	var PNLayout = $('body').layout({ applyDefaultStyles: true });
	PNLayout.sizePane("west", 235);
	PNLayout.sizePane("north", 127);
	PNLayout.close("south");
	openModal();
});

var RLE_triples = [];

window.addEventListener("load",
	function() {
		chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
			if (request.viewerInit){
				//document.getElementById('midiaGallery').innerHTML="<label style='color: navy;'> RDFa Live Extension get "+request.triples.length+ " triples from "+request.url;
				RLE_triples = request.triples;
				extansionLoaded(request.url); // inicia todo o trabalho
			}
		});
	},false
);

// todo o trabalho feito com a extensao deve estar dentro dessa funcao (depois do carregamento da extensao)
function extansionLoaded(url){
	startPresentNav(RLE_triples, url, 
			function(data){
				if(data != null){
					if(data.responseOk === "true"){
						createBasicInfo("basicInfo", data.box.referential); // cria info basicas
						createRelatedLinks("relatedSites", data.box.referential); // cria links relacionados
						createGallery("midiaGallery", data.gallery); // cria a galeria de midias 
						createRelatedResources("listResources", data.box); // cria a lista de recursos relacionados
						createAbstractEng("abstractEng", data.box.referential);
						createRelatedInfos("relatedInfos", data.headline, "createRelatedInfos_accordion");
						createInteractInputs("interactInputPred", data.box);
						createSimilarList("similarList", data.box);
					}
					else{
						console.log(data.info);
						info = "Sorry! An error occurred during the processing of information: <br><br>"+data.info+"<br><br> Try again later... ";
						dialogMsg("dialog-message", info, "Error Message");
					}
				}
				else{
					info = "Sorry! An error occurred during communication. Try again later... ";
					dialogMsg("dialog-message", info, "Error Message");
				}
				closeModal();
			}
	);
}
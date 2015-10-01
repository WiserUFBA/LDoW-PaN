var internalGallery2 = null;
var inputPredDroppableContent = "";
var tooltipDataBoxContentList = [];

var testTip;

function startPresentNav(triples, homepage, cbFunction){
	RLApost("startPresentNav", triplesObjToJSON(triples, homepage),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getTNet(triples, homepage, cbFunction){
	RLApost("getTNet", triplesObjToJSON(triples, homepage),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getBox(tNet, atom, cbFunction){
	RLApost("getBox", JSON.stringify({tNet:tNet, atom:atom}),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getOutList(box, pred, cbFunction){
	RLApost("getOutList", JSON.stringify({box:box, pred:pred}),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getInList(box, pred, cbFunction){
	RLApost("getInList", JSON.stringify({box:box, pred:pred}),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getSimilarList(pred, endpoint, objs, referential, cbFunction){
	RLApost("getSimilarList", JSON.stringify({pred:pred, endpoint:endpoint, objs:objs, referential:referential}),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getGallery(atom, cbFunction){
	RLApost("getGallery", JSON.stringify(atom),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getGalleryLD(URI, cbFunction){
	RLApost("getGalleryLD", JSON.stringify(URI),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getHeadline(atom, cbFunction){
	RLApost("getHeadline", JSON.stringify(atom),
		function(data, textStatus){
			cbFunction(data);
		});
}

function getTipContent(URI, cbFunction){
	RLApost("getTipContent", JSON.stringify(URI),
		function(data, textStatus){
			cbFunction(data);
		});
}

//carregamento da galeria prettyPhoto
function prettyPhotoLoad(){
	$(document).ready(function(){
		$("a[rel^='prettyPhoto']").prettyPhoto();
	});
}

function accordionLoad(accordionId){
	$(function() {
		$( "#"+ accordionId).accordion();
	});
}

function createSimilarList(elementId, box){
	var pred = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"; // por tipo
	var endpoint = "http://dbpedia.org/sparql"; // endpoint dbpedia
	var objs = getObjValueByPred(pred, box);
	var referential = box.referential;
	
	divSimilar = document.createElement('div');
	divSimilar.setAttribute('id', 'similarListByType'); 
	divSimilar.setAttribute('style', 'padding-top: 15px;');
	
	document.getElementById(elementId).appendChild(divSimilar);
	
	divSimilar.innerHTML = "<label class='box_referential_atomType'>Looking for Similar Entities</label><br> <img src='images/tooltip_loader.gif'></img>";
	
	getSimilarList(pred, endpoint, objs, referential, function(data){
		if(data != null){
			if(data.responseOk === "true"){
				html = "<label class='box_referential_atomType'>Similar Entities by Type </label><label class='labelInputdroppable'>"+data.list.reference.atomType+":</label><br>";
				if(data.list.connectElements != undefined){
					for(var i=0; i<data.list.connectElements.length; i++){
						html = html + "<a target='_blank' class='aLinkPreventClick' href='"+data.list.connectElements[i].atomUID+"' title='"+data.list.connectElements[i].atomUID+"'>"+data.list.connectElements[i].atomLabel+"</a><br>";
					}
				}
				else{
					html = html + "<label class='labelInputdroppable'>No similar entity was found for a particular type. You can try again later.</label>";
				}
				
				html = html + "<div style='text-align: center;'><a class='clickHere aLinkSimilarList' href='#'>More Similar Entities</a></div>";
				
				divSimilar.innerHTML = html;
				
				var aLink = document.getElementsByClassName("aLinkSimilarList");
				for(var i=0; i<aLink.length; i++){
					aLink[i].onclick = function(){
						createSimilarList(elementId, box);
				    };
				}
			}
			else{
				console.log(data.info);
				info = "Sorry! An error occurred during the processing of information: <br><br>"+data.info+"<br><br> Try again later... ";
				//dialogMsg("dialog-message", info, "Error Message", null);
			}
		}
		else{
			info = "Sorry! An error occurred during communication. Probably, the server is down. Try again in few minutes!";
			//dialogMsg("dialog-message", info, "Error Message", null);
		}
	});
}

function getObjValueByPred(pred, box){
	var resourceList = new Array();
	var count = 0;
	
	for(var i=0; i<box.outPredicates.length; i++){
		if(box.outPredicates[i].key === "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"){
			value = box.outPredicates[i].value.atomUID;
			if(value.indexOf("Thing") == -1 && value.indexOf("http://dbpedia.org/ontology/Agent") == -1 && value.indexOf("http://dbpedia.org/ontology/Person") == -1 && value.indexOf("http://dbpedia.org/ontology/Organisation") == -1 && value.indexOf("http://dbpedia.org/ontology/Place") == -1){
				resourceList[count] = box.outPredicates[i].value.atomUID;
				count++;
			}
		}
	}
	
	return resourceList;
}

function createGallery(elementId, gallery){
	// construcao da galeria de imagens
	if(gallery.media.length > 0){
		var htmlGallery = "<div class='box_gallery_media'>";
		for(var i=0; i<gallery.media.length; i++){
			if(i<6){
				img = "<img class='box_imgGalleryMedia' src='"+gallery.media[i].mediaURLAux+"' width='60' height='60' alt='"+gallery.media[i].mediaCaption+"'></img>";
			}
			else{
				img = "<img style='display: none;' src='"+gallery.media[i].mediaURLAux+"' width='60' height='60' alt='"+gallery.media[i].mediaCaption+"'></img>";
			}
			htmlGallery = htmlGallery + "<a href='"+gallery.media[i].mediaURL+"' rel='prettyPhoto[glr1]' title='"+gallery.media[i].mediaInfo+"'>"+img+"</a>";
		}
		
		// criacao do link para mais imagens relacionadas ao recurso
		if(gallery.atomUID.indexOf("http://dbpedia.org/resource/") != -1) // se eh recurso do dbpedia...
			// o link para mais recursos estava dando erro, pois nao estava retornando imagens relacionadas porque a base consultada parou de dar suporte
			//htmlGallery = htmlGallery + "<br><label id='labelLinkRelatedImages' class='clickHere' title='Click here for more related images'><a id='aLinkRelatedImages' href='#'>More related images</a></label>";
		htmlGallery = htmlGallery + "</div>";
		document.getElementById(elementId).innerHTML = htmlGallery; // coloca a galeria no DOM
		prettyPhotoLoad(); // carrega a galeria de imagens
		
		/*
		// o link para mais recursos estava dando erro, pois nao estava retornando imagens relacionadas porque a base consultada parou de dar suporte
		if(gallery.atomUID.indexOf("http://dbpedia.org/resource/") != -1){
			var labelLink = document.getElementById("aLinkRelatedImages");
			labelLink.onclick = function(){
				openModal();
				createLinkMoreRelatedImages(gallery.atomUID); //funcao pra chamar a lista de fotos flickrLD
		    };
		}
		*/
	}
	else{
		document.getElementById(elementId).innerHTML = "No images found";
	}
}

function createLinkMoreRelatedImages(URI){
	if(internalGallery2 === null){
		getGalleryLD(URI, function(data){
			if(data != null){
				if(data.responseOk === "true"){
					internalGallery2 = data.gallery;
					element = document.getElementById("labelLinkRelatedImages");
					for(var i=0; i<internalGallery2.media.length; i++){
						img = "<img style='display: none;' alt='"+internalGallery2.media[i].mediaCaption+"'></img>";
						mediaInfo = cleanMediaInfo(internalGallery2.media[i].mediaInfo);
						if(i==0)
							linkHTML = "<a id='aFirstSerieGlr2' href='"+internalGallery2.media[i].mediaURL+"' rel='prettyPhoto[glr2]' title='"+mediaInfo+"'>"+img+"</a>";
						else
							linkHTML = "<a href='"+internalGallery2.media[i].mediaURL+"' rel='prettyPhoto[glr2]' title='"+mediaInfo+"'>"+img+"</a>";
						var newLink = document.createElement('a');
						newLink.innerHTML = linkHTML;
						element.appendChild(newLink);
					}
					prettyPhotoLoad();
					document.getElementById("aFirstSerieGlr2").click();
				}
				else{
					console.log(data.info);
					info = "Sorry! An error occurred during the processing of information: <br><br>"+data.info+"<br><br> Try again... ";
					//dialogMsg("dialog-message", info, "Error Message", null);
				}
			}
			else{
				info = "Sorry! An error occurred during communication. Probably, the server is down. Try again in few minutes!";
				//dialogMsg("dialog-message", info, "Error Message", null);
			}
			closeModal();
		});
	}
	else{
		closeModal();
		document.getElementById("aFirstSerieGlr2").click();
	}
}

function cleanMediaInfo(info){
	var result = info.replace(/"/g, "");
	result = result.replace(/'/g, "");
	result = result.replace(/</g, "");
	result = result.replace(/>/g, "");
	
	return result;
}

function createBasicInfo(elementId, atom){
	htmlInfo = "<div class='pnbox_block_info'><label class='box_referential_atomLabel'>"+atom.atomLabel+"</label><br><hr>";
	if(atom.atomType === "Person")
		htmlInfo = htmlInfo + getPersonInfo(atom.atomLiterals); else
	if(atom.atomType === "Organization")
		htmlInfo = htmlInfo + getOrganizationInfo(atom.atomLiterals); else
	if(atom.atomType === "Place")
		htmlInfo = htmlInfo + getPlaceInfo(atom.atomLiterals); else
	htmlInfo = htmlInfo + getUnknowsInfo(atom.atomLiterals);
	
	htmlInfo = htmlInfo + "</div>";
	document.getElementById(elementId).innerHTML = htmlInfo; // coloca a info basica no DOM
}

function createRelatedLinks(elementId, atom){
	htmlInfo = "<div class='pnbox_block_info'>";
	var atomLiterals = atom.atomLiterals;
	var count1 = 0;
	var str1 = "";
	for(var i=0; i<atomLiterals.length; i++){
		if(count1<4){
			if(count1<4 && atomLiterals[i].litPredicate.indexOf("wikiPageExternalLink") != -1){
				str1 = str1 + "<label class='box_relatedLink' title='"+atomLiterals[i].litValue+"'><a href='"+atomLiterals[i].litValue+"' target='_blank'>"+atomLiterals[i].litValue.substring(0,30)+"...</a></label><br>";
				count1++;
			}
		}
		else{
			break;
		}
	}
	htmlInfo = htmlInfo + str1 + "<label id='labelLinkRelatedSites' class='clickHere' title='Click here for more related sites'><a href='#'>More related sites</a></label>";
	htmlInfo = htmlInfo + "</div>";
	document.getElementById(elementId).innerHTML = htmlInfo; // coloca a info basica no DOM
	
	var labelLink = document.getElementById("labelLinkRelatedSites");
	labelLink.onclick = function(){
		showRelatedSites(atom);
    };
}

function showRelatedSites(atom){
	var atomLiterals = atom.atomLiterals;
	var newDiv = document.createElement('div');
	newDiv.setAttribute('id', 'dialogSites'); 
	newDiv.setAttribute('style', 'display: none;');
	var str = "";
	for(var i=0; i<atomLiterals.length; i++){
		if((atomLiterals[i].litValue.indexOf("http://") != -1 || atomLiterals[i].litValue.indexOf("https://") != -1) && (atomLiterals[i].litPredicate.indexOf("Page") != -1 || atomLiterals[i].litPredicate.indexOf("page") != -1 || atomLiterals[i].litPredicate.indexOf("site") != -1)){
			str = str + "<div class='box_relatedLink'><label><a href='"+atomLiterals[i].litValue+"' target='_blank'>"+atomLiterals[i].litValue+"</a></label></div>";
		}
	}
	newDiv.innerHTML = str;
	document.getElementsByTagName('body')[0].appendChild(newDiv);
	dialogMsg("dialogSites", str, "Related Websites to " + atom.atomLabel, null);
}

function getPersonInfo(atomLiterals){
	var birthNameOk = false, shortDescriptionOK = false, birthDateOK = false;
	var str1 = "", title = "";
	for(var i=0; i<atomLiterals.length; i++){
		if(!birthNameOk || !birthDateOK || !shortDescriptionOK){
			if(!birthNameOk && atomLiterals[i].litPredicate.indexOf("birthName") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; birthNameOk = true;
			}
			if(!birthDateOK && atomLiterals[i].litPredicate.indexOf("birthDate") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; birthDateOK = true;
			}
			if(!shortDescriptionOK && atomLiterals[i].litPredicate.indexOf("shortDescription") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; shortDescriptionOK = true;
			}
			if(atomLiterals[i].litPredicate.indexOf("title") != -1){
				title = "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>";
			}
		}
		else{
			break;
		}
	}
	if(!shortDescriptionOK){
		str1 = str1 + title;
	}
	return str1;
}

function getOrganizationInfo(atomLiterals){
	// criar a apresentacao para organizacao
	var cityOK = false, countryOk = false, nameOk = false, latOk = false, longOk = false;
	var str1 = "";
	for(var i=0; i<atomLiterals.length; i++){
		if(!cityOK || !countryOk || !nameOk || !latOk || !longOk){
			if(!nameOk && atomLiterals[i].litPredicate.indexOf("name") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; nameOk = true;
			}
			if(!countryOk && atomLiterals[i].litPredicate.indexOf("country") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; countryOk = true;
			}
			if(!cityOK && atomLiterals[i].litPredicate.indexOf("city") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; cityOK = true;
			}
			if(!latOk && atomLiterals[i].litPredicate.indexOf("#lat") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>lat: "+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; latOk = true;
			}
			if(!longOk && atomLiterals[i].litPredicate.indexOf("#long") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>long: "+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; longOk = true;
			}
		}
		else{
			break;
		}
	}
	return str1;
}

function getPlaceInfo(atomLiterals){
	// criar a apresentacao para lugar
	var nameOk = false, subdivisionNameOK = false, pushpinMapOk = false, latOk = false, longOk = false;
	var str1 = "";
	for(var i=0; i<atomLiterals.length; i++){
		if(!nameOk || !subdivisionNameOK || !pushpinMapOk || !latOk || !longOk){
			if(!nameOk && atomLiterals[i].litPredicate.indexOf("name") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; nameOk = true;
			}
			if(!subdivisionNameOK && atomLiterals[i].litPredicate.indexOf("subdivisionName") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; subdivisionNameOK = true;
			}
			if(!pushpinMapOk && atomLiterals[i].litPredicate.indexOf("pushpinMap") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; pushpinMapOk = true;
			}
			if(!latOk && atomLiterals[i].litPredicate.indexOf("#lat") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>lat: "+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; latOk = true;
			}
			if(!longOk && atomLiterals[i].litPredicate.indexOf("#long") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>long: "+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; longOk = true;
			}
		}
	}
	return str1;
}

function getUnknowsInfo(atomLiterals){
	// criar a apresentacao para generico
	var officialNamesOk = false, commentOK = false;
	var str1 = "";
	for(var i=0; i<atomLiterals.length; i++){
		if(!officialNamesOk || !commentOK){
			if(!officialNamesOk && atomLiterals[i].litPredicate.indexOf("officialNames") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; officialNamesOk = true;
			}
			if(!commentOK && atomLiterals[i].litPredicate.indexOf("comment") != -1){
				str1 = str1 + "<label class='box_referential_atomType'>"+decode_utf8(atomLiterals[i].litValue)+"</label><br>"; commentOK = true;
			}
		}
	}
	return str1;
}

function arrayContains(array, element){
	for(var i=0; i<array.length; i++){
		if(array[i].element != undefined){
			return i;
		}
	}
	return -1;
}

function createRelatedResources(elementId, box){
	var key, obj={}; // esse objeto contem todos os predicados da lista e a quantidade de recursos por predicado
	var icon = ""; var count = 1;
	htmlInfo = "<div id='"+elementId+"intern'><label class='box_referential_atomType'>"+box.referential.atomLabel+": </label><br><div>";
	document.getElementById(elementId).innerHTML = htmlInfo; // coloca a info basica no DOM
	
	if(box.outPredicates != undefined){
		for(var i=0; i<box.outPredicates.length; i++){ // lista de predicados de saida
			key = box.outPredicates[i].key;
			if(obj[key] == undefined){
				obj[key] = 1;
				icon = getIconRelatedResource(box.outPredicates[i].value.atomType);
				htmlInfo = "<div class='divRelatedResources'><span class='listRelatedResources'><i><a class='aLinkPreventClick' href='"+box.outPredicates[i].key+"'>"+box.outPredicates[i].labelKey+"</a></i><label id='"+key+"'></label></span><div style='text-align:right;'><a href='"+box.outPredicates[i].value.atomUID+"'><img id='ptooltip-"+count+"RelatedResources1' class='tooltipTarget' src='"+icon+"'></img></a></div></div>";
				$("#"+elementId+"intern").append(htmlInfo);
				anchorTooltipAdvanced("#ptooltip-"+count+"RelatedResources1", "img.tooltipTarget", box.outPredicates[i].value);
				count++;
			}
			else{
				obj[key] = obj[key] + 1; // incrementa a quantidade de recursos para o predicado
			}
		}
	}
	
	if(box.inPredicates != undefined){
		for(var i=0; i<box.inPredicates.length; i++){ // lista de predicados de entrada
			key = box.inPredicates[i].key;
			if(obj[key] == undefined){
				obj[key] = 1;
				icon = getIconRelatedResource(box.inPredicates[i].value.atomType);
				htmlInfo = "<div class='divRelatedResources'><div class='pnbox_block_info'><a href='"+box.inPredicates[i].value.atomUID+"'><img id='ptooltip-"+count+"RelatedResources1' class='tooltipTarget' src='"+icon+"'></img></a></div><span style='font-size:small; float:right; vertical-align:top; margin-top:15px;'><i><a class='aLinkPreventClick' href='"+box.inPredicates[i].key+"'>"+box.inPredicates[i].labelKey+"</a></i><label id='"+key+"'></label></span></div>";
				$("#"+elementId+"intern").append(htmlInfo);
				anchorTooltipAdvanced("#ptooltip-"+count+"RelatedResources1", "img.tooltipTarget", box.inPredicates[i].value);
				count++;
			}
			else{
				obj[key] = obj[key] + 1;
			}
		}
	}
	
	for (var k in obj){ // intera no objeto e recupera, para cada predicado, a quantidade de recursos
		document.getElementById(k).innerHTML = " ("+obj[k]+")"; // insere a quantidade no label com o id igual ao predicado
	}
	
	$(".aLinkPreventClick").click(function( event ) {
		event.preventDefault();
	});
}

function getIconRelatedResource(type){
	var icon = "";
	if(type != null){
		if(type.indexOf("Person") != -1)        icon = "images/icon_person.png";       else 
		if(type.indexOf("Organisation") != -1)  icon = "images/icon_organization.png"; else 
		if(type.indexOf("Place") != -1)         icon = "images/images/icon_place.png"; else 
		icon = "images/icon_unknows.png";
	}
	else{
		icon = "images/icon_unknows.png";
	}
	return icon;
}

function anchorTooltipAdvanced(ident, elementTarget, dataContentAtom){
	var tipContent = preparesContent(dataContentAtom, ident);
	$(ident).tooltip({
	    items: elementTarget,
	    content: tipContent,
	    position: { my: "left+15 center", at: "right center" },
	    show: null, // show immediately
	    open: function(event, ui){
	        if (typeof(event.originalEvent) === 'undefined'){
	            return false;
	        }
	        var $id = $(ui.tooltip).attr('id');
	        
	        // close any lingering tooltips
	        $('div.ui-tooltip').not('#' + $id).remove();
	        
	        // ajax function to pull in data and add it to the tooltip goes here
	        
	        if(ui.tooltip[0].innerHTML.indexOf("untypedEntity_basicAtom") != -1){
				divTip = ui.tooltip[0];
				divContent = divTip.getElementsByClassName("ui-tooltip-content")[0];
				img = document.createElement('img');
				img.setAttribute('src', 'images/tooltip_loader.gif');
				divContent.appendChild(img);
				// recupera infos do servico e altera o conteudo do tooltip 
				// chama o metodo passando a uri: dataContentAtom.atomUID;
				getTipContent(dataContentAtom.atomUID, 
					function(data){
						if(data != null){
							if(data.responseOk === "true"){
								$("#"+event.currentTarget.id).tooltip({ content: preparesContent(data.box.referential, event.currentTarget.id) });
								// captura o clique no link MoreInfo do tooltip
								tooltipDataBoxContentList.push(data);
								var tipsMoreInfosLink = document.getElementById("showTipsMoreInfos"+codeURIQuotes(data.box.referential.atomUID));
								tipsMoreInfosLink.onclick = function(){
									showTipsMoreInfos(data.box);// funcao que eh chamada no clique do link
							    };
							}
							else{
								console.log(data.info);
								info = "Sorry! An error occurred during the processing of information: <br><br>"+data.info+"<br><br> Try again later... ";
								img.setAttribute('src', 'images/icon_error.png');
								lb = document.getElementById("tipLookingForInformation");
								lb.innerHTML = " 404 error ";
								
								//dialogMsg("dialog-message", info, "Error Message 1", null);
							}
						}
						else{
							info = "Sorry! An error occurred during communication. Probably, the server is down. Try again in few minutes!";
							//dialogMsg("dialog-message", info, "Error Message", null);
						}
					});
			}
	        else{
	        	var tipContent = $("#"+event.currentTarget.id).tooltip( "option", "content" );
	        	var atomUri = decodeURIQuotes(tipContent.substring(tipContent.indexOf("<a id='showTipsMoreInfos")+24,tipContent.indexOf("' href='#'")));
	        	for(var i=0; i<tooltipDataBoxContentList.length; i++){
	        		if(tooltipDataBoxContentList[i].box.referential.atomUID == atomUri){
	        			$("#"+event.currentTarget.id).tooltip( "option", "content", preparesContent(tooltipDataBoxContentList[i].box.referential, event.currentTarget.id));
	        			var tipsMoreInfosLink = document.getElementById("showTipsMoreInfos"+codeURIQuotes(tooltipDataBoxContentList[i].box.referential.atomUID));
	        			tipsMoreInfosLink.onclick = function(){
	    					showTipsMoreInfos(tooltipDataBoxContentList[i].box);// funcao que eh chamada no clique do link
	    			    };
	    			    break;
	        		}
	        	}
	        }
	    },
	    close: function(event, ui){
	        ui.tooltip.hover(function(){
	            $(this).stop(true).fadeTo(400, 1); 
	        },
	        function(){
	            $(this).fadeOut('400', function(){
	                $(this).remove();
	            });
	        });
	    }
	});
}

function showTipsMoreInfos(box){
	var newDiv = document.createElement('div');
	newDiv.setAttribute('id', 'dialogTipsMoreInfos'); 
	newDiv.setAttribute('style', 'display: none;');
	document.getElementsByTagName('body')[0].appendChild(newDiv);
	
	content = "<div id='dialogTipMoreInfoAbstractEng'></div><div id='dialogTipMoreInfoHeadline'></div>"; // conteudo da jenela aqui createAbstractEng(elementId, atom)
	
	dialogMsg("dialogTipsMoreInfos", content, "More Informations to " + box.referential.atomLabel, 700);
	
	createAbstractEng("dialogTipMoreInfoAbstractEng", box.referential);
	getHeadline(box.referential, 
		function(data){
			createRelatedInfos("dialogTipMoreInfoHeadline", data.headline, "accordionDialogTipMoreInfo");
	});
}

// prepara o conteudo do tooltip para cada recurso
function preparesContent(dataContentAtom, elementId){
	var result = "";
	var type = dataContentAtom.atomType;
	
	if(type != null && type != undefined && type != ""){
		result = "<div><label style='margin-right: 10px;'>"+dataContentAtom.atomLabel+"</label><a id='showTipsMoreInfos"+codeURIQuotes(dataContentAtom.atomUID)+"' href='#' title='More Info'><img class='box_imgGalleryMedia' src='images/icon_plus.png'></img></a></div><hr>";
		if(type.indexOf("Person") != -1){
			// constroi um tooltip de pessoa
			document.getElementById(elementId).setAttribute('src', 'images/icon_person.png');
			result = result + getPersonInfo(dataContentAtom.atomLiterals); // as infos da pessoa sao recuperadas aqui
		}
		else{
			if(type.indexOf("Organization") != -1){
				// constroi um tooltip de organizacao
				document.getElementById(elementId).setAttribute('src', 'images/icon_organization.png');
				result = result + getOrganizationInfo(dataContentAtom.atomLiterals);
			}
			else{
				if(type.indexOf("Place") != -1){
					// constroi um tooltip de lugar
					document.getElementById(elementId).setAttribute('src', 'images/icon_place.png');
					result = result + getPlaceInfo(dataContentAtom.atomLiterals);
				}
				else{
					// constroi um tooltip generico
					result = result + getUnknowsInfo(dataContentAtom.atomLiterals);
				}
			}
		}
	}
	else{
		result = "<input type='hidden' value='untypedEntity_basicAtom'></input><label>"+dataContentAtom.atomLabel+"</label><br><hr><label id='tipLookingForInformation' class='contentTooltip'>Looking for information</label>";
	}
	
	return result;
}

function codeURIQuotes(uri){
	uri = uri.replace(/'/g,"#%sq%#");
	uri = uri.replace(/\"/g,"#%dq%#");
	return uri;
}

function decodeURIQuotes(uri){
	uri = uri.replace(/#%sq%#/g,"'");
	uri = uri.replace(/#%dq%#/g,"\"");
	return uri;
}

function createAbstractEng(elementId, atom){
	var literals = atom.atomLiterals, htmlInfo = "", thumb = "", abst = "No abstract...", thumbOK = false, abstOK = false;
	for(var i=0; i<literals.length; i++){
		if(!thumbOK || !abstOK){
			if(literals[i].litPredicate === "http://dbpedia.org/ontology/thumbnail"){
				thumb = literals[i].litValue;
				thumbOK = true;
			}
			if(literals[i].litPredicate === "http://dbpedia.org/ontology/abstract"){
				abst = literals[i].litValue;
				abstOK = true;
			}
		}
		else{
			break;
		}
	}
	htmlInfo = "<div><img style='float:left; margin-right: 5px;' class='box_gallery_media' src='"+thumb+"'></img><p style='text-align:justify; font-size: small; padding-top: 0; margin-top: 0; margin-bottom:0;'>"+abst+"</p></div>";
	document.getElementById(elementId).innerHTML = htmlInfo; // coloca a info basica no DOM
}

function createRelatedInfos(elementId, headline, accordionId){
	var htmlInfo, news = headline.news;
	htmlInfo = "<br><div id='"+accordionId+"' style='font-size: x-small; clear: both;'>";
	
	if(news != null && news != ""){
		for(var i=0; i<news.length; i++){
			if(news[i].newsSnippet !=  null)
				htmlInfo = htmlInfo + "<h3>"+news[i].newsSnippet+"</h3>";
			htmlInfo = htmlInfo + "<div>";
			if(news[i].newsMultimediaUrl !=  null)
				htmlInfo = htmlInfo + "<img style='float:left; margin-right: 5px;' class='box_gallery_media' src='http://www.nytimes.com/"+news[i].newsMultimediaUrl+"'></img>";
			htmlInfo = htmlInfo + "<p>";
			if(news[i].newsSource !=  null && news[i].newsPubDate !=  null && news[i].newsBylineOriginal !=  null)
				htmlInfo = htmlInfo + "<label style='margin-right: 10px;'>"+news[i].newsSource+", </label><label style='margin-right: 10px;'>"+news[i].newsPubDate.substring(0, news[i].newsPubDate.indexOf("T"))+", </label><label>"+news[i].newsBylineOriginal+"</label><br><br>";
			if(news[i].newsAbstract != null)
				htmlInfo = htmlInfo + "<label>"+news[i].newsAbstract+"</label><br><br>";
			if(news[i].newsUrl != null)
				htmlInfo = htmlInfo + "<a href='"+news[i].newsUrl+"'>Go to headline</a>";
			htmlInfo = htmlInfo + "</p></div>";
		}
		
		htmlInfo = htmlInfo + "</div>";
	}
	else{
		htmlInfo = htmlInfo + "No news...</div>";
	}
	
	document.getElementById(elementId).innerHTML = htmlInfo; // coloca a info basica no DOM
	
	accordionLoad(accordionId);
}

function createInteractInputs(elementId, box){
	var htmlInfo = "<div style='padding-top: 1px;'>", drop1;
	
	drop1 = "<div id='divInteractInput'><label class='labelInputdroppable'>Enter a type of relationship</label><br></div>";
	htmlInfo = htmlInfo + drop1;
	
	htmlInfo = htmlInfo + "</div>";
	
	document.getElementById(elementId).innerHTML = htmlInfo; // coloca a info basica no DOM
	
	var input = $("<input type='text' style='width: 100%;' value='Drag and drop here' title='Put here a type of relationship (e.g. Birth Place)'></input>")
	.on('drop', function(){ // na extesao do google nao pode haver script inline (chamar funcoes js no html). Tem que fazer com jquery
		getResourcesByPred(this, box);
	}).on('dragenter', function(){
		cleanContent(this);
	}).on('dragleave', function(){
		retrievesContent(this);
	});
	$("#divInteractInput").append(input);
}

function getResourcesByPred(element, box){
	// virifica se a div existe. Se nao existe cria, se existe, limpa o conteudo.
	var tooltipDiv = document.getElementById("dialogRelatedListResources");
	if(tooltipDiv == null){
		tooltipDiv = document.createElement('div');
		tooltipDiv.setAttribute('id', 'dialogRelatedListResources'); 
		tooltipDiv.setAttribute('style', 'display: none;');
		document.getElementsByTagName('body')[0].appendChild(tooltipDiv);
	}
	else{
		tooltipDiv.innerHTML = "";
	}
	
	setTimeout(function(){
		openModal();
		// chamada 1 //////////////////////////////////////////////////////////////////////
		getOutList(box, element.value, function(data1){ 
			if(data1 != null){
				if(data1.responseOk === "true"){
					if(data1.list != null && data1.list.connectElements != null){
						title = data1.list.reference.atomLabel + " " + data1.list.predicate;
						outHtml = createHTMLfromList(data1.list.connectElements, title, "data1");
					}
					else{
						outHtml = "";
					}
					// chamada 2 ////////////////////////////////////////////
					getInList(box, element.value, function(data2){
						if(data2 != null){
							if(data2.responseOk === "true"){
								if(data2.list != null && data2.list.connectElements != null){
									title = data2.list.predicate + " " + data2.list.reference.atomLabel;
									inHtml = createHTMLfromList(data2.list.connectElements, title, "data2");
								}
								else{
									inHtml = "";
								}
								dialogMsg("dialogRelatedListResources", null, "Entities related to "+box.referential.atomLabel, null);
							}
							else{
								console.log(data2.info);
								info = "Sorry! An error occurred during the processing of information: <br><br>"+data2.info+"<br><br> Try again later... ";
								//dialogMsg("dialog-message", info, "Error Message", null);
							}
						}
						else{
							info = "Sorry! An error occurred during communication. Probably, the server is down. Try again in few minutes!";
							//dialogMsg("dialog-message", info, "Error Message", null);
						}
					});
					// fim chamada 2 /////////////////////////////////////////
				}
				else{
					console.log(data1.info);
					info = "Sorry! An error occurred during the processing of information: <br><br>"+data1.info+"<br><br> Try again later... ";
					//dialogMsg("dialog-message", info, "Error Message", null);
				}
			}
			else{
				info = "Sorry! An error occurred during communication. Probably, the server is down. Try again in few minutes!";
				//dialogMsg("dialog-message", info, "Error Message", null);
			}
			closeModal();
		});
		// fim chamada 1 //////////////////////////////////////////////////////////////////
	},100);
}

function createHTMLfromList(resourcesList, title, context){
	var html = "<div style='margin-bottom: 10px;'><label>"+title+"</label><a class='linkNonlink' href='#'>:</a></div>";
	var count = 1;
	
	$("#dialogRelatedListResources").append(html);
	
	if(Array.isArray(resourcesList)){
		for(var i=0; i<resourcesList.length; i++){
			//icon = getIconRelatedResource(resourcesList[i].atomType);
			html = "<div class='divRelatedResources'><span><i><a class='aLinkPreventClick' id='ptooltip-"+count+"ResourcesList"+context+"' href='"+resourcesList[i].atomUID+"'>"+resourcesList[i].atomLabel+"</a></i></span></div>";
			$("#dialogRelatedListResources").append(html);
			anchorTooltipAdvanced("#ptooltip-"+count+"ResourcesList"+context, "a.aLinkPreventClick", resourcesList[i]);
			count++;
		}
		$("#dialogRelatedListResources").append("<br>");
	}
	else{
		//icon = getIconRelatedResource(resourcesList.atomType);
		//html = html + "<div class='divRelatedResources'><span><i><a class='aLinkPreventClick' href='"+resourcesList.atomUID+"'>"+resourcesList.atomLabel+"</a></i></span><div style='text-align:right;'><a href='"+resourcesList.atomUID+"'><img title='ptooltip-"+count+"' src='"+icon+"'></img></a></div></div>";
		html = "<div class='divRelatedResources'><span><i><a class='aLinkPreventClick' id='ptooltip-"+count+"ResourcesList"+context+"' href='"+resourcesList.atomUID+"'>"+resourcesList.atomLabel+"</a></i></span></div><br>";
		$("#dialogRelatedListResources").append(html);
		anchorTooltipAdvanced("#ptooltip-"+count+"ResourcesList"+context, "a.aLinkPreventClick", resourcesList);
	}
	
	
}

function dialogMsg(elementId, info, title, width){
	if(info != null)
		document.getElementById(elementId).innerHTML = info;
	if(width == null)
		width = 530;
	
	$(function() {
	    $( "#"+elementId ).dialog({
	      modal: true,
	      title: title,
	      width: width,
	      maxHeight: 650,
	      buttons: {
	        Ok: function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	  });
}

function openModal() {
    document.getElementById('modal').style.display = 'block';
    document.getElementById('fade').style.display = 'block';
}

function closeModal() {
	document.getElementById('modal').style.display = 'none';
	document.getElementById('fade').style.display = 'none';
}

function cleanContent(element){
	inputPredDroppableContent = element.value;
	element.value = "";
}

function retrievesContent(element){
	element.value = inputPredDroppableContent;
}

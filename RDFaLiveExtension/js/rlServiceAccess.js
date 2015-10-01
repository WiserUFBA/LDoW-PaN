var RLAserviceBaseURL = "http://rdfalive-wiiser.rhcloud.com/rest/serviceInterface/";

//var RLAserviceBaseURL = "http://200.128.51.50/rdfalive/rest/serviceInterface/";

//var RLAserviceBaseURL = "http://localhost:8080/RDFaLiveService/rest/serviceInterface/";

function RLAget(RLAmethod, RLAgetCallback){
	$.ajax({
		type: 'GET',
	    url: RLAserviceBaseURL + RLAmethod,
	    crossDomain: true,
	    success: function(data, textStatus, jqXHR ) { console.log("RDFa Live " + RLAmethod+" "+textStatus); RLAgetCallback(data, textStatus); },
		error: function(jqXHR, textStatus, errorThrown){ console.log("RDFa Live " + RLAmethod+" "+textStatus+" "+errorThrown); RLAgetCallback(null, textStatus); }
	});
}

function RLAput(RLAmethod, RLAdata, RLAputCallback){
	$.ajax({
		type: 'PUT',
	    url: RLAserviceBaseURL + RLAmethod,
	    contentType: "application/json; charset=UTF-8",
	    dataType: "json",
	    crossDomain: true,
	    data: RLAdata,
	    success: function(data, textStatus, jqXHR ) { console.log("RDFa Live " + RLAmethod+" "+textStatus); RLAputCallback(data, textStatus); },
		error: function(jqXHR, textStatus, errorThrown){ console.log("RDFa Live " + RLAmethod+" "+textStatus+" "+errorThrown); RLAputCallback(null, textStatus); }
	});
}

function RLApost(RLAmethod, RLAdata, RLApostCallback){
	$.ajax({
	    type: 'POST',
	    url: RLAserviceBaseURL + RLAmethod,
	    contentType: 'application/json; charset=UTF-8', // o tipo de dado enviado
	    dataType: "json", // o tipo de dado recebido
	    crossDomain: true,
	    data: RLAdata,
	    success: function(data, textStatus, jqXHR){ 
	    			console.log("RDFa Live " + RLAmethod+" "+textStatus);
	    			RLApostCallback(data, textStatus);
	    		 },
	    error: function(jqXHR, textStatus, errorThrown){ 
	    			console.log("RDFa Live " + RLAmethod+" "+textStatus+" "+errorThrown);
	    			RLApostCallback(null, textStatus);
	    		}
	});
}


/////////////////////////////////////////Suporte de Acesso ao Servico///////////////////////////////////////////////////////////////
function triplesObjToJSON(listTriplesObj, urlPage) {
	for(var i=0; i<listTriplesObj.length; i++){
		listTriplesObj[i].tripleObject = encode_utf8(listTriplesObj[i].tripleObject);
	}
	
    return JSON.stringify({"triplesList": listTriplesObj, "triplesHome":urlPage});
}

function encode_utf8(s) {
	return unescape(encodeURIComponent(s));
}

function decode_utf8(s) {
	return decodeURIComponent(escape(s));
}


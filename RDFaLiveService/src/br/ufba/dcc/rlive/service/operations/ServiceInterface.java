package br.ufba.dcc.rlive.service.operations;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.google.gson.Gson;

import br.ufba.dcc.rlive.api_interface.PresentNavi;
import br.ufba.dcc.rlive.processing.base.elements.RLTriples;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNGallery;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNHeadline;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNInList;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNOutList;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.TransNetwork;
import br.ufba.dcc.rlive.service.packing.PacInBoxPred;
import br.ufba.dcc.rlive.service.packing.PacInSimilar;
import br.ufba.dcc.rlive.service.packing.PacInTNetAtom;
import br.ufba.dcc.rlive.service.packing.PacOutBox;
import br.ufba.dcc.rlive.service.packing.PacOutGallery;
import br.ufba.dcc.rlive.service.packing.PacOutHeadline;
import br.ufba.dcc.rlive.service.packing.PacOutList;
import br.ufba.dcc.rlive.service.packing.PacOutPresentNav;
import br.ufba.dcc.rlive.service.packing.PacOutTNet;

@Path("/serviceInterface")
public class ServiceInterface {
	
	@GET
	@Path("getVersion")
    @Produces({ MediaType.APPLICATION_JSON })
    public String getVersion() {
		String strVersion = "RDFa Live Service Version 1.0.0.1";
		
		Gson gson = new Gson();
		String jsonVersion = gson.toJson(strVersion);
		String jsonpVersion = "rls_version("+jsonVersion+");"; // resposta para requisicao em formato jsonp
		
		return jsonpVersion;
    }
	
	// recebe uma requisição via post contendo um objeto Triples. Retorna um objeto...
	@POST
	@Path("startWithTriplesPOST")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutBox startWithTriplesPOST(JAXBElement<RLTriples> trsJaxb) {
		RLTriples rlTriples = trsJaxb.getValue(); // recebe o objeto Triples contendo uma lista de triplas
		PacOutBox poBox = new PacOutBox();
		
		try{
			PresentNavi pn = new PresentNavi();
			rlTriples = pn.filterLevel1(rlTriples);
			//System.out.println(triples.toString());
			RLNetwork rlNet = pn.getNet(rlTriples);
			RLAtom atom = pn.getAtom("http://dbpedia.org/resource/Barack_Obama", rlNet);
			PNGallery gallery = pn.getGallery(atom);
			System.out.println(gallery.toString());
			PNBox box = pn.getBox(atom, rlNet);
			System.out.println(box.toString());
			PNOutList outList = pn.getOutList(box, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			System.out.println(outList.toString());
			PNInList inList = pn.getInList(box, "http://dbpedia.org/property/label");
			System.out.println(inList.toString());
			poBox.setBox(box); poBox.setResponseOk(true); poBox.setInfo("Response ok");
			//TransNetwork tNet = Communication.RLNetTOTransNet(rlNet); // transforma a rede em uma transrede. A transrede permite melhor forma de comunicacao entre cliente-servidor e servidor-cliente
			//JAXBElement<RLNetwork> jRLNet = new JAXBElement<RLNetwork>(new QName("RLNet"), RLNetwork.class, rlNet);
			//Testing t = new Testing();t.showGraph(rlNet);
		}
		catch(Exception e){
			e.printStackTrace();
			poBox.setBox(null); poBox.setResponseOk(false); poBox.setInfo(e.toString());
		}
		
		return poBox;
    }
	
	@POST
	@Path("startWithTriplesPOST_jsonp")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.TEXT_PLAIN })
    public String startWithTriplesPOST_jsonp(JAXBElement<RLTriples> trsJaxb) {
		RLTriples rlTriples = trsJaxb.getValue(); // recebe o objeto Triples contendo uma lista de triplas
		PacOutBox poBox = new PacOutBox();
		
		try{
			PresentNavi pn = new PresentNavi();
			rlTriples = pn.filterLevel1(rlTriples);
			RLNetwork rlNet = pn.getNet(rlTriples);
			RLAtom atom = pn.getAtom("http://dbpedia.org/resource/Barack_Obama", rlNet);
			PNBox box = pn.getBox(atom, rlNet);
			poBox.setBox(box); poBox.setResponseOk(true); poBox.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poBox.setBox(null); poBox.setResponseOk(false); poBox.setInfo(e.toString());
		}
		
		Gson gson = new Gson();
		String jsonPoBox = gson.toJson(poBox);
		String jsonpPoBox = "rls_box("+jsonPoBox+");";
		
		return jsonpPoBox;
    }
	
	@POST
	@Path("startPresentNav")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutPresentNav startPresentNav(JAXBElement<RLTriples> trsJaxb) {
		RLTriples rlTriples = trsJaxb.getValue();
		PacOutPresentNav poPresentNav = new PacOutPresentNav();
		
		try{
			PresentNavi pn = new PresentNavi();
			rlTriples = pn.filterLevel1(rlTriples);
			
			RLNetwork rlNet = pn.getNet(rlTriples);
			RLAtom majorAtom = pn.getMajorAtomByDegree(rlNet); // determinar o atomo mais relevante
			// se o atomo principal possuir grau baixo (atomo pobre), usa a descoberta para recuperar mais dados sobre o atomo
			
			PNBox box = pn.getBox(majorAtom, rlNet); // cria um box para o atomo			
			PNGallery gallery = pn.getGallery(majorAtom);// cria galeria para o atomo
			PNHeadline headline = pn.getHeadline(majorAtom); // cria headline para o atomo
			
			poPresentNav.setBox(box); poPresentNav.setGallery(gallery); poPresentNav.setHeadline(headline); poPresentNav.setResponseOk(true); poPresentNav.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poPresentNav.setBox(null); poPresentNav.setGallery(null); poPresentNav.setHeadline(null); poPresentNav.setResponseOk(false); poPresentNav.setInfo(e.toString());
		}
		
		return poPresentNav;
		
	}
	
	@POST
	@Path("getTNet")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutTNet getTNetPOST(JAXBElement<RLTriples> trsJaxb) {
		RLTriples rlTriples = trsJaxb.getValue(); // recebe o objeto Triples contendo uma lista de triplas
		PacOutTNet poTNet = new PacOutTNet();
		
		try{
			PresentNavi pn = new PresentNavi();
			rlTriples = pn.filterLevel1(rlTriples);
			// mais niveis de filtragem
			
			RLNetwork rlNet = pn.getNet(rlTriples);
			TransNetwork tNet = pn.convertRLNet2TNet(rlNet); // transforma a rede em uma transrede. A transrede permite melhor forma de comunicacao entre cliente-servidor e servidor-cliente
			poTNet.settNet(tNet); poTNet.setResponseOk(true); poTNet.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poTNet.settNet(null); poTNet.setResponseOk(false); poTNet.setInfo(e.toString());
		}
		
		return poTNet;
	}
	
	@POST
	@Path("getBox")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutBox getBoxPOST(JAXBElement<PacInTNetAtom> piTnetAtom) {
		PacInTNetAtom mix = piTnetAtom.getValue();
		PacOutBox poBox = new PacOutBox();
		
		try{
			PresentNavi pn = new PresentNavi();
			RLNetwork rlNet = pn.convertTNet2RLNet(mix.gettNet());
			RLAtom atom = pn.getAtom(mix.getAtom().getAtomUID(), rlNet);
			
			if(atom == null){
				return null;
			}
			
			PNBox box = pn.getBox(atom, rlNet);
			poBox.setBox(box); poBox.setResponseOk(true); poBox.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poBox.setBox(null); poBox.setResponseOk(false); poBox.setInfo(e.toString());
		}
		return poBox;
	}
	
	@POST
	@Path("getOutList")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutList getOutListPOST(JAXBElement<PacInBoxPred> piBoxPred) {
		PacInBoxPred mix = piBoxPred.getValue();
		PacOutList poList = new PacOutList();
		
		try{
			PresentNavi pn = new PresentNavi();
			PNBox box = mix.getBox();
			String pred = mix.getPred();
			
			PNOutList outList = pn.getOutList(box, pred);
			poList.setList(outList); poList.setResponseOk(true); poList.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poList.setList(null); poList.setResponseOk(false); poList.setInfo(e.toString());
		}
		
		return poList;
	}
	
	@POST
	@Path("getInList")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutList getInListPOST(JAXBElement<PacInBoxPred> piBoxPred) {
		PacInBoxPred mix = piBoxPred.getValue();
		PacOutList poList = new PacOutList();
		
		try{
			PresentNavi pn = new PresentNavi();
			PNBox box = mix.getBox();
			String pred = mix.getPred();
			
			PNInList inList = pn.getInList(box, pred);
			poList.setList(inList); poList.setResponseOk(true); poList.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poList.setList(null); poList.setResponseOk(false); poList.setInfo(e.toString());
		}
		
		return poList;
	}
	
	@POST
	@Path("getGallery")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutGallery getGalleryPOST(JAXBElement<RLAtom> rlAtom) {
		RLAtom atom = rlAtom.getValue();
		PacOutGallery poGallery = new PacOutGallery();
		
		try{
			PresentNavi pn = new PresentNavi();
			PNGallery gallery = pn.getGallery(atom);

			poGallery.setGallery(gallery); poGallery.setResponseOk(true); poGallery.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poGallery.setGallery(null); poGallery.setResponseOk(false); poGallery.setInfo(e.toString());
		}
		
		return poGallery;
	}
	
	@POST
	@Path("getGalleryLD")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutGallery getGalleryLDPOST(JAXBElement<String> AtomURI) {
		String URI = AtomURI.getValue();
		PacOutGallery poGallery = new PacOutGallery();
		
		try{
			PresentNavi pn = new PresentNavi();
			PNGallery gallery = pn.getGalleryLD(URI);

			poGallery.setGallery(gallery); poGallery.setResponseOk(true); poGallery.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poGallery.setGallery(null); poGallery.setResponseOk(false); poGallery.setInfo(e.toString());
		}
		
		return poGallery;
	}
	
	@POST
	@Path("getHeadline")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutHeadline getHeadlinePOST(JAXBElement<RLAtom> rlAtom) {
		RLAtom atom = rlAtom.getValue();
		PacOutHeadline poHeadline = new PacOutHeadline();
		
		try{
			PresentNavi pn = new PresentNavi();
			PNHeadline headline = pn.getHeadline(atom);

			poHeadline.setHeadline(headline); poHeadline.setResponseOk(true); poHeadline.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poHeadline.setHeadline(null); poHeadline.setResponseOk(false); poHeadline.setInfo(e.toString());
		}
		
		return poHeadline;
	}
	
	@POST
	@Path("getTipContent")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutBox getTipContentPOST(JAXBElement<String> AtomURI) {
		String URI = AtomURI.getValue();
		PacOutBox poBox = new PacOutBox();
		
		try{
			PresentNavi pn = new PresentNavi();
			PNBox box = pn.getBoxFromWD(URI);

			poBox.setBox(box); poBox.setResponseOk(true); poBox.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poBox.setBox(null); poBox.setResponseOk(false); poBox.setInfo(e.toString());
		}
		
		return poBox;
	}
	
	@POST
	@Path("getSimilarList")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public PacOutList getSimilarListPOST(JAXBElement<PacInSimilar> piSimilar) {
		PacInSimilar mix = piSimilar.getValue();
		PacOutList poList = new PacOutList();
		
		try{
			PresentNavi pn = new PresentNavi();
			String endpoint = mix.getEndpoint();
			String pred = mix.getPred();
			ArrayList<String> objList = mix.getObjs();
			RLAtom referential = mix.getReferential();
			
			PNOutList outList = pn.getOutList(endpoint, pred, objList, referential);
			poList.setList(outList); poList.setResponseOk(true); poList.setInfo("Response ok");
		}
		catch(Exception e){
			e.printStackTrace();
			poList.setList(null); poList.setResponseOk(false); poList.setInfo(e.toString());
		}
		
		return poList;
	}
	
}

package br.ufba.dcc.rlive.api_interface;

import java.util.ArrayList;

import br.ufba.dcc.rlive.processing.analytical_filtering.operations.Removal;
import br.ufba.dcc.rlive.processing.base.elements.RLTriples;
import br.ufba.dcc.rlive.processing.base.operations.BasicOperation;
import br.ufba.dcc.rlive.processing.discovery_and_counseling.operations.Discovery;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNGallery;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNHeadline;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNInList;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNOutList;
import br.ufba.dcc.rlive.processing.interface_preparation.operations.Labelling;
import br.ufba.dcc.rlive.processing.interface_preparation.operations.Making;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.TransNetwork;
import br.ufba.dcc.rlive.processing.mapping_and_load.operations.Mounting;
import br.ufba.dcc.rlive.service.operations.Communication;

public class PresentNavi {
	
	public RLTriples filterLevel1(RLTriples rlTriples) throws Exception{
		return Removal.level1(rlTriples);
	}
	
	public boolean isWellFormedURI(String uri){
		try {
			BasicOperation.checksURIFormation(uri);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isResource(String recUri, String predUri){
		if(isWellFormedURI(recUri)){
			return BasicOperation.isLDResourceURI(recUri, predUri);
		}
		return false;
	}
	
	public boolean isBlankNode(String uri) throws Exception{
		if(isWellFormedURI(uri)){
			return BasicOperation.isBlankNode(uri);
		}
		return false;
	}
	
	public String createURILabel(String uri) throws Exception{
		if(isWellFormedURI(uri)){
			return BasicOperation.getURIWithoutPrefix(uri);
		}
		return null;
	}
	
	public String getAtomLabel(String uri) throws Exception{
		if(isWellFormedURI(uri)){
			return Labelling.defineAtomLabel(uri);
		}
		return null;
	}
	
	public String getLinkLabel(String uri) throws Exception{
		if(isWellFormedURI(uri)){
			return Labelling.defineLinkLabel(uri);
		}
		return null;
	}
	
	public PNBox getBox(RLAtom atom, RLNetwork rlNet) throws Exception{
		return Making.makeBox(atom, rlNet);
	}
	
	public PNOutList getOutList(PNBox box, String pred) throws Exception{
		return Making.makeOutList(box, pred);
	}
	
	public PNInList getInList(PNBox box, String pred) throws Exception{
		return Making.makeInList(box, pred);
	}
	
	public RLAtom getAtom(String uri, PNBox box) throws Exception{
		return Making.boxContains(uri, box);
	}
	
	public RLAtom getAtom(String uri, RLNetwork rlNet) throws Exception{
		return Mounting.netContains(uri, rlNet);
	}
	
	public PNBox getBoxFromWD(String uri) throws Exception{
		return Discovery.discoveryResourceOnWD(uri);
	}
	
	public RLNetwork getNet(RLTriples rlTriples) throws Exception{
		return Mounting.buildRLNet(rlTriples);
	}
	
	public TransNetwork convertRLNet2TNet(RLNetwork rlNet) throws Exception{
		return Communication.RLNetToTransNet(rlNet);
	}
	
	public RLNetwork convertTNet2RLNet(TransNetwork tNet) throws Exception{
		return Communication.TransNetToRLNet(tNet);
	}
	
	public RLAtom getMajorAtomByDegree(RLNetwork rlNet) throws Exception{
		return BasicOperation.getAtomWithBiggerDegree(rlNet);
	}
	
	public PNGallery getGallery(RLAtom atom) throws Exception{
		return Making.makeGallery(atom);
	}
	
	public PNGallery getGalleryLD(String atomURI) throws Exception{
		return Making.makeGalleryFlickrLD(atomURI);
	}
	
	public PNHeadline getHeadline(RLAtom atom) throws Exception{
		return Making.makeNews(atom);
	}
	
	public PNOutList getOutList(String endpoint, String pred, ArrayList<String> objList, RLAtom atomRef) throws Exception{
		return Making.makeOutList(endpoint, pred, objList, atomRef);
	}
	
}

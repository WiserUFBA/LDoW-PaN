package br.ufba.dcc.rlive.processing.mapping_and_load.operations;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.ufba.dcc.rlive.processing.base.elements.RLTriple;
import br.ufba.dcc.rlive.processing.base.elements.RLTriples;
import br.ufba.dcc.rlive.processing.base.operations.BasicOperation;
import br.ufba.dcc.rlive.processing.interface_preparation.operations.Labelling;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLLink;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLLiteral;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;

public class Mounting {
	private static RLNetwork mouNet;

	
	
	public static RLNetwork buildRLNet(RLTriples trs) throws Exception{
		if(trs != null && trs.getTriplesList() != null){
			Mounting.mouNet = new RLNetwork();
			List<RLTriple> listTriples = trs.getTriplesList();
			
			for(int i=0; i < listTriples.size(); i++){
				RLAtom existSjtAtom = netContains(listTriples.get(i).getTripleSubject()); // verifica se o atomo ja existe na rede. Caso exista, retorna o atomo
				if(existSjtAtom != null){ // se o sujeito da tripla ja eh um atomo (faz parte da rede)...
					buildAndOrConnectObjAtom(existSjtAtom, listTriples.get(i).getTriplePredicate(), listTriples.get(i).getTripleObject());
				}
				else{ // o sujeito da tripla nao eh atomo e ainda nao esta na rede
					// cria o atomo na rede
					RLAtom newtSjtAtom = buildAndInsertAtom(listTriples.get(i).getTripleSubject());
					buildAndOrConnectObjAtom(newtSjtAtom, listTriples.get(i).getTriplePredicate(), listTriples.get(i).getTripleObject());
				}
			}
			
			return mouNet;
		}
		else{
			throw new Exception("Null trs or trs.getTriplesList(). ");
		}
	}
	
	private static RLAtom netContains(String atomUID) throws Exception{
		if(atomUID != null && !atomUID.isEmpty()){
			Collection<RLAtom> atons = mouNet.getNetGraph().getVertices();
			Iterator<RLAtom> itr = atons.iterator();
			
			while (itr.hasNext()) {
				RLAtom atom = itr.next();
				if(atom.getAtomUID().equals(atomUID)){
					return atom;
				}
			}
			return null;
		}
		else{
			throw new Exception("Null atomUID or atomUID is empty. ");
		}
	}
	
	// sobrecarga de netContains recebendo, alem da uri do atomo, uma rede
	public static RLAtom netContains(String atomUID, RLNetwork rlNet) throws Exception{
		if(atomUID != null && !atomUID.isEmpty() && rlNet != null && rlNet.getNetGraph() != null){
			Collection<RLAtom> atons = rlNet.getNetGraph().getVertices();
			Iterator<RLAtom> itr = atons.iterator();
			
			while (itr.hasNext()) {
				RLAtom atom = itr.next();
				if(atom.getAtomUID().equals(atomUID)){
					return atom;
				}
			}
			return null;
		}
		else{
			throw new Exception("Null atomUID, rlNet or rlNet.getNetGraph(). Or atomUID is empty. ");
		}
	}
	
	private static void buildAndOrConnectObjAtom(RLAtom sjt, String prd, String obj) throws Exception{
		if(prd != null && !prd.isEmpty() && obj != null){
			if(BasicOperation.isLDResourceURI(obj, prd)){ // se o objeto da tripla eh recurso linked data (nao eh literal)...
				RLAtom existObjAtom = netContains(obj);
				if(existObjAtom != null){
					// conecta esse atomo objeto com o atomo sujeito (i)
					connectObjAtom(sjt, prd, existObjAtom);
				}
				else{
					// cria o novo atomo abjeto e faz a ligacao entre ele e o atomo sujeito (i)
					RLAtom newtObjAtom = buildAndInsertAtom(obj);
					connectObjAtom(sjt, prd, newtObjAtom);
				}
			}
			else{ // o objeto da tripla eh literal
				// insere o literal no atomo (i)
				insertRLLiteral(sjt, prd, obj);
			}
		}
		else{
			throw new Exception("Null prd or obj. Or prd is empty. (prd: "+prd+")");
		}
	}
	
	private static void connectObjAtom(RLAtom sjt, String prd, RLAtom obj) throws Exception{
		// cria o link setando as uris dos 3 especificadores
		RLLink link = new RLLink();
		link.getLinkSubject().setSpecURI(sjt.getAtomUID()); // especificador sujeito  
		link.getLinkSubject().setLabel(sjt.getAtomLabel());
		
		link.getLinkPredicate().setSpecURI(prd); 			// especificador predicado
		link.getLinkPredicate().setLabel(Labelling.defineLinkLabel(prd));
		
		link.getLinkObject().setSpecURI(obj.getAtomUID());	// especificador objeto
		link.getLinkObject().setLabel(obj.getAtomLabel());
		
		mouNet.getNetGraph().addEdge(link, sjt, obj);
	}
	
	private static void insertRLLiteral(RLAtom iAtom, String pred, String litValue){
		RLLiteral lit = new RLLiteral();
		
		lit.setLitPredicate(pred);
		lit.setLitValue(BasicOperation.removeMetaRefs(litValue)); // ponto em que o valor dos literais sao inseridos no atomo
		
		iAtom.getAtomLiterals().add(lit);
	}
	
	private static RLAtom buildAndInsertAtom(String atomUID) throws Exception{
		if(atomUID != null && !atomUID.isEmpty()){
			RLAtom newAtom = new RLAtom();
			
			newAtom.setAtomUID(atomUID);
			newAtom.setAtomLabel(Labelling.defineAtomLabel(atomUID));
			if(BasicOperation.isBlankNode(atomUID)) newAtom.setBlankNode(true);
			else newAtom.setBlankNode(false);
			
			mouNet.getNetGraph().addVertex(newAtom);
			
			return newAtom;
		}
		else{
			throw new Exception("Null atomUID or atomUID is empty. ");
		}
	}
	
}

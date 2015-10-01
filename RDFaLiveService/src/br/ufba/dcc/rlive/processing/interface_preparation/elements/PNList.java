package br.ufba.dcc.rlive.processing.interface_preparation.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;

public class PNList {
	private Collection<RLAtom> connectElements; // podem ser objetos ou sujeitos
	protected String predicate;
	protected RLAtom reference;
	
	
	public PNList(){
		this.connectElements = new ArrayList<RLAtom>();
		this.predicate = new String();
		this.reference = new RLAtom();
	}
	
	public String toString(){
		String str = "";
		Iterator<RLAtom> itr = this.connectElements.iterator();
		
		while(itr.hasNext()){
			RLAtom atom = itr.next();
			str = str + atom.getAtomLabel() + "\n";
		}
		
		return str;
	}

	public Collection<RLAtom> getConnectElements() {
		return connectElements;
	}

	public void setConnectElements(Collection<RLAtom> connectElements) {
		this.connectElements = connectElements;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public RLAtom getReference() {
		return reference;
	}

	public void setReference(RLAtom reference) {
		this.reference = reference;
	}
	
}

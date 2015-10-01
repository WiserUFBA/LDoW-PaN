package br.ufba.dcc.rlive.processing.interface_preparation.elements;

import java.util.Iterator;

import br.ufba.dcc.rlive.processing.interface_preparation.operations.Labelling;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;

public class PNOutList extends PNList{
	
	
	
	public PNOutList(){}
	
	public String toString(){
		Iterator<RLAtom> itr = super.getConnectElements().iterator();
		String str = "";
		try {
			str = str + super.reference.getAtomLabel() + " | " + Labelling.defineLinkLabel(super.predicate) + "\n\n";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(itr.hasNext()){
			RLAtom atom = itr.next();
			str = str + "-> "+ atom.getAtomLabel() + "\n";
		}
		
		return str;
	}
	
}

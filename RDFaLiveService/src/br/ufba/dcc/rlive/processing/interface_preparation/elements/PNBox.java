package br.ufba.dcc.rlive.processing.interface_preparation.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;

public class PNBox {
	private RLAtom referential;
	private List<PNEntry> outPredicates;
	private List<PNEntry> inPredicates;
	
	
	
	public PNBox(){
		this.referential = new RLAtom();
		this.outPredicates = new ArrayList<PNEntry>();
		this.inPredicates = new ArrayList<PNEntry>();
	}
	
	public String toString(){
		String str = this.referential.toString() + "\n\n";
		Iterator<PNEntry> itr = this.outPredicates.iterator();
		
		while(itr.hasNext()) {
			PNEntry entry = itr.next();
			str = str + "-> "+ entry.toString();
		}
		
		itr = this.inPredicates.iterator();
		while(itr.hasNext()) { 
			PNEntry entry = itr.next();
			str = str + "<- "+ entry.toString();
		}
		
		return str;
	}

	public RLAtom getReferential() {
		return referential;
	}

	public void setReferential(RLAtom referential) {
		this.referential = referential;
	}

	public List<PNEntry> getOutPredicates() {
		return outPredicates;
	}

	public void setOutPredicates(List<PNEntry> outPredicates) {
		this.outPredicates = outPredicates;
	}

	public List<PNEntry> getInPredicates() {
		return inPredicates;
	}

	public void setInPredicates(List<PNEntry> inPredicates) {
		this.inPredicates = inPredicates;
	}
	
}

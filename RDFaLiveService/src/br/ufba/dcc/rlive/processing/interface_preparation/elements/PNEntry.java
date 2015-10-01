package br.ufba.dcc.rlive.processing.interface_preparation.elements;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;

public class PNEntry {
	private String key;
	private RLAtom value;
	private String labelKey;
	
	
	
	
	public PNEntry(){
		this.key = new String();
		this.value = new RLAtom();
		this.labelKey = new String();
	}
	
	public PNEntry(String key, RLAtom value){
		this.key = key;
		this.value = value;
	}
	
	public String toString(){
		return this.labelKey + " | " + this.value.getAtomLabel() + "\n";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public RLAtom getValue() {
		return value;
	}

	public void setValue(RLAtom value) {
		this.value = value;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}
	
}

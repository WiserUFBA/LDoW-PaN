package br.ufba.dcc.rlive.processing.mapping_and_load.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RLAtom {
	// meta informacao
	private String atomUID;
	private String atomLabel;
	private String atomType;
	private String atomPresentSpec;
	private Map <String,String> atomAttributes;
	private boolean blankNode;
		
	// conteudo
	private List<RLLiteral> atomLiterals;
	
	
	
	public RLAtom(){
		this.atomAttributes = new HashMap <String,String>();
		this.atomLiterals = new ArrayList <RLLiteral>();
		this.atomUID = new String();
		this.atomLabel = new String();
		this.atomType = new String();
	}
	
	public String toString(){
		return this.atomLabel;
	}

	public String getAtomUID() {
		return atomUID;
	}

	public void setAtomUID(String atomUID) {
		this.atomUID = atomUID;
	}

	public String getAtomPresentSpec() {
		return atomPresentSpec;
	}

	public void setAtomPresentSpec(String atomPresentSpec) {
		this.atomPresentSpec = atomPresentSpec;
	}

	public Map<String, String> getAtomAttributes() {
		return atomAttributes;
	}

	public void setAtomAttributes(Map<String, String> atomAttributes) {
		this.atomAttributes = atomAttributes;
	}

	public boolean isBlankNode() {
		return blankNode;
	}

	public void setBlankNode(boolean blankNode) {
		this.blankNode = blankNode;
	}

	public List<RLLiteral> getAtomLiterals() {
		return atomLiterals;
	}

	public void setAtomLiterals(List<RLLiteral> atomLiterals) {
		this.atomLiterals = atomLiterals;
	}

	public String getAtomLabel() {
		return atomLabel;
	}

	public void setAtomLabel(String atomLabel) {
		this.atomLabel = atomLabel;
	}

	public String getAtomType() {
		return atomType;
	}

	public void setAtomType(String atomType) {
		this.atomType = atomType;
	}
	
}

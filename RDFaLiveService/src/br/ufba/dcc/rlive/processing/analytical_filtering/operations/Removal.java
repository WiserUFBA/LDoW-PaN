package br.ufba.dcc.rlive.processing.analytical_filtering.operations;

import java.util.ArrayList;
import java.util.List;

import br.ufba.dcc.rlive.processing.base.elements.RLTriple;
import br.ufba.dcc.rlive.processing.base.elements.RLTriples;
import br.ufba.dcc.rlive.processing.base.operations.BasicOperation;
import br.ufba.dcc.rlive.processing.util.GeneralUtil;

public class Removal {
	
	public static RLTriples level1(RLTriples rlTrp) throws Exception{
		if(rlTrp != null && rlTrp.getTriplesList() != null){
			List<RLTriple> triples = rlTrp.getTriplesList();
			ArrayList<String> abstractsList = new ArrayList<String>();
			ArrayList<String> commentsList = new ArrayList<String>();
			String abstractChosen = null, commentChosen = null;

			RLTriple tripleAbsExample = new RLTriple();
			RLTriple tripleCmtExample = new RLTriple();
			
			for(int i=0; i<triples.size(); i++){
				RLTriple triple = triples.get(i);
				String pred = BasicOperation.getURIWithoutPrefix(triple.getTriplePredicate());
				
				if(pred.length()==1){
					triples.remove(i);
					i--;
				}
				else{
					if(pred.equals("abstract")){
						abstractsList.add(triple.getTripleObject());
						tripleAbsExample = triple;
						triples.remove(i);
						i--;
					}
					else{
						if(pred.equals("comment")){
							commentsList.add(triple.getTripleObject());
							tripleCmtExample = triple;
							triples.remove(i);
							i--;
						}
						else{
							// mais remocoes aqui
						}
					}
				}
			} // fim for
			abstractChosen = filterStrLang(abstractsList, "hq");
			if(abstractChosen != null){
				tripleAbsExample.setTripleObject(abstractChosen);
				rlTrp.getTriplesList().add(tripleAbsExample);
			}
			commentChosen = filterStrLang(commentsList, "hq");
			if(commentChosen != null){
				tripleCmtExample.setTripleObject(commentChosen);
				rlTrp.getTriplesList().add(tripleCmtExample);
			}
			
			return rlTrp;
		}
		else{
			throw new Exception("Null RLTriples or RLTriples.getTriplesList(). ");
		}
	}
	
	public static String filterStrLang(ArrayList<String> strList, String cut){
		String chosen = null;
		int count = 0;
		for(int i=0; i<strList.size(); i++){
			String str = strList.get(i);
			
			int half = str.length()/2;
			int quarter = half/2;
			int eighth = quarter/2;
			
			if(cut.equals("hq"))
				str = str.substring(half+quarter, str.length());
			else
				if(cut.equals("hqe"))
					str = str.substring(half+quarter+eighth, str.length());
			
			String strLang = null;
			if(str.lastIndexOf("@") != -1)
				strLang = str.substring(str.lastIndexOf("@"), str.length()); // xxxx@en
			
			if(strLang != null && strLang.length() == 3){
				if(strLang.equalsIgnoreCase("@en"))
					chosen = strList.get(i);
			}
			else{
				if(GeneralUtil.getCategorizeText(str).equalsIgnoreCase("english")){
					count++;
					if(count > 1){
						if(chosen.length() < strList.get(i).length()){ // escolhe pelo tamanho da string
							chosen = strList.get(i);
						}
					}
					else{
						chosen = strList.get(i);
					}
				}
			}
		}
		
		if (chosen == null && !cut.equals("full")){
			chosen = filterStrLang(strList, "full");
		}
		
		return chosen;
	}
	
}

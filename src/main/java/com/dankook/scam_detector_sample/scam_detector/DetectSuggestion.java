package com.dankook.scam_detector_sample.scam_detector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.trees.TypedDependency;

public class DetectSuggestion implements DetectSententceType {
	
	private List<TypedDependency> tdl;
	private int len;
	private String PRP_val;
	
	public DetectSuggestion(List<TypedDependency> tdl, String PRP_val) {
		this.tdl = tdl;
		this.len = tdl.size();
		this.PRP_val = PRP_val;
	}
	
	public String[] detect() {
		
		String[] pairArray = new String[2];
        boolean is_sugestion = false;

        Map<Integer, String> compound = new HashMap<Integer, String>();
        boolean is_compound = false;
        for(int i = 0; i < len; i++) {
            String extractElement = tdl.get(i).reln().toString();
            String oneWord = tdl.get(i).gov().value().toLowerCase();

            if( extractElement.equals("compound")) {
                is_compound = true;
                compound.put(tdl.get(i).gov().index(), tdl.get(i).gov().originalText());
                compound.put(tdl.get(i).dep().index(), tdl.get(i).dep().originalText());
            }
        }

        String compounded_string = "";

        if(is_compound) {
            for(int i : compound.keySet()) {
                compounded_string += (compound.get(i) + " ");
            }
        }

        for(int i = 0; i<len ; i++){
            String tag_container = tdl.get(i).toString();
            String dep_word = tdl.get(i).dep().originalText();


            // Detect
            if(tag_container.contains("nsubj") && tdl.get(i).dep().originalText().toLowerCase().equals("you") && tdl.get(i+1).toString().contains("aux")){
                is_sugestion = true;
            }

            if(tdl.get(i).dep().originalText().contains(PRP_val)){
                continue;
            }
            
            if(is_sugestion && tag_container.contains("dobj")){
                pairArray[0] = tdl.get(i).gov().originalText();
                if(compounded_string.contains(dep_word)){
                    pairArray[1] = compounded_string;
                }
                else{
                    pairArray[1] = dep_word;
                }
            }
            else if(is_sugestion && tag_container.contains("xcomp")){
                pairArray[0] = tdl.get(i).gov().originalText();
                if(compounded_string.contains(dep_word)){
                    pairArray[1] = compounded_string;
                }
                else{
                    pairArray[1] = dep_word;
                }
            }
            else if(is_sugestion && tag_container.contains("nmod")){
                pairArray[0] = tdl.get(i).gov().originalText();
                if(compounded_string.contains(dep_word)){
                    pairArray[1] = compounded_string;
                }
                else{
                    pairArray[1] = dep_word;
                }
            }
        }
		return pairArray;
	}

}

package com.dankook.scam_detector_sample.scam_detector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.trees.TypedDependency;

public class DetectImperative implements DetectSententceType {

	private List<TypedDependency> tdl;
	private String verb;
	private int len;
    String PRP_val;
	
	public DetectImperative(List<TypedDependency> tdl, String verb, String PRP_val) {
		this.tdl = tdl;
		this.verb = verb;
		this.len = tdl.size();
		this.PRP_val = PRP_val;
	}

	public String[] detect() {
		
		String[] pairArray = new String[2];

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
		
        if(verb != ""){
            for(int i = 0; i<len ; i++){
                String tag_container = tdl.get(i).toString();
                String tagged_first_word = tdl.get(i).gov().toString();
                String dep_word = tdl.get(i).dep().originalText();

                if(dep_word.contains(PRP_val)){
                    continue;
                }
                
                if(tag_container.contains("dobj") && tagged_first_word.contains(verb)){
                    pairArray[0] = verb;
                    if(compounded_string.contains(dep_word)){
                        pairArray[1] = compounded_string;
                    }
                    else{
                        pairArray[1] = dep_word;
                    }
                }
                else if(tag_container.contains("xcomp") && tagged_first_word.contains(verb)){
                    pairArray[0] = verb;
                    if(compounded_string.contains(dep_word)){
                        pairArray[1] = compounded_string;
                    }
                    else{
                        pairArray[1] = dep_word;
                    }
                }
                else if(tag_container.contains("nmod") && tagged_first_word.contains(verb)){
                    pairArray[0] = verb;
                    if(compounded_string.contains(dep_word)){
                        pairArray[1] = compounded_string;
                    }
                    else{
                        pairArray[1] = dep_word;
                    }
                }
            }
        }

		return pairArray;
	}

}

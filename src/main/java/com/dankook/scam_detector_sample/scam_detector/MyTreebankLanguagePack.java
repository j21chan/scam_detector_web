package com.dankook.scam_detector_sample.scam_detector;
import java.util.List;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class MyTreebankLanguagePack {
	
    private TreebankLanguagePack tlp;
    private GrammaticalStructureFactory gsf;
    private GrammaticalStructure gs;
    private List<TypedDependency> tdl;
    
    public MyTreebankLanguagePack(LexicalizedParser lp, Tree parse) {
    	this.tlp = lp.treebankLanguagePack();
    	this.gsf = tlp.grammaticalStructureFactory();
    	this.gs = gsf.newGrammaticalStructure(parse);
    	this.tdl = gs.typedDependenciesCCprocessed();
    }

	public List<TypedDependency> getTdl() {
		return tdl;
	}
	
	public int getTdlLength() {
		return tdl.size();
	}
}

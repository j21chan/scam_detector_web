package com.dankook.scam_detector_sample.scam_detector;
import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class MyTokenizer {
	private TokenizerFactory<CoreLabel> tokenizerFactory;
	private Tokenizer<CoreLabel> tok;
	private List<CoreLabel> rawWords2;
    private Tree parse;
    
    public MyTokenizer(LexicalizedParser lp, String sent) {
    	this.tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
    	this.tok = tokenizerFactory.getTokenizer(new StringReader(sent));
    	this.rawWords2 = tok.tokenize();
    	this.parse = lp.apply(rawWords2);
    }

	public Tree getParse() {
		return parse;
	}
}

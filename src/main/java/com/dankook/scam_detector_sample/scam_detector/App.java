package com.dankook.scam_detector_sample.scam_detector;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

class App {

    public static void main(String[] args) {
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);

        System.out.println(demoAPI(lp, "Send me your bank account number")[0]);
        System.out.println(demoAPI(lp, "Send me your bank account number")[1]);
    }

    /**
     * demoAPI demonstrates other ways of calling the parser with
     * already tokenized text, or in some cases, raw text that needs to
     * be tokenized as a single sentence.  Output is handled with a
     * TreePrint object.  Note that the options used when creating the
     * TreePrint can determine what results to print out.  Once again,
     * one can capture the output by passing a PrintWriter to
     * TreePrint.printTree. This code is for English.
     */
    public static String[] demoAPI(LexicalizedParser lp, String sentence) {

        // This option shows loading and using an explicit tokenizer
        String sent = sentence;
        TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sent));
        List<CoreLabel> rawWords2 = tok.tokenize();
        Tree parse = lp.apply(rawWords2);

        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
        int len = tdl.size();
        System.out.println(tdl);

        // You can also use a TreePrint object to print trees and dependencies
        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        System.out.println(tp.markHeadNodes(parse));

        // get imperative's verb and dobj
        TregexPattern noNP = TregexPattern.compile("((@VP=verb > (S !> SBAR)) !$,,@NP)");
        TregexMatcher n = noNP.matcher(parse);
        String verb = "";

        while (n.find()) {
            String match = n.getMatch().firstChild().label().toString();
            Tree temp = n.getMatch().firstChild().firstChild();

            // remove gerund, to + infinitiv
            if (match.equals("VP")) {
                match = temp.label().toString();
            }
            if (match.equals("TO") || match.equals("VBG")) {
                n.find();
                continue;
            }

            //Find the last node within overlapped "VP" nodes.
            while(temp.firstChild() != null) {
                temp = temp.firstChild();
            }

            //Store root verbs
            verb = temp.toString();
        }

        String[] pairArray = new String[2];

        // Imperative
        if(verb != ""){
            for(int i = 0; i<len ; i++){
                if(tdl.get(i).toString().contains("dobj") && tdl.get(i).toString().contains(verb)){
                    pairArray[0] = verb;
                    pairArray[1] = tdl.get(i).dep().originalText();
                    return pairArray;
                }
            }
        }
        // Suggestion
        boolean is_sugestion = false;
        for(int i = 0; i<len ; i++){
            if(tdl.get(i).toString().contains("nsubj")
            		&& tdl.get(i).dep().originalText().toLowerCase().equals("you")
            		&& tdl.get(i+1).toString().contains("aux")){
                is_sugestion = true;
            }
            if(is_sugestion && tdl.get(i).toString().contains("dobj")){
                pairArray[0] = tdl.get(i).gov().originalText();
                pairArray[1] = tdl.get(i).dep().originalText();
                return pairArray;
            }
        }
        // DesireExpression
        boolean is_desire = false;
        for (int i = 0; i < len; i++) {
        	String extractElement = tdl.get(i).reln().toString();
        	String oneWord = tdl.get(i).gov().value().toString().toLowerCase();
        	
        	if(extractElement.equals("nsubj")) {
        		if(oneWord.contains("want")
        				|| oneWord.contains("hope")
        				|| oneWord.contains("wish")
        				|| oneWord.contains("desire")) {
        			is_desire = true;
        		}
        	}
        	
        	if(is_desire && tdl.get(i).toString().contains("dobj")) {
        		pairArray[0] = tdl.get(i).gov().originalText();
        		pairArray[1] = tdl.get(i).dep().originalText();
        		return pairArray;
        	}
        }
        // Question Detection
        boolean is_question = false;
        for(int i = 0; i < len; i++) {
        	String postag = String.valueOf(parse);
        	if(postag.contains("SQ")
        		|| postag.contains("SBARQ")) {
        		is_question = true;
        		// 우분투에서 보내서 값 받기
        	}
        }
        
        // merge Compound
        Map<Integer, String> compound = new HashMap<Integer, String>();
        boolean is_compound = false;
        for(int i = 0; i < len; i++) {
        	String extractElement = tdl.get(i).reln().toString();
        	String oneWord = tdl.get(i).gov().value().toLowerCase();
        	
        	if(extractElement.equals("compound")) {
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
        
        return pairArray;
    }
}
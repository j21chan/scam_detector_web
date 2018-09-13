package com.dankook.scam_detector_sample.scam_detector;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

public class Scam_Detector {

    public static boolean detect_scam(String input_sentence) {
    	
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
        ArrayList<String[]> bl = new BlackList().getBlacklist();
        boolean scam_flag = false;

        if(input_sentence.length() > 0) {
        	scam_flag = demoAPI(lp, bl, input_sentence);
        }
        return scam_flag;
    }

    private static boolean demoAPI(LexicalizedParser lp, ArrayList<String[]> bl, String sentence) {

    	String sent = sentence;
    	TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sent));
        List<CoreLabel> rawWords2 = tok.tokenize();
        Tree parse = lp.apply(rawWords2);
        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> stdl = gs.typedDependenciesCCprocessed();
        int len_stdl = stdl.size();

        // This option shows loading and using an explicit tokenizer
    	MyTokenizer myTok = new MyTokenizer(lp, sentence);
    	MyLemmatizer myLemma = new MyLemmatizer();
    	MyTreebankLanguagePack myTreeLangPack = new MyTreebankLanguagePack(lp, myTok.getParse());

    	List<TypedDependency> tdl = myTreeLangPack.getTdl();
        
    	//System.out.println(tdl);

        // You can also use a TreePrint object to print trees and dependencies
        String PRP_val = "";
        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        int len = tp.markHeadNodes(myTok.getParse()).firstChild().taggedYield().size();
        for(int i=0; i<len; i++){
            if(tp.markHeadNodes(myTok.getParse()).firstChild().taggedYield().get(i).tag().contains("PRP")){
                PRP_val = tp.markHeadNodes(myTok.getParse()).firstChild().taggedYield().get(i).value();
                break;
            }
        }
        //System.out.println(tp.markHeadNodes(myTok.getParse()).firstChild().taggedYield());

        // get imperative's verb and dobj
        TregexPattern noNP = TregexPattern.compile("((@VP=verb > (S !> SBAR)) !$,,@NP)");
        TregexMatcher n = noNP.matcher(myTok.getParse());
        String verb = "";

        // find root verb (imperative)
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
        
        // verb, dobj, lema-verb, lema-dobj, sentence, tf-idf score
        String[] pairArray = new String[6];
        String[] verb_dobj = new String[2];
        
        DetectSententceType detectSenType1;
        DetectSententceType detectSenType2;
        DetectSententceType detectSenType3;

        detectSenType1 = new DetectImperative(tdl, verb, PRP_val);
        detectSenType2 = new DetectSuggestion(tdl, PRP_val);
        detectSenType3 = new DetectDesire(tdl, PRP_val);

        if(detectSenType1.detect()[0] != null) {
            System.out.println(">>sentype1");
	    verb_dobj = detectSenType1.detect();
            pairArray[0] = verb_dobj[0];
            pairArray[1] = verb_dobj[1];
            pairArray[2] = sentence;
            if(pairArray[1].split(" ").length > 1){
                pairArray[4] = verb_dobj[1];
            }
            else{
                pairArray[4] = myLemma.lemmatize(verb_dobj[1]);
            }
            pairArray[3] = myLemma.lemmatize(verb_dobj[0]);
        }
        else if(detectSenType2.detect()[0] != null) {
            verb_dobj = detectSenType2.detect();
            System.out.println(">>sentype2");
            pairArray[0] = verb_dobj[0];
            pairArray[1] = verb_dobj[1];
            pairArray[2] = sentence;
            pairArray[3] = myLemma.lemmatize(verb_dobj[0]);
            if(pairArray[1].split(" ").length > 1){
                pairArray[4] = verb_dobj[1];
            }
            else{
                pairArray[4] = myLemma.lemmatize(verb_dobj[1]);
            }
        }
        else if(detectSenType3.detect()[0] != null) {
            verb_dobj = detectSenType3.detect();
	    System.out.println(">>sentype3");
            pairArray[0] = verb_dobj[0];
            pairArray[1] = verb_dobj[1];
            pairArray[2] = sentence;
            pairArray[3] = myLemma.lemmatize(verb_dobj[0]);
            if(pairArray[1].split(" ").length > 1){
                pairArray[4] = verb_dobj[1];
            }
            else{
                pairArray[4] = myLemma.lemmatize(verb_dobj[1]);
            }
        }
        
        boolean scam_detect = false;
        boolean is_question = false; 
        
        // Question Detection
		for (int i = 0; i < len_stdl; i++) {

			String postag = String.valueOf(parse);

			if (postag.contains("SQ") || postag.contains("SBARQ")) {
				is_question = true;
			}
		}

		int len_rawWords2 = rawWords2.size();
		String sentence_plus = rawWords2.get(0).originalText();

		for(int i=1;i<len_rawWords2;i++) {
			sentence_plus =  sentence_plus + "+" +rawWords2.get(i).originalText();
		}


		if (is_question) {
			System.out.println(">>Question");
			String[] command = { "/bin/sh", "-c", "curl http://localhost:8083/parse?sent=" + sentence_plus };
			// String[] command = {"/bin/sh","-c","curl 8.8.8.8"};

			try {
				Process ps = Runtime.getRuntime().exec(command);

				ps.waitFor();
				Scanner s = new Scanner(ps.getInputStream()).useDelimiter("\\A");
				String result = s.hasNext() ? s.next() : "";

				if(result.contains("confidential")){
					scam_detect = true;
				}
				ps.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        for(String[] black : bl) {
        	if(black[0].equals(pairArray[3]) && black[1].equals(pairArray[4])) {
        		scam_detect = true;
        		break;
        	}
        }

       if(is_question == false){ 
    	   System.out.println( ">>dobj pair:  "+verb_dobj[0] + ',' + verb_dobj[1]);
       }
       
       if(scam_detect) {
			System.out.println(">>Scam detected!!");
			return true;
        }
        else {
        	System.out.println(">>Normal sentence");
        	return false;
        }
    }
}

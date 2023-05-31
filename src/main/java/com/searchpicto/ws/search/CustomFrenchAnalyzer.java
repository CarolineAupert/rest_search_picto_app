package com.searchpicto.ws.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.fr.FrenchLightStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.ElisionFilter;

/**
 * Custom Analyzer for Frnech Language.
 * 
 * @author carol
 *
 */
public class CustomFrenchAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		StandardTokenizer src = new StandardTokenizer();

		TokenStream result = new FrenchLightStemFilter(src);
		result = new ElisionFilter(result, FrenchAnalyzer.DEFAULT_ARTICLES);
		result = new StopFilter(result, FrenchAnalyzer.getDefaultStopSet());
		result = new LowerCaseFilter(result);
		result = new ASCIIFoldingFilter(result);
		return new TokenStreamComponents(src, result);
	}

}

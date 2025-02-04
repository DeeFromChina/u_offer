package com.golead.art.literature.analyzer;

import java.io.File;
import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class DocAnalyzer extends Analyzer {
   private Set<char[]> stopWords;

   private File        file;

   private boolean     isMaxWordLength = false;

   private Version     matchVersion;

   public DocAnalyzer(Version matchVersion, Set<char[]> stopWords, File file) {
      this.matchVersion = matchVersion;
      this.stopWords = stopWords;
      this.file = file;
   }

   public TokenStream tokenStream(String fieldName, Reader reader) {
      // IKAnalyzer iKAnalyzer = new IKAnalyzer(isMaxWordLength);
      // TokenStream tokenStream = iKAnalyzer.tokenStream(fieldName, reader);
      // StopFilter stopFilter = new StopFilter(matchVersion, tokenStream,
      // stopWords, true);
      //
      // PorterStemFilter pAnalyzer = new PorterStemFilter(stopFilter);

      //暂时用标准分析器
      StandardAnalyzer standardAnalyzer = new StandardAnalyzer(matchVersion);
      PorterStemFilter pAnalyzer = new PorterStemFilter(standardAnalyzer.tokenStream(fieldName, reader));

      return pAnalyzer;
   }
}

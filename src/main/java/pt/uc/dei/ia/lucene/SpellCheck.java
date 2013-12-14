package pt.uc.dei.ia.lucene;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SpellCheck {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

		File dir = new File("/home/padsilva/spellchecker/"); // dict

		Directory directory = FSDirectory.open(dir);

		SpellChecker spellChecker = new SpellChecker(directory);

		spellChecker.indexDictionary(new PlainTextDictionary(new File(
				"/usr/share/dict/words")), new IndexWriterConfig(
				Version.LUCENE_CURRENT, new StandardAnalyzer(
						Version.LUCENE_CURRENT)), false);

		String wordForSuggestions = "hwllo";

		int suggestionsNumber = 5;

		String[] suggestions = spellChecker.suggestSimilar(wordForSuggestions,
				suggestionsNumber);

		if (suggestions != null && suggestions.length > 0) {
			for (String word : suggestions) {
				System.out.println("Did you mean:" + word);
			}
		} else {
			System.out.println("No suggestions found for word:"
					+ wordForSuggestions);
		}
		spellChecker.close();
	}

}

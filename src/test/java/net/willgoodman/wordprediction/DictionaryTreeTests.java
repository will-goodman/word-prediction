package net.willgoodman.wordprediction;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * @author Kelsey McKenna
 */
public class DictionaryTreeTests {

	private String[] testWords = { "the", "bye", "will", "hit", "eclair", "eclairs", "eclair's", "eclat", "eclat's",
			"thorough", "through", "i", "threw", "they", "there", "their" };

	@Test
	public void heightOfRootShouldBeZero() {
		DictionaryTree unit = new DictionaryTree();
		Assertions.assertEquals(0, unit.height());
	}

	@Test
	public void sizeOfRootShouldBeOne() {
		DictionaryTree unit = new DictionaryTree();
		Assertions.assertEquals(1, unit.size());
	}

	@Test
	public void heightOfWordShouldBeWordLength() {
		DictionaryTree unit = new DictionaryTree();
		unit.insert("word", 0);
		Assertions.assertEquals("word".length(), unit.height());
	}

	@Test
	public void insertUsingPopularity() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}

		Assertions.assertEquals(8, testTree.height());
	}

	@Test
	public void maximumBranchingShouldBeSix() {

		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}

		Assertions.assertEquals(6, testTree.maximumBranching());
	}
	
	@Test
	public void numLeavesShouldBeThirteen() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}

		Assertions.assertEquals(13, testTree.numLeaves());
	}
	
	@Test
	public void predictShouldReturnTheBye() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		List<String> wordList = new ArrayList<String>();
		wordList.add("the");
		wordList.add("bye");
		
		Assertions.assertEquals(wordList, testTree.predict("", 2));
	}
	
	@Test
	public void predictShouldReturnTheThorough() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		List<String> wordList = new ArrayList<String>();
		wordList.add("the");
		wordList.add("thorough");
		
		Assertions.assertEquals(wordList, testTree.predict("th", 2));
	}
	
	@Test
	public void predictShouldReturnNoLongerThanTheNumOfWords() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		List<String> wordList = new ArrayList<String>();
		wordList.add("eclair");
		wordList.add("eclairs");
		wordList.add("eclair's");
		wordList.add("eclat");
		wordList.add("eclat's");
		
		Assertions.assertEquals(wordList, testTree.predict("ec", 100));
	}
	
	@Test
	public void predictShouldReturnWordStartingWithPrefix() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		Assertions.assertTrue(testTree.predict("th").get().startsWith("th"));
	}
	
	@Test
	public void containsHiShouldReturnFalse() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		Assertions.assertFalse(testTree.contains("hi"));
	}
	
	@Test
	public void containsHiShouldReturnTrue() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		testTree.insert("hi");
		
		Assertions.assertTrue(testTree.contains("hi"));
	}
	
	@Test
	public void removeShouldRemoveHi() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		testTree.insert("hi");
		
		testTree.remove("hi");
		
		Assertions.assertFalse(testTree.contains("hi"));
	}
	
	@Test
	public void hitShouldNotBeRemoved() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		testTree.insert("hi");
		
		testTree.remove("hi");
		
		Assertions.assertTrue(testTree.contains("hit"));
	}
	
	@Test
	public void removeReturnsFalse() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		testTree.insert("hi");
		
		Assertions.assertFalse(testTree.remove("hi"));
	}
	
	@Test
	public void removeReturnsTrue() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		Assertions.assertTrue(testTree.remove("hit"));
	}
	
	@Test
	public void longestWordShouldBeThorough() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		Assertions.assertEquals("thorough", testTree.longestWord());
	}
	
	@Test
	public void allWordsShouldBeTestWords() {
		DictionaryTree testTree = new DictionaryTree();

		for (int i = 0; i < testWords.length; i++) {
			testTree.insert(testWords[i], i + 1);
		}
		
		List<String> correctDFSList = new ArrayList<String>();
		correctDFSList.add("the");
		correctDFSList.add("they");
		correctDFSList.add("there");
		correctDFSList.add("their");
		correctDFSList.add("thorough");
		correctDFSList.add("through");
		correctDFSList.add("threw");
		correctDFSList.add("bye");
		correctDFSList.add("will");
		correctDFSList.add("hit");
		correctDFSList.add("eclair");
		correctDFSList.add("eclairs");
		correctDFSList.add("eclair's");
		correctDFSList.add("eclat");
		correctDFSList.add("eclat's");
		correctDFSList.add("i");
		
		Assertions.assertEquals(correctDFSList, testTree.allWords());
	}
	
	

}

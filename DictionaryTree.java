import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.Arrays;

//import org.apache.log4j.Logger;

public class DictionaryTree {

	private boolean isEndOfWord = false;
	private int wordPopularity = 100;
	private Map<Character, DictionaryTree> children = new LinkedHashMap<>();
	//private Logger logger = Logger.getLogger(DictionaryTree.class);
	  private static final int EMPTY_STRING = 0;
	  private static final int START_INDEX = 0;
	  private static final int NO_CHILDREN = 0;
	  private static final int EMPTY_TREE = 1;
	  private static final int LEAF_NODE = 1;
	  private static final int SMALLEST_BRANCH_SIZE = 0;

	/**
	 * Inserts the given word into this dictionary. If the word already exists,
	 * nothing will change.
	 *
	 * @param word
	 *            the word to insert
	 */
	void insert(String word) {
		if (word.length() > EMPTY_STRING) {
			if (!this.contains(word)) {
				char[] wordChars = word.toCharArray();
				insertChars(wordChars, START_INDEX);
				//logger.info(word + " inserted");
			} else {
				//logger.info("Word already exists in the tree");
			}
		} else {
			//logger.debug("Word is empty");
		}
	}

	public void insertChars(char[] wordChars, int currentIndex) {
		if (currentIndex < wordChars.length) {
			boolean entryExists = false;
			char currentCharacter = wordChars[currentIndex];
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (entry.getKey() == currentCharacter) {
					if (currentIndex == wordChars.length - 1) {
						entry.getValue().isEndOfWord = true;
					}
					entry.getValue().insertChars(wordChars, ++currentIndex);

					entryExists = true;
					if (currentIndex == wordChars.length && !entry.getValue().isEndOfWord) {
						entry.getValue().isEndOfWord = true;

					}

				}
			}

			if (!entryExists) {
				DictionaryTree newTree = new DictionaryTree();
				if (currentIndex == wordChars.length - 1) {
					newTree.isEndOfWord = true;

				}
				children.put(wordChars[currentIndex], newTree);

				children.get(wordChars[currentIndex]).insertChars(wordChars, ++currentIndex);

			}
		}
	}

	/**
	 * Inserts the given word into this dictionary with the given popularity. If the
	 * word already exists, the popularity will be overriden by the given value.
	 *
	 * @param word
	 *            the word to insert
	 * @param popularity
	 *            the popularity of the inserted word
	 */
	void insert(String word, int popularity) {
		if (word.length() > EMPTY_STRING) {
			char[] wordChars = word.toCharArray();
			insertChars2(wordChars, START_INDEX, popularity);
			//logger.info(word + " inserted");
		} else {
			//logger.debug("Word is empty");
		}

	}

	public void insertChars2(char[] wordChars, int currentIndex, int popularity) {
		if (currentIndex < wordChars.length) {
			boolean entryExists = false;
			char currentCharacter = wordChars[currentIndex];
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (entry.getKey() == currentCharacter) {
					if (currentIndex == wordChars.length - 1) {
						entry.getValue().isEndOfWord = true;
						entry.getValue().wordPopularity = popularity;
					}
					entry.getValue().insertChars2(wordChars, ++currentIndex, popularity);
					entryExists = true;

				}
			}

			if (!entryExists) {
				DictionaryTree newTree = new DictionaryTree();
				if (currentIndex == wordChars.length - 1) {
					newTree.isEndOfWord = true;
					newTree.wordPopularity = popularity;
				}
				children.put(wordChars[currentIndex], newTree);

				children.get(wordChars[currentIndex]).insertChars2(wordChars, ++currentIndex, popularity);

			}
		}
	}

	/**
	 * Removes the specified word from this dictionary. Returns true if the caller
	 * can delete this node without losing part of the dictionary, i.e. if this node
	 * has no children after deleting the specified word.
	 *
	 * @param word
	 *            the word to delete from this dictionary
	 * @return whether or not the parent can delete this node from its children
	 */
	boolean remove(String word) {
		if (!this.contains(word)) {
			//logger.debug("Word doesn't exist in tree");
			return false;
		} else {

			char[] wordChars = word.toCharArray();
			int endIndex = wordChars.length - 1;
			boolean returnValue = true;
			while (endIndex > -1) {
				returnValue = returnValue && this.deleteEndNode(wordChars, endIndex, 0);
				endIndex -= 1;
			}

			//logger.info(word + " removed");
			return returnValue;
		}
	}

	private boolean deleteEndNode(char[] wordChars, int endIndex, int currentIndex) {
		if (currentIndex < endIndex) {
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (wordChars[currentIndex] == entry.getKey()) {
					return entry.getValue().deleteEndNode(wordChars, endIndex, ++currentIndex);
				}
			}
			return false;
		} else if (currentIndex == endIndex) {
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (wordChars[currentIndex] == entry.getKey()) {
					if (entry.getValue().children.size() == NO_CHILDREN) {
						this.children.remove(entry.getKey());
						return true;

					} else {
						entry.getValue().isEndOfWord = false;
						return false;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Determines whether or not the specified word is in this dictionary.
	 *
	 * @param word
	 *            the word whose presence will be checked
	 * @return true if the specified word is stored in this tree; false otherwise
	 */
	boolean contains(String word) {
		char[] wordChars = word.toCharArray();
		if (wordChars.length > EMPTY_STRING) {
			return this.containsChars(wordChars, START_INDEX);
		} else {
			//logger.debug("Word is empty");
			return false;
		}
	}

	private boolean containsChars(char[] wordChars, int currentIndex) {
		boolean wordExists = false;
		if (currentIndex < wordChars.length - 1) {
			char currentChar = wordChars[currentIndex];
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if ((entry.getKey() == Character.valueOf(currentChar))
						&& (entry.getValue().containsChars(wordChars, ++currentIndex))) {
					wordExists = true;
				}
			}
		} else if (currentIndex < wordChars.length) {
			char currentChar = wordChars[currentIndex];
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (entry.getKey() == Character.valueOf(currentChar) && entry.getValue().isEndOfWord) {
					//logger.info("Word exists");
					wordExists = true;
				}
			}

		}

		return wordExists;
	}

	/**
	 * @param prefix
	 *            the prefix of the word returned
	 * @return a word that starts with the given prefix, or an empty optional if no
	 *         such word is found.
	 */
	Optional<String> predict(String prefix) {
		char[] prefixChars = prefix.toCharArray();
		Optional<String> word = searchChars(prefixChars, START_INDEX);

		return word;
	}

	private Optional<String> searchChars(char[] prefixChars, int currentIndex) {
		if (currentIndex < prefixChars.length) {
			char currentCharacter = prefixChars[currentIndex];
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (currentCharacter == entry.getKey()) {
					return entry.getValue().searchChars(prefixChars, ++currentIndex);
				}
			}

			//logger.debug("Prefix doesn't exist");
			return Optional.empty();
		} else {
			if (this.children.size() > NO_CHILDREN) {
				return Optional.of(new String(prefixChars) + this.getToLeaf());
			} else {
				//logger.debug("Prefix has no children");
				return Optional.empty();
			}
		}
	}

	private String getToLeaf() {
		if (this.size() == EMPTY_TREE) {
			return "";
		} else {
			String word = "";
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				word += entry.getKey();
				word += entry.getValue().getToLeaf();
				break;
			}
			//logger.info("Found " + word);
			return word;
		}
	}

	/**
	 * Predicts the (at most) n most popular full English words based on the
	 * specified prefix. If no word with the specified prefix is found, an empty
	 * list is returned.
	 *
	 * @param prefix
	 *            the prefix of the words found
	 * @return the (at most) n most popular words with the specified prefix
	 */
	List<String> predict(String prefix, int n) {
		char[] prefixChars = prefix.toCharArray();
		List<String> words = searchChars2(prefixChars, START_INDEX, n);

		return words;
	}

	private List<String> searchChars2(char[] prefixChars, int currentIndex, int numOfWords) {
		if (currentIndex < prefixChars.length) {
			char currentCharacter = prefixChars[currentIndex];
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (currentCharacter == entry.getKey()) {
					return entry.getValue().searchChars2(prefixChars, ++currentIndex, numOfWords);
				}
			}

			//logger.debug("Prefix doesn't exist");
			return new ArrayList<String>();
		} else {
			if (this.children.size() > NO_CHILDREN) {
				HashMap<Integer, String> words = new HashMap<Integer, String>();
				String currentWord = new String(prefixChars);
				words = this.allWordsDFS3(words, currentWord);
				Object[] keys = words.keySet().toArray();
				Arrays.sort(keys);
				List<String> mostPopularWords = new ArrayList<String>();
				int i = START_INDEX;
				while (i < numOfWords && i < words.size()) {
					mostPopularWords.add(words.get(keys[i]));
					i++;
				}

				return mostPopularWords;
			} else {
				//logger.debug("Prefix has no children");
				return new ArrayList<String>();
			}
		}
	}

	private HashMap<Integer, String> allWordsDFS3(HashMap<Integer, String> words, String currentWord) {
		if (this.size() == EMPTY_TREE) {
			return words;
		} else {
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (entry.getValue().isEndOfWord) {
					String newWord = currentWord;
					newWord += entry.getKey();
					//logger.info("Found " + newWord);
					words.put(entry.getValue().wordPopularity, newWord);
					entry.getValue().allWordsDFS3(words, newWord);
				} else {
					String newWord = currentWord;
					newWord += entry.getKey();
					entry.getValue().allWordsDFS3(words, newWord);
				}
			}

			return words;
		}
	}

	/**
	 * @return the number of leaves in this tree, i.e. the number of words which are
	 *         not prefixes of any other word.
	 */
	int numLeaves() {
		if (this.size() == EMPTY_TREE) {
			return LEAF_NODE;
		} else {
			int numLeaves = 0;
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				numLeaves += entry.getValue().numLeaves();
			}
			return numLeaves;
		}
	}

	/**
	 * @return the maximum number of children held by any node in this tree
	 */
	int maximumBranching() {
		return this.getBranchSize(SMALLEST_BRANCH_SIZE);

	}

	public int getBranchSize(int maximumBranchSize) {
		if (this.size() == EMPTY_TREE) {
			return SMALLEST_BRANCH_SIZE;
		} else {
			int branchSizeAcc = SMALLEST_BRANCH_SIZE;
			int currentBranchSize;
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				branchSizeAcc++;
				currentBranchSize = entry.getValue().getBranchSize(maximumBranchSize);
				if (currentBranchSize > maximumBranchSize) {
					maximumBranchSize = currentBranchSize;
				}
			}

			if (branchSizeAcc > maximumBranchSize) {
				maximumBranchSize = branchSizeAcc;
			}

			return maximumBranchSize;
		}
	}

	/**
	 * @return the height of this tree, i.e. the length of the longest branch
	 */
	int height() {
		if (this.size() == EMPTY_TREE) {
			return NO_CHILDREN;
		} else {
			int largestHeight = NO_CHILDREN;
			int branchHeight;
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				branchHeight = entry.getValue().height();
				if (branchHeight > largestHeight) {
					largestHeight = branchHeight;
				}
			}
			return 1 + largestHeight;
		}
	}

	/**
	 * @return the number of nodes in this tree
	 */
	int size() {
		if (this.children.size() == NO_CHILDREN) {
			return EMPTY_TREE;
		} else {
			int sizeAcc = 0;
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				sizeAcc += entry.getValue().size();
			}

			return ++sizeAcc;
		}
	}

	/**
	 * @return the longest word in this tree
	 */
	String longestWord() {
		if (this.size() == EMPTY_TREE) {
			//logger.debug("Empty DictionaryTree");
			return "";
		} else {
			List<String> words = this.allWords();
			String longestWord = "";
			for (String word : words) {
				if (word.length() > longestWord.length()) {
					longestWord = word;
				}
			}

			//logger.info("Longest word: " + longestWord);
			return longestWord;

		}
	}

	/**
	 * @return all words stored in this tree as a list
	 */
	List<String> allWords() {
		if (this.size() == EMPTY_TREE) {
			//logger.debug("Empty DictionaryTree");
			return new ArrayList<String>();
		} else {

			List<String> words = new ArrayList<String>();
			return allWordsDFS2(words, "");

		}
	}

	private List<String> allWordsDFS2(List<String> words, String currentWord) {
		if (this.size() == EMPTY_TREE) {
			return words;
		} else {
			for (Map.Entry<Character, DictionaryTree> entry : children.entrySet()) {
				if (entry.getValue().isEndOfWord) {
					String newWord = currentWord;
					newWord += entry.getKey();
					//logger.info("Found " + newWord);
					words.add(newWord);
					entry.getValue().allWordsDFS2(words, newWord);
				} else {
					String newWord = currentWord;
					newWord += entry.getKey();
					entry.getValue().allWordsDFS2(words, newWord);
				}
			}

			return words;
		}
	}

	/**
	 * Folds the tree using the given function. Each of this node's children is
	 * folded with the same function, and these results are stored in a collection,
	 * cResults, say, then the final result is calculated using f.apply(this,
	 * cResults).
	 *
	 * @param f
	 *            the summarising function, which is passed the result of invoking
	 *            the given function
	 * @param <A>
	 *            the type of the folded value
	 * @return the result of folding the tree using f
	 */
	<A> A fold(BiFunction<DictionaryTree, Collection<A>, A> f) {
		throw new RuntimeException("DictionaryTree.fold not implemented yet");
	}

}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Kelsey McKenna
 */
public class CLI {

    /**
     * Loads words (lines) from the given file and inserts them into
     * a dictionary.
     *
     * @param f the file from which the words will be loaded
     * @return the dictionary with the words loaded from the given file
     * @throws IOException if there was a problem opening/reading from the file
     */
    static DictionaryTree loadWords(File f) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String word;
            DictionaryTree d = new DictionaryTree();
            int i = 1;
            while ((word = reader.readLine()) != null) {
                d.insert(word, i++);
            }

            return d;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Loading dictionary ... ");
        DictionaryTree d = loadWords(new File(args[0]));
        System.out.println("done");

        //System.out.println("Contains will: " + d.contains("will"));
        System.out.println("Height: " + d.height());
        System.out.println("Size: " + d.size());
        System.out.println("maximumBranching: " + d.maximumBranching());
        System.out.println("numLeaves: " + d.numLeaves());
        //System.out.println("allWords: " + d.allWords().toString());
        System.out.println("longestWord: " + d.longestWord());
        //System.out.println("contains hi: " + d.contains("hi"));
        //d.insert("hi");
        //System.out.println("contains hi2: " + d.contains("hi"));
        //d.remove("hi");
        //System.out.println("contains hi3: " + d.contains("hi"));
        //System.out.println("allWords2: " + d.allWords().toString());
        //System.out.println("contains ecl: " + d.contains("ecl"));
        //d.insert("hi");
       // System.out.println("allWords3: " + d.allWords().toString());
        System.out.println("predict 'th' with popularity: " + d.predict("th", 30));

        System.out.println("Enter prefixes for prediction below.");

        try (BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("---> " + d.predict(fromUser.readLine()));
            }
        }
    }

}

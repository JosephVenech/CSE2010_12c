/*

  Authors (group members): Joseph Venech, Avery Haughwout, Christopher Blackwell
  Email addresses of group members:
  Group name: 12C (Maybe make it smth cool later idk)

  Course: CSE 2010
  Section:3:30

  Description:
    SmartWord uses a Trie data structure to store English words.
    It learns word frequencies from old messages and predicts
    the three most frequently used words matching a typed prefix.
    The Trie maintains the three best predictions at each node.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class SmartWord {

    String[] guesses = new String[3];

    // Trie node
    private static class TrieNode {
        char letter;

        TrieNode firstChild;
        TrieNode nextSibling;

        boolean isWord;
        String word;
        int frequency;

        TrieNode best1;
        TrieNode best2;
        TrieNode best3;

        TrieNode(char letter) {
            this.letter = letter;
        }
    }

    private TrieNode root;

    // Current word being typed
    private StringBuilder currentWord = new StringBuilder();

    // Initialize SmartWord with words.txt
    public SmartWord(String wordFile) {

        root = new TrieNode('\0');

        try (BufferedReader br = new BufferedReader(new FileReader(wordFile))) {

            String line;

            while ((line = br.readLine()) != null) {

                String word = normalize(line.trim());

                if (!word.isEmpty()) {
                    insert(word, false);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Convert to lowercase and remove non-English letters
    private String normalize(String input) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {

            char c = Character.toLowerCase(input.charAt(i));

            if (c >= 'a' && c <= 'z') {
                result.append(c);
            }
        }

        return result.toString();
    }

    // Find a child node
    private TrieNode findChild(TrieNode node, char c) {

        TrieNode child = node.firstChild;

        while (child != null) {

            if (child.letter == c) {
                return child;
            }

            child = child.nextSibling;
        }

        return null;
    }

    // Get or create a child node
    private TrieNode getChild(TrieNode node, char c) {

        TrieNode child = findChild(node, c);

        if (child != null) {
            return child;
        }

        TrieNode newNode = new TrieNode(c);

        newNode.nextSibling = node.firstChild;
        node.firstChild = newNode;

        return newNode;
    }

    // Insert a word into the Trie
    private void insert(String word, boolean updateFrequency) {

        TrieNode[] path = new TrieNode[word.length() + 1];

        TrieNode current = root;
        path[0] = root;

        for (int i = 0; i < word.length(); i++) {

            current = getChild(current, word.charAt(i));
            path[i + 1] = current;
        }

        if (!current.isWord) {

            current.isWord = true;
            current.word = word;
            current.frequency = 1;

        } else if (updateFrequency) {

            current.frequency++;
        }

        // Update predictions along the path
        for (int i = word.length(); i >= 0; i--) {
            updateBest(path[i]);
        }
    }

    // Compare two candidate words
    private boolean better(TrieNode a, TrieNode b) {

        if (a == null) return false;
        if (b == null) return true;

        if (a.frequency != b.frequency) {
            return a.frequency > b.frequency;
        }

        return a.word.compareTo(b.word) < 0;
    }

    // Add a candidate to the top three
    private void offer(TrieNode node, TrieNode candidate) {

        if (candidate == null) return;

        if (better(candidate, node.best1)) {

            node.best3 = node.best2;
            node.best2 = node.best1;
            node.best1 = candidate;

        } else if (better(candidate, node.best2)) {

            node.best3 = node.best2;
            node.best2 = candidate;

        } else if (better(candidate, node.best3)) {

            node.best3 = candidate;
        }
    }

    // Update the three best words beneath a node
    private void updateBest(TrieNode node) {

        node.best1 = null;
        node.best2 = null;
        node.best3 = null;

        if (node.isWord) {
            offer(node, node);
        }

        TrieNode child = node.firstChild;

        while (child != null) {

            offer(node, child.best1);
            offer(node, child.best2);
            offer(node, child.best3);

            child = child.nextSibling;
        }
    }

    // Find the node corresponding to a prefix
    private TrieNode findPrefix(String prefix) {

        TrieNode current = root;

        for (int i = 0; i < prefix.length(); i++) {

            current = findChild(current, prefix.charAt(i));

            if (current == null) {
                return null;
            }
        }

        return current;
    }

    // Process old messages
    public void processOldMessages(String oldMessageFile) {

        try (BufferedReader br =
                     new BufferedReader(new FileReader(oldMessageFile))) {

            String line;

            while ((line = br.readLine()) != null) {

                StringBuilder word = new StringBuilder();

                for (int i = 0; i < line.length(); i++) {

                    char c = Character.toLowerCase(line.charAt(i));

                    if (c >= 'a' && c <= 'z') {

                        word.append(c);

                    } else {

                        if (word.length() > 0) {

                            insert(word.toString(), true);
                            word.setLength(0);
                        }
                    }
                }

                // Process final word on the line
                if (word.length() > 0) {
                    insert(word.toString(), true);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate three guesses
    public String[] guess(
            char letter,
            int letterPosition,
            int wordPosition) {

        // Beginning of a new word
        if (letterPosition == 0) {
            currentWord.setLength(0);
        }

        char c = Character.toLowerCase(letter);

        if (c >= 'a' && c <= 'z') {
            currentWord.append(c);
        }

        // Clear previous guesses
        for (int i = 0; i < 3; i++) {
            guesses[i] = null;
        }

        TrieNode node = findPrefix(currentWord.toString());

        if (node == null) {
            return guesses;
        }

        if (node.best1 != null) {
            guesses[0] = node.best1.word;
        }

        if (node.best2 != null) {
            guesses[1] = node.best2.word;
        }

        if (node.best3 != null) {
            guesses[2] = node.best3.word;
        }

        return guesses;
    }

    // Process feedback
    public void feedback(
            boolean isCorrectGuess,
            String correctWord) {

        if (correctWord == null) {
            return;
        }

        String word = normalize(correctWord);

        if (!word.isEmpty()) {
            insert(word, true);
        }
    }
}


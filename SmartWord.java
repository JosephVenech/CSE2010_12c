/*

  Authors (group members): Joseph Venech, Avery Haughwout, Christopher Blackwell
  Email addresses of group members:
  Group name: 12C (Maybe make it smth cool later idk)

  Course: CSE 2010
  Section:3:30

  Description of the overall algorithm:


*/
import java.io.BufferedReader;

public class SmartWord
{
	//Added 17 November by Avery Haughwout - Added a BufferedReader for each input argument 
	BufferedReader knownWordReader = new BufferedReader(new file(args[0]);
	BufferedReader oldTextReader = new BufferedReader(new file(args[1]);
	BufferedReader newTextReader = new BufferedReader(new file(args[2]);
    String[] guesses = new String[3];  // 3 guesses from SmartWord

    // initialize SmartWord with a file of English words
    public SmartWord(String wordFile)
    {

    }

    // process old messages from oldMessageFile
    public void processOldMessages(String oldMessageFile)
    {
     
    }

    // based on a letter typed in by the user, return 3 word guesses in an array
    // letter: letter typed in by the user
    // letterPosition:  position of the letter in the word, starts from 0
    // wordPosition: position of the word in a message, starts from 0
    public String[] guess(char letter,  int letterPosition, int wordPosition)
    {
	
        return guesses;
    }

    // feedback on the 3 guesses from the user
    // isCorrectGuess: true if one of the guesses is correct
    // correctWord: 3 cases:
    // a.  correct word if one of the guesses is correct
    // b.  null if none of the guesses is correct, before the user has typed in 
    //            the last letter
    // c.  correct word if none of the guesses is correct, and the user has 
    //            typed in the last letter
    // That is:
    // Case       isCorrectGuess      correctWord   
    // a.         true                correct word
    // b.         false               null
    // c.         false               correct word
    public void feedback(boolean isCorrectGuess, String correctWord)        
    {

    }

}

package com.MemoryLadder;

//import com.MemoryLadderFull.R;

public class Scoring {
	final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
	final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
	final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
//	final private static int LISTS_WORDS     = Constants.LISTS_WORDS;
	final private static int LISTS_EVENTS    = Constants.LISTS_EVENTS;	
	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;
	final private static int CARDS_SPEED     = Constants.CARDS_SPEED;
    final private static int CARDS_LONG      = Constants.CARDS_LONG;
	
	
  public static int[] getScore(int gameType, String[][] guess, String[][] answer ) {
	  switch (gameType) {	  	
	  	case LISTS_EVENTS: return getScore_HISTORICAL_DATES(guess, answer);
	  	case SHAPES_FACES:    return getScore_NAMES_FACES(guess, answer);
	  	case SHAPES_ABSTRACT: return getScore_ABSTRACT_IMAGES(guess, answer);
	  	case CARDS_SPEED: return getScore_SPEED_CARDS(guess, answer);
	  	case CARDS_LONG:  return getScore_LONG_CARDS_WMC(guess, answer);
	  	default: return null;
	  }
  }
  
  public static int[] getScore(String[][] guess, String[][] answer, int columnsize) {
	  return getScore_RANDOM_WORDS(guess, answer, columnsize);
  }
  
  public static int[] getScore(int gameType, char[][] guess, char[][] answer ) {
	  switch (gameType) {
	  	case NUMBERS_SPEED:  return getScore_StringArray_ByRow( guess, answer );
	  	case NUMBERS_LONG:   return getScore_StringArray_ByRow( guess, answer );
	  	case NUMBERS_BINARY: return getScore_StringArray_ByRow( guess, answer );
	  	case NUMBERS_SPOKEN: return getScore_SPOKEN_NUMBERS(guess, answer);
	  	default: return null;
	  }
  }
  
  
  public static int getScore_BINARY_NUMBERS(String[][] guess, String[][] answer ) { //  String[][] guess, String[][] answer 
    return getScore_StringArray_ByRow( guess, answer, false );
  }
  
  public static int getScore_WRITTEN_NUMBERS(String[][] guess, String[][] answer ) { //  String[][] guess, String[][] answer 
    return getScore_StringArray_ByRow( guess, answer, false );
  }
  
  public static int[] getScore_WRITTEN_NUMBERS_CHAR(char[][] guess, char[][] answer ) { //  String[][] guess, String[][] answer 
	    return getScore_StringArray_ByRow( guess, answer );
	  }
  
  /* might be easier to use strings */
  public static int[] getScore_SPOKEN_NUMBERS( char[][] guess, char[][] answer ) {
    
    double numCorrect = 0;
    int numRows = guess.length;
    int numCols = guess[0].length;
    
    double TotalScore = 0;
    Boolean done = false;
    double recallCorrect = 0;
    double recallAttempt = 0;
    
    for (int row = 0; row < numRows; row++) { 
      for (int col = 0; col < numCols; col++) {
        if (guess[row][col] != answer[row][col]) {
          if (!done) {
        	TotalScore = numCorrect;
        	done = true;        	
          }
          if (guess[row][col] != '*' && guess[row][col] != ' ')
        	  recallAttempt++;       	  
        }
        else {
          numCorrect++;
          recallCorrect++;
          recallAttempt++;        	  
        }
      }
    }
    if (!done)
    	TotalScore = numCorrect;
    
    int[] scores = new int[3];
    scores[0] = (int) recallCorrect;
    scores[1] = (int) recallAttempt;
    scores[2] = (int) roundUpToZero(TotalScore);
    return scores;
  }
  
  
  
   
  
  public static int[] getScore_RANDOM_WORDS( String[][] guess, String[][] answer, int columnsize ) {
    return getScore_StringArray_ByColumn( toLowerCase(guess), toLowerCase(answer), true, columnsize );
  }
  
  public static int[] getScore_HISTORICAL_DATES( String[][] guess, String[][] answer ) {
    
    double numCorrect = 0;
    double numWrong = 0;
    
    double recallCorrect = 0;
    double recallAttempt = 0;
    
    for (int i=0; i<guess.length; i++) {
      if (guess[i][0].equals(answer[i][0])) {
        numCorrect++;
        recallCorrect++;
        recallAttempt++;
      }
      else if (!guess[i][0].equals("")) {
        numWrong++;
        recallAttempt++;
      }
      else
    	;
    }
    
    int[] scores = new int[3];
    scores[0] = (int) recallCorrect;
    scores[1] = (int) recallAttempt;
    scores[2] = (int) roundUpToZero( (numCorrect * 1) - (numWrong * 0.5) );
    return scores;
  }
  
  
  
  
  
  /* untested */
  public static int[] getScore_SPEED_CARDS( String[][] guess, String[][] answer ) {
    double numCorrect = 0;
    
    double recallCorrect = 0;
    double recallAttempt = 0;
    Boolean done = false;
    double totalScore = 0;
    
    for (int i=0; i<guess[0].length; i++) {
      if (guess[0][i].equals(answer[0][i])) {
		  recallCorrect++;
		  recallAttempt++;
		  if (!done)
			  numCorrect++;
      }
      else {
    	  if (!done)
    		  totalScore = numCorrect;
    	  if (!guess[0][i].equals("card_xx"))
    		  recallAttempt++;
      }
    }
    int[] scores = new int[3];
    scores[0] = (int) recallCorrect;
    scores[1] = (int) recallAttempt;
    scores[2] = (int) roundUpToZero( totalScore );
    return scores;   
  }
  
  
  
  public static int[] getScore_LONG_CARDS_WMC( String[][] guess, String[][] answer ) {
	  
    double TotalScore = 0;
    int numDecks = guess.length;
    
    double recallCorrect = 0;
    double recallAttempt = 0;
    
    int lastAttemptedDeck = lastAttemptedRow(guess, "card_xx");
    
    for (int deck=0; deck<numDecks; deck++) {
      int numCorrect = 0;
      int numBlank = 0;
      int numWrong = 0;
      double deckPoints = 0;
      
      if (attemptedDeck(guess[deck]))
    	  recallAttempt += guess[deck].length;
      
      for (int i=0; i<guess[deck].length; i++) {
        if (guess[deck][i].equals(answer[deck][i]))
          numCorrect++;
        else if (guess[deck][i].equals("card_xx"))
          numBlank++;
        else
          numWrong++;        
      }
      
      recallCorrect += numCorrect;
      
      if (deck == lastAttemptedDeck) {   // final deck only
        if (numWrong == 0)
          deckPoints = numCorrect;
        else if (numWrong == 1)
          deckPoints = roundUp((double) (numCorrect+numWrong) / 2);
        else
          deckPoints = 0;
      }
      
      else {
        if (numWrong+numBlank == 0)
          deckPoints = guess[deck].length;
        else if (numWrong+numBlank == 1)
          deckPoints = (int) (((double) guess[deck].length / 2) + .5);
        else
          deckPoints = 0;
      }
      
      TotalScore += deckPoints;
    }
    
    int[] scores = new int[3];
    scores[0] = (int) recallCorrect;
    scores[1] = (int) recallAttempt;
    scores[2] = (int) roundUpToZero(TotalScore);
    return scores;
  }
   
  
  public static int[] getScore_NAMES_FACES( String[][] guess, String[][] answer  ) { 
    
    int numRows = guess.length;
    int numCols = guess[0].length;
    
    double firstRecallCorrect = 0;
    double firstRecallAttempt = 0;
    double lastRecallCorrect = 0;
    double lastRecallAttempt = 0;
    
    for (int row=0; row<numRows; row++) {
        for (int col=0; col<numCols; col++) {
      	  guess[row][col] = guess[row][col].toLowerCase();
      	  answer[row][col] = answer[row][col].toLowerCase();
        }
      }
    
    double TotalScore = 0;
    double FaceScore = 0;
    
    for (int row=0; row<numRows; row++) {
      for (int col=0; col<numCols; col++) {
        
        FaceScore = 0;
        String name     = guess[row][col];
        String ansFirst = answer[row][col].substring(0, answer[row][col].indexOf(" "));
        String ansLast  = answer[row][col].substring(answer[row][col].indexOf(" ") + 1, answer[row][col].length());
        
        if (name == null || name.equals("")) 
        	FaceScore = 0;
        else {   
	        if (name.charAt(name.length() - 1) == ' ')
	          name = name.substring(0, name.length() - 1);
	        name = name.toLowerCase();
	        
	
	        if (!name.contains(" ")) {  // only one name entered
	          
	          if (name.equals(ansFirst)) {
	            FaceScore = 1;
	            firstRecallCorrect++;
	            firstRecallAttempt++;
	          }
	          else if (name.equals(ansLast)) {
	            FaceScore = 1;
	            lastRecallCorrect++;
	            lastRecallAttempt++;
	          }
	          else if (oneCharAway(name, ansFirst)) {
	            FaceScore = 0.5;
	            firstRecallCorrect++;
	            firstRecallAttempt++;
	          }
	          else if (oneCharAway(name, ansLast)) {
	            FaceScore = 0.5;
	            lastRecallCorrect++;
	            lastRecallAttempt++;
	          }
	          else {
	            FaceScore = -0.5;
	            firstRecallAttempt++;
	            lastRecallAttempt++;
	          }
	        }
	        
	        
	        else {                    // standard name entry, two names entered
	          String guessFirst = name.substring(0, name.indexOf(" "));
	          String guessLast = name.substring(name.indexOf(" ") + 1, name.length());
	          double firstScore = 0;
	          double lastScore = 0;
	          
	          if (guessFirst.equals(ansFirst)) {
	            firstScore = 1;
	            firstRecallCorrect++;
	            firstRecallAttempt++;
	          }
	          else if (oneCharAway(guessFirst, ansFirst)) {
	            firstScore = 0.5;
	            firstRecallCorrect++;
	            firstRecallAttempt++;
	          }
	          else if (guessFirst.length() == ansFirst.length() && withinThreeChars(guessFirst, ansFirst))
	            firstScore = 0;
	          else {
	            firstScore = -0.5;	           
	            firstRecallAttempt++;
	          }
	          
	          if (guessLast.equals(ansLast)) {
	            lastScore = 1;
	            lastRecallCorrect++;
	            lastRecallAttempt++;
	          }
	          else if (oneCharAway(guessLast, ansLast)) { 
	            lastScore = 0.5;
	            lastRecallCorrect++;
	            lastRecallAttempt++;
	          }
	          else if (guessLast.length() == ansLast.length() && withinThreeChars(guessLast, ansLast))
	            lastScore = 0;
	          else {
	            lastScore = -0.5;
	            lastRecallAttempt++;
	          }
	          
	          FaceScore = firstScore + lastScore;
	        }
        }
        
        TotalScore += FaceScore;
      }
    }
    
    int[] scores = new int[5];
    scores[0] = (int) firstRecallCorrect;
    scores[1] = (int) firstRecallAttempt;
    scores[2] = (int) lastRecallCorrect;
    scores[3] = (int) lastRecallAttempt;
    scores[4] = (int) roundUpToZero(TotalScore);
    return scores;
  }
  
  /* untested */
  public static Boolean withinThreeChars(String guess, String answer) {
    if (guess.length() != answer.length())
      return false;
    else {
      int numMistakes = 0;
      for (int i=0; i<guess.length(); i++) {
        if (guess.charAt(i) != answer.charAt(i))
          numMistakes++;
      }
      if (numMistakes > 3)
        return false;
    }
    return true;   
  }
  
  
  
  /* untested */
  public static int[] getScore_ABSTRACT_IMAGES( String[][] guess, String[][] answer ) {
    double numCorrect = 0;
    double numWrong = 0;
    double numBlank = 0;
    double TotalScore = 0;
    int numRows = guess.length;
    int numCols = guess[0].length;
    
    int recallCorrect = 0;
    int recallAttempt = 0;
    
    for (int row=0; row<numRows; row++) {
      numCorrect = 0;
      
      if (attemptedRow(guess[row], ""))
    	  recallAttempt += guess[row].length;
      
      for (int col=0; col<numCols; col++) {
        if (guess[row][col].equals(answer[row][col]))
          numCorrect++;
        else if (guess[row][col].equals(""))
          numBlank++;
        else
          numWrong++;
      }
      
      recallCorrect += numCorrect;
      
      if (numCorrect == numCols)
        TotalScore += numCorrect;
      else if (numBlank < numCols)
        TotalScore --;
      else
        TotalScore += 0;
    }
    
    int[] scores = new int[3];
    scores[0] = recallCorrect;
    scores[1] = recallAttempt;
    scores[2] = (int) roundUpToZero(TotalScore);
    return scores;
  }
  
  
  
  
  
  
  
  
  
  
  /* Note to self: I have not tested the case of having some blank entries in the last row for either or the behemoths */
  /* double check rounding properly 
   * */
  public static int[] getScore_StringArray_ByColumn( String[][] guess, String[][] answer, Boolean checkSpelling, int columnsize ) {

    double TotalScore = 0;
    
    int numRows = guess.length;
    int numCols = guess[0].length;
    
    int numPages = numRows / columnsize;
    int lastAttemptedColumn = lastAttemptedColumn(guess, "", columnsize);
    
    System.out.println("Last attempted column: " + lastAttemptedColumn);
    
    int recallCorrect = 0;
    int recallAttempt = 0;
    int totalMispelled = 0;
        
    
    for (int page=0; page<numPages; page++) {
    	int rowstart = 0 + (columnsize * page);
    	int rowend = rowstart + columnsize;
    
	    for (int col = 0; col < numCols; col++) {
	      
	      int numCorrect = 0;
	      int numWrong = 0;
	      int numMispelled = 0;
	      int numBlank = 0;
	      double columnScore = 0;
	      
	      if (attemptedColumn(guess, col%numCols, rowstart, rowend, ""))
	    	  recallAttempt += columnsize;
	      
	      for (int row = rowstart; row<rowend; row++) {
	        
	        if (guess[row][col] .equals (answer[row][col])) {                        // right
	          numCorrect++;
	          recallCorrect++;
	        }
	        else if (checkSpelling && oneCharAway(guess[row][col], answer[row][col]))               // right, but mispelled
	          numMispelled++;
	        else if (guess[row][col].equals("")) {                                  // blank
	          numBlank++;
	          numWrong++;
	        }
	        else {                                                                  // wrong
	          numWrong++;
	        }
	      }
	      
	      if (!( (col + (numCols * page)) == lastAttemptedColumn) ) { // not the last column attempted
	        if (numWrong == 0)
	          columnScore = numCorrect + numMispelled;
	        else if (numWrong == 1)
	          columnScore = (((double) (numCorrect + numMispelled) / 2) + .5);
	        else
	          columnScore = 0;
	        
	        columnScore -= numMispelled;
	      }
	      
	      else {                                       // indeed the last column attempted
	        if ((numWrong - numBlank) == 0)
	          columnScore = numCorrect + numMispelled;
	        else if ((numWrong - numBlank) == 1)
	          columnScore = (((double) (numCorrect + numMispelled) / 2) + .5);
	        else
	          columnScore = 0;
	        columnScore -= numMispelled;
	      }
	      
	      TotalScore += columnScore;
	      totalMispelled += numMispelled;
	      System.out.println("Col: " + col + " colscore: " + columnScore);
	    }
	    
    }
    
    
    int[] scores = new int[4];
    scores[0] = recallCorrect;
    scores[1] = recallAttempt;
    scores[2] = totalMispelled;
    scores[3] = (int) roundUpToZero(TotalScore);
    return scores;
  }
  
  
  
  
  
  
  /* double check that i'm rounding properly */
  public static int getScore_StringArray_ByRow(String[][] guess, String[][] answer, Boolean checkSpelling) {
    
    double TotalScore = 0;
    int numRows = guess.length;
    int numCols = guess[0].length;
        
    for (int row = 0; row < numRows; row++) {   // one row at a time
      
      int numCorrect = 0;
      int numWrong = 0;
      int numMispelled = 0;
      int numBlank = 0;
      double columnScore = 0;
      
      for (int col = 0; col<numCols; col++) {
        
        if (guess[row][col] .equals (answer[row][col]))                        // right
          numCorrect++;
        else if (checkSpelling && oneCharAway(guess[row][col], answer[row][col]))               // right, but mispelled
          numMispelled++;
        else if (guess[row][col].equals("")) {                                  // blank
          numBlank++;
          numWrong++;
        }
        else                                                                   // wrong
          numWrong++;                                                                  
      }
      
      if (row != numRows - 1) {
        if (numWrong == 0)
          columnScore = numCorrect + numMispelled;
        else if (numWrong == 1)
          columnScore = (((double) (numCorrect + numMispelled) / 2) + .5);
        else
          columnScore = 0;
        
        columnScore -= numMispelled;
      }
      
      else {
        if ((numWrong - numBlank) == 0)
          columnScore = numCorrect + numMispelled;
        else if ((numWrong - numBlank) == 1)
          columnScore = (((double) (numCorrect + numMispelled) / 2) + .5);
        else
          columnScore = 0;
        columnScore -= numMispelled;
      }
      
      TotalScore += columnScore;
    }
    return (int) roundUpToZero(TotalScore);
  }
  
  
  public static int lastAttemptedColumn(String[][] guess, String blank, int columnsize) {
	  int numRows = guess.length;
	  int numCols = guess[0].length;
	  int numPages = numRows / columnsize;
	  
	  for (int col=(numCols * numPages) - 1; col >= 0; col--) {
		  int modcol = col % numCols;
		  int page = col / numCols;
		  int rowstart = columnsize * page;
		  int rowend = rowstart + columnsize;
		  
		  System.out.println(modcol + " " + page + " " + rowstart + " " + rowend);

		  if (attemptedColumn(guess, modcol, rowstart, rowend, blank))
			  return col;
	  }			  
	  return 0;
  }
  
  public static Boolean attemptedColumn(String[][] array, int col, int rowstart, int rowend, String blank) {
	  for (int row=rowstart; row<rowend; row++) {
		  if (!array[row][col].equals(blank))
			  return true;
	  }
	  return false;
  }
  
  
  /* Returns the highest row index that includes an attempted answer */
  public static int lastAttemptedRow(String[][] guess, String blank) {
	  for (int row=guess.length - 1; row>=0; row--) {
		  if (attemptedRow(guess[row], blank))
			  return row;
	  }
	  return 0;
  }
  
  public static int lastAttemptedRow(char[][] guess) {
	  for (int row=guess.length - 1; row>=0; row--) {
		  if (attemptedRow(guess[row]))
			  return row;
	  }
	  return 0;
  }
  
  
  public static Boolean attemptedColumn(String[][] array, int col, String blank) {
	  for (int row=0; row<array.length; row++) {
		  if (!array[row][col].equals(blank))
			  return true;
	  }
	  return false;
  }
  
  
  public static Boolean attemptedRow(String[] row, String blank) {
	  for (int i=0; i<row.length; i++) {
		  if (!row[i].equals(blank))
			  return true;
	  }
	  return false;
  }
  
  public static Boolean attemptedRow(char[] row) {
	  for (int i=0; i<row.length; i++) {
		  if (! (row[i] == '*' || row[i] == ' '))
			  return true;
	  }
	  return false;
  }
  
  
  public static Boolean attemptedDeck(String[] deck) {
	  for (int i=0; i<deck.length; i++) {
		  if (!deck[i].equals("card_xx"))
			  return true;
	  }
	  return false;
  }
  
  
  
  /* double check that i'm rounding properly */
  public static int[] getScore_StringArray_ByRow(char[][] guess, char[][] answer) {
    
    double TotalScore = 0;
    int numRows = guess.length;
    int numCols = guess[0].length;
    
    int recallCorrect = 0;
    int recallAttempt = 0;
        
    for (int row = 0; row < numRows; row++) {   // one row at a time
      
      int numCorrect = 0;
      int numWrong = 0;
      int numBlank = 0;
      double columnScore = 0;
      int lastAttemptedRow = lastAttemptedRow(guess);
      
      Utils.print(guess);
      System.out.println(lastAttemptedRow + " ***************last attempted row***");
      
      if (attemptedRow(guess[row]))
    	  recallAttempt += guess[row].length;
      
      for (int col = 0; col<numCols; col++) {
        
        if (guess[row][col] == (answer[row][col])) {                        // right
          numCorrect++;
          recallCorrect++;
        }
        else if (guess[row][col] == '*' || guess[row][col] == ' ') {                                  // blank
          numBlank++;
          numWrong++;
        }
        else {                                                                  // wrong
          numWrong++;
        }
      }
      
      if (row != lastAttemptedRow) {
        if (numWrong == 0)
          columnScore = numCorrect;
        else if (numWrong == 1)
          columnScore = roundUp((double) numCols / 2);
        else
          columnScore = 0;
      }
      
      else {
        if ((numWrong - numBlank) == 0)
          columnScore = numCorrect;
        else if ((numWrong - numBlank) == 1)
          columnScore = roundUp((double) (numCorrect+numWrong-numBlank) / 2);
        else
          columnScore = 0;
      }
      
      TotalScore += columnScore;
    }
    int[] scores = new int[3];
    scores[0] = recallCorrect;
    scores[1] = recallAttempt;
    scores[2] = (int) roundUpToZero(TotalScore);
    return scores;
  }
  
  
  
  
  public static String[][] toLowerCase(String [][] array) {
    int numRows = array.length;
    int numCols = array[0].length;
    for (int row = 0; row < numRows; row++) { 
      for (int col = 0; col < numCols; col++) {
        array[row][col] = array[row][col].toLowerCase();
      }
    }
    return array;
  }
  
  
  public static Boolean oneCharAway(String guess, String answer) {

    int guessLength = guess.length();
    int answerLength = answer.length();
    
    int diff = guessLength - answerLength;
    if (diff < 0)
      diff = -1 * diff;
    
    if      (diff > 1)
      return false;
 
    else if (diff == 1) {
      String small;
      String large;
      if (guessLength < answerLength) {
        small = guess;
        large = answer;
      }
      else {
        small = answer;
        large = guess;
      }
      for (int pos=0; pos<=small.length(); pos++) {
        for (char b: "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
          String a = small.substring(0, pos);
        //String b = b;
          String c = small.substring(pos, small.length());
          String newGuess = a + b + c;
          
          if (newGuess.equals(large))
            return true;
        }
      }
    }
    
    else {   // diff == 0
      for (int pos=0; pos<guess.length(); pos++) {
        for (char b: "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
          String a = guess.substring(0, pos);
        //String b = b;
          String c = guess.substring(pos+1, guess.length());
          String newGuess = a + b + c;
          
          if (newGuess.equals(answer))
            return true;
        }
      }
    }
    return false;
  }
  
  public static void print(Object [][] a) {
    for (int i =0; i < a.length; i++) {
      for (int j = 0; j < a[0].length; j++) {
        System.out.print(" " + a[i][j]);
      }
      System.out.println("");
    }
  }
  
  public static void print(char[][] a) {
	  System.out.println(a.length + " " + a[0].length);
	  for (int i =0; i < a.length; i++) {
	      for (int j = 0; j < a[0].length; j++) {
	        System.out.print(" " + a[i][j]);
	      }
	      System.out.println("");
	    }
  }
  
  
  public static double roundUp(double d) {
    return (double) ((int) (d + 0.5));
  }
  
  public static double roundUpToZero(double d) {
	  if (d < 0)
		  return 0;
	  return roundUp(d);
  }
  
}
    
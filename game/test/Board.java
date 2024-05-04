package test;
import java.util.ArrayList;


public class Board {
    private static final int SIZEBOARD = 15;
    private static Board singleBoard = null; // for singleton
    Tile[][] gameBoard;
    ArrayList <Word> wordsExist = new ArrayList<>();
    private final String[][] tileBonus;


    private Board() {
        gameBoard = new Tile[SIZEBOARD][SIZEBOARD];
        tileBonus = new String[SIZEBOARD][SIZEBOARD];

        int[][] positions = {
            {0, 0}, {0, 3}, {0, 7}, {0, 11}, {0, 14}, 
            {1, 1}, {1, 5}, {1, 9}, {1, 13},
            {2, 2}, {2, 6}, {2, 8}, {2, 12}, 
            {3, 0}, {3, 3}, {3, 7}, {3, 11}, {3, 14},
            {4, 4}, {4, 10},
            {5, 1}, {5, 5}, {5, 9}, {5, 13},
            {6, 2}, {6, 6}, {6, 8}, {6, 12}, 
            {7, 0}, {7, 3}, {7, 7}, {7, 11}, {3, 14}, // שורה עם כוכב
            {8, 2}, {8, 6}, {8, 8}, {8, 12},
            {9, 1}, {9, 5}, {9, 9}, {9, 13}, 
            {10, 4}, {10, 10},
            {11, 0}, {11, 3}, {11, 7}, {11, 11}, {11, 14}, 
            {12, 2}, {12, 6}, {12, 8}, {12, 12},
            {13, 1}, {13, 5}, {13, 9}, {13, 13}, 
            {14, 0}, {14, 3}, {14, 7}, {14, 11}, {14, 14},
        };

        String[] colors = {
            "wordValueMultiThree", "tileValueMulti", "wordValueMultiThree", "tileValueMulti", "wordValueMultiThree",
            "wordValueMulti", "tileValueMultiThree", "tileValueMultiThree", "wordValueMulti",
            "wordValueMulti", "tileValueMulti", "tileValueMulti", "wordValueMulti",
            "tileValueMulti", "wordValueMulti", "tileValueMulti", "wordValueMulti", "tileValueMulti",
            "wordValueMulti", "wordValueMulti",
            "tileValueMultiThree", "tileValueMultiThree", "tileValueMultiThree", "tileValueMultiThree",
            "tileValueMulti", "tileValueMulti", "tileValueMulti", "tileValueMulti",
            "wordValueMultiThree", "tileValueMulti", "starWordValueMulti", "tileValueMulti", "wordValueMultiThree",
            "tileValueMulti", "tileValueMulti", "tileValueMulti", "tileValueMulti",
            "tileValueMultiThree", "tileValueMultiThree", "tileValueMultiThree", "tileValueMultiThree",
            "wordValueMulti", "wordValueMulti",
            "tileValueMulti", "wordValueMulti", "tileValueMulti", "wordValueMulti", "tileValueMulti",
            "wordValueMulti", "tileValueMulti", "tileValueMulti", "wordValueMulti",
            "wordValueMulti", "tileValueMultiThree", "tileValueMultiThree", "wordValueMulti",
            "wordValueMultiThree", "tileValueMulti", "wordValueMultiThree", "tileValueMulti", "wordValueMultiThree",
        };
        

        for (int i = 0; i < positions.length; i++) {
            int[] position = positions[i];
            int row = position[0];
            int col = position[1];
            tileBonus[row][col] = colors[i];

        }

    }

    public static Board getBoard() {
        if (singleBoard == null) { 
            singleBoard = new Board();
        }

        return singleBoard;

    }

    public Tile[][] getTiles() {
    // עובר על המערך ויוצר העתק של כל תא
        Tile[][] copyTiles = new Tile[SIZEBOARD][SIZEBOARD];
        for (int i = 0; i < SIZEBOARD; i++) {
            for (int j = 0; j < SIZEBOARD; j++) {
                copyTiles[i][j] = gameBoard[i][j];
            }
        }

        return copyTiles;

    }

    public boolean boardLegal(Word word) {

        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical(); 
        boolean isValidPlace = isValidPlace(word);
        int row1 = row;
        int col1 = col;
    
        for (Tile tile : tiles) {
            //האם האריח נמצא בגבולות הלוח
            if (!isInBounds(row1, col1)) {
                return false;
            }
            //אם יש אריח קיים האריח החדש שווה לו - לא דרשה החלפה של אריחים קיימים
            if (gameBoard[row1][col1] != null && (tile != null) && !tile.equals(gameBoard[row1][col1]) ) { 
                    return false; 
            }

            if (vertical) {
                row1++;
            } else {
                col1++; 
            }
        }

        //  אם הלוח ריק מוודא שאחד האריחים במילה נמצא על משבצת הכוכב
        if (isBoardEmpty()) {
            for (int i = 0; i < tiles.length; i++) {

                if (row == ((SIZEBOARD-1) / 2)  && col == ((SIZEBOARD-1) / 2)) 
                {
                    return true;
                }

                if (vertical) {
                    row++;
                } else {
                    col++; 
                }    
            }

            return false;
        }
        
        // מחזיר אמת אם אחד האריחים במילה שכן או חופף לאריח קיים
        return isValidPlace;

    }
    

    private boolean isInBounds(int row, int col) {

        return row >= 0 && row < SIZEBOARD && col >= 0 && col < SIZEBOARD;
    }


    private boolean isBoardEmpty() {
        for (int i = 0; i < SIZEBOARD; i++) {
            for (int j = 0; j < SIZEBOARD; j++) {
                if (gameBoard[i][j] != null) {
                    return false; 
                }
            }
        }
        return true; 
    }


    private boolean isValidPlace(Word word) {
        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical(); 
    
        for (Tile tile : tiles) {
            //בדיקה האם האריח נמצא בגבולות הלוח
            if (!isInBounds(row, col)) {
                return false;
            }

            if ( (isOverlappingTile (row, col) && ( tile != null && tile.letter != '_')) || (col > 0 && isOverlappingTile (row, col-1)) ||
                (col < SIZEBOARD - 1 && isOverlappingTile (row, col+1)) ||
                (row < SIZEBOARD - 1 && isOverlappingTile (row+1, col)) ||
                (row > 0 && isOverlappingTile (row-1, col)) ) {
                return true;
            }
    
            if (vertical) {
                row++;
            } else {
                col++; 
            }    
        }
    
        return false;
    }
    
    
    //בדיקה האם קיים כבר אריח במיקום הזה
    private boolean isOverlappingTile (int row, int col) { 
        return gameBoard[row][col] != null; 
    }

    //  בדיקה האם המילה חוקית במילון הספרים 
    public boolean dictionaryLegal(Word word) {
        return true;
    }
  

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> createdWords = new ArrayList<>();
        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical(); 
        // int rownull = row, colnull = col, completion = 0;

            //אם הלוח ריק מחזיר את המילה כמו שהיא
            if (isBoardEmpty()) {
                wordsExist.add(word);
                createdWords.add(word);
                return createdWords;    
            }

            for (int i = 0; i < tiles.length; i++){  

                if (isInBounds(row + 1, col) && isOverlappingTile (row + 1, col) ||
                    isInBounds(row - 1, col) && isOverlappingTile (row - 1, col)) {
                    wordUpDown(word , row, col, createdWords);
                }

                if (isInBounds(row, col + 1) && isOverlappingTile (row, col + 1) || 
                    isInBounds(row, col - 1) && isOverlappingTile (row, col - 1)) {
                    wordLeftRight(word , row, col, createdWords);
                }

                if (vertical) {
                    row++;
                } else {
                    col++;
                }         
            }


        return createdWords;

    }


    //מתודת עזר למתודה GetWord
    private void wordUpDown(Word word, int row, int col, ArrayList<Word> createdWords) {
        ArrayList<Tile> tileWords = new ArrayList<>();
        int rowStart = row;
        int rowStop = row;

        tileWords.add(gameBoard[row][col]);
        
        while (isInBounds(rowStart-1, col) && gameBoard[rowStart - 1][col] != null) {
            rowStart--;
            tileWords.add(0,gameBoard[rowStart][col]);
        };

        while (isInBounds(rowStop + 1, col) && gameBoard[rowStop + 1][col] != null) {
            rowStop++;
            tileWords.add(gameBoard[rowStop][col]);
        };

        Tile[] newTiles = tileWords.toArray(new Tile[0]);
        Word newWord = new Word(newTiles, rowStart, col, true);

        if (word.isVertical() && word.getTiles().length >= newTiles.length && !wordsExist.contains(word)) {
            wordsExist.add(word);
            createdWords.add(word);
        }
        
        if (word.isVertical() && word.getTiles().length < newTiles.length) {
            if (boardLegal(newWord) && dictionaryLegal(newWord) && !wordsExist.contains(newWord)) {
                wordsExist.add(newWord);
                createdWords.add(newWord);
            }
        } 
        if (!word.isVertical()) {
            if (!wordsExist.contains(word)) {
                wordsExist.add(word);
                createdWords.add(word);
            }
            if (boardLegal(newWord) && dictionaryLegal(newWord) && !wordsExist.contains(newWord)) {
                wordsExist.add(newWord);
                createdWords.add(newWord);
            }
        }

    }


    private void wordLeftRight(Word word, int row, int col, ArrayList<Word> createdWords) {
        ArrayList<Tile> tileWords = new ArrayList<>();
        int colStart = col;
        int colStop = col;

        tileWords.add(gameBoard[row][col]);

        while (isInBounds(row, colStart-1) && gameBoard[row][colStart-1] != null) {
            colStart--;
            tileWords.add(0,gameBoard[row][colStart]);
        };

        while (isInBounds(row, colStop+1) && gameBoard[row][colStop+1] != null) {
            colStop++;
            tileWords.add(gameBoard[row][colStop]);
        };


        Tile[] newTiles = tileWords.toArray(new Tile[0]);
        Word newWord = new Word(newTiles, row, colStart, false);

        if (!word.isVertical() && word.getTiles().length >= newTiles.length && !wordsExist.contains(word)) {
            wordsExist.add(word);
            createdWords.add(word);
        }
        
        if (!word.isVertical() && word.getTiles().length < newTiles.length) {
            if (boardLegal(newWord) && dictionaryLegal(newWord) && !wordsExist.contains(newWord)) {
                wordsExist.add(newWord);
                createdWords.add(newWord);
            }
        } 
        if (word.isVertical()) {
            if (!wordsExist.contains(word)) {
                wordsExist.add(word);
                createdWords.add(word);
            }
            if (boardLegal(newWord) && dictionaryLegal(newWord) && !wordsExist.contains(newWord)) {
                wordsExist.add(newWord);
                createdWords.add(newWord);
            }
        }
        

    }  


    public int getScore(Word word) {
        Tile[] tiles = word.getTiles();
        int rowWordScore = word.getRow();
        int colWordScore = word.getCol();
        int rowBonusScore = word.getRow();
        int colBonusScore = word.getCol();
        boolean vertical = word.isVertical(); 
        int wordScore = 0;
        int bonusScore = 0;
        int totleScore = 0;


            for (Tile tile : tiles) {
                if (vertical) {
                    wordScore += BonusSlotTile(rowWordScore, colWordScore, tile);
                    rowWordScore++;
                } else {
                    wordScore += BonusSlotTile(rowWordScore, colWordScore, tile);
                    colWordScore++;
                }

            }
        
            for (int i = 0; i < tiles.length; i++) {
                if (vertical) {
                    bonusScore = BonusSlotWord(rowBonusScore, colBonusScore, wordScore);
                    totleScore += bonusScore;
                    bonusScore = 0;
                    rowBonusScore++;
                } else {
                    bonusScore = BonusSlotWord(rowBonusScore, colBonusScore, wordScore);
                    totleScore += bonusScore;
                    bonusScore = 0;
                    colBonusScore++;
                } 
            }

        if (totleScore == 0) {
            return wordScore;
        } else {
            return totleScore;
        }
    }

    // מתודת עזר ל get score מחזירה בונוסים של האותיות
    private int BonusSlotTile(int row, int col, Tile tile) {

        //אם האריח הוא _ הוא יקבל את האריח שנמצא במיקום שהוא אמור להיות בלוח
        if (tile == null && gameBoard[row][col] != null) {
            tile = gameBoard[row][col];
        }
        
        

        if (tile != null && tileBonus[row][col] != null) {

            switch (tileBonus[row][col]) {

                case "tileValueMulti":
                    return tile.score*2;

                case "tileValueMultiThree":
                    return tile.score*3;

                default:
                    return tile.score;
            }
        }

        if (tile != null) {
        return tile.score;
        }
        
        return 0;

    }

    private int BonusSlotWord(int row, int col, int wordScore) {

        if (row == 7 && col == 7 && wordsExist.size() > 1) {
            return 0;
        }

        if (tileBonus[row][col] != null) {

            switch (tileBonus[row][col]) {

                case "wordValueMultiThree":
                    return wordScore *= 3;
    
                case "wordValueMulti":
                    return wordScore*= 2;
    
                case "starWordValueMulti":
                    return wordScore*= 2;
    
                default:
                    return 0;

            }
        }

        return 0;
         
    }


    private void PlaceOnBoard (Word word) {
        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
       
        for (Tile tile : tiles) {

            if (tile != null && gameBoard[row][col] == null) {
                gameBoard[row][col] = tile;
            }

            if (word.isVertical()) {
                row++;
            } else {
                col++;
            }     
        }
    }

    private void fixNullTiles(Word word) {
        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
    
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == null) {
                if (vertical) {
                    tiles[i] = gameBoard[row + i][col];
                } else {
                    tiles[i] = gameBoard[row][col + i];
                }
            }
        }
    }
    

    public int tryPlaceWord(Word word) {

        if (!boardLegal(word) || !dictionaryLegal(word)) {
            return 0; 
        }

        ArrayList<Word> newWords = getWords(word);
        int score = 0;

        for (Word wordInList : newWords) {
            PlaceOnBoard(wordInList);
            score += getScore(wordInList);
            fixNullTiles(wordInList);
        }

        return score;
    }
    
}


package test;
import java.util.Arrays;
import java.util.Random;


public class Tile {
    public final int score;
    public final char letter;

    private Tile (int score, char letter) {
        this.score = score;
        this.letter = letter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + score;
        result = prime * result + letter;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (score != other.score)
            return false;
        if (letter != other.letter)
            return false;
        return true;
    }

    public static class Bag {

        private static Bag singleBag = null;
        private int sizeBag = 98;
        private final int[] arrStart;
        private final int[] arrAmount;
        private final int[] arrScore;
        private final Tile[] arrTile;
        
    private Bag(){

        arrStart = new int[] {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        arrAmount = arrStart.clone();
        arrScore = new int[] {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        arrTile = new Tile[26];

        for (int i=0; i < arrScore.length; i++ ){
            char let = (char) ('A' + i);
            arrTile[i] = new Tile(arrScore[i], let);
        }

        }

    public static Bag getBag() {

        if (singleBag == null) { 
                singleBag = new Bag();
            }

        return singleBag;

        }

    public Tile getRand() {
        
        Random rand = new Random();
        int randIndex = rand.nextInt(arrTile.length - 1);

        if (sizeBag == 0)
            return null;

        while (arrAmount[randIndex] == 0) {
            randIndex = rand.nextInt(arrTile.length - 1); 
        }

        arrAmount[randIndex]--;
        sizeBag--;

        return arrTile[randIndex];  

        }

        
    public Tile getTile(char letter) {
        
        if (letter >= 'A' && letter <= 'Z') {
            int tileIndex = letter - 'A';
            if (arrAmount[tileIndex] > 0) {
                arrAmount[tileIndex]--;
                sizeBag--;
                return arrTile[tileIndex];
            }
        }
        return null;
    }
        

        public void put(Tile t) {

            if (t.letter >= 'A' && t.letter <= 'Z') {
                int index = t.letter - 'A';
                if (arrAmount[index] < arrStart[index]) {
                    arrAmount[index]++;
                    sizeBag++;
                }
            } 
        }
        


        public int size() {
            return sizeBag;
        }
        
    

        // מחזיר העתק של המערך הכמויות הקיים
        public int[] getQuantities() {
            return Arrays.copyOf(arrAmount, 26);
        }

    }
}


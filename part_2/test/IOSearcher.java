package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {

    // המתודה תחזיר ערך בוליאני האם המילה קיימת בספרי המשחק
    public static boolean search(String word, String... fileNames) {
        for (String fileName : fileNames) { 
            boolean found = searchInFile(word, fileName); // מחפש בעזרת מתודת עזר האם המילה קיימת בקובץ שמות הספציפי
            if (found) { //המתודת עזרת תחזיר ערך בוליאני שיכנס למשתנה
                return true;
            }
        }
        return false;
    }

    // מתודת עזר שתחזיר ערך בוליאני האם המילה קיימת בספר
    private static boolean searchInFile(String word, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) 
        {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(word)) {
                    return true;
                }
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}


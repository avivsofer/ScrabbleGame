package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {

    PrintWriter outWriter;
    private Scanner inReader;
    private boolean exist;


    @Override
    public void handleClient(InputStream inFromClient,OutputStream outToClient) {
        outWriter = new PrintWriter(outToClient);
        inReader = new Scanner(inFromClient);

        String textIn = inReader.nextLine();
        String[] words = textIn.split(",");
        String startSearch = words[0];

        String[] fileNames = new String[words.length - 1];
        System.arraycopy(words, 1, fileNames, 0, fileNames.length);

        DictionaryManager dictionary = DictionaryManager.get();

        if (startSearch.equals("Q")) 
            exist = dictionary.query(fileNames);
        
        else if(startSearch.equals("C"))
            exist = dictionary.challenge(fileNames);
        
        outWriter.print(exist);
		outWriter.flush();
    }

    @Override
    public void close() {
            inReader.close();
            outWriter.close();
    }
}

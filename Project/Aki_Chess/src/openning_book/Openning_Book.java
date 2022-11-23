/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openning_book;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Goran
 */
public class Openning_Book {

    HashMap<String, String> openning = new HashMap<>(400);

    public Openning_Book() {

        try {
            String filePath = new File("").getAbsolutePath();
            filePath += "\\Openning_book.txt";
            System.out.println(filePath);
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int lastIndex = data.length() - 5;
                if (lastIndex > 0 && !data.contains(":")) {
                    String fen = data.substring(0, lastIndex);
                    String move = data.substring(lastIndex + 1, lastIndex + 5);

                    this.openning.put(fen, move);
                }
            }
            System.out.println("Size HashMap:" + this.openning.size());
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading file.");
            e.printStackTrace();
        }

    }

    public String getMove(String fen) {
        String move = this.openning.get(fen);
        return move;
    }

}

import java.io.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }


    public void generate() {
        findPossibilities("", letters);
    }
    public void findPossibilities(String word, String lettersLeft) {
        if(lettersLeft.equals("")){
            words.add(word);
            return;
        }
        for(int i = 0; i < lettersLeft.length(); i++){
            findPossibilities(word+lettersLeft.charAt(i), lettersLeft.substring(0, i)
                    + lettersLeft.substring(i + 1));
        }
    }

    public void sort(){
        words = mergeSort(words, words.size(), 0);
    }
    public ArrayList merge(ArrayList<String> firstHalf, ArrayList<String> secondHalf){
        int x = 0;
        int y = 0;
        ArrayList<String> merged = new ArrayList<>();
        while(firstHalf.size() > x && secondHalf.size() > y){
            if(firstHalf.get(x).compareTo(secondHalf.get(y)) < 0){
                merged.add(firstHalf.get(x));
                x++;
            }
            else{
                merged.add(secondHalf.get(y));
                y++;
            }
        }
        while(x < firstHalf.size()){
            merged.add(firstHalf.get(x));
            x++;
        }
        while(y < secondHalf.size()){
            merged.add(firstHalf.get(y));
            y++;
        }
        return merged;
    }
    public ArrayList mergeSort(ArrayList<String> arr, int high, int low){
        if(high - low == 0){
            ArrayList<String> newArr = new ArrayList<>();
            return newArr;
        }
        int med = (high + low) / 2;
        ArrayList<String> firstHalf = mergeSort(arr, low, med);
        ArrayList<String> secondHalf = mergeSort(arr, med + 1, high);
        return merge(firstHalf, secondHalf);
    }
    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    public void checkWords() {
        ArrayList<String> realWords = new ArrayList<>();
        int temp = words.size();
        for(int i = 0; i < temp; i++) {
            if(!binarySearch(words.get(i))){
                words.remove(i);
            }
        }
    }
    public boolean binarySearch(String s){
        int right = DICTIONARY_SIZE;
        int left = 0;
        while (right >= left) {
            int mid = (right + left) / 2;
            if(DICTIONARY[mid].equals(s)){
                return true;
            }
            else if(DICTIONARY[mid].compareTo(s) > 0){
                right = mid - 1;
            }
            else if(DICTIONARY[mid].compareTo(s) < 0){
                left = mid + 1;
            }
        }
        return false;
    }



    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}

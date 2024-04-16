package frontend.src;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {
    static private ArrayList<DictionaryEntry> dictionary = new ArrayList<>();

    public Dictionary() {
        //dictionary = new ArrayList<>();
        //this.readDictionary();
    }

    public void readDictionary() {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<DictionaryEntry>>() {
        }.getType();

        String dictionaryFile = "dictionary.json";

        try {
            dictionary = gson.fromJson(new FileReader(dictionaryFile), listType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // TODO: Use errorHandler
        }
    }

    static public String findToken(String input) {
        Pattern pattern;
        Matcher matcher;
        for (DictionaryEntry entry : dictionary) {
            pattern = Pattern.compile(entry.getRegex(), Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(input);
            if (matcher.find()) {
                return entry.getToken();
            }
        }
        System.out.println("Not found => |" + input + "|"); // TODO: Remove this is only dev
        return null;
    }
}

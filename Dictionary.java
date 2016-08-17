package shitbot;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {

	public static String getNameOfDictionary(int x) {
		return dictionaryNames.get(x);
	}

	public static String pickFrom(int x) {
		Dictionary subject = dictionaries.get(x);
		int limit = subject.length();
		return subject.get((int)Math.floor(Math.random()*limit));

	}

	public static String pickFrom(String in) {
		return pickFrom(dictionaryNames.indexOf(in));
	}

	public static int size() {
		return dictionaryNames.size();
	}

	private static List<Dictionary> dictionaries = new ArrayList<Dictionary>();

	private static List<String> dictionaryNames = new ArrayList<String>();

	private List<String> words;

	public Dictionary(String[] strings, String name) {
		words = new ArrayList<String>();
		addAll(strings);
		dictionaries.add(this);
		dictionaryNames.add(name);
	}

	private void addAll(String[] strings) {
		for (int x = 0; x < strings.length; x++) {
			words.add(strings[x]);
		}
	}

	public String get(int index) {
		return words.get(index);
	}
	
	public int length()
	{
		return words.size();
	}

	public static void clear() {
		dictionaryNames.clear();
		dictionaries.clear();
		
	}

}

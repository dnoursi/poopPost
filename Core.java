package shitbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.tumblr.jumblr.exceptions.JumblrException;

public class Core {

	public static final boolean DO_NOT_PUBLISH = false;

	public static void main(String[] args) {
		Core shitbot = null;
		try {
			shitbot = new Core();
			System.out.println("Entering Loop");
			shitbot.loop();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		// shitbot.test();
	}

	public void test() {
		try {
			createDirectories();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(generate());

	}

	private final String PROPERTIES_FILE_NAME = "properties.txt";

	private final long SHITPOST_WAIT_TIME;

	private TumblrConnector tumblr;
	private File[] keys;
	private int keyIndex = 0;
	final String KEY_FOLDER_NAME = "Keys";
	final String DIRECTOR_FOLDER_NAME = "DisgustingWords";
	final long THREAD_WAIT_TIME = 30000;
	private Properties properties;

	public Core() throws IOException {
		System.out.println("Getting Keys");
		keys = getFiles("Keys");
		System.out.println("Connecting to Tumblr");
		tumblr = new TumblrConnector(getNewKey());
		tumblr.connect();
		tumblr.setBlog("shitpostgenerator");
		System.out.println("Loading Properties");
		properties = new Properties();
		properties.load(new FileInputStream(PROPERTIES_FILE_NAME));
		SHITPOST_WAIT_TIME = Long.parseLong(properties
				.getProperty("SHITPOST_WAIT_TIME"));

	}

	private void createDirectories() throws IOException {
		Dictionary.clear();
		File[] files = getFiles(DIRECTOR_FOLDER_NAME);
		for (int x = 0; x < files.length; x++) {
			new Dictionary(scanFile(files[x]), files[x].getName().replace(
					".txt", ""));
		}
	}

	public String generate() {
		String subject = null;
		int limit = Dictionary.size();
		String shitpost = Dictionary.pickFrom("base");
		boolean loop = true;
		while (loop) {
			loop = false;
			for (int x = 0; x < limit; x++) {
				subject = Dictionary.getNameOfDictionary(x);
				while (shitpost.contains("%" + subject + "%")) {
					loop = true;
					shitpost = shitpost.replaceFirst("%" + subject + "%",
							Dictionary.pickFrom(x));
				}
			}
		}

		return shitpost;
	}

	// TODO Actually set up an account-switching method so there's a point to
	// having multiple keys jackass
	public File[] getFiles(String folder) throws IOException {
		File directory = new File(folder);
		if (!directory.isDirectory())
			throw new IOException();
		return directory.listFiles();
	}

	public TumblrKey getNewKey() throws IOException {
		TumblrKey key = new TumblrKey(scanFile(keys[keyIndex]));
		return key;
	}

	public long getTimeSinceLastPost() {
		long now = new Date().getTime() / 1000;
		long then = tumblr.getLastPostDate();
		return ((now - then) / 60);
	}

	public void sleep(long mili) {
		try {
			Thread.sleep(mili);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loop() throws IOException {
		String title = null;
		while (true) {
			while (getTimeSinceLastPost() < SHITPOST_WAIT_TIME) {
				sleep(THREAD_WAIT_TIME);
			}
			try {
				createDirectories();
				tumblr.Post(tumblr.makeTextPost(title, generate(), title));
			} catch (JumblrException | IllegalAccessException
					| InstantiationException e) {
				if (e.getMessage().equals("Bad Request")) {
					System.err.println("Post Limit Reached");
				} else {
					e.printStackTrace();
				}
				System.exit(0);
			}
			tumblr = new TumblrConnector(new TumblrKey(scanFile(keys[keyIndex])));
			tumblr.connect();
			tumblr.setBlog("shitpostgenerator");
			sleep(5000);
		}
	}

	public String[] scanFile(File file) throws IOException {
		List<String> words = new ArrayList<String>();
		Scanner in = new Scanner(file);
		String line;
		while (in.hasNextLine()) {
			line = in.nextLine();
			words.add(line);
		}
		in.close();
		return words.toArray(new String[words.size()]);
	}

}

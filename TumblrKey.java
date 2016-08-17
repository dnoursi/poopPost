package shitbot;

public class TumblrKey {

	private final String CONSUMER_KEY;
	private final String CONSUMER_SECRET;
	private final String OAUTH_TOKEN;
	private final String OAUTH_SECRET;
	
	public TumblrKey(String[] keys)
	{
		CONSUMER_KEY = keys[0];
		CONSUMER_SECRET = keys[1];
		OAUTH_TOKEN = keys[2];
		OAUTH_SECRET = keys[3];
	}
	
	public String getCONSUMER_KEY() {
		return CONSUMER_KEY;
	}

	public String getOAUTH_TOKEN() {
		return OAUTH_TOKEN;
	}

	public String getCONSUMER_SECRET() {
		return CONSUMER_SECRET;
	}

	public String getOAUTH_SECRET() {
		return OAUTH_SECRET;
	}
	
	
	
}

package shitbot;

import java.util.ArrayList;
import java.util.List;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.TextPost;
import com.tumblr.jumblr.types.User;

public class TumblrConnector {

	private TumblrKey key;
	private JumblrClient client;
	private User user;
	private Blog blog;

	public TumblrConnector(TumblrKey key) {
		this.key = key;
		client = new JumblrClient(key.getCONSUMER_KEY(),
				key.getCONSUMER_SECRET());
	}

	public void connect() {
		client.setToken(key.getOAUTH_TOKEN(), key.getOAUTH_SECRET());
		user = client.user();
	}

	public TextPost makeTextPost(String title, String body, List<String> tags)
			throws IllegalAccessException, InstantiationException {
		TextPost post = null;
		post = client.newPost(blog.getName(), TextPost.class);
		post.setTitle(title);
		post.setBody(body);
		post.setTags(tags);
		return post;
	}
	
	public long getLastPostDate()
	{
		return blog.getUpdated();
	}

	public TextPost makeTextPost(String title, String body, String tag)
			throws IllegalAccessException, InstantiationException {
		List<String> tags = new ArrayList<String>();
		tags.add(tag);
		return makeTextPost(title, body, tags);
	}

	public boolean Post(Post post) {
		if (Core.DO_NOT_PUBLISH) return false;
		try {
			post.save();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean queue(Post post) {
		try {
			post.setState("Queue");
			post.save();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setBlog(String in) {
		List<Blog> blogList = user.getBlogs();
		Blog outBlog;
		for (int x = 0; x < blogList.size(); x++) {
			outBlog = blogList.get(x);
			if (outBlog.getName().equals(in)) {
				blog = outBlog;
				return true;
			}
		}
		return false;
	}

}

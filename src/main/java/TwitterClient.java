import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterClient {
    public static List<String> search(String search) throws Exception {

        Twitter twitter = new TwitterInitializer().initializeTwitter("twitter4j.properties");

        Query query = new Query(search);
        query.setResultType(Query.RECENT);
        query.setLang("en");
        query.setCount(100);

        try {
            List<Status> tweets = twitter.search(query).getTweets();
            List<String> texts = new ArrayList<String>(tweets.size());

            for (Status tweet : tweets) {
                texts.add(tweet.getText());
            }

            return texts;
        } catch (TwitterException e) {
            if (e.getMessage().toLowerCase().contains("authentication")) {
                String msg = "Twitter Error: Problem authenticating with Twitter. "
                        + "Make sure you have included correct keys and tokens.";
                throw new Exception(msg, e);
            }

            throw e;
        }
    }

    private static void init() {

    }
}
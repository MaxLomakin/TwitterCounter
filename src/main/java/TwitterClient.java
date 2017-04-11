import java.util.ArrayList;
import java.util.List;

import twitter4j.*;

public class TwitterClient {

    private static int pagesToLoad = 1;

    public static List<String> search(String search) throws Exception {

        Twitter twitter = new TwitterInitializer().initializeTwitter("twitter4j.properties");

        Query query = new Query(search);
        query.setResultType(Query.ResultType.mixed);
        query.setLang("en");
        query.setCount(100);

        try {
            QueryResult result = twitter.search(query);
            List<String> texts = new ArrayList<String>();

            for (int i = 0; i < pagesToLoad && result.hasNext(); i++) {
                List<Status> tweets = result.getTweets();

                for (Status tweet : tweets) {
                    texts.add(tweet.getText().replace("\n\n", "\n"));
                }
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
}
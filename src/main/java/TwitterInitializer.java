import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Max on 14.03.2017.
 */
public class TwitterInitializer {

    InputStream inputStream;

    public Twitter initializeTwitter(String propFileName) throws IOException {
        Twitter twitter = TwitterFactory.getSingleton();

        Properties properties = new Properties();
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                properties.load(inputStream);

                twitter.setOAuthConsumer(properties.getProperty("oauth.consumerKey"),
                        properties.getProperty("oauth.consumerSecret"));
                twitter.setOAuthAccessToken(new AccessToken(properties.getProperty("oauth.accessToken"),
                        properties.getProperty("oauth.accessTokenSecret")));
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }

        return twitter;
    }
}

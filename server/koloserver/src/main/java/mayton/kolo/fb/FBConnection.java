package mayton.kolo.fb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * https://www.mkyong.com/maven/how-to-create-a-web-application-project-with-maven/
 *
 * http://kolonet.ua
 *
 *
 */
public class FBConnection {

    public static final String FB_APP_ID     = "241268786384428";
    public static final String FB_APP_SECRET = "y2a73dede3n49uid13502f5ab8cdt390";
    public static final String REDIRECT_URI  = "http://kolonet.ua/Facebook_Login/fbhome";

    // TODO: Research for lifecycle
    private String accessToken = "";

    static Logger logger = LoggerFactory.getLogger(FBConnection.class);

    @Nonnull
    public String getFBAuthUrl() {
        String fbLoginUrl = "";
        try {
            fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
                    + FBConnection.FB_APP_ID + "&redirect_uri="
                    + URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
                    + "&scope=email";
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
        return fbLoginUrl;
    }

    @Nonnull
    public String getFBGraphUrl(String code) {
        String fbGraphUrl = "";
        try {
            fbGraphUrl = "https://graph.facebook.com/oauth/access_token?"
                    + "client_id=" + FBConnection.FB_APP_ID + "&redirect_uri="
                    + URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
                    + "&client_secret=" + FB_APP_SECRET + "&code=" + code;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString());
        }
        return fbGraphUrl;
    }

    public String getAccessToken(String code) {
        if ("".equals(accessToken)) {
            URL fbGraphURL;
            try {
                fbGraphURL = new URL(getFBGraphUrl(code));
            } catch (MalformedURLException e) {
                logger.error(e.toString());
                throw new RuntimeException("Invalid code received " + e);
            }
            URLConnection fbConnection;
            StringBuilder b;
            try {
                fbConnection = fbGraphURL.openConnection();
                BufferedReader in;
                in = new BufferedReader(new InputStreamReader(fbConnection.getInputStream()));
                String inputLine;
                b = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    b.append(inputLine);
                    b.append("\n");
                }
                in.close();
            } catch (IOException e) {
                logger.error(e.toString());
                throw new RuntimeException("Unable to connect with Facebook " + e);
            }

            accessToken = b.toString();
            if (accessToken.startsWith("{")) {
                throw new RuntimeException("ERROR: Access Token Invalid: " + accessToken);
            }
        }
        return accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

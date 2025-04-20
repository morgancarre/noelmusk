package antix.service;

import antix.model.MastodonPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class MastodonService {

    public List<MastodonPost> fetchPostsFromTag(String tag) {
        try {
            var uri = new URIBuilder("https://mastodon.social/api/v1/timelines/tag/" + tag)
                    .addParameter("limit", "40")
                    .build();

            URL url = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return Arrays.stream(mapper.readValue(response.toString(), MastodonPost[].class)).toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

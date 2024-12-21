package antix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private String url;
    private String title;
    private String description;
    private String language;
    private String type;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("author_url")
    private String authorUrl;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("provider_url")
    private String providerUrl;

    @JsonProperty("html")
    private String html;

    private int width;
    private int height;

    @JsonProperty("image")
    private String image;

    @JsonProperty("image_description")
    private String imageDescription;

    @JsonProperty("embed_url")
    private String embedUrl;

    private String blurhash;

    @JsonProperty("published_at")
    private ZonedDateTime publishedAt;
}

package antix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    private String id;
    private String username;
    private String acct;

    @JsonProperty("display_name")
    private String displayName;

    private boolean locked;
    private boolean bot;
    private boolean discoverable;
    private boolean indexable;
    private boolean group;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    private String note;
    private String url;
    private String uri;
    private String avatar;

    @JsonProperty("avatar_static")
    private String avatarStatic;

    private String header;

    @JsonProperty("header_static")
    private String headerStatic;

    @JsonProperty("followers_count")
    private int followersCount;

    @JsonProperty("following_count")
    private int followingCount;

    @JsonProperty("statuses_count")
    private int statusesCount;

    @JsonProperty("last_status_at")
    private String lastStatusAt;
}

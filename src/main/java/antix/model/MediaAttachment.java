package antix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaAttachment {
    private String id;
    private String type;
    private String url;

    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("remote_url")
    private String remoteUrl;

    @JsonProperty("preview_remote_url")
    private String previewRemoteUrl;

    @JsonProperty("text_url")
    private String textUrl;

    private Meta meta;
    private String description;

    @Data
    public static class Meta {
        private Image original;
        private Image small;
        private Object focus;

        @Data
        public static class Image {
            private int width;
            private int height;
            private String size;
            private double aspect;
            @JsonProperty("frame_rate")
            private String frameRate;
            private String duration;
            private String bitrate;
        }
    }
}

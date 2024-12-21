package antix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mention {
    private String id;
    private String username;
    private String url;
    private String acct;
}

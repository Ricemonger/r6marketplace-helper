package github.ricemonger.marketplace;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class UbiServiceConfiguration {

    @Value("${ubi.urls.graphql}")
    private String graphqlUrl;
    @Value("${ubi.urls.authorization}")
    private String authorizationUrl;
    @Value("${ubi.session.contentType}")
    private String contentType;
    @Value("${ubi.session.userAgent}")
    private String userAgent;
    @Value("${ubi.session.appId}")
    private String ubiAppId;
    @Value("${ubi.session.regionId}")
    private String regionId;
    @Value("${ubi.session.localeCode}")
    private String localeCode;
    @Value("${ubi.session.expireTimeout}")
    private Integer expireTimeout;
    @Value("${ubi.session.dateFormat}")
    private String dateFormat;
}

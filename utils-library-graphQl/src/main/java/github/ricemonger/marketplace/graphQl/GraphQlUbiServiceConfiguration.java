package github.ricemonger.marketplace.graphQl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class GraphQlUbiServiceConfiguration {
    @Value("${ubi.urls.graphql}")
    private String graphqlUrl;
    @Value("${ubi.session.contentType}")
    private String contentType;
    @Value("${ubi.session.userAgent}")
    private String userAgent;
    @Value("${ubi.session.baseAppId}")
    private String ubiBaseAppId;
    @Value("${ubi.session.spaceId}")
    private String ubiSpaceId;
    @Value("${ubi.session.regionId}")
    private String ubiRegionId;
    @Value("${ubi.session.localeCode}")
    private String UbiLocaleCode;
    @Value("${ubi.session.dateFormat}")
    private String dateFormat;
    @Value("${ubi.session.itemSaleStatsDateFormat}")
    private String itemSaleStatsDateFormat;
    @Value("${ubi.session.uncommonTagValue}")
    private String uncommonTagValue;
    @Value("${ubi.session.rareTagValue}")
    private String rareTagValue;
    @Value("${ubi.session.epicTagValue}")
    private String epicTagValue;
    @Value("${ubi.session.legendaryTagValue}")
    private String legendaryTagValue;
}

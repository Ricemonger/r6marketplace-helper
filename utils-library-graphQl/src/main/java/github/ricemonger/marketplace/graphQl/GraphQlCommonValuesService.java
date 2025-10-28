package github.ricemonger.marketplace.graphQl;

import github.ricemonger.utils.DTOs.common.ConfigTrades;
import github.ricemonger.utils.abstract_services.CommonValuesDatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GraphQlCommonValuesService {

    private final CommonValuesDatabaseService commonValuesDatabaseService;

    private final GraphQlUbiServiceConfiguration graphQlUbiServiceConfiguration;

    public ConfigTrades getConfigTrades() {
        return commonValuesDatabaseService.getConfigTrades();
    }

    public String getPaymentItemId() {
        return commonValuesDatabaseService.getPaymentItemId();
    }

    public String getMainUserAuthorizationToken() {
        return commonValuesDatabaseService.getMainUserAuthorizationToken();
    }

    public String getMainUserProfileId() {
        return commonValuesDatabaseService.getMainUserProfileId();
    }

    public String getMainUserSessionId() {
        return commonValuesDatabaseService.getMainUserSessionId();
    }

    public String getGraphqlUrl() {
        return graphQlUbiServiceConfiguration.getGraphqlUrl();
    }

    public String getContentType() {
        return graphQlUbiServiceConfiguration.getContentType();
    }

    public String getUserAgent() {
        return graphQlUbiServiceConfiguration.getUserAgent();
    }

    public String getUbiBaseAppId() {
        return graphQlUbiServiceConfiguration.getUbiBaseAppId();
    }

    public String getUbiGameSpaceId() {
        return graphQlUbiServiceConfiguration.getUbiSpaceId();
    }

    public String getUbiRegionId() {
        return graphQlUbiServiceConfiguration.getUbiRegionId();
    }

    public String getUbiLocaleCode() {
        return graphQlUbiServiceConfiguration.getUbiLocaleCode();
    }

    public String getDateFormat() {
        return graphQlUbiServiceConfiguration.getDateFormat();
    }

    public String getItemSaleStatsDateFormat() {
        return graphQlUbiServiceConfiguration.getItemSaleStatsDateFormat();
    }

    public String getUncommonTagValue() {
        return graphQlUbiServiceConfiguration.getUncommonTagValue();
    }

    public String getRareTagValue() {
        return graphQlUbiServiceConfiguration.getRareTagValue();
    }

    public String getEpicTagValue() {
        return graphQlUbiServiceConfiguration.getEpicTagValue();
    }

    public String getLegendaryTagValue() {
        return graphQlUbiServiceConfiguration.getLegendaryTagValue();
    }
}

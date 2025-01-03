package github.ricemonger.marketplace.services;

import github.ricemonger.marketplace.services.configurations.UbiServiceConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UbiServiceConfigurationTest {

    @Autowired
    private UbiServiceConfiguration ubiServiceConfiguration;

    @Test
    public void ubiServiceConfigurationPropertiesShouldBeAutowired() {
        assertNotNull(ubiServiceConfiguration.getGraphqlUrl());
        assertNotNull(ubiServiceConfiguration.getAuthorizationUrl());
        assertNotNull(ubiServiceConfiguration.getContentType());
        assertNotNull(ubiServiceConfiguration.getUserAgent());
        assertNotNull(ubiServiceConfiguration.getUbiRegionId());
        assertNotNull(ubiServiceConfiguration.getUbiLocaleCode());
        assertNotNull(ubiServiceConfiguration.getUbiBaseAppId());
        assertNotNull(ubiServiceConfiguration.getExpireTimeout());
        assertNotNull(ubiServiceConfiguration.getDateFormat());
        assertNotNull(ubiServiceConfiguration.getMinUncommonPrice());
        assertNotNull(ubiServiceConfiguration.getMaxUncommonPrice());
        assertNotNull(ubiServiceConfiguration.getMinRarePrice());
        assertNotNull(ubiServiceConfiguration.getMaxRarePrice());
        assertNotNull(ubiServiceConfiguration.getMinEpicPrice());
        assertNotNull(ubiServiceConfiguration.getMaxEpicPrice());
        assertNotNull(ubiServiceConfiguration.getMinLegendaryPrice());
        assertNotNull(ubiServiceConfiguration.getMaxLegendaryPrice());
    }
}

package github.ricemonger.marketplace;

import github.ricemonger.marketplace.services.UbiServiceConfiguration;
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
        assertNotNull(ubiServiceConfiguration.getUbiAppId());
        assertNotNull(ubiServiceConfiguration.getExpireTimeout());
        assertNotNull(ubiServiceConfiguration.getDateFormat());
    }
}

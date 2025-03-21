package github.ricemonger.marketplace.graphQl.mappers;

import github.ricemonger.marketplace.graphQl.personal_query_credits_amount.DTO.Meta;
import github.ricemonger.marketplace.graphQl.personal_query_credits_amount.PersonalQueryCreditAmountMapper;
import github.ricemonger.utils.exceptions.server.GraphQlPersonalCreditAmountMappingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PersonalQueryCreditAmountMapperTest {

    private final PersonalQueryCreditAmountMapper personalQueryCreditAmountMapper = new PersonalQueryCreditAmountMapper();

    @Test
    public void mapCreditAmount_should_map_if_valid_fields() {
        Meta meta = new Meta(50);

        assertEquals(50, personalQueryCreditAmountMapper.mapCreditAmount(meta));
    }

    @Test
    public void mapCreditAmount_should_throw_exception_if_meta_is_null() {
        Meta meta = null;

        assertThrows(GraphQlPersonalCreditAmountMappingException.class, () -> personalQueryCreditAmountMapper.mapCreditAmount(meta));
    }

    @Test
    public void mapCreditAmount_should_throw_exception_if_quantity_is_null() {
        Meta meta = new Meta(null);

        assertThrows(GraphQlPersonalCreditAmountMappingException.class, () -> personalQueryCreditAmountMapper.mapCreditAmount(meta));
    }
}
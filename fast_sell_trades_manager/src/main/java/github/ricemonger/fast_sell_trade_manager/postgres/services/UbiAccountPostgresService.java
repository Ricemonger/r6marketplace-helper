package github.ricemonger.fast_sell_trade_manager.postgres.services;

import github.ricemonger.fast_sell_trade_manager.postgres.dto_projections.FastSellManagedUserProjection;
import github.ricemonger.fast_sell_trade_manager.postgres.repositories.CustomUbiAccountEntryPostgresRepository;
import github.ricemonger.fast_sell_trade_manager.postgres.repositories.CustomUbiAccountStatsPostgresRepository;
import github.ricemonger.fast_sell_trade_manager.postgres.repositories.FetchingUbiAccountAuthorizationEntryPostgresRepository;
import github.ricemonger.fast_sell_trade_manager.postgres.services.entity_mappers.fetch.FetchingUbiAccountAuthorizationEntryEntityMapper;
import github.ricemonger.fast_sell_trade_manager.postgres.services.entity_mappers.item.ItemEntityMapper;
import github.ricemonger.fast_sell_trade_manager.postgres.services.entity_mappers.user.UbiAccountStatsEntityMapper;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ManagedUser;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.abstractions.UbiAccountEntryDatabaseService;
import github.ricemonger.utils.DTOs.personal.auth.AuthorizationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UbiAccountPostgresService implements UbiAccountEntryDatabaseService {

    private final CustomUbiAccountEntryPostgresRepository customUbiAccountEntryPostgresRepository;

    private final CustomUbiAccountStatsPostgresRepository customUbiAccountStatsPostgresRepository;

    private final FetchingUbiAccountAuthorizationEntryPostgresRepository fetchingUbiAccountAuthorizationEntryPostgresRepository;

    private final UbiAccountStatsEntityMapper ubiAccountStatsEntityMapper;

    private final ItemEntityMapper itemEntityMapper;

    private final FetchingUbiAccountAuthorizationEntryEntityMapper fetchingUbiAccountAuthorizationEntryEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public ManagedUser getFastSellManagedUserById(Long fastSellManagedUserId, String email) {
        FastSellManagedUserProjection projection = customUbiAccountEntryPostgresRepository.findFastSellManagedUserById(fastSellManagedUserId, email).get();
        return ubiAccountStatsEntityMapper.createFastSellManagedUser(projection, customUbiAccountStatsPostgresRepository.findAllUserResaleLocksItemIds(projection.getUbiProfileId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemMedianPriceAndRarity> getOwnedItemsMedianPriceAndRarity(String ubiProfileId) {
        return customUbiAccountStatsPostgresRepository.findOwnedItemsMedianPriceAndRarity(ubiProfileId).stream()
                .map(itemEntityMapper::createItemMedianPriceAndRarity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationDTO> getAllFetchAccountsAuthorizationDTOs() {
        return fetchingUbiAccountAuthorizationEntryPostgresRepository.findAll().stream()
                .map(fetchingUbiAccountAuthorizationEntryEntityMapper::createAuthorizationDTO)
                .toList();
    }
}

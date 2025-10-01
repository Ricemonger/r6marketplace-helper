package github.ricemonger.trades_manager.postgres.services.entity_mappers.user;

import github.ricemonger.trades_manager.postgres.custom_entities.manageable_users.CustomItemResaleLockEntity;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import org.springframework.stereotype.Service;

@Service
public class ItemResaleLockEntityMapper {

    public ItemResaleLock createDTO(CustomItemResaleLockEntity entity) {
        return new ItemResaleLock(entity.getItemId_(), entity.getExpiresAt());
    }
}

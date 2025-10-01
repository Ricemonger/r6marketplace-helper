package github.ricemonger.trades_manager.postgres.repositories;


import github.ricemonger.trades_manager.postgres.custom_entities.items.CustomItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomItemPostgresRepository extends JpaRepository<CustomItemEntity, String> {

}

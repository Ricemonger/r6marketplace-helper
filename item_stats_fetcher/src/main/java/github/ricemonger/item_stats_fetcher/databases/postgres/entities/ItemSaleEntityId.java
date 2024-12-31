package github.ricemonger.item_stats_fetcher.databases.postgres.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemSaleEntityId implements Serializable {
    private ItemEntity item;
    private LocalDateTime soldAt;
}
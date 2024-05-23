package github.ricemonger.marketplace.databases.neo4j.entities;

import github.ricemonger.marketplace.databases.neo4j.enums.ItemType;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Date;
import java.util.List;

@Node("Item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemNode {

    @Id
    private String itemFullId;

    private String assetUrl;

    private String name;

    private List<String> tags;

    private ItemType type;

    private int maxBuyPrice;

    private int buyOrders;

    private int minSellPrice;

    private int sellOrders;

    private int expectedProfit;

    private int expectedProfitPercentage;

    private Date lastSoldAt;

    private int lastSoldPrice;
}
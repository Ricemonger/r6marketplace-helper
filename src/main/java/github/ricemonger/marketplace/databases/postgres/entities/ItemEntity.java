package github.ricemonger.marketplace.databases.postgres.entities;

import github.ricemonger.marketplace.databases.postgres.enums.ItemType;
import github.ricemonger.marketplace.databases.postgres.services.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Entity(name = "item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity implements Item {

    @Id
    @Column(name = "item_full_id")
    private String itemFullId;

    private String assetUrl;

    private String name;

    private ItemType type;

    private int maxBuyPrice;

    private int buyOrdersCount;

    private int minSellPrice;

    private int sellOrdersCount;

    private int expectedProfit;

    private int expectedProfitPercentage;

    private Date lastSoldAt;

    private int lastSoldPrice;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name="items_tags",
            joinColumns=  @JoinColumn(name="item_id", referencedColumnName="item_full_id"),
            inverseJoinColumns= @JoinColumn(name="tag", referencedColumnName="tag"))
    private List<TagEntity> tags = new ArrayList<>();

    @Override
    public List<String> getTagsList() {
        return tags.stream().map(TagEntity::getTag).toList();
    }
}

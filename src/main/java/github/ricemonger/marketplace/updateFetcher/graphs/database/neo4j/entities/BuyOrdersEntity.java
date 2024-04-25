package github.ricemonger.marketplace.updateFetcher.graphs.database.neo4j.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;


import java.util.List;

@Node("BuyOrders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyOrdersEntity {

    @Id
    private String id;

    @Relationship(value = "OWNER", direction = Relationship.Direction.INCOMING)
    private UserEntity user;

    @Relationship(value = "TRANSACTIONS", direction = Relationship.Direction.OUTGOING)
    private List<TransactionEntity> transaction;

    private int currentAmount;

    private int createdToday;
}

package github.ricemonger.trades_manager.postgres.custom_entities.items;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Table(name = "tag")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomTagValueEntity {
    @Id
    @Column(name = "tag_value") // "value" column name conflicts with H2
    private String value;

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomTagValueEntity tagEntity)) return false;
        return Objects.equals(value, tagEntity.value);
    }

    public boolean isFullyEqual(Object o) {
        return equals(o);
    }
}

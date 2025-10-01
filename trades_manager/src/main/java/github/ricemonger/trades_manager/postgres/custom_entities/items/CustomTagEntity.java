package github.ricemonger.trades_manager.postgres.custom_entities.items;

import github.ricemonger.utils.enums.TagGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Table(name = "tag")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomTagEntity {
    @Id
    @Column(name = "tag_value") // "value" column name conflicts with H2
    private String value;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tag_group")
    private TagGroup tagGroup;

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomTagEntity tagEntity)) return false;
        return Objects.equals(value, tagEntity.value);
    }

    public boolean isFullyEqual(Object o) {
        if (this == o) return true;
        if (o instanceof CustomTagEntity tagEntity) {
            return equals(tagEntity) &&
                   Objects.equals(name, tagEntity.name) &&
                   tagGroup == tagEntity.tagGroup;
        }
        return false;
    }
}

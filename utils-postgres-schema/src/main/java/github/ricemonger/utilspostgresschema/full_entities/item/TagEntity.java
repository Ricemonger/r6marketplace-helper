package github.ricemonger.utilspostgresschema.full_entities.item;

import github.ricemonger.utils.enums.TagGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Table(name = "tag")
@Entity(name = "tag")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity {
    @Id
    @Column(name = "tag_value")
    private String value;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tag_group")
    private TagGroup tagGroup = TagGroup.Unknown;

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity tagEntity)) return false;
        return Objects.equals(value, tagEntity.value);
    }

    public boolean isFullyEqual(TagEntity entity) {
        return equals(entity) && Objects.equals(name, entity.name) && tagGroup == entity.tagGroup;
    }
}

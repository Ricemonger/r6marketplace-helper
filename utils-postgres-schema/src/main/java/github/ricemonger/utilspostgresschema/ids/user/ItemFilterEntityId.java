package github.ricemonger.utilspostgresschema.ids.user;


import github.ricemonger.utilspostgresschema.full_entities.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFilterEntityId {
    private UserEntity user;
    private String name;
}

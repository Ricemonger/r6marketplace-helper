package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utils.enums.InputGroup;
import github.ricemonger.utils.enums.InputState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "telegram_user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUserEntity {
    @Id
    @Column(name = "chat_id")
    private String chatId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "telegramUser")
    private List<TelegramUserInputEntity> telegramUserInputs = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "input_state")
    private InputState inputState = InputState.BASE;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "input_group")
    private InputGroup inputGroup = InputGroup.BASE;

    @Column(name = "item_show_messages_limit")
    private Integer itemShowMessagesLimit = 50;
    @Column(name = "item_show_few_in_message_flag")
    private Boolean itemShowFewInMessageFlag = false;

    public TelegramUserEntity(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TelegramUserEntity telegramUserEntity)) {
            return false;
        }
        return Objects.equals(chatId, telegramUserEntity.chatId);
    }
}

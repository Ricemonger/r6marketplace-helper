package github.ricemonger.marketplace.services;

import github.ricemonger.utils.dtos.UbiUser;

import java.util.Collection;

public interface UbiUserRepositoryService {
    void save(UbiUser user);

    void deleteById(String chatId, String email);

    void deleteAllByChatId(String chatId);

    Collection<String> getOwnedItemsIds(String chatId, String email);

    UbiUser findById(String chatId, String email);

    Collection<UbiUser> findAllByChatId(String chatId);

    Collection<UbiUser> findAll();
}

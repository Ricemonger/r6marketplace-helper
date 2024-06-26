package github.ricemonger.marketplace.services.abstractions;

import github.ricemonger.utils.dtos.Tag;

import java.util.Collection;

public interface TagDatabaseService {
    void saveAll(Collection<Tag> tags);

    Collection<Tag> findAllByNames(Collection<String> tagNames);

    Collection<Tag> findAll();
}

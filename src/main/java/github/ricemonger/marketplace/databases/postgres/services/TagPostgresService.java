package github.ricemonger.marketplace.databases.postgres.services;

import github.ricemonger.marketplace.databases.postgres.repositories.TagPostgresRepository;
import github.ricemonger.marketplace.databases.postgres.services.entity_mappers.item.TagEntityMapper;
import github.ricemonger.marketplace.services.abstractions.TagDatabaseService;
import github.ricemonger.utils.DTOs.common.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagPostgresService implements TagDatabaseService {

    private final TagPostgresRepository tagRepository;

    private final TagEntityMapper tagEntityMapper;

    @Override
    @Transactional
    public void saveAll(Collection<Tag> tags) {
        tagRepository.saveAll(tags.stream().map(tagEntityMapper::createEntity).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAllByNames(Collection<String> tagNames) {
        return tagRepository.findAllByNames(tagNames).stream().map(tagEntityMapper::createDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findAll().stream().map(tagEntityMapper::createDTO).toList();
    }
}

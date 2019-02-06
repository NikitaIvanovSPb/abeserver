package ru.nikita.abeserver.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.nikita.abeserver.domain.File;

import java.util.Optional;

@Repository
public interface FileRepository extends PagingAndSortingRepository<File, String> {
    Optional<File> findFirstByGuidEquals(String guid);
}

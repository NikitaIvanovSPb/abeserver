package ru.nikita.abeserver.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.nikita.abeserver.domain.FTP;

public interface FTPRepository extends CrudRepository<FTP, Long> {
    Iterable<FTP> getAllByDeletedIsFalse();
}

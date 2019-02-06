package ru.nikita.abeserver.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nikita.abeserver.domain.ABEKey;

import java.util.Optional;

@Repository
public interface ABEKeyRepository extends CrudRepository<ABEKey, Long> {
    Optional<ABEKey> findFirstByActive(boolean active);
}

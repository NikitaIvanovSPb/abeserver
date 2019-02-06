package ru.nikita.abeserver.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.nikita.abeserver.domain.Token;
import ru.nikita.abeserver.domain.User;

import java.util.Optional;

public interface TokenRepository extends PagingAndSortingRepository<Token, String> {
    Optional<Token> findFirstByGuidEquals(String guid);
    Iterable<Token> getAllByUserEqualsAndUsedIsFalse(User user);
}

package ru.nikita.abeserver.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nikita.abeserver.domain.Attribute;

@Repository
public interface AttributeRepository extends CrudRepository<Attribute, Long> {
    Iterable<Attribute> getAllByTypeEquals(Attribute.Type type);
}

package myapp.repository;

import myapp.domain.Client;
import myapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
}

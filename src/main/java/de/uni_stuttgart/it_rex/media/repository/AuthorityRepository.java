package de.uni_stuttgart.it_rex.media.repository;

import de.uni_stuttgart.it_rex.media.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

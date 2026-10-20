package com.realestate.app.repository;

import com.realestate.app.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link Project} entity operations.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findAllByDeveloperId(UUID developerId);

    Optional<Project> findByIdAndDeveloperId(UUID projectId, UUID developerId);
}

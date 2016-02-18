package com.github.bikeholik.datarest.company;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface OwnerRepository extends PagingAndSortingRepository<Owner, Long> {
    List<Company> findByNameLike(@Param("name") String name);
}

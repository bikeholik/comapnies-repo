package com.github.bikeholik.datarest.company;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection = SimplifiedInfo.class)
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);
}

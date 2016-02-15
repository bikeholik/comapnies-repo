package com.github.bikeholik.datarest;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = InlineOwner.class)
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

}

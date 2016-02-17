package com.github.bikeholik.datarest;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = SimplifiedInfo.class)
interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

}

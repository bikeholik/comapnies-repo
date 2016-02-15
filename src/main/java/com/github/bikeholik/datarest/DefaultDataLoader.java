package com.github.bikeholik.datarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
@Profile("!clean-data")
public class DefaultDataLoader {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @PostConstruct
    @Transactional
    public void init(){
        Owner sampleOwner = new Owner("Jeff Briggs");
        ownerRepository.save(sampleOwner);
        ownerRepository.save(new Owner("Ben King"));

        Company sampleCompany = new Company();
        sampleCompany.setName("Spring Inc.");
        sampleCompany.setAddress("Nowhere street 0");
        sampleCompany.setCity("Nowhere");
        sampleCompany.setCountry("Nowhereland");
        sampleCompany.setEmail("info@spring.io");
        sampleCompany.setOwners(Collections.singletonList(sampleOwner));
        companyRepository.save(sampleCompany);
    }
}

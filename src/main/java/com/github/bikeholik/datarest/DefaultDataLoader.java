package com.github.bikeholik.datarest;

import com.github.bikeholik.datarest.Company.CompanyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

@Component
@Profile("!clean-data")
public class DefaultDataLoader {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private DefaultDataProperties properties;

    @PostConstruct
    @Transactional
    public void init() {
        Owner sampleOwner = new Owner("Jeff Briggs");
        ownerRepository.save(sampleOwner);
        ownerRepository.save(new Owner("Ben King"));

        companyRepository.save(new CompanyBuilder()
                .name("Spring Inc.")
                .address("666 Road")
                .city("Void")
                .country("Neverland")
                .email("info@spring.io")
                .owners(Collections.singletonList(sampleOwner)).build());

        if (properties.getCompaniesCount() > 0) {
            companyRepository.save(rangeClosed(1, properties.getCompaniesCount())
                    .mapToObj(this::createCompany)
                    .collect(toList()));
        }
    }

    private Company createCompany(int index) {
        return new CompanyBuilder()
                .name(properties.getCompanyNamePrefix() + index)
                .address("Default Street " + index)
                .city("Malaga")
                .country("Spain")
                .build();
    }

    @Configuration
    @ConfigurationProperties(prefix = "default.data")
    static class DefaultDataProperties {
        private int companiesCount;
        private String companyNamePrefix;

        public int getCompaniesCount() {
            return companiesCount;
        }

        public void setCompaniesCount(int companiesCount) {
            this.companiesCount = companiesCount;
        }

        public String getCompanyNamePrefix() {
            return companyNamePrefix;
        }

        public void setCompanyNamePrefix(String companyNamePrefix) {
            this.companyNamePrefix = companyNamePrefix;
        }
    }
}

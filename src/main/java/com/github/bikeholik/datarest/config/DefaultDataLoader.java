package com.github.bikeholik.datarest.config;

import com.github.bikeholik.datarest.company.Company;
import com.github.bikeholik.datarest.company.Company.CompanyBuilder;
import com.github.bikeholik.datarest.company.CompanyRepository;
import com.github.bikeholik.datarest.company.Owner;
import com.github.bikeholik.datarest.company.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Random;

import static java.lang.System.currentTimeMillis;
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

    private final Random random = new Random(currentTimeMillis());

    @PostConstruct
    @Transactional
    public void init() {
        Owner sampleOwner = new Owner("Jeff Briggs");
        ownerRepository.save(sampleOwner);
        ownerRepository.save(new Owner("Ben King"));

        if (properties.getCompaniesCount() > 0) {
            companyRepository.save(rangeClosed(1, properties.getCompaniesCount())
                    .mapToObj(this::createCompany)
                    .collect(toList()));
        }

        companyRepository.save(new CompanyBuilder()
                .name("Spring Inc.")
                .address("666 Road")
                .city("Void")
                .country("Neverland")
                .email("info@spring.io")
                .owners(Collections.singletonList(sampleOwner)).build());

    }

    private Company createCompany(int index) {
        return new CompanyBuilder()
                .name(createName(index))
                .address("Calle Larios " + index)
                .city("Malaga")
                .country("Spain")
                .build();
    }

    private String createName(int index) {
        String[] companyNamePrefixes = properties.getCompanyNamePrefixes();
        return companyNamePrefixes[random.nextInt(companyNamePrefixes.length)] + '-' + index;
    }

    @Configuration
    @ConfigurationProperties(prefix = "default.data")
    static class DefaultDataProperties {
        private int companiesCount;
        private String[] companyNamePrefixes;

        public int getCompaniesCount() {
            return companiesCount;
        }

        public void setCompaniesCount(int companiesCount) {
            this.companiesCount = companiesCount;
        }

        public String[] getCompanyNamePrefixes() {
            return companyNamePrefixes;
        }

        public void setCompanyNamePrefixes(String[] companyNamePrefixes) {
            this.companyNamePrefixes = companyNamePrefixes;
        }
    }
}

package com.github.bikeholik.datarest.company;

import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name = "inlineOwner", types = { Company.class })
interface InlineOwner {
    Long getId();
    String getName();
    String getAddress();
    String getCity();
    String getCountry();
    String getEmail();
    String getPhoneNumber();
    List<Owner> getOwners();
}

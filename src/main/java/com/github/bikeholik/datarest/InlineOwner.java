package com.github.bikeholik.datarest;

import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name = "inlineOwner", types = { Company.class })
interface InlineOwner {
    Long getId();
    String getName();
    List<Owner> getOwners();
}

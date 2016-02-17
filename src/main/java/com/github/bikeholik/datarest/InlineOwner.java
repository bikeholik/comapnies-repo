package com.github.bikeholik.datarest;

import com.github.bikeholik.datarest.Company;
import com.github.bikeholik.datarest.Owner;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name = "inlineOwner", types = { Company.class })
interface InlineOwner {
    Long getId();
    String getName();
    List<Owner> getOwners();
}

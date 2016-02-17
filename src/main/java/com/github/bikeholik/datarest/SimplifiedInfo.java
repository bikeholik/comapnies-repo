package com.github.bikeholik.datarest;

import com.github.bikeholik.datarest.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "simplifiedInfo", types = { Company.class })
interface SimplifiedInfo {
    Long getId();
    String getName();
    @Value("#{target.address}, #{target.city}, #{target.country}")
    String getInfo();
}

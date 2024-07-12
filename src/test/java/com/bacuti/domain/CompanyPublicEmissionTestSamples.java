package com.bacuti.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyPublicEmissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CompanyPublicEmission getCompanyPublicEmissionSample1() {
        return new CompanyPublicEmission()
            .id(1L)
            .reportingCompany("reportingCompany1")
            .reportingYear(1)
            .permanentEmployees(1)
            .dataSourceType("dataSourceType1")
            .disclosureType("disclosureType1")
            .dataSource("dataSource1")
            .industryCodes("industryCodes1")
            .codeType("codeType1")
            .companyWebsite("companyWebsite1")
            .companyActivities("companyActivities1");
    }

    public static CompanyPublicEmission getCompanyPublicEmissionSample2() {
        return new CompanyPublicEmission()
            .id(2L)
            .reportingCompany("reportingCompany2")
            .reportingYear(2)
            .permanentEmployees(2)
            .dataSourceType("dataSourceType2")
            .disclosureType("disclosureType2")
            .dataSource("dataSource2")
            .industryCodes("industryCodes2")
            .codeType("codeType2")
            .companyWebsite("companyWebsite2")
            .companyActivities("companyActivities2");
    }

    public static CompanyPublicEmission getCompanyPublicEmissionRandomSampleGenerator() {
        return new CompanyPublicEmission()
            .id(longCount.incrementAndGet())
            .reportingCompany(UUID.randomUUID().toString())
            .reportingYear(intCount.incrementAndGet())
            .permanentEmployees(intCount.incrementAndGet())
            .dataSourceType(UUID.randomUUID().toString())
            .disclosureType(UUID.randomUUID().toString())
            .dataSource(UUID.randomUUID().toString())
            .industryCodes(UUID.randomUUID().toString())
            .codeType(UUID.randomUUID().toString())
            .companyWebsite(UUID.randomUUID().toString())
            .companyActivities(UUID.randomUUID().toString());
    }
}

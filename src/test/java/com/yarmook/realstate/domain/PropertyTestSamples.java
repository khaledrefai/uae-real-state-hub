package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PropertyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Property getPropertySample1() {
        return new Property()
            .id(1L)
            .extId(1L)
            .slug("slug1")
            .name("name1")
            .developer("developer1")
            .area("area1")
            .city("city1")
            .country("country1")
            .saleStatus("saleStatus1")
            .readiness("readiness1")
            .serviceCharge("serviceCharge1")
            .furnishing("furnishing1")
            .priceCurrency("priceCurrency1")
            .areaUnit("areaUnit1")
            .coverUrl("coverUrl1")
            .videoUrl("videoUrl1")
            .brochureUrl("brochureUrl1")
            .layoutsPdfUrl("layoutsPdfUrl1")
            .website("website1")
            .buildingsJson("buildingsJson1")
            .detailJson("detailJson1");
    }

    public static Property getPropertySample2() {
        return new Property()
            .id(2L)
            .extId(2L)
            .slug("slug2")
            .name("name2")
            .developer("developer2")
            .area("area2")
            .city("city2")
            .country("country2")
            .saleStatus("saleStatus2")
            .readiness("readiness2")
            .serviceCharge("serviceCharge2")
            .furnishing("furnishing2")
            .priceCurrency("priceCurrency2")
            .areaUnit("areaUnit2")
            .coverUrl("coverUrl2")
            .videoUrl("videoUrl2")
            .brochureUrl("brochureUrl2")
            .layoutsPdfUrl("layoutsPdfUrl2")
            .website("website2")
            .buildingsJson("buildingsJson2")
            .detailJson("detailJson2");
    }

    public static Property getPropertyRandomSampleGenerator() {
        return new Property()
            .id(longCount.incrementAndGet())
            .extId(longCount.incrementAndGet())
            .slug(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .developer(UUID.randomUUID().toString())
            .area(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .saleStatus(UUID.randomUUID().toString())
            .readiness(UUID.randomUUID().toString())
            .serviceCharge(UUID.randomUUID().toString())
            .furnishing(UUID.randomUUID().toString())
            .priceCurrency(UUID.randomUUID().toString())
            .areaUnit(UUID.randomUUID().toString())
            .coverUrl(UUID.randomUUID().toString())
            .videoUrl(UUID.randomUUID().toString())
            .brochureUrl(UUID.randomUUID().toString())
            .layoutsPdfUrl(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .buildingsJson(UUID.randomUUID().toString())
            .detailJson(UUID.randomUUID().toString());
    }
}

package com.yarmook.realstate.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MediaAssetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MediaAsset getMediaAssetSample1() {
        return new MediaAsset()
            .id(1L)
            .url("url1")
            .mimeType("mimeType1")
            .width(1)
            .height(1)
            .mediaSize(1L)
            .source("source1")
            .title("title1")
            .altText("altText1")
            .sortOrder(1);
    }

    public static MediaAsset getMediaAssetSample2() {
        return new MediaAsset()
            .id(2L)
            .url("url2")
            .mimeType("mimeType2")
            .width(2)
            .height(2)
            .mediaSize(2L)
            .source("source2")
            .title("title2")
            .altText("altText2")
            .sortOrder(2);
    }

    public static MediaAsset getMediaAssetRandomSampleGenerator() {
        return new MediaAsset()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .mimeType(UUID.randomUUID().toString())
            .width(intCount.incrementAndGet())
            .height(intCount.incrementAndGet())
            .mediaSize(longCount.incrementAndGet())
            .source(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .altText(UUID.randomUUID().toString())
            .sortOrder(intCount.incrementAndGet());
    }
}

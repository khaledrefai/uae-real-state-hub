package com.yarmook.realstate.service.util;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Simple pagination helper for in-memory Mongo query services.
 */
public final class MongoPageUtils {

    private MongoPageUtils() {}

    public static <T> Page<T> toPage(List<T> items, Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return new PageImpl<>(items);
        }
        int start = Math.toIntExact(pageable.getOffset());
        if (start >= items.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, items.size());
        }
        int end = Math.min(start + pageable.getPageSize(), items.size());
        return new PageImpl<>(items.subList(start, end), pageable, items.size());
    }
}

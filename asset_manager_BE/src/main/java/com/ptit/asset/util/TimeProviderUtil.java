package com.ptit.asset.util;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class TimeProviderUtil {

    private final static ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    private final static ZoneOffset zoneOffset = ZoneOffset.of("+07:00");
}

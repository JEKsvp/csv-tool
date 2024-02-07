package com.abadeksvp.csvtool.service.impl.csv;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum CSVHeader {
    SOURCE("source", 0),
    CODE_LIST_CODE("codeListCode", 1),
    CODE("code", 2),
    DISPLAY_VALUE("displayValue", 3),
    LONG_DESCRIPTION("longDescription", 4),
    FROM_DATE("fromDate", 5),
    TO_DATE("toDate", 6),
    SORTING_PRIORITY("sortingPriority", 7);

    private final String header;
    private final int order;

    CSVHeader(String header, int order) {
        this.header = header;
        this.order = order;
    }

    public static String[] headers() {
        return Arrays.stream(CSVHeader.values())
                .map(CSVHeader::getHeader)
                .toArray(String[]::new);
    }

    public String name(String[] headers) {
        return headers.length > order ? headers[order] : null;
    }

}

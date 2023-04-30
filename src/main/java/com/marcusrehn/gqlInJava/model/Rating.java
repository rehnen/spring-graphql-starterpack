package com.marcusrehn.gqlInJava.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Rating {
    FIVE_STARS("FIVE"),
    FOUR_STARS("Four"),
    THREE_STARS("Three"),
    TWO_STARS("Two"),
    ONE_STAR("One");

    private String star;
    Rating(String star) {
        this.star = star;
    }

    @JsonValue
    String getStar() {
        return star;
    }
}

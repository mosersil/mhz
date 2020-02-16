package com.silviomoser.mhz.api.core;

import org.springframework.data.jpa.domain.Specification;

public abstract class SpecificationBuilder<T> {

    public abstract Specification<T> build();
}

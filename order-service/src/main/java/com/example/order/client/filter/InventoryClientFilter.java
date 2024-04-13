package com.example.order.client.filter;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.micronaut.http.annotation.FilterMatcher;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@FilterMatcher
@Documented
@Retention(RUNTIME)
@Target({TYPE, PARAMETER})
public @interface InventoryClientFilter {}

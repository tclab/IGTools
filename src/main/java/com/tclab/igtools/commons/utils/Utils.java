package com.tclab.igtools.commons.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Stream;

public class Utils {

  public static Optional<String> firstNotEmpty(String... values) {
    return values == null ? Optional.empty() :
        Stream.of(values)
            .filter(s -> s != null && !s.isEmpty())
            .findFirst();
  }

  public static ObjectMapper setupMapper(ObjectMapper mapper) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(dateFormat);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  public static boolean isEmpty(String value) {
    return value == null || value.isEmpty();
  }

  public static boolean isNotEmpty(String value) {
    return !isEmpty(value);
  }

  public static boolean isEmpty(LocalDateTime value) {
    return value == null;
  }

  public static boolean isNotEmpty(LocalDateTime value) {
    return !isEmpty(value);
  }

  public static <K, V> boolean isNotEmpty(Map<K, V> value) {
    return !isEmpty(value);
  }



  public static <K, V> boolean isEmpty(Map<K, V> value) {
    return value == null || value.isEmpty();
  }

  public static <V> boolean isNotEmpty(Collection<V> value) {
    return !isEmpty(value);
  }

  public static <V> boolean isEmpty(Collection<V> value) {
    return value == null || value.isEmpty();
  }

  public static <T> boolean isNotEmpty(T[] value) {
    return !isEmpty(value);
  }

  public static <T> boolean isEmpty(T[] value) {
    return value == null || value.length == 0;
  }

  @SafeVarargs
  public static <T> boolean isOneOf(T value, T... values) {
    Objects.requireNonNull(values, "Values args cannot be null");
    return Arrays.asList(values).contains(value);
  }

  public static <T> Stream<T> toStream(Collection<T> values) {
    return values != null ? values.stream() : Stream.empty();
  }
}

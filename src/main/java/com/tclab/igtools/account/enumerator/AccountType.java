package com.tclab.igtools.account.enumerator;

import java.util.Optional;

public enum AccountType {

  MANAGED,
  FEED;

  public static AccountType fromValue(String value) throws Exception {
    return Optional.ofNullable(value)
        .map(String::toUpperCase)
        .map(
            upperValue -> {
              try {
                return AccountType.valueOf(upperValue);
              } catch (Exception ex) {
                return null;
              }
            })
        .orElseThrow(() -> new Exception(String.format("Unknown account type: %s", value)));
  }

}

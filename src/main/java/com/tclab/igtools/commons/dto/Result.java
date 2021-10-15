package com.tclab.igtools.commons.dto;

import java.util.List;

public interface Result <T> {

    boolean isLast();

    List<T> getContent();
}
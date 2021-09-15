package com.tclab.igtools.commons.dto;

import com.tclab.igtools.commons.utils.Utils;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultPage<T> implements Result<T>{

    protected int number;
    protected int size;
    protected int numberOfElements;
    protected long totalElements;
    protected int totalPages;
    protected boolean first;
    protected boolean last;
    protected List<T> content;

    protected ResultPage(Page<T> page) {
        Objects.requireNonNull(page, "Page cannot be null");
        this.number = page.getNumber();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.content = page.getContent();
    }

    public static <V> ResultPage<V> of(Page<V> page) {
        return new ResultPage<>(page);
    }

    public <R> ResultPage<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        return new ResultPage<>(
                this.number,
                this.size,
                this.numberOfElements,
                this.totalElements,
                this.totalPages,
                this.first,
                this.last,
                Utils.isNotEmpty(content) ?
                    content.stream()
                        .map(mapper)
                        .collect(Collectors.toList()): null);
    }
}

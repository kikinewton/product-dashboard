package com.bsupply.productdashboard.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class PageResponseDto<T> {

    private boolean hasNext;
    private List<?> data;

    PageResponseDto() {
        this.hasNext = false;
        this.data = Collections.emptyList();
    }

    public static <T extends Page> PageResponseDto wrapResponse(T input) {
        PageResponseDto<T> p = new PageResponseDto<>();
        p.data = input.getContent();
        p.hasNext = input.hasNext();
        return p;
    }
}

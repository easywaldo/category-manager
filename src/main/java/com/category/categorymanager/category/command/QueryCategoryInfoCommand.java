package com.category.categorymanager.category.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QueryCategoryInfoCommand {
    private Integer categoryParentSeq;
    private String categoryName;

    @Builder
    public QueryCategoryInfoCommand(Integer categoryParentSeq, String categoryName) {
        this.categoryParentSeq = categoryParentSeq;
        this.categoryName = categoryName;
    }
}

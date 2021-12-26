package com.category.categorymanager.category.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QueryCategoryInfoCommand {
    @ApiModelProperty(value = "조회 카테고리 일련번호", position = 1)
    private Integer categoryParentSeq;
    public Integer getCategoryParentSeq() {
        return categoryParentSeq == null ? 0: categoryParentSeq;
    }
    @ApiModelProperty(value = "조회 카테고리 이름", position = 2)
    private String categoryName;

    @Builder
    public QueryCategoryInfoCommand(Integer categoryParentSeq, String categoryName) {
        this.categoryParentSeq = categoryParentSeq;
        this.categoryName = categoryName;
    }
}

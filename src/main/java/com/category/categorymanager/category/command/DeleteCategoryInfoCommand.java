package com.category.categorymanager.category.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCategoryInfoCommand {
    @ApiModelProperty(value = "삭제대상 카테고리 일련번호", position = 1)
    private Integer categoryInfoSeq;

    @Builder
    public DeleteCategoryInfoCommand(Integer categoryInfoSeq) {
        this.categoryInfoSeq = categoryInfoSeq;
    }
}

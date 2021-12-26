package com.category.categorymanager.category.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCategoryInfoCommand {
    @ApiModelProperty(value = "수정대상 카테고리 일련번호", position = 1)
    private Integer categoryInfoSeq;
    @ApiModelProperty(value = "수정대상 카테고리 상위 카테고리 일련번호", position = 2)
    private Integer parentSeq;
    @ApiModelProperty(value = "수정대상 카테고리 이름", position = 3)
    private String categoryName;
    @ApiModelProperty(value = "수정대상 카테고리 삭제여부", position = 4)
    private Boolean isDelete;
    @ApiModelProperty(value = "수정대상 카테고리 뎁스", position = 5)
    private Integer categoryDepth;

    @Builder
    public UpdateCategoryInfoCommand(Integer categoryInfoSeq,
                                     Integer parentSeq,
                                     String categoryName,
                                     Boolean isDelete,
                                     Integer categoryDepth) {
        this.categoryInfoSeq = categoryInfoSeq;
        this.parentSeq = parentSeq;
        this.categoryName = categoryName;
        this.isDelete = isDelete;
        this.categoryDepth = categoryDepth;
    }
}

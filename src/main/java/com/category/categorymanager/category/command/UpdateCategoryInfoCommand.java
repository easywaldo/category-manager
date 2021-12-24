package com.category.categorymanager.category.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCategoryInfoCommand {
    private Integer categoryInfoSeq;
    private Integer parentSeq;
    private String categoryName;
    private Boolean isDelete;
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

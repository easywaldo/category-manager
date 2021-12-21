package com.category.categorymanager.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryInfoCommand {
    private String categoryName;
    private Integer categoryDepth;
    private Boolean isDelete;
    private Integer parentSeq;

    @Builder
    public CreateCategoryInfoCommand(String categoryName,
                                     Integer categoryDepth,
                                     Boolean isDelete,
                                     Integer parentSeq) {
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.isDelete = isDelete;
        this.parentSeq = parentSeq;
    }
}

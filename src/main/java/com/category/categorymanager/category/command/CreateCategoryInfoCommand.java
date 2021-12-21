package com.category.categorymanager.category.command;

import com.category.categorymanager.category.entity.CategoryInfo;
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

    public CategoryInfo toEntity() {
        return CategoryInfo.builder()
            .categoryName(this.categoryName)
            .categoryDepth(this.categoryDepth)
            .isDelete(false)
            .parentSeq(this.parentSeq)
            .build();
    }
}

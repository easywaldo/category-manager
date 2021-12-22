package com.category.categorymanager.category.command;

import com.category.categorymanager.category.entity.CategoryInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateCategoryInfoCommand {
    private String categoryName;
    private Integer categoryDepth;
    private Boolean isDelete;
    private Integer parentSeq;
    private Integer categoryInfoSeq;

    @Builder
    public CreateCategoryInfoCommand(String categoryName,
                                     Integer categoryDepth,
                                     Boolean isDelete,
                                     Integer parentSeq,
                                     Integer categoryInfoSeq) {
        this.categoryName = categoryName;
        this.categoryDepth = categoryDepth;
        this.isDelete = isDelete;
        this.parentSeq = parentSeq;
        this.categoryInfoSeq = categoryInfoSeq;
    }

    public CategoryInfo toEntity() {
        return CategoryInfo.builder()
            .categoryInfoSeq(this.categoryInfoSeq)
            .categoryName(this.categoryName)
            .categoryDepth(this.categoryDepth)
            .isDelete(false)
            .parentSeq(this.parentSeq)
            .build();
    }
}

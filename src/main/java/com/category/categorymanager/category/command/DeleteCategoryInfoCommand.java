package com.category.categorymanager.category.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCategoryInfoCommand {
    private Integer categoryInfoSeq;

    @Builder
    public DeleteCategoryInfoCommand(Integer categoryInfoSeq) {
        this.categoryInfoSeq = categoryInfoSeq;
    }
}

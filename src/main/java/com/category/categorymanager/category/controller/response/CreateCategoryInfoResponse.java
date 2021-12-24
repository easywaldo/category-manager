package com.category.categorymanager.category.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryInfoResponse {
    private Integer registeredCategoryInfoSeq;
    private CategoryInfoValidationEnum categoryInfoValidationEnum;

    @Builder
    public CreateCategoryInfoResponse(Integer registeredCategoryInfoSeq,
                                      CategoryInfoValidationEnum categoryInfoValidationEnum) {
        this.registeredCategoryInfoSeq = registeredCategoryInfoSeq;
        this.categoryInfoValidationEnum = categoryInfoValidationEnum;
    }
}

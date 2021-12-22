package com.category.categorymanager.category.command;

import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.config.validator.RequestValidator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class CreateCategoryInfoCommand implements RequestValidator {
    @NotBlank(message = "category name can not be blank")
    private String categoryName;
    @Min(value = 1)
    @Max(value = 3)
    private Integer categoryDepth;
    private Boolean isDelete;
    public Boolean getIsDelete() {
        return isDelete != null && isDelete;
    }
    @Min(value = 0)
    private Integer parentSeq;
    @Min(value = 1)
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

    @Override
    public void validate(Validator validator, Errors errors) {
        SpringValidatorAdapter springValidatorAdapter = (SpringValidatorAdapter) validator;
        springValidatorAdapter.validate(this, errors);
        if (isDelete && (categoryInfoSeq == null || categoryInfoSeq == 0)) {
            errors.rejectValue("categoryInfoSeq", "isDelete can't not be allowed");
        }
    }
}

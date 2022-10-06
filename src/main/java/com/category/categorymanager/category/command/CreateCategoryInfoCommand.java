package com.category.categorymanager.category.command;

import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.config.validator.CategoryInput;
import com.category.categorymanager.config.validator.RequestValidator;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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

    @ApiModelProperty(value = "카테고리 이름", position = 1)
    @NotBlank(message = "category name can not be blank")
    @CategoryInput
    private String categoryName;
    @ApiModelProperty(value = "카테고리 뎁스(1부터 3뎁스까지 허용한다)", position = 2)
    @Min(value = 1)
    @Max(value = 3)
    private Integer categoryDepth;
    @ApiModelProperty(value = "카테고리 삭제여부(삭제 요청 시 해당 값을 실제 업데이트는 하지 않으며 수정요청시 파라미터 값에 따라서 갱신한다", position = 3)
    private Boolean isDelete;
    public Boolean getIsDelete() {
        return isDelete != null && isDelete;
    }
    @ApiModelProperty(value = "부모 카테고리 일련번호", position = 4)
    @Min(value = 0)
    private Integer parentSeq;
    @ApiModelProperty(value = "카테고리 일련번호", position = 5)
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

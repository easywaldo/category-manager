package com.category.categorymanager.category.controller;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.service.CategoryInfoService;
import com.category.categorymanager.config.validator.CustomValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class CategoryManagerController {
    private final CategoryInfoService categoryInfoService;
    private final CustomValidator customValidator;

    @Autowired
    public CategoryManagerController(CategoryInfoService categoryInfoService,
                                     CustomValidator customValidator) {
        this.categoryInfoService = categoryInfoService;
        this.customValidator = customValidator;
    }

    @ApiOperation(value = "Register category")
    @PostMapping("/category/register-category")
    public Mono<ResponseEntity<?>> registerCategory(@RequestBody @Validated CreateCategoryInfoCommand createCommand,
                                                    @ApiIgnore Errors errors) {
        this.customValidator.validate(createCommand, errors);
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().body(errors.getAllErrors()));
        }
        var categorySeq = categoryInfoService.createCategoryInfo(createCommand);
        createCommand.setCategoryInfoSeq(categorySeq);
        return Mono.just(ResponseEntity.accepted().body(createCommand));
    }
}

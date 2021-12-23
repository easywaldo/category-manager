package com.category.categorymanager.category.controller;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.DeleteCategoryInfoCommand;
import com.category.categorymanager.category.command.QueryCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.service.CategoryInfoService;
import com.category.categorymanager.config.validator.CustomValidator;
import com.category.categorymanager.querygenerator.CategoryQueryGenerator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.Optional;

@RestController
public class CategoryManagerController {
    private final CategoryInfoService categoryInfoService;
    private final CustomValidator customValidator;
    private final CategoryQueryGenerator categoryQueryGenerator;

    @Autowired
    public CategoryManagerController(CategoryInfoService categoryInfoService,
                                     CategoryQueryGenerator categoryQueryGenerator,
                                     CustomValidator customValidator) {
        this.categoryInfoService = categoryInfoService;
        this.categoryQueryGenerator = categoryQueryGenerator;
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
        return Mono.just(ResponseEntity.created(
            URI.create(String.format("/category/%s", categorySeq)))
            .body(createCommand));
    }

    @ApiOperation(value = "Get category", notes = "categoryInfoSeq 필수값이 아니도록 설정이 되어 있으며 swagger ui 상으로는 입력을 해야 한다.")
    @GetMapping(value = {"/category", "/category/{categoryInfoSeq}"})
    public Mono<ResponseEntity<?>> selectCategory(@PathVariable(required = false, name = "categoryInfoSeq") Optional<Integer> categoryInfoSeq) {
        var result = this.categoryQueryGenerator.selectCategoryInfo(categoryInfoSeq.orElse(0));
        return Mono.just(ResponseEntity.ok().body(result));
    }

    @ApiOperation(value = "Update category")
    @PutMapping("/category/updateCategory}")
    public Mono<ResponseEntity<?>> updateCategory(@RequestBody UpdateCategoryInfoCommand updateCommand) {
        var result = this.categoryInfoService.updateCategoryInfo(updateCommand);
        return Mono.just(ResponseEntity.ok().body(result));
    }

    @ApiOperation(value = "Delete category")
    @DeleteMapping("/category/deleteCategory}")
    public Mono<ResponseEntity<?>> deleteCategory(@RequestBody DeleteCategoryInfoCommand deleteCommand) {
        this.categoryInfoService.deleteCategoryInfo(deleteCommand);
        return Mono.just(ResponseEntity.ok().body(true));
    }

    @ApiOperation(value = "Query category")
    @PostMapping("/category/queryCategory")
    public Mono<ResponseEntity<?>> selectCategory(@RequestBody QueryCategoryInfoCommand queryCommand) {
        var result = this.categoryQueryGenerator.queryCategoryInfoList(queryCommand);
        return Mono.just(ResponseEntity.ok().body(result));
    }


}

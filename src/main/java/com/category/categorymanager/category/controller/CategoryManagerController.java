package com.category.categorymanager.category.controller;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.service.CategoryInfoService;
import com.category.categorymanager.config.validator.CustomValidator;
import com.category.categorymanager.querygenerator.CategoryQueryGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;

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

    @ApiOperation(value = "Get category")
    @GetMapping("/category/{categoryInfoSeq}")
    public Mono<ResponseEntity<?>> selectCategory(@PathVariable Integer categoryInfoSeq) {
        var result = this.categoryQueryGenerator.selectCategoryInfo(categoryInfoSeq);
        return Mono.just(ResponseEntity.ok().body(result));
    }

    @ApiOperation(value = "Update category")
    @PutMapping("/category/updateCategory}")
    public Mono<ResponseEntity<?>> updateCategory(@RequestBody UpdateCategoryInfoCommand updateCommand) {
        var result = this.categoryInfoService.updateCategoryInfo(updateCommand);
        return Mono.just(ResponseEntity.ok().body(result));
    }
}

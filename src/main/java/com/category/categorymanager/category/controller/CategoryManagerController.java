package com.category.categorymanager.category.controller;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.service.CategoryInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CategoryManagerController {
    private final CategoryInfoService categoryInfoService;

    @Autowired
    public CategoryManagerController(CategoryInfoService categoryInfoService) {
        this.categoryInfoService = categoryInfoService;
    }

    @ApiOperation(value = "Register category")
    @PostMapping("/category/register-category")
    public Mono<ResponseEntity<?>> registerCategory(@RequestBody CreateCategoryInfoCommand createCommand) {
        var categorySeq = categoryInfoService.createCategoryInfo(createCommand);
        createCommand.setCategoryInfoSeq(categorySeq);
        return Mono.just(ResponseEntity.accepted().body(createCommand));
    }
}

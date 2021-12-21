package com.category.categorymanager.category.service;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.DeleteCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CategoryInfoServiceTest {

    @Autowired
    public CategoryInfoService categoryInfoService;

    @Autowired
    public CategoryInfoRepository categoryInfoRepository;

    @BeforeEach
    public void setUp() {
        categoryInfoRepository.deleteAll();
    }

    @Test
    public void 신규_카테고리_등록이_정상적으로_이뤄지는지_신규_카테고리_생성_커맨드를_통해_서비스를_호출하여_확인한다() {

        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryDepth(0)
            .categoryName("MEN")
            .parentSeq(0)
            .build();

        // act
        categoryInfoService.createCategoryInfo(createCommand);

        // assert
        var result = categoryInfoRepository.findAll();
        assertEquals("MEN", result.stream().findFirst().get().getCategoryName());

    }

    @Test
    public void 기존_카테고리_수정이_정상적으로_이뤄지는지_수정_커맨드를_통해_서비스를_호출하야_확인한다() {

        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryDepth(0)
            .categoryName("MEN")
            .parentSeq(0)
            .build();
        var categoryInfoSeq = categoryInfoService.createCategoryInfo(createCommand);
        UpdateCategoryInfoCommand updateCommand = UpdateCategoryInfoCommand.builder()
            .categoryInfoSeq(categoryInfoSeq)
            .isDelete(false)
            .parentSeq(0)
            .categoryName("SHOES")
            .build();


        // act
        var updated = categoryInfoService.updateCategoryInfo(updateCommand);

        // assert
        var result = categoryInfoRepository.findById(categoryInfoSeq);
        assertEquals("SHOES", result.get().getCategoryName());;
        assertEquals("SHOES", updated.getCategoryName());
    }

    @Test
    public void 카테고리를_삭제하는_경우_하위_카테고리도_모두_삭제가_되는지_서비스를_호출하여_확인한다() {

        // arrange
        var bulkInsertCommand = List.of(
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(1)
                .categoryDepth(0)
                .categoryName("MEN")
                .parentSeq(0)
                .build(),
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(2)
                .categoryDepth(1)
                .categoryName("OUTER")
                .parentSeq(1)
                .build(),
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(3)
                .categoryDepth(2)
                .categoryName("COAT")
                .parentSeq(2)
                .build());

        categoryInfoService.bulkInsertCategoryInfo(bulkInsertCommand);

        DeleteCategoryInfoCommand deleteCommand = DeleteCategoryInfoCommand.builder()
            .categoryInfoSeq(0)
            .build();

        // act
        this.categoryInfoService.deleteCategoryInfo(deleteCommand);

        // assert
        var result = this.categoryInfoRepository.findAll();
        assertEquals(0, result.size());
    }
}
package com.category.categorymanager.category.service;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.DeleteCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CategoryInfoServiceTest {

    @Autowired
    public CategoryInfoService categoryInfoService;

    @Autowired
    public CategoryInfoRepository categoryInfoRepository;

    @BeforeTestExecution
    @BeforeEach
    public void setUp() {
        categoryInfoRepository.deleteAll();
    }

    @Test
    public void 신규_카테고리_등록이_정상적으로_이뤄지는지_신규_카테고리_생성_커맨드를_통해_서비스를_호출하여_확인한다() {

        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryDepth(1)
            .categoryName("MEN")
            .parentSeq(0)
            .isDelete(false)
            .build();

        // act
        categoryInfoService.createCategoryInfo(createCommand);

        // assert
        var result = categoryInfoRepository.findAll();
        assertEquals("MEN", result.stream().findFirst().get().getCategoryName());

    }

    @Test
    public void 기존_카테고리_수정이_정상적으로_이뤄지는지_수정_커맨드를_통해_서비스를_호출하여_확인한다() {

        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryDepth(1)
            .categoryName("MEN")
            .parentSeq(0)
            .build();
        var commandResult = categoryInfoService.createCategoryInfo(createCommand);
        UpdateCategoryInfoCommand updateCommand = UpdateCategoryInfoCommand.builder()
            .categoryInfoSeq(commandResult.getRegisteredCategoryInfoSeq())
            .isDelete(false)
            .parentSeq(0)
            .categoryDepth(1)
            .categoryName("SHOES")
            .build();

        // act
        var updated = categoryInfoService.updateCategoryInfo(updateCommand);

        // assert
        var result = categoryInfoRepository.findById(commandResult.getRegisteredCategoryInfoSeq());
        assertEquals("SHOES", result.get().getCategoryName());;
        assertEquals("SHOES", updated.getCategoryName());
    }

    @Test
    public void 기존_카테고리_뎁스가_3뎁스_보다_큰_경우_뎁스_수정이_이뤄지지_않는지를_수정_커맨드를_통해_서비스를_호출하여_확인한다() {

        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryDepth(1)
            .categoryName("BEAUTY")
            .parentSeq(0)
            .build();
        var commandResult = categoryInfoService.createCategoryInfo(createCommand);
        UpdateCategoryInfoCommand updateCommand = UpdateCategoryInfoCommand.builder()
            .categoryInfoSeq(commandResult.getRegisteredCategoryInfoSeq())
            .isDelete(false)
            .parentSeq(0)
            .categoryDepth(3)
            .categoryName("FACE")
            .build();

        // act
        var updated = categoryInfoService.updateCategoryInfo(updateCommand);

        // assert
        var result = categoryInfoRepository.findById(commandResult.getRegisteredCategoryInfoSeq());
        assertEquals("FACE", result.get().getCategoryName());;
        assertEquals("FACE", updated.getCategoryName());
        assertEquals(1, result.get().getCategoryDepth());
    }

    @Test
    public void 카테고리를_삭제하는_경우_하위_카테고리도_모두_삭제가_되는지_서비스를_호출하여_확인한다() {

        // arrange
        var bulkInsertCommand = List.of(
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(1)
                .categoryDepth(1)
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
            .categoryInfoSeq(1)
            .build();

        // act
        this.categoryInfoService.deleteCategoryInfo(deleteCommand);

        // assert
        var result = this.categoryInfoRepository.findAllById(List.of(1,2,3));
        assertEquals(0, result.size());
    }

    @Test
    public void 카테고리를_삭제하는_경우_해당하지_않는_카테고리는_삭제가_되지_않는지_서비스를_호출하여_확인한다() throws InterruptedException {
        // arrange
        var foo = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(1)
                .categoryName("FOO")
                .parentSeq(0)
                .build().toEntity());
        var bar = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(1)
                .categoryName("BAR")
                .parentSeq(0)
                .build().toEntity());
        var baz = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(2)
                .categoryName("BAZ")
                .parentSeq(bar.getCategoryInfoSeq())
                .build().toEntity());
        DeleteCategoryInfoCommand deleteCommand = DeleteCategoryInfoCommand.builder()
            .categoryInfoSeq(bar.getCategoryInfoSeq())
            .build();

        // act
        this.categoryInfoService.deleteCategoryInfo(deleteCommand);

        // assert
        var deleteResult = this.categoryInfoRepository.findAllById(
            List.of(bar.getCategoryInfoSeq(), baz.getCategoryInfoSeq()));
        assertEquals(0, deleteResult.size());
        var notDeleteResult = this.categoryInfoRepository.findAllById(
            List.of(foo.getCategoryInfoSeq()));
        assertEquals(1, notDeleteResult.size());
    }
}
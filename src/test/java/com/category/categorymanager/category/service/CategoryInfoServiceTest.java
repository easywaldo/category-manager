package com.category.categorymanager.category.service;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.DeleteCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.controller.response.CategoryInfoValidationEnum;
import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class CategoryInfoServiceTest {

    @Autowired
    public CategoryInfoService categoryInfoService;

    @InjectMocks
    public CategoryInfoService injectMockCategoryInfoService;

    @Autowired
    public CategoryInfoRepository categoryInfoRepository;

    @Mock
    public CategoryInfoRepository categoryInfoMockRepository;

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

    @Test
    public void 카테고리를_하향_이동하는_경우_서비스를_호출하여_확인한다() {
        // arrange
        var category1 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(1)
                .categoryName("CATEGORY-1")
                .parentSeq(0)
                .build().toEntity());
        var category1Child1 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(2)
                .categoryName("CATEGORY-1-Child-1")
                .parentSeq(category1.getCategoryInfoSeq())
                .build().toEntity());
        var category2 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(1)
                .categoryName("CATEGORY-2")
                .parentSeq(category1.getCategoryInfoSeq())
                .build().toEntity());
        var category21 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(2)
                .categoryName("CATEGORY-2-1")
                .parentSeq(category2.getCategoryInfoSeq())
                .build().toEntity());

        // act
        this.categoryInfoService.updateCategoryTreeInfo(UpdateCategoryInfoCommand.builder()
            .categoryInfoSeq(category1.getCategoryInfoSeq())
            .parentSeq(category2.getCategoryInfoSeq())
            .categoryDepth(2)
            .build());

        // assert
        var result = categoryInfoRepository.findAll();
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1.getCategoryInfoSeq())).findFirst().get().getParentSeq().equals(category2.getCategoryInfoSeq()));
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1.getCategoryInfoSeq())).findFirst().get().getCategoryDepth().equals(2));
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1Child1.getCategoryInfoSeq())).findFirst().get().getParentSeq().equals(category1.getCategoryInfoSeq()));
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1Child1.getCategoryInfoSeq())).findFirst().get().getCategoryDepth().equals(3));

    }

    @Test
    public void 카테고리를_상향_이동하는_경우_서비스를_호출하여_확인한다() {
        // arrange
        var category1 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(1)
                .categoryName("CATEGORY-1")
                .parentSeq(0)
                .build().toEntity());
        var category1Child1 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(2)
                .categoryName("CATEGORY-1-Child-1")
                .parentSeq(category1.getCategoryInfoSeq())
                .build().toEntity());
        var category1Child11 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(3)
                .categoryName("CATEGORY-1-Child-1-1")
                .parentSeq(category1Child1.getCategoryInfoSeq())
                .build().toEntity());
        var category2 = this.categoryInfoRepository.save(
            CreateCategoryInfoCommand.builder()
                .categoryDepth(1)
                .categoryName("CATEGORY-2-1")
                .parentSeq(0)
                .build().toEntity());

        // act
        this.categoryInfoService.updateCategoryTreeInfo(UpdateCategoryInfoCommand.builder()
            .categoryInfoSeq(category1Child1.getCategoryInfoSeq())
            .parentSeq(0)
            .categoryDepth(1)
            .build());

        // assert
        var result = categoryInfoRepository.findAll();
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1Child1.getCategoryInfoSeq())).findFirst().get().getParentSeq().equals(0));
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1Child1.getCategoryInfoSeq())).findFirst().get().getCategoryDepth().equals(1));
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1Child11.getCategoryInfoSeq())).findFirst().get().getParentSeq().equals(category1Child1.getCategoryInfoSeq()));
        assertThat(result.stream().filter(x -> x.getCategoryInfoSeq().equals(
            category1Child11.getCategoryInfoSeq())).findFirst().get().getCategoryDepth().equals(2));
    }

    @Test
    public void 카테고리_이동_시_수정_명령의_카테고리_부모값이_3뎁스인경우_수정이_되지_않는지_확인한다() {
        // arrange
        var bulkInsertCommand = List.of(
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(1)
                .categoryDepth(1)
                .categoryName("MEN")
                .parentSeq(0)
                .isDelete(false)
                .build(),
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(2)
                .categoryDepth(2)
                .categoryName("OUTER")
                .parentSeq(1)
                .isDelete(false)
                .build(),
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(3)
                .categoryDepth(3)
                .categoryName("COAT")
                .parentSeq(2)
                .isDelete(false)
                .build(),
            CreateCategoryInfoCommand.builder()
                .categoryInfoSeq(4)
                .categoryDepth(3)
                .categoryName("PADDING")
                .parentSeq(2)
                .isDelete(false)
                .build());

        this.categoryInfoService.bulkInsertCategoryInfo(bulkInsertCommand);
        this.categoryInfoService.createCategoryInfo(CreateCategoryInfoCommand.builder()
            .categoryInfoSeq(5)
            .categoryName("NEW")
            .parentSeq(0)
            .isDelete(false)
            .categoryDepth(1)
            .build());

        // assert
        var assertTarget = this.categoryInfoRepository.findAll().stream().filter(x -> x.getCategoryName().equals("NEW")).findFirst().get();
        var parentTarget = this.categoryInfoRepository.findAll().stream().filter(x -> x.getCategoryName().equals("PADDING")).findFirst().get();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> this.categoryInfoService.updateCategoryInfo(UpdateCategoryInfoCommand.builder()
            .categoryDepth(3)
            .parentSeq(parentTarget.getCategoryInfoSeq())
            .categoryName("MOVED-COAT")
            .categoryInfoSeq(assertTarget.getCategoryInfoSeq())
            .isDelete(false)
            .build()));
        assertEquals("cat not be moved category", exception.getMessage());

    }

    @Test
    public void 카테고리_이동_시_수정_명령의_카테고리_부모가_존재하지_않는_경우_수정_되지_않는지_확인한다() {
        // arrange
        this.categoryInfoService.createCategoryInfo(CreateCategoryInfoCommand.builder()
            .categoryInfoSeq(10)
            .categoryName("UNKNOWN")
            .parentSeq(0)
            .isDelete(false)
            .categoryDepth(1)
            .build());

        // assert
        var assertTarget = this.categoryInfoRepository.findAll().stream().filter(x -> x.getCategoryName().equals("UNKNOWN")).findFirst().get();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> this.categoryInfoService.updateCategoryInfo(UpdateCategoryInfoCommand.builder()
            .categoryDepth(3)
            .parentSeq(99999)
            .categoryName("UNKNOWN-MOVED")
            .categoryInfoSeq(assertTarget.getCategoryInfoSeq())
            .isDelete(false)
            .build()));
        assertEquals("parent category is not exists", exception.getMessage());

    }

    @Test
    public void 부모카테고리가_존재하지_않는_경우에_대한_신규_카테고리_등록_시_올바른_리턴값을_확인한다() {
        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryInfoSeq(10)
            .parentSeq(99)
            .categoryDepth(2)
            .categoryName("부모없는 자식카테고리")
            .build();

        when(categoryInfoMockRepository.findById(1)).thenReturn(Optional.empty());

        // act
        var result = injectMockCategoryInfoService.createCategoryInfo(createCommand);

        // assert
        assertEquals(CategoryInfoValidationEnum.CATEGORY_IS_NOT_EXISTS, result.getCategoryInfoValidationEnum());
    }

    @Test
    public void 동일한_이름의_카테고리가_존재하는_경우에_대한_신규_카테고리_등록_시_올바른_리턴값을_확인한다() {
        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryInfoSeq(10)
            .parentSeq(0)
            .categoryDepth(1)
            .categoryName("이미_존재하는_카테고리")
            .build();

        when(categoryInfoMockRepository.findByCategoryName("이미_존재하는_카테고리")).thenReturn(Optional.of(CategoryInfo.builder()
            .isDelete(false)
            .categoryName("이미_존재하는_카테고리").build()));

        // act
        var result = injectMockCategoryInfoService.createCategoryInfo(createCommand);

        // assert
        assertEquals(CategoryInfoValidationEnum.CATEGORY_NAME_IS_DUPLICATED, result.getCategoryInfoValidationEnum());
    }
}
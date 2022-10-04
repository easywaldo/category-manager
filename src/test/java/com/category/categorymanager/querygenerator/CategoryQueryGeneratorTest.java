package com.category.categorymanager.querygenerator;

import com.category.categorymanager.category.command.QueryCategoryInfoCommand;
import com.category.categorymanager.category.dto.CategoryInfoDto;
import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CategoryQueryGeneratorTest {

    @Autowired
    public CategoryQueryGenerator queryGenerator;

    @Autowired
    public CategoryInfoRepository categoryInfoRepository;

    @BeforeEach
    public void setUp() {
        categoryInfoRepository.deleteAll();
        categoryInfoRepository.save(CategoryInfo.builder()
            .parentSeq(0)
            .categoryInfoSeq(1)
            .categoryDepth(1)
            .categoryName("Women")
            .isDelete(false)
            .build());
        categoryInfoRepository.save(CategoryInfo.builder()
            .parentSeq(1)
            .categoryInfoSeq(2)
            .categoryDepth(2)
            .categoryName("Apparel")
            .isDelete(false)
            .build());
        categoryInfoRepository.save(CategoryInfo.builder()
            .parentSeq(2)
            .categoryInfoSeq(3)
            .categoryDepth(3)
            .categoryName("Outer")
            .isDelete(false)
            .build());
    }

    @Test
    public void 카테고리_데이터가_존재하는_경우_쿼리제너레이터가_카테고리_데이터를_리턴한다() {

        // arrange
        QueryCategoryInfoCommand command = QueryCategoryInfoCommand.builder()
            .build();
        var expectedCategoryList = List.of("Women", "Apparel", "Outer");

        // act
        var result = queryGenerator.queryCategoryInfoList(command);

        // assert
        assertNotNull(result);
        assertEquals(expectedCategoryList.size(), result.size());
        assertEquals(expectedCategoryList,
            result.stream().map(CategoryInfoDto::getCategoryName).collect(Collectors.toList()));
    }

    @Test
    public void 카테고리_query_dsl_support_test() {
        List<CategoryInfo> categoryInfoList = categoryInfoRepository.findByName("Women");

        for(CategoryInfo c : categoryInfoList) {
            System.out.println(c.getCategoryInfoSeq());
        }
    }

}
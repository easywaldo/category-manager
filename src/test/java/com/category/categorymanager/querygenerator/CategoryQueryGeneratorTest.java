package com.category.categorymanager.querygenerator;

import com.category.categorymanager.category.command.QueryCategoryInfoCommand;
import com.category.categorymanager.category.dto.CategoryInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CategoryQueryGeneratorTest {

    @Autowired
    public CategoryQueryGenerator queryGenerator;

    @Autowired
    public EntityManager entityManager;

    @Test
    public void 카테고리_데이터가_존재하는_경우_쿼리제너레이터가_카테고리_데이터를_리턴한다() {

        // arrange
        QueryCategoryInfoCommand command = QueryCategoryInfoCommand.builder()
            .build();

        // act
        var result = queryGenerator.selectCategoryInfo(command);

        // assert
        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertEquals(result.stream().map(CategoryInfoDto::getCategoryName).collect(Collectors.toList()),
            List.of("Women", "Apparel", "Outer"));
    }

}
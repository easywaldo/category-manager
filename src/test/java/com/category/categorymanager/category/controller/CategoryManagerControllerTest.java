package com.category.categorymanager.category.controller;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.dto.CategoryInfoDto;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryManagerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    CategoryInfoRepository categoryInfoRepository;

    @Test
    public void 신규_카테고리_등록_통합_테스트() {

        // arrange
        CreateCategoryInfoCommand createCommand = CreateCategoryInfoCommand.builder()
            .categoryDepth(1)
            .parentSeq(0)
            .categoryName("MEN")
            .build();

        // act
        this.webTestClient.post().uri("/category/register-category")
            .bodyValue(createCommand)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(CreateCategoryInfoCommand.class)
            .value(category -> {
                assertThat(category.getCategoryName()).isNotNull();
                assertThat(category.getCategoryInfoSeq()).isEqualTo(4);
                assertThat(category.getIsDelete()).isEqualTo(false);
            });

        // assert
        var result = this.categoryInfoRepository.findById(4).get();
        assertEquals("MEN", result.getCategoryName());
        assertEquals(false, result.getIsDelete());
        assertEquals(4, this.categoryInfoRepository.count());
    }

    @Test
    public void 카테고리_조회_통합_테스트() {

        // assert
        var result = this.webTestClient.get().uri(String.format("/category/%s", 1))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(CategoryInfoDto.class).hasSize(2);

    }

}
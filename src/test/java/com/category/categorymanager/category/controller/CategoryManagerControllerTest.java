package com.category.categorymanager.category.controller;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.controller.response.CategoryInfoValidationEnum;
import com.category.categorymanager.category.controller.response.CreateCategoryInfoResponse;
import com.category.categorymanager.category.dto.CategoryInfoDto;
import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryManagerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    CategoryInfoRepository categoryInfoRepository;

    @BeforeEach
    public void setUp() {
        categoryInfoRepository.deleteAll();
        categoryInfoRepository.save(CategoryInfo.builder()
            .parentSeq(0)
            .categoryDepth(1)
            .categoryName("Women")
            .isDelete(false)
            .build());
        categoryInfoRepository.save(CategoryInfo.builder()
            .parentSeq(categoryInfoRepository.findAll().stream().filter(x -> x.getCategoryName().equals("Women")).findFirst().get().getCategoryInfoSeq())
            .categoryDepth(2)
            .categoryName("Apparel")
            .isDelete(false)
            .build());
        categoryInfoRepository.save(CategoryInfo.builder()
            .parentSeq(categoryInfoRepository.findAll().stream().filter(x -> x.getCategoryName().equals("Apparel")).findFirst().get().getCategoryInfoSeq())
            .categoryDepth(3)
            .categoryName("Outer")
            .isDelete(false)
            .build());
        categoryInfoRepository.save(CategoryInfo.builder()
            .parentSeq(categoryInfoRepository.findAll().stream().filter(x -> x.getCategoryName().equals("Apparel")).findFirst().get().getCategoryInfoSeq())
            .categoryDepth(3)
            .categoryName("Pants")
            .isDelete(false)
            .build());
    }

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
            .expectBody(CreateCategoryInfoResponse.class)
            .value(commandResult -> {
                assertThat(commandResult.getCategoryInfoValidationEnum().equals(CategoryInfoValidationEnum.REGISTERED));
                assertThat(commandResult.getRegisteredCategoryInfoSeq() > 0);
            });

        // assert
        var result = this.categoryInfoRepository.findAll();
        assertEquals(1, result.stream().filter(x -> x.getCategoryName().equals("MEN")).count());
        assertEquals(false, result.stream().filter(x -> x.getCategoryName().equals("MEN")).findFirst().get().getIsDelete());
    }

    @Test
    public void 카테고리_조회_통합_테스트() {
        // assert
        var result = this.webTestClient.get()
            .uri(String.format("/category/%s",
                this.categoryInfoRepository.findAll()
                    .stream()
                    .filter(x -> x.getCategoryName().equals("Women")).findFirst().get().getCategoryInfoSeq()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(CategoryInfoDto.class).hasSize(2)
            .returnResult()
            .getResponseBody();
        assertThat(result.stream()
            .anyMatch(x -> List.of("Women", "Apparel", "Outer", "Pants")
            .contains(x.getCategoryName())));
    }

    @Test
    public void 카테고리_벌크_등록_시_중복된_이름이_존재하는_경우_예외처리를_리턴하는_테스트() {
        // assert
        var result = this.webTestClient.post()
            .uri("/category/bulkInsert")
            .bodyValue(List.of(
                CreateCategoryInfoCommand.builder()
                    .categoryName("Category Name")
                    .isDelete(false)
                    .parentSeq(0)
                    .categoryDepth(1)
                    .categoryInfoSeq(1)
                    .build(),
                CreateCategoryInfoCommand.builder()
                    .categoryName("Category Name")
                    .isDelete(false)
                    .parentSeq(0)
                    .categoryDepth(1)
                    .categoryInfoSeq(2)
                    .build(),
                CreateCategoryInfoCommand.builder()
                    .categoryName("Category Name")
                    .isDelete(false)
                    .parentSeq(0)
                    .categoryDepth(1)
                    .categoryInfoSeq(3)
                    .build()
            ))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError();
    }

}
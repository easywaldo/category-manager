package com.category.categorymanager;

import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CategoryManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CategoryManagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(
        CategoryInfoRepository categoryInfoRepository) {

        return (args) -> {
            categoryInfoRepository.deleteAll();
            categoryInfoRepository.save(CategoryInfo.builder()
                .categoryDepth(1)
                .parentSeq(0)
                .categoryInfoSeq(1)
                .categoryName("Women")
                .isDelete(false)
                .build());
            categoryInfoRepository.save(CategoryInfo.builder()
                .categoryDepth(2)
                .parentSeq(1)
                .categoryInfoSeq(2)
                .categoryName("Apparel")
                .isDelete(false)
                .build());
            categoryInfoRepository.save(CategoryInfo.builder()
                .categoryDepth(3)
                .parentSeq(2)
                .categoryInfoSeq(3)
                .categoryName("Outer")
                .isDelete(false)
                .build());
            categoryInfoRepository.save(CategoryInfo.builder()
                .categoryDepth(3)
                .parentSeq(2)
                .categoryInfoSeq(4)
                .categoryName("Pants")
                .isDelete(false)
                .build());
            categoryInfoRepository.save(CategoryInfo.builder()
                .categoryDepth(1)
                .parentSeq(0)
                .categoryInfoSeq(5)
                .categoryName("Men")
                .isDelete(false)
                .build());

        };
    }

}

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
            categoryInfoRepository.save(CategoryInfo.builder()
                .parentSeq(2)
                .categoryInfoSeq(4)
                .categoryDepth(3)
                .categoryName("Pants")
                .isDelete(false)
                .build());
        };
    }

}

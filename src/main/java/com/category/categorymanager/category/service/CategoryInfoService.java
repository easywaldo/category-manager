package com.category.categorymanager.category.service;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.DeleteCategoryInfoCommand;
import com.category.categorymanager.category.command.QueryCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.dto.CategoryInfoDto;
import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import com.category.categorymanager.querygenerator.CategoryQueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryInfoService {
    private final CategoryInfoRepository categoryInfoRepository;
    private final CategoryQueryGenerator categoryQueryGenerator;

    @Autowired
    public CategoryInfoService(CategoryInfoRepository categoryInfoRepository,
                               CategoryQueryGenerator categoryQueryGenerator) {
        this.categoryInfoRepository = categoryInfoRepository;
        this.categoryQueryGenerator = categoryQueryGenerator;
    }

    @Transactional
    public Integer createCategoryInfo(CreateCategoryInfoCommand createCommand) {
        return this.categoryInfoRepository.save(createCommand.toEntity()).getCategoryInfoSeq();
    }

    @Transactional
    public void bulkInsertCategoryInfo(List<CreateCategoryInfoCommand> bulkCommand) {
        this.categoryInfoRepository.saveAll(
            bulkCommand.stream().map(
                CreateCategoryInfoCommand::toEntity).collect(Collectors.toList())
        );
    }

    @Transactional
    public CategoryInfo updateCategoryInfo(UpdateCategoryInfoCommand updateCommand) {
        var targetCategory = this.categoryInfoRepository.findById(
            updateCommand.getCategoryInfoSeq());

        return targetCategory.orElseThrow(() -> new IllegalStateException("not exists category"))
            .updateCategory(updateCommand);
    }

    @Transactional
    public void deleteCategoryInfo(DeleteCategoryInfoCommand deleteCommand) {
        var targetCategory = this.categoryQueryGenerator.selectCategoryInfo(
            QueryCategoryInfoCommand.builder()
                .categoryParentSeq(deleteCommand.getCategoryInfoSeq())
                .build());

        this.categoryInfoRepository.deleteAllByIdInBatch(
            targetCategory.stream()
                .map(CategoryInfoDto::getCategoryInfoSeq)
                .collect(Collectors.toList()));
    }
}

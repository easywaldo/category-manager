package com.category.categorymanager.category.service;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
import com.category.categorymanager.category.command.UpdateCategoryInfoCommand;
import com.category.categorymanager.category.entity.CategoryInfo;
import com.category.categorymanager.category.repository.CategoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryInfoService {
    private final CategoryInfoRepository categoryInfoRepository;

    @Autowired
    public CategoryInfoService(CategoryInfoRepository categoryInfoRepository) {
        this.categoryInfoRepository = categoryInfoRepository;
    }

    @Transactional
    public Integer createCategoryInfo(CreateCategoryInfoCommand createCommand) {
        return this.categoryInfoRepository.save(createCommand.toEntity()).getCategoryInfoSeq();
    }

    @Transactional
    public CategoryInfo updateCategoryInfo(UpdateCategoryInfoCommand updateCommand) {
        var targetCategory = this.categoryInfoRepository.findById(
            updateCommand.getCategoryInfoSeq());

        return targetCategory.orElseThrow(() -> new IllegalStateException("not exists category"))
            .updateCategory(updateCommand);
    }
}

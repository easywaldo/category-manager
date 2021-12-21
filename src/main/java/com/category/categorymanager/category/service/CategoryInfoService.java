package com.category.categorymanager.category.service;

import com.category.categorymanager.category.command.CreateCategoryInfoCommand;
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
    public void createCategoryInfo(CreateCategoryInfoCommand createCommand) {
        this.categoryInfoRepository.save(createCommand.toEntity());
    }
}

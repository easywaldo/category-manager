package com.category.categorymanager.category.repository;

import com.category.categorymanager.category.entity.CategoryInfo;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryInfo> findByName(String name);
}

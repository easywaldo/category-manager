package com.category.categorymanager.category.repository;

import com.category.categorymanager.category.entity.CategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Integer> {
}

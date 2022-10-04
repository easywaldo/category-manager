package com.category.categorymanager.category.repository;

import com.category.categorymanager.category.entity.CategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Integer>, CategoryRepositoryCustom {
    List<CategoryInfo> findByParentSeqAndCategoryDepth(Integer parentSeq, Integer categoryDepth);
    List<CategoryInfo> findByParentSeqInAndCategoryDepth(List<Integer> parentSeqList, Integer categoryDepth);
    Optional<CategoryInfo> findByCategoryName(String categoryName);
}

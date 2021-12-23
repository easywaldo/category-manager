package com.category.categorymanager.category.repository;

import com.category.categorymanager.category.entity.CategoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Integer> {
    List<CategoryInfo> findByParentSeqAndCategoryDepth(Integer parentSeq, Integer categoryDepth);
    List<CategoryInfo> findByParentSeqInAndCategoryDepth(List<Integer> parentSeqList, Integer categoryDepth);
}

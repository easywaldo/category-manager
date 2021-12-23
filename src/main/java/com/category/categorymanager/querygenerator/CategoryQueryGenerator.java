package com.category.categorymanager.querygenerator;

import com.category.categorymanager.category.command.QueryCategoryInfoCommand;
import com.category.categorymanager.category.dto.CategoryInfoDto;
import com.category.categorymanager.category.entity.QCategoryInfo;
import com.google.common.base.Strings;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.category.categorymanager.category.entity.QCategoryInfo.categoryInfo;

@Repository(value = "category")
public class CategoryQueryGenerator {
    @PersistenceContext
    protected EntityManager entityManager;

    protected JPAQueryFactory jpaQueryFactory;

    public CategoryQueryGenerator(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public List<CategoryInfoDto> selectCategoryInfo(Integer categoryInfoSeq) {
        QCategoryInfo categoryInfoDepth2 = new QCategoryInfo("qci2");
        QCategoryInfo categoryInfoDepth3 = new QCategoryInfo("qci3");
        QCategoryInfo categoryInfoTable = new QCategoryInfo("categoryInfoTable");

        SQLTemplates templates = H2Templates.builder().build();
        JPASQLQuery<?> mainQuery = new JPASQLQuery<Void>(this.entityManager, templates);

        var mediumCategoryQuery = SQLExpressions
            .select(
                categoryInfo.categoryInfoSeq.as("categoryInfoSeq"),
                categoryInfoDepth2.categoryInfoSeq.as("categoryInfoSeqDepth2"),
                categoryInfo.categoryName.as("categoryName"),
                categoryInfoDepth2.categoryName.as("depth2CategoryName")
            )
            .from(categoryInfo)
            .leftJoin(categoryInfoDepth2).on(categoryInfoDepth2.parentSeq.eq(categoryInfo.categoryInfoSeq))
            .where(categoryInfo.categoryInfoSeq.eq(categoryInfoSeq));

        return mainQuery.from(mediumCategoryQuery, Expressions.stringPath("mediumCategoryView"))
            .innerJoin(categoryInfoTable).on(
                categoryInfoTable.categoryInfoSeq.eq(CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeq")))
            .leftJoin(categoryInfoDepth3).on(categoryInfoDepth3.parentSeq.eq(CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeqDepth2")))
            .select(Projections.fields(CategoryInfoDto.class,
                CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeq").as("categoryInfoSeq"),
                CustomBeanPath.getIntegerPath("mediumCategoryView", "categoryInfoSeqDepth2").as("depth2CategoryInfoSeq"),
                categoryInfoDepth3.categoryInfoSeq.as("depth3CategoryInfoSeq"),
                CustomBeanPath.getStringPath("mediumCategoryView", "categoryName").as("categoryName"),
                CustomBeanPath.getStringPath("mediumCategoryView", "depth2CategoryName").as("depth2CategoryName"),
                categoryInfoDepth3.categoryName.as("depth3CategoryName")))
            .fetch();
    }

    public List<CategoryInfoDto> selectCategoryInfoList(QueryCategoryInfoCommand command) {
        QCategoryInfo categoryInfoDepth2 = new QCategoryInfo("qci2");
        QCategoryInfo categoryInfoDepth3 = new QCategoryInfo("qci3");

        BooleanBuilder whereClause = new BooleanBuilder();
        if (!Strings.isNullOrEmpty(command.getCategoryName())) {
            whereClause.or(categoryInfo.categoryName.like("%" + command.getCategoryName() + "%"));
            whereClause.or(categoryInfoDepth2.categoryName.like("%" + command.getCategoryName() + "%"));
            whereClause.or(categoryInfoDepth3.categoryName.like("%" + command.getCategoryName() + "%"));
        }

        if (command.getCategoryParentSeq() > 0) {
            whereClause.or(categoryInfo.parentSeq.eq(command.getCategoryParentSeq()));
            whereClause.or(categoryInfoDepth2.parentSeq.eq(command.getCategoryParentSeq()));
            whereClause.or(categoryInfoDepth3.parentSeq.eq(command.getCategoryParentSeq()));
        }

        return this.jpaQueryFactory.from(categoryInfo)
            .leftJoin(categoryInfoDepth2).on(categoryInfoDepth2.parentSeq.eq(categoryInfo.categoryInfoSeq))
            .leftJoin(categoryInfoDepth3).on(categoryInfoDepth3.parentSeq.eq(categoryInfo.categoryInfoSeq))
            .where(whereClause)
            .select(Projections.fields(CategoryInfoDto.class,
                categoryInfo.categoryInfoSeq,
                categoryInfo.categoryName,
                categoryInfoDepth2.categoryInfoSeq.as("depth2CategoryInfoSeq"),
                categoryInfoDepth2.categoryName.as("depth2CategoryName"),
                categoryInfoDepth2.parentSeq.as("depth2ParentSeq"),
                categoryInfoDepth3.categoryInfoSeq.as("depth3CategoryInfoSeq"),
                categoryInfoDepth3.categoryName.as("depth3CategoryName"),
                categoryInfoDepth3.parentSeq.as("depth3ParentSeq")))
            .fetch();
    }

}

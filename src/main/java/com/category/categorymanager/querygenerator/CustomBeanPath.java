package com.category.categorymanager.querygenerator;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Nullable;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class CustomBeanPath extends EntityPathBase<Object> {
    public CustomBeanPath(String variable) {
        super(CustomBeanPath.class, forVariable(variable));
    }

    public CustomBeanPath(Class<?> type, String variable) {
        super(type, variable);
    }

    public CustomBeanPath(Class<?> type, PathMetadata metadata) {
        super(type, metadata);
    }

    public StringPath getStringPath(String property) {
        return createString(property);
    }

    public CustomBeanPath(Class<?> type, PathMetadata metadata, @Nullable PathInits inits) {
        super(type, metadata, inits);
    }

    public static StringPath getStringPath(String path, String property) {
        return new CustomBeanPath(String.class, path).getStringPath(property);
    }

    public static NumberPath<Integer> getIntegerPath(String path, String property) {
        return new CustomBeanPath(path).createNumber(property, Integer.class);
    }
}

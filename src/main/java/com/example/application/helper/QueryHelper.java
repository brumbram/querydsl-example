package com.example.application.helper;

import com.example.application.model.SortField;
import com.example.application.exception.UnsupportedInputException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QueryHelper {

    /**
     * Helper method to {@code OrderSpecifier} using sort field input and select path expressions
     * @param sortExpression
     * @param selectExpression
     * @return
     */
    public static List<OrderSpecifier> buildOrderSpecifiers(List<SortField> sortExpression, Map<String, Path> selectExpression) {

        List<OrderSpecifier> orderSpecifiers = new LinkedList<>();
        if (sortExpression != null) {
            for (SortField sortField: sortExpression) {
                Path fieldNamePath = getFieldPath(sortField.getFieldName(), selectExpression);
                Order order = parseToOrder(sortField.getSortOrder());
                orderSpecifiers.add(new OrderSpecifier(order, fieldNamePath));
            }
        }
        return orderSpecifiers;
    }

    /**
     * Helper method to fetch the stored {@code Path} expression for a given fieldName
     * @param fieldName
     * @param selectExpression
     * @return
     */
    private static Path getFieldPath(String fieldName, Map<String, Path> selectExpression) {

        Path fieldNamePath = selectExpression.get(fieldName);
        if (fieldNamePath == null) {
            throw new UnsupportedInputException(String.format("Unsupported sort field name -> %s", fieldName));
        }
        return  fieldNamePath;
    }

    /**
     * Helper method to convert a {@code String} representation of order by strategy to a {@code Order} type
     * @param sortOrder
     * @return
     */
    private static Order parseToOrder(String sortOrder) {

        Optional<Order> order = Arrays.stream(Order.class.getEnumConstants()).filter(e -> e.name().equalsIgnoreCase(sortOrder)).findFirst();
        if (order.isEmpty()) {
            throw new UnsupportedInputException(String.format("Unsupported sort order -> %s", sortOrder));
        }
        return order.get();
    }
}

package com.pizzara.data;

import com.pizzara.data.annotations.Ignore;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericDAO<T> {
    private final Map<String, String> fieldToColumnMapping;
    protected Connection connection;

    public GenericDAO() {
        this.connection = DatabaseConnector.getConnection();
        this.fieldToColumnMapping = createFieldToColumnMapping();
    }

    protected T getById(int id, String tableName, Class<T> entityClass) {
        T entity = null;
        String sql = "SELECT * FROM " + tableName + " WHERE " + getPrimaryKeyColumnName(entityClass) + " = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                entity = createEntityFromResultSet(resultSet, entityClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    protected void insertEntity(String tableName, T entity) {
        String sql = generateInsertQuery(tableName, entity);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setEntityParametersInPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateEntity(String tableName, T entity) {
        String sql = generateUpdateQuery(tableName, entity);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setEntityParametersInPreparedStatement(entity, preparedStatement);
            Field primaryKeyField = getPrimaryKeyField((Class<T>) entity.getClass());
            primaryKeyField.setAccessible(true);
            preparedStatement.setInt(preparedStatement.getParameterMetaData().getParameterCount(), (int) primaryKeyField.get(entity));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void deleteEntity(String tableName, T entity) {
        Field primaryKeyField = getPrimaryKeyField((Class<T>) entity.getClass());
        primaryKeyField.setAccessible(true);
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + getColumnName(primaryKeyField) + " = " + primaryKeyField.get(entity))) {
            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public List<T> getAllEntities(String tableName, Class<T> entityClass) {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                T entity = createEntityFromResultSet(resultSet, entityClass);
                entities.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    private boolean isFieldIgnored(Field field) {
        return field.isAnnotationPresent(Ignore.class);
    }

    private T createEntityFromResultSet(ResultSet resultSet, Class<T> entityClass) {
        T entity = null;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                // Ignoriere Felder mit IgnoreInDatabase-Annotation
                if (isFieldIgnored(field)) {
                    continue;
                }

                String fieldName = getColumnName(field);
                Object value = resultSet.getObject(fieldName);

                if (field.getType() == double.class || field.getType() == Double.class) {
                    value = ((BigDecimal) value).doubleValue();
                }

                field.set(entity, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    private String getPrimaryKeyColumnName(Class<T> entityClass) {
        Field primaryKeyField = getPrimaryKeyField(entityClass);
        return getColumnName(primaryKeyField);
    }

    private Field getPrimaryKeyField(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return fields[0]; // Default to the first field if no primary key annotation is found
    }

    private String generateInsertQuery(String tableName, T entity) {
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            query.append(getColumnName(field)).append(", ");
        }
        query.setLength(query.length() - 2); // Remove the last comma and space
        query.append(") VALUES (");
        for (Field ignored : fields) {
            query.append("?, ");
        }
        query.setLength(query.length() - 2); // Remove the last comma and space
        query.append(")");
        return query.toString();
    }

    private String generateUpdateQuery(String tableName, T entity) {
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            query.append(getColumnName(field)).append("=?, ");
        }
        query.setLength(query.length() - 2); // Remove the last comma and space
        query.append(" WHERE ").append(getPrimaryKeyColumnName((Class<T>) entity.getClass())).append("=?");
        return query.toString();
    }

    private void setEntityParametersInPreparedStatement(T entity, PreparedStatement preparedStatement) throws SQLException, IllegalAccessException {
        Field[] fields = entity.getClass().getDeclaredFields();
        int parameterIndex = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(entity);
            preparedStatement.setObject(parameterIndex, value);
            parameterIndex++;
        }
    }

    private String getColumnName(Field field) {
        String fieldName = field.getName();
        return fieldToColumnMapping.getOrDefault(fieldName, fieldName);
    }

    protected Map<String, String> createFieldToColumnMapping() {
        return new HashMap<>();
    }
}
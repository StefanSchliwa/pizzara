package data;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T> {
    protected Connection connection;

    public GenericDAO() {
        this.connection = DatabaseConnector.getConnection();
    }

    public T getById(int id, String tableName, Class<T> entityClass) {
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

    public void insertEntity(String tableName, T entity) {
        String sql = generateInsertQuery(tableName, entity);
        System.out.println(sql);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setEntityParametersInPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEntity(String tableName, T entity) {
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

    public void deleteEntity(String tableName, T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + fields[0].getName() + " = " + fields[0].get(entity))) {
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

    private T createEntityFromResultSet(ResultSet resultSet, Class<T> entityClass) throws SQLException {
        T entity = null;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                field.set(entity, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    private String getPrimaryKeyColumnName(Class<T> entityClass) {
        Field primaryKeyField = getPrimaryKeyField(entityClass);
        return primaryKeyField.getName();
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
            query.append(field.getName()).append(", ");
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
            query.append(field.getName()).append("=?, ");
        }
        query.setLength(query.length() - 2); // Remove the last comma and space
        query.append(" WHERE ").append(fields[0].getName()).append("=?");
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
}
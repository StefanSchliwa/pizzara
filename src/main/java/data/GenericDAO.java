package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class GenericDAO<T> {
    protected Connection connection;

    public GenericDAO() {
        this.connection = DatabaseConnector.getConnection();
    }

    public abstract T getById(int id);

    public abstract void insert(T entity);

    public abstract void update(T entity);

    public abstract void delete(int id);

    protected void closeResources(ResultSet resultSet, PreparedStatement preparedStatement) {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
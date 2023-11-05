package data;

public class SqlQueries {
    public static final String SELECT_INGREDIENT_BY_ID = "SELECT * FROM ingredient WHERE ingredient_id = ?";
    public static final String INSERT_INGREDIENT = "INSERT INTO ingredient (name, typ) VALUES (?, ?)";
    public static final String UPDATE_INGREDIENT = "UPDATE ingredient SET name=?, typ=? WHERE ingredient_id=?";
    public static final String DELETE_INGREDIENT = "DELETE FROM ingredient WHERE ingredient_id=?";
    public static final String SELECT_ALL_INGREDIENTS = "SELECT * FROM ingredient";
}
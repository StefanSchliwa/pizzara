import gui.IngredientForm;

public class AppLauncher {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new IngredientForm());
    }
}
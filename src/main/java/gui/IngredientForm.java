package gui;

import model.Ingredient;
import logic.IngredientService;

import javax.swing.*;
import java.util.List;

public class IngredientForm {
    private IngredientService ingredientService;
    private JFrame frame;
    private JList<Ingredient> ingredientList;
    private DefaultListModel<Ingredient> ingredientListModel;

    public IngredientForm() {
        this.ingredientService = new IngredientService();
        this.frame = new JFrame("Ingredient Management");
        this.ingredientListModel = new DefaultListModel<>();
        this.ingredientList = new JList<>(ingredientListModel);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshIngredientList());

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> createIngredient());

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateIngredient());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteIngredient());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(ingredientList);
        frame.add(refreshButton);
        frame.add(createButton);
        frame.add(updateButton);
        frame.add(deleteButton);

        frame.pack();
        frame.setVisible(true);
    }

    private void refreshIngredientList() {
        ingredientListModel.clear();
        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        for (Ingredient ingredient : ingredients) {
            ingredientListModel.addElement(ingredient);
        }
    }

    private void createIngredient() {
        String name = JOptionPane.showInputDialog(frame, "Enter ingredient name:");
        String typ = JOptionPane.showInputDialog(frame, "Enter ingredient type:");
        Ingredient newIngredient = new Ingredient();
        newIngredient.setName(name);
        newIngredient.setTyp(typ);
        ingredientService.createIngredient(newIngredient);
        refreshIngredientList();
    }

    private void updateIngredient() {
        Ingredient selectedIngredient = ingredientList.getSelectedValue();
        if (selectedIngredient != null) {
            String newName = JOptionPane.showInputDialog(frame, "Enter new ingredient name:");
            String newTyp = JOptionPane.showInputDialog(frame, "Enter new ingredient type:");
            selectedIngredient.setName(newName);
            selectedIngredient.setTyp(newTyp);
            ingredientService.updateIngredient(selectedIngredient);
            refreshIngredientList();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an ingredient to update.");
        }
    }

    private void deleteIngredient() {
        Ingredient selectedIngredient = ingredientList.getSelectedValue();
        if (selectedIngredient != null) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this ingredient?");
            if (confirm == JOptionPane.YES_OPTION) {
                ingredientService.deleteIngredient(selectedIngredient.getIngredientId());
                refreshIngredientList();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an ingredient to delete.");
        }
    }
}

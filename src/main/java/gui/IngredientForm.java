package gui;

import model.Ingredient;
import logic.IngredientService;

import javax.swing.*;
import java.awt.*;
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

        JButton readButton = new JButton("Read");
        readButton.addActionListener(e -> readIngredientList());

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> createIngredient());

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateIngredient());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteIngredient());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(readButton);
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(ingredientList, gbc);

        gbc.gridy = 1;
        frame.add(buttonPanel, gbc);

        frame.setMinimumSize(new Dimension(400, 200));

        frame.pack();
        frame.setVisible(true);
    }

    private void readIngredientList() {
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
        readIngredientList();
    }

    private void updateIngredient() {
        Ingredient selectedIngredient = ingredientList.getSelectedValue();
        if (selectedIngredient != null) {
            String newName = JOptionPane.showInputDialog(frame, "Enter new ingredient name:");
            String newTyp = JOptionPane.showInputDialog(frame, "Enter new ingredient type:");
            selectedIngredient.setName(newName);
            selectedIngredient.setTyp(newTyp);
            ingredientService.updateIngredient(selectedIngredient);
            readIngredientList();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an ingredient to update.");
        }
    }

    private void deleteIngredient() {
        Ingredient selectedIngredient = ingredientList.getSelectedValue();
        if (selectedIngredient != null) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this ingredient?");
            if (confirm == JOptionPane.YES_OPTION) {
                ingredientService.deleteIngredient(selectedIngredient);
                readIngredientList();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an ingredient to delete.");
        }
    }
}

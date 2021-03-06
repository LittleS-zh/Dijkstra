package view;

import controller.IngredientFormController;
import model.Ingredient;
import utils.UnitEnum;

import javax.swing.*;
import java.awt.*;

public class IngredientFormView extends View {
    private IngredientFormController c;
    private Ingredient m;

    /**
     * User interface for editing ingredient information.
     * @param c Controller for editing ingredient information.
     * @param m Model to record ingredient information in current form.
     */
    public IngredientFormView(IngredientFormController c, Ingredient m) {
        this.c = c;
        this.m = m;

        this.setTitle("Brew Day! - Ingredient Form"); // Set frame title
        this.setSize(800, 600); // Set frame size
        this.setLayout(new BorderLayout());

        JPanel pageTitle = new JPanel();
        pageTitle.setLayout(new BoxLayout(pageTitle, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Ingredient Form");
        title.setFont(new Font(title.getFont().getFontName(), title.getFont().getStyle(), 36));
        pageTitle.add(title);
        this.add(pageTitle, BorderLayout.PAGE_START);

        JPanel mainPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(groupLayout);// Vertically display
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        JLabel nameLabel = new JLabel("Name:");
        JLabel amountLabel = new JLabel("Amount:");
        JLabel unitLabel = new JLabel("Unit:");

        JTextField nameTextField = new JTextField();
        JTextField amountTextField = new JTextField();
        JComboBox<UnitEnum> unitSelect = new JComboBox<>();
        for (UnitEnum unit : UnitEnum.values()) {
            unitSelect.addItem(unit);
        }
        if (m.getID() > 0) {
            nameTextField.setText(m.getName());
            amountTextField.setText(Double.toString(m.getAmount()));
            for (UnitEnum unit : UnitEnum.values()) {
                if (!unit.equals(m.getUnit())) {
                    unitSelect.removeItem(unit);
                }
            }
        }

        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();

        hGroup.addGroup(groupLayout.createParallelGroup().addComponent(nameLabel)
                .addComponent(amountLabel).addComponent(unitLabel));
        hGroup.addGroup(groupLayout.createParallelGroup().addComponent(nameTextField)
                .addComponent(amountTextField).addComponent(unitSelect));
        groupLayout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();

        vGroup.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nameLabel).addComponent(nameTextField));
        vGroup.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(amountLabel).addComponent(amountTextField));
        vGroup.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(unitLabel).addComponent(unitSelect));

        groupLayout.setVerticalGroup(vGroup);

        this.add(mainPanel, BorderLayout.CENTER);

        JPanel pageEndButtonGroup = new JPanel();
        pageEndButtonGroup.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        pageEndButtonGroup.add(saveBtn);
        pageEndButtonGroup.add(cancelBtn);
        saveBtn.addActionListener(e -> {
            double amount = Double.parseDouble(amountTextField.getText());

            // Check if the amount of ingredient is valid.
            if (amount < 0) {
                JOptionPane.showMessageDialog(null, "Amount should be more than 0.");
                return;
            }else {
                int status = JOptionPane.showConfirmDialog(null,
                        "Are you sure to save current information?", "Warning",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                // Check the selection of user
                if (status == JOptionPane.YES_OPTION) {
                    if (nameTextField.getText().length() <= 0) {
                        JOptionPane.showMessageDialog(null, "Invalid input!");
                        return;
                    }
                    try {
                        c.saveIngredient(nameTextField.getText(), amount, unitSelect.getSelectedIndex());
                        c.cancel();
                        dispose();
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(null, "Amount is invalid!");
                    }

                }
            }

        });

        cancelBtn.addActionListener(e -> {
            c.cancel();
            dispose();
        });
        this.add(pageEndButtonGroup, BorderLayout.PAGE_END);
    }

    @Override
    public void update() {

    }
}

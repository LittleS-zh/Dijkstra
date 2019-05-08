package view;

import controller.BrewReciptController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrewReciptView extends View {
    private BrewReciptController c;
    public BrewReciptView(BrewReciptController c){
        this.c = c;
        this.setTitle("Brew Day! - Brew Recipe"); // set frame title
        this.setSize(800, 600); // set frame size
        this.setLayout(new BorderLayout()); // set borderlayout to the frame
        JPanel jp2 = new JPanel();
        jp2.setLayout(new BorderLayout());
        JPanel word = new JPanel();
        word.setLayout(new BorderLayout());
        JLabel title = new JLabel("Recipe C"); // Recipe Name
        //greeting.setHorizontalAlignment(JLabel.CENTER); // Vertical central the label in BorderLayout
        // Set Font size
        title.setFont(new Font(title.getFont().getFontName(), title.getFont().getStyle(), 36));
        word.add(title, BorderLayout.LINE_START);
        JLabel batchSize = new JLabel("Batch Size: 1000mL");
        word.add(batchSize, BorderLayout.LINE_END);
        jp2.add(word, BorderLayout.PAGE_START);
        this.add(jp2, BorderLayout.PAGE_START);

        String[] columnNames = {"Ingredient", "Unit", "Amount"};

        Object[][] data =
                {
                        {"Barley", "GRAM", "3.5"},
                        {"Yeast", "MILLILITER", "25"},
                };

        JTable table = new JTable(data, columnNames);
        this.add(table, BorderLayout.CENTER);
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Finish");
        footerPanel.add(saveBtn);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.finish();
                dispose();
            }
        });
        this.add(footerPanel, BorderLayout.PAGE_END);
    }
    @Override
    public void update() {

    }
}
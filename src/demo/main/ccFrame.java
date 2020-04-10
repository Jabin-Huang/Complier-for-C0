package demo.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ccFrame {
    private JPanel MainPanel;
    private JButton openButton;
    private JButton executeButton;
    private JTextField openPathField;
    private JTextArea inArea;
    private JTextArea outArea;

    public ccFrame() {
        openPathField.setEditable(false);

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Run run = new Run(inArea.getText());
                try {
                    outArea.append(run.work());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String filePath = File_op.open(openButton);
                    if(filePath != null){
                        openPathField.setText(filePath);
                        inArea.setText(File_op.fileToString(filePath));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("ccFrame");
        frame.setContentPane(new ccFrame().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}

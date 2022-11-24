package com.abc.foo.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.abc.thread.*;

public class FooUiMain {
    public static void main(String[] args) {
        ThreadTools.outln("Building UI...");
        JPanel contentPane = new JPanel(new GridLayout());
        contentPane.add(new FooUi());

        JFrame f = new JFrame("SwingInvokeLater");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setContentPane(contentPane);

        f.setSize(600, 450);
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}

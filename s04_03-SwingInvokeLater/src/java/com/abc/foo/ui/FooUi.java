package com.abc.foo.ui;

import java.awt.*;

import javax.swing.*;

import com.abc.foo.search.*;
import com.abc.thread.*;

public class FooUi extends JPanel {
    private final JTextField searchTF;
    private final JButton searchB;
    private final JTextField resultTF;

    public FooUi() {
        searchTF = new JTextField(10);
        searchB = new JButton("Search");
        resultTF = new JTextField(15);
        resultTF.setEditable(false);

        setLayout(new FlowLayout());
        add(searchTF);
        add(searchB);
        add(resultTF);

        searchB.addActionListener((e) -> initiateSearch());
    }

    @SuppressWarnings("unused")
    private void initiateSearch() {
        ThreadTools.outln("inside initiateSearch...");
        searchB.setEnabled(false);
        new Searcher(searchTF.getText().trim(), (results) -> setResults(results));
    }

    // can safely be called by any thread
    private void setResults(String results) {
        ThreadTools.outln("entering setResults with: %s", results);

        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> setResults(results));
            return;
        }

        resultTF.setText(results);
        searchB.setEnabled(true);
    }
}

package threadbook.ch09;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SimpleEvent extends Object {
	private static void print(String msg) {
		String name = Thread.currentThread().getName();
		System.out.println(name + ": " + msg);
	}

	public static void main(String[] args) {
		final JLabel label = new JLabel("--------");
		JButton button = new JButton("Click Here");

		JPanel panel = new JPanel(new FlowLayout());
		panel.add(button);
		panel.add(label);

		button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print("in actionPerformed()");
					label.setText("CLICKED!");
				}
			});

		JFrame f = new JFrame("SimpleEvent");
		f.setContentPane(panel);
		f.setSize(300, 100);
		f.setVisible(true);
	}
}

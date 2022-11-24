package com.abc.countdown;

import java.awt.*;
import java.awt.event.*;
import java.nio.channels.IllegalSelectorException;

import javax.swing.*;

import com.abc.thread.ThreadTools;

public class CountdownTimer extends JPanel {
    private MinuteSecondDisplay display;
    private JButton startB;
    
    
    public CountdownTimer() {
        display = new MinuteSecondDisplay();

        startB = new JButton("Start");
        startB.addActionListener((e)->kickOffCountdown());

        setLayout(new FlowLayout());
        add(startB);
        add(display);
    }

    private void kickOffCountdown()
    {
    	ThreadTools.outln("entering kickOffCountdown");
    	setStartButtonEnabled(false);
        new CountdownWorker(10, display, (msActual) -> {
        	ThreadTools.outln("Countdown Complete, %d ms", msActual);
        	setStartButtonEnabled(true);
        });
        ThreadTools.outln("leaving kickOffCountdown");
    }
    
    private void setStartButtonEnabled(boolean enabled) {
    	ThreadTools.outln("setStartButtonEnabled (%b)", enabled);
    	
    	if (!SwingUtilities.isEventDispatchThread()) {
    		SwingUtilities.invokeLater(() ->setStartButtonEnabled(enabled));
    		return;
		} 
    	ThreadTools.outln("about to call startB.setEnabled (%b)", enabled);
    	startB.setEnabled(enabled);
    }
    
    
    public static void main(String[] args) {
        JPanel p = new JPanel(new GridLayout(0, 1));
        p.add(new CountdownTimer());
        p.add(new CountdownTimer());
        p.add(new CountdownTimer());

        JFrame f = new JFrame("CountdownTimer Demo");
        f.setContentPane(p);
        f.pack();
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
}

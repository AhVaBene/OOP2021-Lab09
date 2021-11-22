package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AnotherConcurrentGUI extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_P = 0.2;
    private static final double HEIGHT_P = 0.1;
    private final JLabel display = new JLabel();
    private final JButton stop = new JButton("stop");
    
    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.width * WIDTH_P), (int) (screenSize.height * HEIGHT_P));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        final JButton up = new JButton("up");
        final JButton down = new JButton("down");
        panel.add(display);
        panel.add(stop);
        panel.add(down);
        panel.add(up);
        this.getContentPane().add(panel);
        this.setVisible(true);
        
        final Agent agent = new Agent();
        new Thread(agent).start();
        final Ending end = new Ending();
        new Thread(end).start();
        
        stop.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                // TODO Auto-generated method stub
                agent.stopCounting();
                stop.setEnabled(false);
                up.setEnabled(false);
                down.setEnabled(false);
            }
        });                   
        up.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                // TODO Auto-generated method stub
                agent.goUp();
            }
        });
        down.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                // TODO Auto-generated method stub
                agent.goDown();
            }
        });
    }
    private class Agent implements Runnable{

        private volatile boolean stop;
        private volatile int counter;
        private boolean flag = true;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while(!this.stop) {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            AnotherConcurrentGUI.this.display.setText(Integer.toString(counter));
                        }
                    });
                    if(flag) {
                        this.counter++;
                        
                    } else {
                        this.counter--;
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    /*
                     * This is just a stack trace print, in a real program there
                     * should be some logging and decent error reporting
                     */
                    ex.printStackTrace();
                }
            }
        }
        
        public void stopCounting() {
            this.stop = true;
        }
        
        public void goUp() {
            this.flag = true;
        }
        
        public void goDown() {
            this.flag = false;
        }
    }
    private class Ending implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(3_000);
                SwingUtilities.invokeAndWait(new Runnable() {
                    
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub)
                        AnotherConcurrentGUI.this.stop.doClick();
                        AnotherConcurrentGUI.this.display.setText("Tempo Scaduto " + AnotherConcurrentGUI.this.display.getText());
                    }
                });
            } catch (InvocationTargetException | InterruptedException ex) {
                /*
                 * This is just a stack trace print, in a real program there
                 * should be some logging and decent error reporting
                 */
                ex.printStackTrace();
            }
        }
        
    }
}

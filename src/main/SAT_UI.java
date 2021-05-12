package main;


// Java Program to create a simple JTextArea
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import solver.DPLL;

public class SAT_UI extends JFrame implements ActionListener {
  
    // JFrame
    static JFrame f;
  
    // JButton
    static JButton b;
  
    // label to display text
    static JLabel l;
  
    // text area
    static JTextArea jt;
    
    public static ArrayList<ArrayList<Integer>> parseDimacsInput(String input) {
    	ArrayList<ArrayList<Integer>> formset = new ArrayList<ArrayList<Integer>>();
        
        for (String line : input.split("\n")) 
        {
        	ArrayList<Integer> form = new ArrayList<Integer>();
        	
        	//if token is integer -> add to formula
        	for (String token : line.split("\\s+"))
        		if (token.matches("-?(0|[1-9]\\d*)"))
        			form.add(Integer.parseInt(token));
        		else
        			System.out.println("PROBLEM PARSING");
        	
        	if (form.size() > 0)
        		formset.add(form);
        }
        return formset;
    }
  
    // if the button is pressed
    public void actionPerformed(ActionEvent e)
    {
    	ArrayList<ArrayList<Integer>> formset;
        String s = e.getActionCommand();
        boolean results;
        
        if (s.equals("submit")) 
        {              
            formset = parseDimacsInput(jt.getText());
            //input to algorithm
            DPLL dpll_sat = new DPLL();
            //dpll_sat.
            results = dpll_sat.solve(formset);
            //dpll_sat.testCopy(formset);
            //print results
            l.setText(Boolean.toString(results));
        }
    }
    

    // main class
    public static void main(String[] args)
    {
        // create a new frame to store text field and button
        f = new JFrame("textfield");
  
        // create a label to display text
        l = new JLabel("nothing entered");
  
        // create a new button
        b = new JButton("submit");
  
        // create a object of the text class
        SAT_UI te = new SAT_UI();
  
        // addActionListener to button
        b.addActionListener(te);
  
        // create a text area, specifying the rows and columns
        jt = new JTextArea(10, 10);
  
        JPanel p = new JPanel();
  
        // add the text area and button to panel
        p.add(jt);
        p.add(b);
        p.add(l);
  
        f.add(p);
        // set the size of frame
        f.setSize(500, 500);
  
        f.show();
    }
  


}

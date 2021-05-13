package main;


// Java Program to create a simple JTextArea
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import solver.DPLL;

public class SAT_UI extends JFrame implements ActionListener {
	
	private static final int MAX_VARS = 200;
  
    // JFrame
    static JFrame f;
  
    // JButton
    static JButton b;
    static JButton all;
  
    // label to display text
    static JLabel l;
    static JLabel ta;
  
    // text area
    static JTextArea jt;
    
    static JComboBox t;
    
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
    	ArrayList<ArrayList<Integer>> formset, allsoln;
    	int num_vars;
        String s = e.getActionCommand();
        System.out.println(s);
        boolean results;
        
        if (s.equals("sat")) 
        {              
        	formset = parseDimacsInput(jt.getText());
            num_vars = Integer.parseInt((String) t.getSelectedItem());
            //input to algorithm
            DPLL dpll_sat = new DPLL();
            //dpll_sat.
            results = dpll_sat.solve(formset, num_vars);
            //dpll_sat.testCopy(formset);
            //print results
            l.setText(Boolean.toString(results));
        }
        if (s.equals("allsat"))
        {
        	System.out.println("Hello");
        	formset = parseDimacsInput(jt.getText());
            num_vars = Integer.parseInt((String) t.getSelectedItem());
            //input to algorithm
            DPLL dpll_sat = new DPLL();
            //dpll_sat.
            allsoln = dpll_sat.allSolve(formset, num_vars);
            
            System.out.println("Solutions!");
            for (int i = 0; i < allsoln.size(); i++)
            {
            	for (int j = 0; j < allsoln.get(i).size(); j++)
            		System.out.print(allsoln.get(i).get(j) + " ");
                System.out.println(" ");
            }

            //dpll_sat.testCopy(formset);
            //print results
            //l.setText(Boolean.toString(results));
        }
    }
    
    
    public void useAllSolve() {
    	
    }
    

    // main class
    public static void main(String[] args)
    {
    	//make combobox values
    	String[] vars = new String[MAX_VARS-1];
    	for (int i = 0; i < vars.length; i++)
    		vars[i] = String.valueOf(i+1);
    	
        // create a new frame to store text field and button
        f = new JFrame("textfield");
  
        // create a label to display text
        l = new JLabel("# Variables");
        t = new JComboBox(vars);
  
        // create a new button
        b = new JButton("sat");
        all = new JButton("allsat");
  
        // create a object of the text class
        SAT_UI te = new SAT_UI();
  
        // addActionListener to button
        b.addActionListener(te);
        all.addActionListener(te);
  
        // create a text area, specifying the rows and columns
        ta = new JLabel("CNF Set");
        jt = new JTextArea(10, 10);
  
        JPanel p = new JPanel();
  
        // add the text area and button to panel
        p.add(l);
        p.add(t);
        p.add(ta);
        p.add(jt);
        p.add(b);
        p.add(all);

  
        f.add(p);
        // set the size of frame
        f.setSize(500, 500);
  
        f.show();
    }
  


}

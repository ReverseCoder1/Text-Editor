import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class undo_redo extends JFrame implements ActionListener, ItemListener{
    static JFrame frame;
    static JCheckBox c1, c2;
    JButton b0, b1, b2, b3, b4, b5, b6, b7, b8;
    JTextArea a;
    JScrollPane p;
    JComboBox d;
    UndoManager m = new UndoManager();
    JLabel label1, label2, label3, label4, label5;
    JToolBar toolbar1 = new JToolBar();  // Toolbar creation for button
    JToolBar toolbar2 = new JToolBar();
    float size = 16.0f;  //variable for assign text size to textarea font
    String fontFamily[];
    Integer array[];
    Character keyvalue[] = {'`','~','/','\\','\'','?','"',' ','.','>',',','<',';',':','[','{',']','}','|','=','+','-','_',')','(','*','&','^','%','$','#','@','!'};
    static Stack<String> Undo = new Stack<String>();  //Strong elements for undo operation
    static Stack<String> Redo = new Stack<String>();  //Storing the element for redo operation
    public undo_redo() {
        a = new JTextArea(20, 55);
        a.setFont(a.getFont().deriveFont(size));
        JScrollPane scrollPane = new JScrollPane(a);
        p = new JScrollPane(a);
        m = new UndoManager();
        b0 = new JButton("Undo");
        b1 = new JButton("Redo");
        b2 = new JButton("Exit");
        b3 = new JButton("Color");
        b4 = new JButton("+");
        b5 = new JButton("-");
        b6 = new JButton("L");
        b7 = new JButton("R");
        b8 = new JButton("Dark theme");
        label1 = new JLabel("Size :");
        label2 = new JLabel("Style :");
        label3 = new JLabel();
        label3.setText(size+"pt");  //set value of font size to label3
        label4 = new JLabel("Font :");
        label5 = new JLabel("Align :");
        c1 = new JCheckBox("Bold");
        c2 = new JCheckBox("Italic");

        //create drop down and assign available all font family list
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontFamily = ge.getAvailableFontFamilyNames();
        array = new Integer[fontFamily.length];
        for(int i=1;i<=fontFamily.length;i++) {
            array[i-1] = i;
        }

        d = new JComboBox(array);
        ComboBoxRenderar renderar = new ComboBoxRenderar();
        d.setRenderer(renderar);
        d.setMaximumSize(d.getPreferredSize());  // fix the width of drop down box

        //for close system on close button(top right side) click
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //set style of label show in toolbar
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        label2.setFont(new Font("Arial", Font.BOLD, 14));
        label3.setFont(new Font("Arial", Font.PLAIN, 13));
        label3.setForeground(Color.RED);  //set font color to label red
        label4.setFont(new Font("Arial", Font.BOLD, 14));
        label5.setFont(new Font("Arial", Font.BOLD, 14));

        //set textarea property
        a.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        a.setBackground(new Color(255,255,255));
        a.setForeground(Color.BLACK);
        a.setCaretColor(Color.BLACK);

        //Use for set height & width of button
        Dimension bDim = new Dimension(60, 30);
        b0.setPreferredSize(bDim);
        b0.setMaximumSize(bDim);
        b1.setPreferredSize(bDim);
        b1.setMaximumSize(bDim);
        b2.setPreferredSize(bDim);
        b2.setMaximumSize(bDim);
        b3.setPreferredSize(bDim);
        b3.setMaximumSize(bDim);

        b8.setPreferredSize(new Dimension(80, 30));
        b8.setMaximumSize(new Dimension(80, 30));

        Dimension bDim1 = new Dimension(35, 30);
        b4.setPreferredSize(bDim1);
        b4.setMaximumSize(bDim1);
        b5.setPreferredSize(bDim1);
        b5.setMaximumSize(bDim1);
        b6.setPreferredSize(bDim1);
        b6.setMaximumSize(bDim1);
        b7.setPreferredSize(bDim1);
        b7.setMaximumSize(bDim1);

        //set action for button in toolbar
        b0.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
//                    m.undo();
                    UNDO(Undo,Redo);
                    a.setText(Undo.peek());  //set changed text to textarea
                } catch (Exception ex) {}
            }
        });
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
//                    m.redo();
                    REDO(Undo,Redo);
                    a.setText(Undo.peek());  //set changed text to textarea
                } catch (Exception ex) {}
            }
        });
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        b3.addActionListener(this);
        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                size = a.getFont().getSize() + 1.0f;
                a.setFont(a.getFont().deriveFont(size));
                label3.setText(size+"pt");
            }
        });
        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(size > 5) {
                    size = a.getFont().getSize() - 1.0f;
                    a.setFont(a.getFont().deriveFont(size));
                    label3.setText(size+"pt");
                }
            }
        });
        b6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                a.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            }
        });
        b7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                a.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            }
        });
        b8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(b8.getText()=="Dark theme") {
                    b8.setText("Light theme");
                    if(a.getForeground().getRed()==0 && a.getForeground().getBlue()==0 && a.getForeground().getGreen()==0) {
                        a.setCaretColor(Color.WHITE);
                        a.setForeground(Color.WHITE);
                    }
                    a.setBackground(new Color(0,0,0));
                    a.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                }else if(b8.getText()=="Light theme") {  //Check you want to move to light theme
                    b8.setText("Dark theme");
                    if(a.getForeground().getRed()==255 && a.getForeground().getBlue()==255 && a.getForeground().getGreen()==255) {
                        a.setCaretColor(Color.BLACK);
                        a.setForeground(Color.BLACK);
                    }
                    a.setBackground(new Color(255,255,255));
                    a.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                }
            }
        });

        //textarea action listener
        a.getDocument().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                m.addEdit(e.getEdit());
            }
        });

        //set key event for control action
        a.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                //This is for set ctrl+Z,Y,L,R,B,I,W
                if((e.getKeyCode()==KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK)!=0)) {
                    try {
//                        m.undo();
                        UNDO(Undo,Redo);
                        a.setText(Undo.peek());  //set changed text to textarea
                    } catch (Exception ex) {}
                }else if((e.getKeyCode()==KeyEvent.VK_Y) && ((e.getModifiers() & KeyEvent.CTRL_MASK)!=0)) {
                    try {
//                        m.redo();
                        REDO(Undo,Redo);
                        a.setText(Undo.peek());  //set changed text to textarea
                    } catch (Exception ex) {}
                }else if((e.getKeyCode()==KeyEvent.VK_B) && ((e.getModifiers() & KeyEvent.CTRL_MASK)!=0)) {
                    try {
                        if(c1.isSelected()==false) c1.setSelected(true);
                        else c1.setSelected(false);
                    } catch (Exception ex) {}
                }else if((e.getKeyCode()==KeyEvent.VK_I) && ((e.getModifiers() & KeyEvent.CTRL_MASK)!=0)) {
                    try {
                        if(c2.isSelected()==false) c2.setSelected(true);
                        else c2.setSelected(false);
                    } catch (Exception ex) {}
                }else if((e.getKeyCode()==KeyEvent.VK_R) && ((e.getModifiers() & KeyEvent.CTRL_MASK)!=0)) {
                    try {
                        a.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                    } catch (Exception ex) {}
                }else if((e.getKeyCode()==KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.CTRL_MASK)!=0)) {
                    try {
                        a.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    } catch (Exception ex) {}
                }else if((e.getKeyCode()==KeyEvent.VK_W) && ((e.getModifiers() & KeyEvent.CTRL_MASK)!=0)) {
                    try {
                        System.exit(0);
                    } catch (Exception ex) {}
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                //For on write letter, digit or special symbol undo stack get input otherwise not
                List<Character> l = new ArrayList<>(Arrays.asList(keyvalue));
                if(l.contains(e.getKeyChar()) || Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyCode()==KeyEvent.VK_BACK_SPACE || e.getKeyCode()== KeyEvent.VK_DELETE) {
                    WRITE(Undo,a.getText());
//            		System.out.println(e.getKeyChar());
                }
            }
        });

        //set all button & label to toolbar
        toolbar1.add(label4);
        toolbar1.add(Box.createRigidArea(new Dimension(10, 5)));
        toolbar1.add(d);
        toolbar1.add(Box.createRigidArea(new Dimension(10, 10)));
        toolbar1.add(label1);
        toolbar1.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar1.add(b4);
        toolbar1.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar1.add(b5);
        toolbar1.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar1.add(label3);
        toolbar1.add(Box.createRigidArea(new Dimension(10, 10)));
        toolbar1.add(label2);
        toolbar1.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar1.add(c1);
        toolbar1.add(c2);
        toolbar1.add(Box.createRigidArea(new Dimension(10, 10)));
        toolbar1.add(label5);
        toolbar1.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar1.add(b6);
        toolbar1.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar1.add(b7);

        toolbar2.add(b2);
        toolbar2.add(Box.createRigidArea(new Dimension(5, 5))); //dimension (horizontal,vertical)
        toolbar2.add(b0);
        toolbar2.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar2.add(b1);
        toolbar2.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar2.add(b3);
        toolbar2.add(Box.createRigidArea(new Dimension(5, 5)));
        toolbar2.add(b8);


//      add toolbar and text area to panel
        add(toolbar1, "North");
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(p, "Center");
        add(toolbar2, "South");

        addWindowListener(new FocusSetter(a));  //set primary focus to textarea

        setVisible(true);
        pack();
    }
    //Method for color palette action listener
    public void actionPerformed(ActionEvent e)
    {
        Color initialcolor = a.getForeground();
        JColorChooser color_box= new JColorChooser(a.getForeground());
        Color color=color_box.showDialog(frame,"Select a Color",initialcolor);

        a.setForeground(color);
        a.setCaretColor(color);
    }
    //method for check box item change listener
    public void itemStateChanged(ItemEvent e)
    {
        if(c1.isSelected() && c2.isSelected()) {
            a.setFont(a.getFont().deriveFont(Font.BOLD+Font.ITALIC, a.getFont().getSize()));
        }else if(c2.isSelected()) {
            a.setFont(a.getFont().deriveFont(Font.ITALIC, a.getFont().getSize()));
        }else if(c1.isSelected()) {
            a.setFont(a.getFont().deriveFont(Font.BOLD, a.getFont().getSize()));
        }else {
            a.setFont(a.getFont().deriveFont(Font.PLAIN, a.getFont().getSize()));
        }
    }
    //method for font family drop down
    public class ComboBoxRenderar extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            int offset = ((Integer)value).intValue() - 1 ;
            String name = fontFamily[offset];
            setText(name);
            setFont(new Font(name,Font.PLAIN,20));
            a.setFont(new Font(name, a.getFont().getStyle(), a.getFont().getSize()));
            return this;
        }
    }
    //methods for add item in stack while change in textarea
    static void WRITE(Stack<String> Undo, String s) {
        Undo.push(s);  //add new added item in stack
    }
    //method for undone your last performed task
    static void UNDO(Stack<String> Undo, Stack<String> Redo) {
        String s = (String)Undo.peek();   //get top element of Undo stack
        Undo.pop();        //Then pop the top element from Undo stack
        Redo.push(s);      //Now push the removed element to Redo stack so we can retrieve it from redone task
    }
    //methods for redone your task
    public void REDO(Stack<String> Undo, Stack<String> Redo) {
        String s = (String)Redo.peek();  //get top element of Redo stack
        Redo.pop();        //Then pop the top element from Redo stack
        Undo.push(s);      //Now push the removed element to Undo stack
    }
    //method for print element of stack for testing purpose
    static void PrintStack(Stack<String> s) {
        if (s.isEmpty())
            return;
        String x = s.peek();
        s.pop();
        PrintStack(s);
        System.out.println(x);
        s.push(x);
    }

    public static void main(String[] args) {
        undo_redo o = new undo_redo();
        c1.addItemListener(o);
        c2.addItemListener(o);
    }
}
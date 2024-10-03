import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.FontUIResource;


public class Calculator extends JFrame {
    private static final double BASE_RATE = 0.008; //Base rate for 5* per pull
    private static final int GUARANTEE = 80; //Hard guarantee for 5*
    private static final double FINAL_RATE = 0.018; //Final adjusted rate after guarantee factored
    //Final rate seems redundnat, but will do some cool comparison later with it maybe

    private void setUIFont(FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private JPanel inputPanel;
    private JComboBox<String> itemSelector;
    private JTextField desiredCopiesField;
    private JTextField currentCopiesField;
    private JTextField pullsField;

    public Calculator() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUIFont(new javax.swing.plaf.FontUIResource("Comic Sans MS", Font.PLAIN, 16));
        //Set global font size and type here

        setTitle("Wuthering Waves Pull Calculator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
    //Dropdown for selecting weapon banner vs character banner
    JPanel selectionPanel = new JPanel();
    itemSelector = new JComboBox<>(new String[]{"Limited Character", "Limited Weapon"});
    itemSelector.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateInputFields();
        }
    });
    selectionPanel.add(new JLabel("Select Type:"));
    selectionPanel.add(itemSelector);

    //Boxes to input relevant data like desired, current, and number of pulls
    inputPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    desiredCopiesField = new JTextField(10);
    currentCopiesField = new JTextField(10);
    pullsField = new JTextField(10);

    updateInputFields();

    //Calculate button
    JButton calculateButton = new JButton("Calculate Pulls");
    calculateButton.setPreferredSize(new Dimension(150, 30)); 
    calculateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            calculatePulls();
        }
    });

    //Panel to move button around
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(calculateButton);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Add space above the button

    //Add all components to the frame
    add(selectionPanel, BorderLayout.NORTH);
    add(inputPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
}

    private void updateInputFields() {
        inputPanel.removeAll();

        //Setup input fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        //Change label based on dropdown selection
        String selectedItem = (String) itemSelector.getSelectedItem();
        String desiredLabel = selectedItem.equals("Limited Character") ? "Desired Sequence:" : "Desired Rank:";

        //Fields for the data
        inputPanel.add(new JLabel(desiredLabel), gbc);
        gbc.gridx = 1;
        inputPanel.add(desiredCopiesField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Current Copies:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(currentCopiesField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Pulls:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(pullsField, gbc);

        //Refresh to update
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private void calculatePulls() {
        try {
            int desiredCopies = Integer.parseInt(desiredCopiesField.getText());
            int currentCopies = Integer.parseInt(currentCopiesField.getText());
            int totalPulls = Integer.parseInt(pullsField.getText());
            String selectedItem = (String) itemSelector.getSelectedItem();

            boolean guaranteedNext = selectedItem.equals("Limited Weapon");
            //Weapons are guaranteed, characters aren't

            double rate = BASE_RATE; //Current rate of 5 star, starts at base rate
            int success = 0; //Number of successful pulls
            int pity = 0; //Number of pulls since last 5 star

            for(int currentPull = 1; currentPull <= totalPulls; currentPull++){
                pity++; //Increment pity counter on pull
                //Edit rates based on pity
                if(pity >= 80){ //Final 80th pull is guaranteed 5*
                    rate = 1.0;
                }else if(pity >= 64){ //If at the pity level, increment 6% each time until success
                    rate += 0.06;
                }else{
                    rate = BASE_RATE; //If not at pity, reset to base rate
                }

                //Implement calculations for success

            }
            
            

        } catch (NumberFormatException e) { //Basic error message because you can't calculate with empty or 0
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}

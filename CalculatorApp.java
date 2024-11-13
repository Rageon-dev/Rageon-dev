import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;

public class CalculatorApp extends JFrame implements ActionListener {
    private JTextField display;
    private JTextArea outputWindow;
    private StringBuilder currentExpression;

    public CalculatorApp() {
        setTitle("Calculator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        display = new JTextField();
        currentExpression = new StringBuilder();

        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);


        outputWindow = new JTextArea();
        outputWindow.setEditable(false);
        outputWindow.setFont(new Font("Arial", Font.PLAIN, 16));
        outputWindow.setLineWrap(true);
        outputWindow.setWrapStyleWord(true);
        outputWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane scrollPane = new JScrollPane(outputWindow);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));


        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+",
            "History", "Clear History"
        };


        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(this);
            buttonPanel.add(button);
        }


        setLayout(new BorderLayout(10, 10));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);  
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();


        switch (command) {
            case "=":
                evaluateExpression();
                break;
            case "C":
                clearExpression();
                break;
            case "History":
                viewHistory();
                break;
            case "Clear History":
                clearHistory();
                break;
            default:
                appendToExpression(command);
                break;
        }
    }


    private void appendToExpression(String text) {
        currentExpression.append(text);
        display.setText(currentExpression.toString());
    }


    private void clearExpression() {
        currentExpression.setLength(0);
        display.setText("");
    }

  
    private void evaluateExpression() {
        try {
            double result = evaluate(currentExpression.toString());
            String resultString = currentExpression.toString() + " = " + result;
            outputWindow.append(resultString + "\n"); 
            display.setText(Double.toString(result));
            currentExpression.setLength(0); 
        } catch (Exception ex) {
            display.setText("Error");
            currentExpression.setLength(0);
        }
    }


    private double evaluate(String expression) throws Exception {

        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == ' ') {
                continue;
            }

            if (Character.isDigit(c)) {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                values.push(Double.parseDouble(number.toString()));
                i--; 
            }


            else if (c == '+' || c == '-' || c == '*' || c == '/') {

                while (!ops.isEmpty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOperator(ops.pop(), values.pop(), values.pop()));
                }

                ops.push(c);
            }
        }


        while (!ops.isEmpty()) {
            values.push(applyOperator(ops.pop(), values.pop(), values.pop()));
        }


        return values.pop();
    }


    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '+' || op2 == '-') {
            return false;
        }
        if (op1 == '*' || op1 == '/') {
            return true;
        }
        return false;
    }


    private double applyOperator(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return a / b;
            default:
                throw new UnsupportedOperationException("Invalid operator");
        }
    }


    private void viewHistory() {
        outputWindow.setCaretPosition(outputWindow.getDocument().getLength());  // Scroll to the bottom
    }


    private void clearHistory() {
        outputWindow.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorApp calculator = new CalculatorApp();
            calculator.setVisible(true);
        });
    }
}

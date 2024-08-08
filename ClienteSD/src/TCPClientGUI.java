import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.*;

public class TCPClientGUI {
    private static DataOutputStream out;
    private static JTextArea responseArea;

    public static void main(String[] args) {
    	
    	
        JFrame frame = new JFrame("ROLETA");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);

        
        TCPClient client = new TCPClient();
        client.connectToServer("DESKTOP-CRABSBP", 49666);
        TCPClientGUI.out = client.getOut();


    }

    public static void setResponse(String response){
    	TCPClientGUI.responseArea.append(response);
        responseArea.setCaretPosition(responseArea.getDocument().getLength());
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Digite algo:");
        userLabel.setBounds(10, 300, 80, 25); // Ajusta a posição do rótulo
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 300, 200, 25); // Ajusta a posição do campo de texto
        panel.add(userText);

        JButton sendButton = new JButton(">");
        sendButton.setBounds(305, 300, 45, 25); // Ajusta a posição do botão
        panel.add(sendButton);

        responseArea = new JTextArea(16, 58);
        responseArea.setWrapStyleWord(true);
        responseArea.setLineWrap(true);
        responseArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(responseArea);
        scroll.setBounds(10, 10, 565, 280); // Define a posição e o tamanho do JScrollPane
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll);



        // Ação do botão
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = userText.getText();
                try {
                    out.writeUTF(userInput);
                    userText.setText("");
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });

        userText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String userInput = userText.getText();
                    try {
                        out.writeUTF(userInput);
                        userText.setText("");

                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }
            }
        });
    }
}

package networkAssignment;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.sql.Timestamp;
import javax.swing.*;

public class MultiClient implements ActionListener {
   private Socket socket;
   private ObjectInputStream ois;
   private ObjectOutputStream oos;
   
   private JFrame jframe, loginFrame;
   private JTextField messageField, userName;
   private JTextArea jta;
   private JLabel serverIPLabel, jPW;
   private JPanel jp1, jp2, jp3, jp4;
   private JButton exitButton, loginButton;
   
   private String ip = "13.125.148.108";  // AWS ec2 elastic IP address
   private String id;
   
   private Timestamp ts;

   public MultiClient() {
      jframe = new JFrame("Chatting Program");
      loginFrame = new JFrame("Login");

      messageField = new JTextField(20);
      userName = new JTextField(20);

      jta = new JTextArea(43, 43);
      jta.setOpaque(false);
      
      serverIPLabel = new JLabel("SERVER IP: " + ip);
      serverIPLabel.setOpaque(true);
      
      jPW = new JLabel("Enter Your Name");
      exitButton = new JButton("EXIT");
      loginButton = new JButton("LOGIN");
      jp1 = new JPanel();
      jp2 = new JPanel();
      jp3 = new JPanel();
      jp4 = new JPanel();
      exitButton.setFont(new Font("", Font.PLAIN, (int) 20));
      serverIPLabel.setFont(new Font("", Font.PLAIN, (int) 15));

      jPW.setFont(new Font("", Font.PLAIN, (int) 30));
      jPW.setHorizontalAlignment(jPW.CENTER);

      userName.setFont(new Font("", Font.PLAIN, (int) 30));
      userName.setBackground(Color.WHITE);
      loginButton.setFont(new Font("", Font.PLAIN, (int) 20));

      jp1.setLayout(new BorderLayout());
      jp2.setLayout(new BorderLayout());
      jp3.setLayout(new GridLayout(2,1,10,10));
      jp4.setLayout(new GridLayout(1, 2, 10, 10));

      jp2.add(exitButton, BorderLayout.EAST);
      jp1.add(messageField, BorderLayout.CENTER);
      jp2.add(serverIPLabel, BorderLayout.WEST);

      jp4.add(jPW);
      jp4.add(userName);
      jp3.add(jp4);
      jp3.add(loginButton);
      jframe.add(jp1, BorderLayout.SOUTH);
      jframe.add(jp2, BorderLayout.NORTH);
      loginFrame.add(jp3);

      JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      jframe.add(jsp, BorderLayout.CENTER);
      loginFrame.add(jp3, BorderLayout.CENTER);

      messageField.addActionListener(this);
      exitButton.addActionListener(this);

      jframe.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            try {
               oos.writeObject(id + "#LEFT");
            } catch (IOException ee) {
               ee.printStackTrace();
            }
            System.exit(0);
         }
         public void windowOpened(WindowEvent e) {
            messageField.requestFocus();
         }
      });

      loginButton.addActionListener(this);

      jta.setEditable(false);

      jframe.pack();
      jframe.setResizable(false);
      jframe.setVisible(false);

      loginFrame.setSize(800, 300);
      loginFrame.setResizable(false);
      loginFrame.setVisible(true);
   }

   public void actionPerformed(ActionEvent e) {
      Object obj = e.getSource();
      String msg = messageField.getText();

      String str = e.getActionCommand();

      if (str.equals("LOGIN")) {
         jframe.setVisible(true);
         loginFrame.setVisible(false);

         id = userName.getText();
         try {
			oos.writeObject(id + "#ENTERED");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      }

      if (str.equals("LEFT")){
         System.exit(0);
      }

      if (obj == messageField) {
         if (msg == null || msg.length() == 0) {
            JOptionPane.showMessageDialog(jframe, "내용을 입력하세요", "WARNING", JOptionPane.WARNING_MESSAGE);
         } else {
            try {
            	TimeStamp();
            	oos.writeObject(id + "#" + msg + "   [" + ts + "]");
            } catch (IOException ee) {
               ee.printStackTrace();
            }
            messageField.setText("");
         }
      } else if (obj == exitButton) {
         try {
            oos.writeObject(id + "#LEFT");
         } catch (IOException ee) {
            ee.printStackTrace();
         }
         System.exit(0);
      }
   }

   public void exit() {
      System.exit(0);
   }

   public void init() throws IOException {
      socket = new Socket(ip, 8791);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      MultiClientThread ct = new MultiClientThread(this);
      Thread t = new Thread(ct);
      t.start();
   }

   public ObjectInputStream getOis() {
      return ois;
   }

   public JTextArea getJta() {
      return jta;
   }

   public String getId() {
      return id;
   }

   public void SetName(String a) {
      id = a;
   }

   public void Clear() {
      jta.setText("");
      messageField.requestFocus();
   }
   
	public void TimeStamp() {
		Date date = new Date();
		ts = new Timestamp(date.getTime());
	}
}
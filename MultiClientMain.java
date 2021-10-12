package networkAssignment;

import java.io.IOException;

import javax.swing.JFrame;

public class MultiClientMain {
	   public static void main(String args[]) throws IOException {
		      JFrame.setDefaultLookAndFeelDecorated(true);
		      MultiClient cc = new MultiClient();
		      cc.init();
	   }
}

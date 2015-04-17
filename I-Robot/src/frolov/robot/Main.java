package frolov.robot;


import java.net.*;
import java.util.*;
import javax.swing.*;
import org.apache.commons.logging.*;
import frolov.robot.ui.*;



public class Main extends JFrame{
   private static final long serialVersionUID = -8510057476499416417L;
   
   private static Log log = LogFactory.getLog(Main.class);
   private static final String LOG = "[Main] ";
   
   private static final ImageIcon iconWating;
   private static final JDialog dlgWaiting = new JDialog();
   
   
   static{
      iconWating = new ImageIcon(JFrame.class.getResource("/loaderB32.gif"));
      
      
      JPanel panel = new JPanel();
      
      //panel.setBackground(new java.awt.Color(230, 230, 255));
      
      JLabel jLabel = new JLabel(" Please wait...");
      jLabel.setIcon(iconWating);
      panel.add(jLabel);
      dlgWaiting.add(panel);
      dlgWaiting.setModalExclusionType(JDialog.ModalExclusionType.TOOLKIT_EXCLUDE);

      dlgWaiting.setUndecorated(true);
      dlgWaiting.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

      dlgWaiting.setSize(130, 45);
      dlgWaiting.setAlwaysOnTop(true);
      dlgWaiting.setLocationRelativeTo(null);
      dlgWaiting.setResizable(false);

      panel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));
   }
   
   
   
   

   public Main(){
//      JFrame frame = new JFrame();
//      frame.setVisible(true);
//      frame.setLocationRelativeTo(null);
//      JOptionPane.showMessageDialog(frame, "пизда");
   }





   /**
    * @param args
    * We starts here.
    */
   public static void main(String args[]) throws Exception{
      log.info(LOG + "ok, let's start");
      
      //Only one instance
      try{
         new ServerSocket(12345, 1, null);
      }
      catch (Exception e){
         //Let's show puke 
         System.exit(0);
      }
      
      // We need ".", not "," in numbers
      Locale.setDefault(Locale.US);
      
      InterfaceHelper.setLookAndFeel();

      
      
      
      
      
      
      InterfaceHelper.showWaiting("Пизда");
      
      try{
         Thread.sleep(4000);
      }
      catch (InterruptedException e){
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      InterfaceHelper.hideWaiting();
      
   }
}
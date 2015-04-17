package frolov.robot.ui;

import java.awt.*;
import javax.swing.*;

public final class InterfaceHelper{
   
   
   private static final ImageIcon iconWating;
   private static JDialog dlgWaiting = null;
   
   
   static{
      iconWating = new ImageIcon(JFrame.class.getResource("/loaderB32.gif"));
   }
   
   
   
   
   
   
   
   public static void setLookAndFeel(){
      // try {
      // for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
      // if ("tiny".contains(info.getName().toLowerCase())) {
      // UIManager.setLookAndFeel(info.getClassName());
      // break;
      // }
      // }
      // } catch (Exception e) {
      // // If Nimbus is not available, you can set the GUI to another look and
      // feel.
      // }
      
      Toolkit.getDefaultToolkit().setDynamicLayout(true);
      System.setProperty("sun.awt.noerasebackground", "true");
      JFrame.setDefaultLookAndFeelDecorated(true);
      JDialog.setDefaultLookAndFeelDecorated(true);

      //FrameLucy.setDefaultLookAndFeelDecorated(true);
      
      try{
         UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
      }
      catch (Exception ex){
         ex.printStackTrace();
      }
   }
   
   
   


   public static void showWaiting(String sMessage){
      dlgWaiting = new JDialog();

      JPanel panel = new JPanel();

      // panel.setBackground(new java.awt.Color(230, 230, 255));

      JLabel jLabel = new JLabel(sMessage);
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
      dlgWaiting.setVisible(true);
   }
   

   
   
   public static void hideWaiting(){
      if(dlgWaiting == null){
      }
      else{
         dlgWaiting.setVisible(false);
         dlgWaiting = null;
      }
   }

}

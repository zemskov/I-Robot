package frolov.robot;


import java.net.*;
import java.util.*;
import javax.swing.*;
import org.apache.commons.logging.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
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

      
      
      

      ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
//      ApplicationContext ctx = new AnnotationConfigApplicationContext(Configs.class);      
      Commands commands = ctx.getBean("commands", Commands.class);       
      
      
      String sPort;
      
      while(true){
         InterfaceHelper.showWaiting("Ишем Робота...");

         sPort = commands.portDetecter.findRobot();

         InterfaceHelper.hideWaiting();
         

         if(sPort == null) {
            if(JOptionPane.showOptionDialog(null, 
                     "Робот не найден.", 
                     "Ошибка", 
                     JOptionPane.OK_CANCEL_OPTION, 
                     JOptionPane.ERROR_MESSAGE, 
                     null, 
                     new String[]{"Повторить поиск", "Выход"}, // this is the array
                     "default") == JOptionPane.YES_OPTION){
               continue;
            }
            else{
               System.exit(0);
            }
         }
         else{
            break;
         }
      };
      
      
      if(JOptionPane.showOptionDialog(null, 
                                      "Робот найден на порту " + sPort, 
                                      "Готов к запуску", 
                                      JOptionPane.OK_CANCEL_OPTION, 
                                      JOptionPane.INFORMATION_MESSAGE, 
                                      null, 
                                      new String[]{"Начать работу", "Диагностика"}, // this is the array
                                      "default") == JOptionPane.YES_OPTION){
      }
      else{
      }

      
      
      
//      JFrame frame = new JFrame();
//      frame.setVisible(true);
//      frame.setLocationRelativeTo(null);
//      
//      if(sPort == null) {
//         JOptionPane.showMessageDialog(frame, "Робот не найден.");
//      }
//      else {
//         JOptionPane.showMessageDialog(frame, "Робот найден на порту " + sPort);
//      }
   }
}
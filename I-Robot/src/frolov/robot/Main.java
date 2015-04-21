package frolov.robot;


import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import jssc.*;
import org.apache.commons.logging.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import frolov.robot.ui.*;



public class Main extends JFrame{
   private static final long serialVersionUID = -8510057476499416417L;
   
   private static Log log = LogFactory.getLog(Main.class);
   private static final String LOG = "[Main] ";
   
   private static final ImageIcon iconWating;
   private static final JDialog dlgWaiting = new JDialog();
   
   public static SerialPort serialPort;
   
   
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
      final Commands commands = ctx.getBean("commands", Commands.class);       
      
      
      
      while(true){
         InterfaceHelper.showWaiting("Ишем Робота...");

         serialPort = commands.portDetecter.findRobot();

         InterfaceHelper.hideWaiting();
         

         if(serialPort == null) {
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
               //Let's quit then
               System.exit(0);
            }
         }
         else{
            break;
         }
      };
      
      
      
      
      
      if(JOptionPane.showOptionDialog(null, 
                                      "Робот найден на порту " + serialPort.getPortName(), 
                                      "Готов к запуску", 
                                      JOptionPane.YES_NO_OPTION, 
                                      JOptionPane.INFORMATION_MESSAGE, 
                                      null, 
                                      new String[]{"Начать работу", "Диагностика"}, // this is the array
                                      "default") == JOptionPane.YES_OPTION){
         
//         ICommand command = commands.mapCommands.values().iterator().next();
//         command.run();
         
      }
      else{
         //Диагностика
         // создаем окно и устанавливаем его размер. 
         JFrame jf = new JFrame(); 
         jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
         jf.setSize(400, 300); 
         jf.setLocationRelativeTo(null);
         jf.setVisible(true); 
  
         // создаем  панель. 
         JPanel p = new JPanel(); 
         jf.add(p); 
  
         // к панели добавляем менеджер FlowLayout. 
         p.setLayout(new FlowLayout()); 
  
         // к панели добавляем кнопки. 
         for(final String sCommandName : commands.mapCommands.keySet()) {
            JButton btn = new JButton(sCommandName);
            
            btn.addActionListener(new ActionListener(){
               public void actionPerformed(ActionEvent paramActionEvent){
                  ICommand command = commands.mapCommands.get(sCommandName);
                  
                  try{
                     command.run();
                  }
                  catch (Exception e){
                     log.error(LOG, e);
                  }
               }
            });
            p.add(btn); 
         }
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
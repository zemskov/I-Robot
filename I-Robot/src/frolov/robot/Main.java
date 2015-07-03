package frolov.robot;


import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.*;
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
   
   public static volatile SerialPort serialPort;
   
   
   
   public static final TrayIcon trayIcon;
   public static final PopupMenu popup; 
   
   static{
      popup = new PopupMenu();
      Image image = Toolkit.getDefaultToolkit().getImage(JFrame.class.getResource("/loaderB32.gif"));
      
   
      trayIcon = new TrayIcon(image, "Скратчдуино v2.0", popup);
   }

   
   
   static{
      iconWating = new ImageIcon(JFrame.class.getResource("/loaderB32.gif"));
      
      
      JPanel panel = new JPanel();
      
      //panel.setBackground(new java.awt.Color(230, 230, 255));
      
      JLabel jLabel = new JLabel(" Ожидайте...");
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
   
   
   private static ServerSocket socket = null;
   

   public Main(){
   }





   /**
    * @param args
    * We starts here.
    */
   public static void main(String args[]) throws Exception{
      log.info(LOG + "ok, let's start");
      
      
      // We need ".", not "," in numbers
      Locale.setDefault(Locale.US);
      
      InterfaceHelper.setLookAndFeel();

      
      
      
      //Only one instance
      try{
         socket = new ServerSocket(12345);
         socket.isBound();
      }
      catch (Exception e){
         InterfaceHelper.showSecondInstance();
         return;
      }
      

      
      

      final ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
//      ApplicationContext ctx = new AnnotationConfigApplicationContext(Configs.class);      
//      final RoboConfig roboConfig = ctx.getBean("robo_config", RoboConfig.class);       
      
      

      findRobot();      
      
      
      
      final AtomicBoolean bStartDiagnostic = new AtomicBoolean();      
      
      SwingUtilities.invokeAndWait(new Runnable(){
         public void run(){
            if(JOptionPane.showOptionDialog(null, 
                     "Робот найден на порту " + serialPort.getPortName(), 
                     "Готов к запуску", 
                     JOptionPane.YES_NO_OPTION, 
                     JOptionPane.INFORMATION_MESSAGE, 
                     null, 
                     new String[]{"Начать работу", "Диагностика"}, // this is the array
                     "default") == JOptionPane.YES_OPTION){
               bStartDiagnostic.set(false);
            }
            else{
               bStartDiagnostic.set(true);
            }
         }
      });
      
      
      
      if(bStartDiagnostic.get()){
         ctx.getBean("diagnostic");       
      }
      else{
         if(SystemTray.isSupported()){

            SystemTray tray = SystemTray.getSystemTray();
 
            MouseListener mouseListener = new MouseListener(){

               public void mouseClicked(MouseEvent event){
                  // System.out.println("Tray Icon - Mouse clicked!");
               }

               public void mouseEntered(MouseEvent event){
                  // System.out.println("Tray Icon - Mouse entered!");
               }

               public void mouseExited(MouseEvent event){
                  // System.out.println("Tray Icon - Mouse exited!");
               }

               public void mousePressed(MouseEvent event){
                  // System.out.println("Tray Icon - Mouse pressed!");

                  try{
                     ctx.getBean("tray_command", ICommand.class).run();
                  }
                  catch (Exception e){
                     log.error(LOG, e);
                  }
               }

               public void mouseReleased(MouseEvent e){
                  // System.out.println("Tray Icon - Mouse released!");
               }
            };

            ActionListener exitListener = new ActionListener(){
               public void actionPerformed(ActionEvent e){
                  System.out.println("Exiting...");
                  System.exit(0);
               }
            };


            ActionListener actionListener = new ActionListener(){
               public void actionPerformed(ActionEvent e){
                  //trayIcon.displayMessage("Action Event", "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
               }
            };
            
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            trayIcon.addMouseListener(mouseListener);

            try{
               tray.add(trayIcon);
            }
            catch (AWTException e){
               System.err.println("TrayIcon could not be added.");
            }

         }
         else{
            // System Tray is not supported
         }        
         
         
         ctx.getBean("rest", IRest.class).start();       
      }
   }
   





   public static void setIconImage(String sIconName){
      if(sIconName == null){
      }
      else{
         Image imageIcon = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/" + sIconName));
         Main.trayIcon.setImage(imageIcon);
      }
   }
   
   
   
   public static void robotLost(){
      if(SwingUtilities.isEventDispatchThread()){
         _robotLost();
      }
      else{
         SwingUtilities.invokeLater(new Runnable(){
            public void run(){
               _robotLost();
            }
         });
      }

      synchronized (Main.class){
         try{
            Main.class.wait();
         }
         catch (InterruptedException e){
            // Let's quit then
            System.exit(0);
         }
      }
      
   }
   private static void _robotLost(){
      if(JOptionPane.showOptionDialog(null,
                                      "Потеряна связь с роботом!",
                                      "Ошибка",
                                      JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[] {
                                      "Повторить поиск", "Выход" }, // this is the array
                                      "default") == JOptionPane.YES_OPTION){
         synchronized(Main.class){
            Main.class.notifyAll();
         }
         
         Main.findRobot();
         
         JOptionPane.showMessageDialog(null,
                                       "Соединение восстановлено!",
                                       Main.serialPort.getPortName(),
                                       JOptionPane.INFORMATION_MESSAGE);
      }
      else{
         // Let's quit then
         System.exit(0);
      }
   }
 
   
   
   
   
   
   
   
   
   
   public static void findRobot(){
      final ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
      final RoboConfig roboConfig = ctx.getBean("robo_config", RoboConfig.class);       
      
      
      while(true){
         InterfaceHelper.showWaiting("Ищем Робота...");

         serialPort = roboConfig.portDetecter.findRobot();

         InterfaceHelper.hideWaiting();
         
         
         if(serialPort == null){
            if(SwingUtilities.isEventDispatchThread()){
               _findRobot();
            }
            else{
               SwingUtilities.invokeLater(new Runnable(){
                  public void run(){
                     _findRobot();
                  }
               });
            }
            
            synchronized(Main.class){
               try{
                  Main.class.wait();
               }
               catch (InterruptedException e){
                  // Let's quit then
                  System.exit(0);
               }
            }
            continue;
         }
         else{
            break;
         }
      };
   }
   private static void _findRobot(){
      if(JOptionPane.showOptionDialog(null,
                                      "Робот не найден.",
                                      "Ошибка",
                                      JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[] {
                                      "Повторить поиск", "Выход" }, // this is the array
                                      "default") == JOptionPane.YES_OPTION){
         
         synchronized(Main.class){
            Main.class.notifyAll();            
         }
      }
      else{
         // Let's quit then
         System.exit(0);
      }
   }
   
}
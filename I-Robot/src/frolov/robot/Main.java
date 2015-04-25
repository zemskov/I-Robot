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
      final RoboConfig commands = ctx.getBean("robo_config", RoboConfig.class);       
      
      
      
      
      while(true){
         InterfaceHelper.showWaiting("Ищем Робота...");

         serialPort = commands.portDetecter.findRobot();

         InterfaceHelper.hideWaiting();
         

         if(serialPort == null) {
            SwingUtilities.invokeAndWait(new Runnable(){
               public void run(){
                  if(JOptionPane.showOptionDialog(null, 
                                                  "Робот не найден.", 
                                                  "Ошибка", 
                                                  JOptionPane.OK_CANCEL_OPTION, 
                                                  JOptionPane.ERROR_MESSAGE, 
                                                  null, 
                                                  new String[]{"Повторить поиск", "Выход"}, // this is the array
                                                  "default") == JOptionPane.YES_OPTION){
                  }
                  else{
                     //Let's quit then
                     System.exit(0);
                  }
               }
            });
            
            continue;
         }
         else{
            break;
         }
      };
      
      
      
      
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
         final TrayIcon trayIcon;
         
         if(SystemTray.isSupported()) {
          
             SystemTray tray = SystemTray.getSystemTray();
             Image image = Toolkit.getDefaultToolkit().getImage(JFrame.class.getResource("/loaderB32.gif"));
          
             MouseListener mouseListener = new MouseListener() {
                          
                 public void mouseClicked(MouseEvent e) {
                     System.out.println("Tray Icon - Mouse clicked!");                
                 }
          
                 public void mouseEntered(MouseEvent e) {
                     System.out.println("Tray Icon - Mouse entered!");                
                 }
          
                 public void mouseExited(MouseEvent e) {
                     System.out.println("Tray Icon - Mouse exited!");                
                 }
          
                 public void mousePressed(MouseEvent e) {
                     System.out.println("Tray Icon - Mouse pressed!");                
                 }
          
                 public void mouseReleased(MouseEvent e) {
                     System.out.println("Tray Icon - Mouse released!");                
                 }
             };
          
             ActionListener exitListener = new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     System.out.println("Exiting...");
                     System.exit(0);
                 }
             };
                      
             PopupMenu popup = new PopupMenu();
             MenuItem defaultItem = new MenuItem("Exit");
             defaultItem.addActionListener(exitListener);
             popup.add(defaultItem);
          
             trayIcon = new TrayIcon(image, "Tray Demo", popup);
          
             ActionListener actionListener = new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     trayIcon.displayMessage("Action Event",
                         "An Action Event Has Been Performed!",
                         TrayIcon.MessageType.INFO);
                 }
             };
                      
             trayIcon.setImageAutoSize(true);
             trayIcon.addActionListener(actionListener);
             trayIcon.addMouseListener(mouseListener);
          
             try {
                 tray.add(trayIcon);
             } catch (AWTException e) {
                 System.err.println("TrayIcon could not be added.");
             }
          
         } else {
          
             //  System Tray is not supported
          
         }      
         
         
         
         
         ctx.getBean("rest", IRest.class).start();       
      }
   }
}
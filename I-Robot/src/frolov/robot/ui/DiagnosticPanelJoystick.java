package frolov.robot.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.apache.commons.logging.*;
import frolov.robot.*;

public class DiagnosticPanelJoystick extends JFrame{
   private static Log log = LogFactory.getLog(DiagnosticPanelJoystick.class);
   private static final String LOG = "[Diagnostic] ";
   
   private static final long serialVersionUID = 6788051540896144101L;
   
   


   
   
   
   public DiagnosticPanelJoystick(final RoboConfig roboConfig) throws HeadlessException{
      java.awt.EventQueue.invokeLater(new Runnable(){
         public void run(){
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
            p.setLayout(new GridLayout(3, 4));
            
            
            
            ImageIcon iconForward  = new ImageIcon(JFrame.class.getResource("/up.png"));
            JButton buttonUP = new JButton();
            buttonUP.setIcon(iconForward);
            
            ImageIcon iconBackward = new ImageIcon(JFrame.class.getResource("/down.png"));
            ImageIcon iconLeft     = new ImageIcon(JFrame.class.getResource("/left.png"));
            ImageIcon iconRight    = new ImageIcon(JFrame.class.getResource("/right.png"));
            ImageIcon iconStop     = new ImageIcon(JFrame.class.getResource("/stop.png"));

            
            p.add(buttonUP);            

            
            
            
//            // к панели добавляем кнопки. 
//            for(final String sCommandName : roboConfig.mapCommands.keySet()) {
//               JButton btn = new JButton(sCommandName);
//               
//               btn.addActionListener(new ActionListener(){
//                  public void actionPerformed(ActionEvent paramActionEvent){
//                     ICommand command = roboConfig.mapCommands.get(sCommandName);
//                     
//                     try{
//                        command.run();
//                     }
//                     catch (Exception e){
//                        log.error(LOG, e);
//                     }
//                  }
//               });
//               p.add(btn); 
//            }
         }
      });
   }
}

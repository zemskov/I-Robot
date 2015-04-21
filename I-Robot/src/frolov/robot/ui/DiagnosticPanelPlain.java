package frolov.robot.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.apache.commons.logging.*;
import frolov.robot.*;

public class DiagnosticPanelPlain extends JFrame{
   private static Log log = LogFactory.getLog(DiagnosticPanelPlain.class);
   private static final String LOG = "[Diagnostic] ";
   
   private static final long serialVersionUID = 6788051540896144101L;
   
   


   
   
   
   public DiagnosticPanelPlain(final RoboConfig roboConfig) throws HeadlessException{
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
            p.setLayout(new FlowLayout()); 

            // к панели добавляем кнопки. 
            for(final String sCommandName : roboConfig.mapCommands.keySet()) {
               JButton btn = new JButton(sCommandName);
               
               btn.addActionListener(new ActionListener(){
                  public void actionPerformed(ActionEvent paramActionEvent){
                     ICommand command = roboConfig.mapCommands.get(sCommandName);
                     
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
      });
   }
}

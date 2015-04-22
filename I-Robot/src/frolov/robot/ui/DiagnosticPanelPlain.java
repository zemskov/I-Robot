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
   
   
   
   
   public DiagnosticPanelPlain(final RoboConfig roboConfig){
      java.awt.EventQueue.invokeLater(new Runnable(){
         public void run(){
            //Диагностика
            // создаем окно и устанавливаем его размер. 
            JFrame jf = new JFrame(); 
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            jf.setSize(400, 400); 
            jf.setLocationRelativeTo(null);
            jf.setResizable(false);
            jf.setVisible(true); 

            // создаем  панель. 
            JPanel p = new JPanel(); 
            jf.add(p); 

            // к панели добавляем менеджер FlowLayout. 
            p.setLayout(new FlowLayout());
            
            final JTextArea taCommandResponse = new JTextArea();
            taCommandResponse.setPreferredSize(new Dimension(350, 100));
            taCommandResponse.setLineWrap(true);
            taCommandResponse.setWrapStyleWord(true);
            
            
            final JTextArea taCommandResponsePairs = new JTextArea();
            taCommandResponsePairs.setPreferredSize(new Dimension(350, 180));
            taCommandResponsePairs.setLineWrap(true);
            taCommandResponsePairs.setWrapStyleWord(true);
            

            // к панели добавляем кнопки. 
            for(final String sCommandName : roboConfig.mapCommands.keySet()) {
               JButton btn = new JButton(sCommandName);
               
               btn.addActionListener(new ActionListener(){
                  public void actionPerformed(ActionEvent paramActionEvent){
                     ICommand command = roboConfig.mapCommands.get(sCommandName);
                     
                     taCommandResponse.setText("");
                     taCommandResponsePairs.setText("");
                     
                     try{
                        Response response = command.run();
                        
                        final StringBuilder sb = new StringBuilder();
                        
                        for(int f = 0; f < response.rawData.length; f++){
                           int incomingByte = response.rawData[f] & 0xff;
                           
                           if(sb.length() > 0){
                              sb.append(", ");                              
                           }
                           sb.append(String.format("%8s", Integer.toBinaryString(incomingByte)).replaceAll(" ", "0"));
                        }
                        
                        java.awt.EventQueue.invokeLater(new Runnable(){
                           public void run(){
                              taCommandResponse.setText("Ответ робота:\n" + sb);
                           }
                        });
                        
                        for(String sKey : response.parsedValues.keySet()){
                           taCommandResponsePairs.setText(taCommandResponsePairs.getText() + sKey + "=" + response.parsedValues.get(sKey) + "\n");
                        }
                     }
                     catch (Exception e){
                        log.error(LOG, e);
                     }
                  }
               });
               p.add(btn); 
            }
            
            p.add(taCommandResponse);
            p.add(taCommandResponsePairs);
         }
      });
   }
}

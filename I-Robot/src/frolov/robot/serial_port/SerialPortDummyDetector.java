package frolov.robot.serial_port;

import java.util.*;
import jssc.*;
import org.apache.commons.logging.*;
import frolov.robot.*;



public class SerialPortDummyDetector implements IPortDetector{
   private static Log log = LogFactory.getLog(SerialPortDummyDetector.class);
   private static final String LOG = "[Detector] ";
   
   
   private static SerialPort serialPort = null;
   
   //We wait for 10 sec
   private static final int MAX_DETECTION_TIME = 10000;
   

   public SerialPort findRobot(){
      String[] arrPorts = SerialPortList.getPortNames();
      
      List<PortChecker> listCheckers = new ArrayList<PortChecker>();

      for(String sPortName : arrPorts){
         PortChecker pc = new PortChecker(sPortName);
         listCheckers.add(pc);
         pc.start();
      }

      //Let's wait for 10 sec
      try{
         synchronized(SerialPortDummyDetector.class){
            SerialPortDummyDetector.class.wait(MAX_DETECTION_TIME);
         }
      }
      catch(Exception e){
         log.error(LOG, e);
      }
      finally{
         log.trace(LOG + "Cleanup");
         
         for(PortChecker pc : listCheckers){
            try{
               pc.serialPort.removeEventListener();
               pc.serialPort.purgePort(255);

               if(serialPort == null || serialPort.getPortName() != pc.portName) {
                  //Note
                  //We close all alien ports here
                  //But keep our robot opened.
                  pc.serialPort.closePort();
                  
                  pc.interrupt();
               }
            }
            catch (SerialPortException e){
               log.error(LOG, e);
            }
         }
      }
      
      if(serialPort == null){
         log.info(LOG + "Seems no Robot found.");
      }
      else{
         log.info(LOG + "We did it! Robot is here: " + serialPort.getPortName());
      }
      
      return serialPort;
   }
   
   
   
   private static void gotIt(SerialPort serialPort){
      synchronized(SerialPortDummyDetector.class){
         SerialPortDummyDetector.serialPort = serialPort;
         SerialPortDummyDetector.class.notifyAll();
      }
   }
   

   
   
   
   
   
   public static final class PortChecker extends Thread{
      public final String portName;
      public final SerialPort serialPort;
      

      public PortChecker(String port){
         this.portName = port;
         this.serialPort = new SerialPort(port);
      }
      

      
      public void run(){
         log.info(LOG + portName + " Starting...");
         
         try{
            // Let's open
            serialPort.openPort();
            log.debug(LOG + portName + " opened.");

            // Something standart
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);

            // Hardware Overflow
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

            // Listens for incoming data
            serialPort.addEventListener(new PortReader(this), SerialPort.MASK_RXCHAR);

            // Send some data
            serialPort.writeByte((byte) 32);
            log.debug(LOG + portName + " Test data sent.");
            
            Thread.sleep(MAX_DETECTION_TIME);
         }
         catch (InterruptedException e){
         }
         catch (Exception e){
            log.error(LOG, e);
         }
      }

      

      
      
      
      
      
      private static class PortReader implements SerialPortEventListener{
         private PortChecker portChecker;


         public PortReader(PortChecker portChecker){
            this.portChecker = portChecker;
         }


         public void serialEvent(SerialPortEvent event){
            if(event.isRXCHAR() && event.getEventValue() > 0){
               //log.debug(LOG + portChecker.portName);
               SerialPortDummyDetector.gotIt(portChecker.serialPort);
               
               // try{
               // Получаем ответ от устройства, обрабатываем данные и т.д.
               // String data = serialPort.readString(event.getEventValue());
               // System.out.println(portChecker.serialPort.readBytes());

               // И снова отправляем запрос
               // serialPort.writeString("Get data");
               // }
               // catch (SerialPortException ex){
               // System.out.println(ex);
               // }
            }
         }
      }
   }
}

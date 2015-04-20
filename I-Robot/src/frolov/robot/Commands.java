package frolov.robot;


public class Commands{
 
   public IPortDetector portDetecter;

   public Commands(){
   }
   
   
   public Commands(IPortDetector portDetecter){
      super();
      this.portDetecter = portDetecter;
   }


   public IPortDetector getPortDetecter(){
      return portDetecter;
   }


   public void setPortDetecter(IPortDetector portDetecter){
      this.portDetecter = portDetecter;
   }
   
   
   
}


package frolov.robot;

import java.util.*;


public class Commands{
 
   public final IPortDetector portDetecter;
   public final Map<String, ICommand> mapCommands;


   public Commands(IPortDetector portDetecter, Map<String, ICommand> mapCommands){
      this.portDetecter = portDetecter;
      this.mapCommands = mapCommands;
   }
}


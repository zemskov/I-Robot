package frolov.robot;

import java.util.*;


public class RoboConfig{
 
   public final IPortDetector portDetecter;
   public final Map<String, ICommand> mapCommands;


   public RoboConfig(IPortDetector portDetecter, Map<String, ICommand> mapCommands){
      this.portDetecter = portDetecter;
      this.mapCommands = mapCommands;
   }
}


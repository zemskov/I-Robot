package frolov.robot.serial_port;

import frolov.robot.*;
import frolov.robot.serial_port.type1.*;

public class CommandMoveBackward extends Command implements ICommand{
   
   public CommandMoveBackward(String sParameters) throws Exception{
      super(sParameters);
   }
}

package frolov.robot.config;

import java.util.*;
import javax.xml.bind.annotation.*;


@XmlRootElement(name="commands")
public class XCommands{

   public List<XGroup> listGroups = new ArrayList<XGroup>();
   
   
   public static class XGroup{
      public String name;

      @XmlElement(name="command")
      public List<XCommand> listCommands = new ArrayList<XCommand>();
   }
   
   
   
   public static class XCommand{
      public String   name;
      public String   code;
      public XResponse response = new XResponse();
   }
   
   
   
   
   public static class XResponse{
      @XmlElement(name="value")
      public List<Value> listValues = new ArrayList<Value>();
   }

   
   
   public class Value{
      public String name;
      public String type;
      public int length;
   }
}

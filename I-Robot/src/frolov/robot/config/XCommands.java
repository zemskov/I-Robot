package frolov.robot.config;

import java.util.*;
import javax.xml.bind.annotation.*;


@XmlRootElement(name="commands")
public class XCommands{

   @XmlElement(name="command")
   public List<Command> listCommands = new ArrayList<Command>();
   
   
   public static class Command{
      public String   name;
      public String   code;
      public Response response = new Response();
   }
   
   
   
   public static class Response{
      @XmlElement(name="value")
      public List<Value> listValues = new ArrayList<Value>();
   }
   
   
   public class Value{
      public String name;
      public String type;
      public int length;
   }
}

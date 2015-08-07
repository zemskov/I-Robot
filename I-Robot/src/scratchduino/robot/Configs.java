package scratchduino.robot;

import java.io.*;
import java.util.*;
import org.springframework.context.annotation.*;

@Configuration
public class Configs{
   
   public static int getSpeed() throws Exception{

      Properties properties = new Properties();

      InputStream inputStream = Configs.class.getClassLoader().getResourceAsStream("config.ini");
      properties.load(inputStream);

      return Integer.parseInt(properties.getProperty("Speed"));
   }

   
   public static void main(String[] test) throws Exception{
      System.out.println(getSpeed());
   }
   
   
   
   
   
   
   
//   @Bean(name="commands")
//   public Commands commands(){
//      return new Commands();
//   }

}

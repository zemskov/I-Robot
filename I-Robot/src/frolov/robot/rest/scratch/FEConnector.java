package frolov.robot.rest.scratch;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.apache.commons.logging.*;
import frolov.robot.*;



@Path("/")
public class FEConnector{
   private static Log log = LogFactory.getLog(FEConnector.class);
   private static final String LOG = "[Connector] ";
   
   
   @Context ServletContext context;   
   
   public FEConnector(@Context HttpServletRequest hsr){
      log.info(LOG + "\n------------------------------------\n" + hsr.getRequestURI());
      
   }
   

   @GET
   @Path("/{paths:.+}")
   @Produces("text/plain; charset=UTF-8")
   public String serice(@PathParam("paths")  List<PathSegment> uglyPath) throws Exception{
      
      List<String> listParameters = new ArrayList<String>();

      for(PathSegment pathSegment : uglyPath){
         listParameters.add(pathSegment.getPath());
      }
      
      String sCommand = listParameters.remove(0);
      
      if("crossdomain.xml".equals(sCommand)){
         return "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" /></cross-domain-policy>";
      }
      else{
         ICommand command = ((RoboConfig) context.getAttribute("roboConfig")).mapCommands.get(sCommand);
         
         if(command == null){
            throw new WebApplicationException(javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE);
         }
         else{
            frolov.robot.Response reponse = command.run();
            
            StringBuilder sb = new StringBuilder();
            for(String sKey : reponse.parsedValues.keySet()){
               sb.append(sKey + "=" + reponse.parsedValues.get(sKey) + "\n");
            }
            
            return sb.toString();
         }
      }
   }
}

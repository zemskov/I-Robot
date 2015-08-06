package scratchduino.robot.rest.scratch;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.*;
import scratchduino.robot.*;

public class ScratchRest implements IRest{
   
   private final RoboConfig roboConfig;
   
   public ScratchRest(final RoboConfig roboConfig){
      this.roboConfig = roboConfig;
   }
   
   
   
   public void start() throws Exception{
      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");
      context.setAttribute("roboConfig", roboConfig);

      Server jettyServer = new Server(8080);
      jettyServer.setHandler(context);

      ServletHolder jerseyServlet = context.addServlet(com.sun.jersey.spi.container.servlet.ServletContainer.class, "/*");
      jerseyServlet.setInitOrder(0);

      // Tells the Jersey Servlet which REST service/class to load.
      jerseyServlet.setInitParameter(
         "com.sun.jersey.config.property.packages",
         "scratchduino.robot.rest.scratch");

      try{
         jettyServer.start();
         jettyServer.join();
      }
      finally{
         jettyServer.destroy();
      }
   }
}

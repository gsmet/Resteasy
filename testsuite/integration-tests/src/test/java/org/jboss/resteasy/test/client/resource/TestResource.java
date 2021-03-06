package org.jboss.resteasy.test.client.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.utils.PortProviderUtil;

@Path("")
public class TestResource
{
   @HttpMethod("TRACE")
   @Target(value= ElementType.METHOD)
   @Retention(value= RetentionPolicy.RUNTIME)
   public @interface TRACE {
   }
   
   @HttpMethod("METHOD")
   @Target({ElementType.METHOD})
   @Retention(RetentionPolicy.RUNTIME)
   public @interface METHOD {
   }
   
   @Path("get")
   @GET
   public Response get()
   {
      return Response.ok("get").build();
   }

   @Path("put")
   @PUT
   @Consumes(MediaType.TEXT_PLAIN)
   public Response put(String s)
   {
      return Response.ok(s).build();
   }

   @Path("post")
   @POST
   @Consumes(MediaType.TEXT_PLAIN)
   public Response post(String s)
   {
      return Response.ok(s).build();
   }

   @Path("delete")
   @DELETE
   public Response delete()
   {
      return Response.ok("delete").build();
   }

   @Path("head")
   @HEAD
   public Response head()
   {
      return Response.noContent().header("key", "head").build();
   }

   @Path("options")
   @OPTIONS
   public Response options()
   {
      return Response.ok("options").build();
   }

   @Path("trace")
   @TRACE
   public Response trace()
   {
      return Response.ok("trace").build();
   }

   @Path("method")
   @METHOD
   public Response method()
   {
      return Response.ok("method").build();
   }

   @Path("methodEntity")
   @METHOD
   public Response methodEntity(String s)
   {
      return Response.ok(s).build();
   }
   
   @Path("redirect/{p}")
   @GET
   public Response redirect(@PathParam("p") String p)
   {
      return Response.temporaryRedirect(PortProviderUtil.createURI("/redirected", p)).build();
   }

   @Path("redirected")
   @GET
   public Response redirected()
   {
      return Response.ok("OK").build();
   }

   @Path("post-redirect")
   @POST
   public Response postRedirect(String p)
   {
      return Response.seeOther(PortProviderUtil.createURI("/redirected", p)).build();
   }
}

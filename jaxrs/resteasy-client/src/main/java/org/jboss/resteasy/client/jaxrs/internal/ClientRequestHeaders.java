package org.jboss.resteasy.client.jaxrs.internal;

import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.CaseInsensitiveMap;
import org.jboss.resteasy.util.DateUtil;
import org.jboss.resteasy.util.HeaderHelper;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ClientRequestHeaders
{
   protected CaseInsensitiveMap<Object> headers = new CaseInsensitiveMap<Object>();
   protected ResteasyProviderFactory providerFactory;

   public ClientRequestHeaders(ResteasyProviderFactory providerFactory)
   {
      this.providerFactory = providerFactory;
   }

   public ClientRequestHeaders clone()
   {
      ClientRequestHeaders copy = new ClientRequestHeaders(providerFactory);
      copy.headers.putAll(headers);
      return copy;
   }

   public CaseInsensitiveMap<Object> getHeaders()
   {
      return headers;
   }

   public void setHeaders(MultivaluedMap<String, Object> newHeaders)
   {
      headers.clear();
      headers.putAll(newHeaders);
   }

   public void setLanguage(Locale language)
   {
      header(HttpHeaders.CONTENT_LANGUAGE, language);
   }

   public void setLanguage(String language)
   {
      setLanguage(new Locale(language));
   }

   public void setMediaType(MediaType mediaType)
   {
      header(HttpHeaders.CONTENT_TYPE, mediaType);
   }

   public void acceptLanguage(Locale... locales)
   {
      headers.remove(HttpHeaders.ACCEPT_LANGUAGE);
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (Locale l : locales)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT_LANGUAGE, builder.toString());
   }

   public void acceptLanguage(String... locales)
   {
      headers.remove(HttpHeaders.ACCEPT_LANGUAGE);
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (String l : locales)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT_LANGUAGE, builder.toString());
   }

   public void accept(String... types)
   {
      headers.remove(HttpHeaders.ACCEPT);
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (String l : types)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT, builder.toString());
   }

   public void accept(MediaType... types)
   {
      headers.remove(HttpHeaders.ACCEPT);
      StringBuilder builder = new StringBuilder();
      boolean isFirst = true;
      for (MediaType l : types)
      {
         if (isFirst)
         {
            isFirst = false;
         }
         else
         {
            builder.append(", ");
         }
         builder.append(l.toString());
      }
      headers.putSingle(HttpHeaders.ACCEPT, builder.toString());
   }

   public void cookie(Cookie cookie)
   {
      headers.add(HttpHeaders.COOKIE, cookie);
   }

   public void allow(String... methods)
   {
      HeaderHelper.setAllow(this.headers, methods);
   }

   public void allow(Set<String> methods)
   {
      HeaderHelper.setAllow(headers, methods);
   }

   public void cacheControl(CacheControl cacheControl)
   {
      headers.putSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
   }

   public void header(String name, Object value)
   {
      if (value == null)
      {
         headers.remove(name);
         return;
      }
      headers.add(name, value);
   }

   public Date getDate()
   {
      Object d = headers.getFirst(HttpHeaders.DATE);
      if (d == null) return null;
      if (d instanceof Date) return (Date) d;
      return DateUtil.parseDate(d.toString());
   }

   public String getHeader(String name)
   {
      List vals = headers.get(name);
      if (vals == null) return null;
      StringBuilder builder = new StringBuilder();
      boolean first = true;
      for (Object val : vals)
      {
         if (first) first = false;
         else builder.append(",");
         builder.append(providerFactory.toHeaderString(val));
      }
      return builder.toString();
   }

   public MultivaluedMap<String, String> asMap()
   {
      CaseInsensitiveMap<String> map = new CaseInsensitiveMap<String>();
      for (Map.Entry<String, List<Object>> entry : headers.entrySet())
      {
         for (Object obj : entry.getValue())
         {
            map.add(entry.getKey(), providerFactory.toHeaderString(obj));
         }
      }
      return map;
   }

   public List<String> getHeaderValues(String name)
   {
      List<Object> vals = headers.get(name);
      if (vals == null) return new ArrayList<String>();
      List<String> values = new ArrayList<String>();
      for (Object val : vals)
      {
         values.add(providerFactory.toHeaderString(val));
      }
      return values;
   }

   public Locale getLanguage()
   {
      Object obj = headers.getFirst(HttpHeaders.CONTENT_LANGUAGE);
      if (obj instanceof Locale) return (Locale) obj;
      if (obj == null) return null;
      return new Locale(obj.toString());
   }

   public int getLength()
   {
      return -1;
   }

   public MediaType getMediaType()
   {
      Object obj = headers.getFirst(HttpHeaders.CONTENT_TYPE);
      if (obj instanceof MediaType) return (MediaType) obj;
      if (obj == null) return null;
      return MediaType.valueOf(providerFactory.toHeaderString(obj));
   }

   public List<MediaType> getAcceptableMediaTypes()
   {
      List<MediaType> list = new ArrayList<MediaType>();
      List accepts = headers.get(HttpHeaders.ACCEPT);
      for (Object obj : accepts)
      {
         if (obj instanceof MediaType)
         {
            list.add((MediaType) obj);
            continue;
         }
         String accept = null;
         if (obj instanceof String)
         {
            accept = (String) obj;
         }
         else
         {
            accept = providerFactory.toHeaderString(obj);

         }
         StringTokenizer tokenizer = new StringTokenizer(accept, ",");
         while (tokenizer.hasMoreElements())
         {
            String item = tokenizer.nextToken().trim();
            list.add(MediaType.valueOf(item));
         }
      }
      return list;
   }

   public List<Locale> getAcceptableLanguages()
   {
      List<Locale> list = new ArrayList<Locale>();
      List accepts = headers.get(HttpHeaders.ACCEPT_LANGUAGE);
      for (Object obj : accepts)
      {
         if (obj instanceof Locale)
         {
            list.add((Locale) obj);
            continue;
         }
         String accept = null;
         if (obj instanceof String)
         {
            accept = (String) obj;
         }
         else
         {
            accept = providerFactory.toHeaderString(obj);

         }
         StringTokenizer tokenizer = new StringTokenizer(accept, ",");
         while (tokenizer.hasMoreElements())
         {
            String item = tokenizer.nextToken().trim();
            list.add(new Locale(item));
         }
      }
      return list;
   }

   public Map<String, Cookie> getCookies()
   {
      Map<String, Cookie> cookies = new HashMap<String, Cookie>();
      List list = headers.get(HttpHeaders.COOKIE);
      for (Object obj : list)
      {
         if (obj instanceof Cookie)
         {
            Cookie cookie = (Cookie)obj;
            cookies.put(cookie.getName(), cookie);
         }
         else
         {
            String str = providerFactory.toHeaderString(obj);
            Cookie cookie = Cookie.valueOf(str);
            cookies.put(cookie.getName(), cookie);
         }
      }
      return cookies;
   }
}

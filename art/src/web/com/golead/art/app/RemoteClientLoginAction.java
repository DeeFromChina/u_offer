package com.golead.art.app;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import com.golead.art.app.service.QueryService;
import com.golead.art.utils.Quick;
import com.golead.common.service.login.LoginService;
import com.golead.core.exception.WebException;
import com.golead.core.util.Const;
import com.golead.core.web.UserSession;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class RemoteClientLoginAction implements Action {

   private static final long   serialVersionUID = 1L;

   public static final String  LOGIN            = "LOGIN";

   private String              action;
   private String              userName;
   private String              password;
   private String version;

   private Map<String, Object> request;
   private Map<String, Object> session;
   protected ActionContext     ctx;

   @Override
   public String execute() throws Exception {
      String forward = null;
      ActionContext ctx = ActionContext.getContext();
      request = (Map<String, Object>) ctx.get("request");
      session = ctx.getSession();

      if (Quick.isStrNoE(action)) {
         action = LOGIN;
      }

      try {
         if (LOGIN.equalsIgnoreCase(action)) {
            validateLogin();
            UserSession userSession = (UserSession) session.get(Const.SESSION);
            String uid = userSession.getUserId();
            String uname = userSession.getUserName();
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("version", version);
//            map.put("module", "pictureShow");
//            map.put("pageName", "mainPage");
//            String res = queryService.query(map);
//            setResponse("{\"exit\":\"0\",\"data\":" + res + "}");
            setResponse("{\"exit\":0,\"userId\":\"" + uid + "\",\"userName\":\"" + uname + "\",\"accountName\":\"" + userSession.getAccountName() + "\"}");

         }
         else {
            throw new WebException("找不到该action方法：" + action);
         }

      }
      catch (WebException e) {
         setResponse("{\"exit\":1,\"message\":\"" + e.getMessage() + "\"}");

      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{\"exit\":1,\"message\":\"服务器错误\"}");
      }
      return forward;
   }

   public void validateLogin() throws Exception {
      userName = "anonymous";
      password = "12345678";
      UserSession userSession = loginService.appLogin(userName, password);
      if (userSession != null) {
         session.put(Const.SESSION, userSession);
      }
      else {
         throw new Exception("用户名或密码错误。");
      }
   }

   protected HttpServletResponse setResponse(String doc) throws Exception {
      ctx = ActionContext.getContext();
      HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(doc);
      response.getWriter().close();
      return response;
   }

   @Resource
   private LoginService loginService;
   
   @Resource
   private QueryService queryService;

   public void setAction(String action) {
      this.action = action;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setSession(Map<String, Object> session) {
      this.session = session;
   }

   public void setRequest(Map<String, Object> request) {
      this.request = request;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

}

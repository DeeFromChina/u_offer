package com.golead.art.auction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.auction.model.ArtAuctionExchange;
import com.golead.art.auction.service.ArtAuctionExchangeService;
import com.golead.art.utils.DateUtils;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

/**
 * 汇率管理
 * 
 * @author 尹湘
 * @version 1.0
 * @since 2016年9月3日 下午2:37:37
 */
public class ArtAuctionExchangeAction extends BaseAction {

   private Log           log           = LogFactory.getLog(ArtAuctionExchangeAction.class);

   private DecimalFormat decimalFormat = new DecimalFormat("##0.00##");

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) action = LIST;
      if (log.isDebugEnabled()) log.debug("action" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = sreachExchangeRate();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageExchangeRate();
         else if (ADD.equalsIgnoreCase(action)) forward = addExchangeRate();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveExchangeRate();
         else if (EDIT.equalsIgnoreCase(action)) forward = editExchangeRate();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateExchangeRate();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteExchangeRate();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewExchangeRate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String sreachExchangeRate() throws Exception {
      initForm();
      return LIST;
   }

   public String pageExchangeRate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageRole' method");
      try {
         initForm();
         PageQuery pageQuery = form.getQuery() == null ? new PageQuery() : form.getQuery();
         pageQuery = artAuctionExchangeService.queryArtAuctionExchange(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> rs = pageQuery.getRecordSet();
            if (rs != null) {
               List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < rs.size(); i++) {
                  Map<String, Object> exchange = rs.get(i);
                  Map<String, Object> map = new HashMap<String, Object>();
                  map.put("id", exchange.get("id"));
                  String link = "^javascript:view(" + exchange.get("id") + ")^_self";
                  String year = findCodeName(form, "DATE_YEAR", exchange.get("year") + "");
                  String season = findCodeName(form, "SEASON", exchange.get("season") + "");
                  map.put("data",
                        new Object[] { "", year + link, season, decimalFormat.format(exchange.get("hkExchangeRate")),
                              decimalFormat.format(exchange.get("dExchangeRate")), decimalFormat.format(exchange.get("eExchangeRate")),
                              decimalFormat.format(exchange.get("pExchangeRate")) });
                  list.add(map);
               }
               String res = genGridJson(pageQuery, list);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "汇率管理", "汇率管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
               setResponse(res);
            }
            else setResponse("{exit:1,message:\"数据访问错误。\"}");
         }
         else setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   public String addExchangeRate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addExchangeRate' method");
      try {
         initForm();
         form.getRecord().put("year", DateUtils.getCurrentYear());
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveExchangeRate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveExchangeRate' method");
      try {
         ArtAuctionExchange exchange = new ArtAuctionExchange();

         ConvertUtil.mapToObject(exchange, form.getRecord(), false);
         artAuctionExchangeService.createArtAuctionExchange(exchange);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "汇率管理", "汇率管理", "添加汇率情况：" + exchange.getYear());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(getForm(), e.getMessage());
         initForm();
         return ADD;
      }
   }

   public String editExchangeRate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editExchangeRate' method");
      try {
         initForm();
         ArtAuctionExchange exchange = artAuctionExchangeService.getArtAuctionExchange(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), exchange);
         form.getRecord().put("hkExchangeRate", decimalFormat.format(exchange.getHkExchangeRate()));
         form.getRecord().put("dExchangeRate", decimalFormat.format(exchange.getDexchangeRate()));
         form.getRecord().put("eExchangeRate", decimalFormat.format(exchange.getEexchangeRate()));
         form.getRecord().put("pExchangeRate", decimalFormat.format(exchange.getPexchangeRate()));
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateExchangeRate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateExchangeRate' method");
      try {
         artAuctionExchangeService.updateArtAuctionExchange(form.getRecord());
         artSysLogService.createArtSysLog(currentUser.getUserName(), "汇率管理", "汇率管理", "修改汇率情况：" + form.getRecord().get(ArtAuctionExchange.PROP_YEAR));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         initForm();
         return EDIT;
      }
   }

   public String deleteExchangeRate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteAuction' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtAuctionExchange artAuctionExchange = artAuctionExchangeService.getArtAuctionExchange(del_ids[i]);
            if (log.length() > 0) log.append(",");
            log.append(artAuctionExchange.getYear());
         }
         artAuctionExchangeService.deleteArtAuctionExchanges(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "汇率管理", "汇率管理", "删除汇率情况：" + log.toString());
         setResponse("{exit:0}");
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         setResponse("{exit:1,message:\"删除操作失败!\"}");
      }
      return null;
   }

   public String viewExchangeRate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewExchangeRate' method");
      String forward = null;
      try {
         initForm();
         ArtAuctionExchange exchange = artAuctionExchangeService.getArtAuctionExchange(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), exchange);
         String year = findCodeName(form, "DATE_YEAR", exchange.getYear() + "");
         form.getRecord().put("year", year);
         form.getRecord().put("hkExchangeRate", decimalFormat.format(exchange.getHkExchangeRate()));
         form.getRecord().put("dExchangeRate", decimalFormat.format(exchange.getDexchangeRate()));
         form.getRecord().put("eExchangeRate", decimalFormat.format(exchange.getEexchangeRate()));
         form.getRecord().put("pExchangeRate", decimalFormat.format(exchange.getPexchangeRate()));
         forward = "VIEW";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("err", e);
         forward = "ERROR";
      }
      return forward;
   }

   private void initForm() {
      setCode(form, "DATE_YEAR,SEASON");
   }

   @Resource
   private ArtAuctionExchangeService artAuctionExchangeService;

   @Resource
   private ArtSysLogService          artSysLogService;
}

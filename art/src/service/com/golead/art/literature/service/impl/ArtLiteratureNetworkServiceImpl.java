package com.golead.art.literature.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;
import com.golead.art.literature.dao.ArtLiteratureNetworkDao;
import com.golead.art.literature.model.ArtLiteratureNetwork;
import com.golead.art.literature.model.ArtLiteratureNetworkResearch;
import com.golead.art.literature.service.ArtLiteratureNetworkResearchService;
import com.golead.art.literature.service.ArtLiteratureNetworkService;
import com.golead.art.works.dao.ArtWorksNetworkDao;
import com.golead.art.works.model.ArtWorksNetwork;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtLiteratureNetworkServiceImpl extends BaseServiceImpl implements ArtLiteratureNetworkService {
	private static final long serialVersionUID = 1L;

	public ArtLiteratureNetwork getArtLiteratureNetwork(Serializable id) throws ServiceException {
		try {
			return artLiteratureNetworkDao.get(id);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void createArtLiteratureNetwork(ArtLiteratureNetwork artLiteratureNetwork) throws ServiceException {
		try {
			artLiteratureNetworkDao.save(artLiteratureNetwork);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void updateArtLiteratureNetwork(Map<String, String> artLiteratureNetworkMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artLiteratureNetworkMap.get(ArtLiteratureNetwork.PROP_ID));
            ArtLiteratureNetworkResearch artLiteratureNetworkResearch = artLiteratureNetworkResearchService.findByNetworkId(pkId);
            artLiteratureNetworkResearch.setCoreThesis(artLiteratureNetworkMap.get("coreThesis"));
            artLiteratureNetworkResearch.setObjectiveEval(artLiteratureNetworkMap.get("objectiveEval"));
            artLiteratureNetworkResearch.setSubjectiveEval(artLiteratureNetworkMap.get("subjectiveEval"));
            artLiteratureNetworkResearchService.updateArtLiteratureNetworkResearch(artLiteratureNetworkResearch);
			ArtLiteratureNetwork tmp = artLiteratureNetworkDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artLiteratureNetworkMap, true);			
			artLiteratureNetworkDao.update(tmp);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void deleteArtLiteratureNetwork(Serializable id) throws ServiceException {
		try {
			artLiteratureNetworkDao.delete(id);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void deleteArtLiteratureNetworks(Serializable[] ids) throws ServiceException {
		try {
		   boolean pass = false;
		   for(Serializable id : ids){
		      List<ArtWorksNetwork> artWorksNetworks = artWorksNetworkDao.findByField(ArtWorksNetwork.PROP_NETWORK_ID, id);
		      if(artWorksNetworks != null && artWorksNetworks.size() > 0){
               pass = true;
            }
            if(pass){
               throw new ServiceException();
            }
		   }
			artLiteratureNetworkDao.deleteAll(ids);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public PageQuery queryArtLiteratureNetwork(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artLiteratureNetworkList", pageQuery);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}
	
	private void createSqlFilter(PageQuery page) {
      String paras = "";
      Map<String, String> map = page.getParameters();
      String artArtistId = map.get("artArtistId");
      if (artArtistId != null && !"".equals(artArtistId)) {
         paras = " network.artist_id =" + artArtistId.trim();
      }

      String artArtistName = map.get("artArtistName");
      if (artArtistName != null && !"".equals(artArtistName)) {
         paras = paras.trim().length()>0 ? (paras + " AND") : paras;
         paras += " artist.c_name LIKE '%" + artArtistName.trim() + "%'";
      }
      
      String literatureTitle = map.get("literatureTitle");
      if (literatureTitle != null && !"".equals(literatureTitle)) {
         paras = " network.literature_title like '%" + literatureTitle.trim() + "%'";
      }
      
      String literatureAuther = map.get("literatureAuther");
      if (literatureAuther != null && !"".equals(literatureAuther)) {
         paras = " network.literature_auther like '%" + literatureAuther.trim() + "%'";
      }
      
      String literatureWorks = map.get("literatureWorks");
      if (literatureWorks != null && !"".equals(literatureWorks)) {
         paras += " network.literature_works like '%" + literatureWorks.trim() + "%'";
      }

      String relatedExhib = map.get("relatedExhib");
      if (relatedExhib != null && !"".equals(relatedExhib)) {
         paras += " network.related_exhib like '%" + relatedExhib.trim() + "%'";
      }
      String relatedEvent = map.get("relatedEvent");
      if (relatedEvent != null && !"".equals(relatedEvent)) {
         paras += " network.related_event like '%" + relatedEvent.trim() + "%'";
      }
      String personInvolved = map.get("personInvolved");
      if (personInvolved != null && !"".equals(personInvolved)) {
         paras += " network.person_involved like '%" + personInvolved.trim() + "%'";
      }
      String quoteLiterature = map.get("quoteLiterature");
      if (quoteLiterature != null && !"".equals(quoteLiterature)) {
         paras += " network.quote_literature like '%" + quoteLiterature.trim() + "%'";
      }
      
      if (paras.length() > 0) {
         page.getParameters().put("paras", paras);
      }
   }

   @SuppressWarnings("deprecation")
   public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException{
      try {
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
         HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
         HSSFRow row = sheet.createRow((int) 0);  
         
         HSSFCell cell = row.createCell((short) 0);  
         cell.setCellValue("艺术家");  
         cell = row.createCell((short) 1);  
         cell.setCellValue("文献标题");  
         cell = row.createCell((short) 2);  
         cell.setCellValue("作者/记者");  
         cell = row.createCell((short) 3);  
         cell.setCellValue("发布时间");  
         cell = row.createCell((short) 4);  
         cell.setCellValue("来源");  
         cell = row.createCell((short) 5);  
         cell.setCellValue("文献类型");  
         cell = row.createCell((short) 6);  
         cell.setCellValue("文献提及作品");  
         cell = row.createCell((short) 7);  
         cell.setCellValue("引用文献");  
         cell = row.createCell((short) 8);  
         cell.setCellValue("文献相关人物");  
         cell = row.createCell((short) 9);  
         cell.setCellValue("文献相关展览");  
         cell = row.createCell((short) 10);  
         cell.setCellValue("文献相关事件");  
         cell = row.createCell((short) 11);  
         cell.setCellValue("链接");  
         pageQuery.setPageSize("10000");
         PageQuery pageQuery2 = queryArtLiteratureNetwork(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            String wordsType = returnString(item.get("wordsType"));
            if(!"".equals(wordsType)){
               wordsType = findCodeName(form, "WORDS_TYPE", returnString(item.get("wordsType")));
            }
            
            row = sheet.createRow(i+1); 
            row.createCell((short) 0).setCellValue(returnString(item.get("CName"))); 
            row.createCell((short) 1).setCellValue(returnString(item.get("literatureTitle"))); 
            row.createCell((short) 2).setCellValue(returnString(item.get("literatureAuther"))); 
            row.createCell((short) 3).setCellValue(returnString(item.get("publicationTime"))); 
            row.createCell((short) 4).setCellValue(returnString(item.get("literatureSource"))); 
            row.createCell((short) 5).setCellValue(wordsType); 
            row.createCell((short) 6).setCellValue(returnString(item.get("literatureWorks"))); 
            row.createCell((short) 7).setCellValue(returnString(item.get("quoteLiterature"))); 
            row.createCell((short) 8).setCellValue(returnString(item.get("personInvolved"))); 
            row.createCell((short) 9).setCellValue(returnString(item.get("relatedExhib"))); 
            row.createCell((short) 10).setCellValue(returnString(item.get("relatedEvent"))); 
            row.createCell((short) 11).setCellValue(returnString(item.get("literatureLink"))); 
            i++;
         }
         return hssfWorkbook;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   private String returnString(Object object) {
      String str = object == null ? "" : object.toString();
      return str;
   }

   private String addString(String str, String equalStr, String addStr) {
      if (!"".equals(equalStr)) {
         if (equalStr.indexOf(addStr) > -1) return str + equalStr;
         else return str + equalStr + addStr;
      }
      return str;
   }
   
   private String findCodeName(Object objForm, String codeNo, Serializable value){
      BaseForm bf = (BaseForm) objForm;

      List<Code> list = bf.getCodeSets().get(codeNo);
      if (value == null) return "";
      if (list == null) return "";
      for (int i = 0; i < list.size(); i++) {
         Code code = list.get(i);
         String v;
         if (value instanceof String) v = ((String) value).trim();
         else v = value.toString();
         if (v.equals(code.getValue())) return code.getCodeName();
      }
      return "";
   }

    @Resource
	private ArtLiteratureNetworkDao	artLiteratureNetworkDao;
    
    @Resource
    private ArtWorksNetworkDao	artWorksNetworkDao;
    
    @Resource
    private ArtLiteratureNetworkResearchService	artLiteratureNetworkResearchService;

	public void setArtLiteratureNetworkDao(ArtLiteratureNetworkDao artLiteratureNetworkDao) {
		this.artLiteratureNetworkDao = artLiteratureNetworkDao;
	}
}


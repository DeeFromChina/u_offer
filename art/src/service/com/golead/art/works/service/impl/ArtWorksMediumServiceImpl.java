package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.works.dao.ArtWorksMediumDao;
import com.golead.art.works.model.ArtWorksMedium;
import com.golead.art.works.service.ArtWorksMediumService;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;

@Service
public class ArtWorksMediumServiceImpl extends BaseServiceImpl implements ArtWorksMediumService {
	private static final long serialVersionUID = 1L;

	public ArtWorksMedium getArtWorksMedium(Serializable id) throws ServiceException {
		try {
			return artWorksMediumDao.get(id);
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

	public void createArtWorksMedium(ArtWorksMedium artWorksMedium) throws ServiceException {
		try {
			artWorksMediumDao.save(artWorksMedium);
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
	
	public void createArtWorksMedium(int id, int[] ids) throws ServiceException{
	   try {
         for(int i = 0; i<ids.length; i++){
            ArtWorksMedium artWorksMedium = new ArtWorksMedium();
            artWorksMedium.setWorksId(id);
            artWorksMedium.setMediumId(ids[i]);
            createArtWorksMedium(artWorksMedium);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}

	public void updateArtWorksMedium(Map<String, String> artWorksMediumMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artWorksMediumMap.get(ArtWorksMedium.PROP_ID));
			ArtWorksMedium tmp = artWorksMediumDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artWorksMediumMap, true);			
			artWorksMediumDao.update(tmp);
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

	public void deleteArtWorksMedium(Serializable id) throws ServiceException {
		try {
			artWorksMediumDao.delete(id);
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

	public void deleteArtWorksMediums(Serializable[] ids) throws ServiceException {
		try {
			artWorksMediumDao.deleteAll(ids);
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

	public PageQuery queryArtWorksMedium(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artWorksMediumList", pageQuery);
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

      String paras = " 1=1 ";
      Map<String, String> map = page.getParameters();

      String worksId = map.get("worksId");
      if (worksId != null && worksId.trim().length() > 0) {
         paras += " AND medium.works_id=" + worksId.trim();
      }
      
      String literatureTitle = map.get("literatureTitle");
      if (literatureTitle != null && literatureTitle.trim().length() > 0) {
         paras += " AND literature.literature_title like '%" + literatureTitle.trim() + "%'";
      }
      
      String literatureWorks = map.get("literatureWorks");
      if (literatureWorks != null && literatureWorks.trim().length() > 0) {
         paras += " AND literature.literature_works like '%" + literatureWorks.trim() + "%'";
      }
      
      String personInvolved = map.get("personInvolved");
      if (personInvolved != null && personInvolved.trim().length() > 0) {
         paras += " AND literature.person_involved like '%" + personInvolved.trim() + "%'";
      }
      
      String relatedExhib = map.get("relatedExhib");
      if (relatedExhib != null && relatedExhib.trim().length() > 0) {
         paras += " AND literature.related_exhib like '%" + relatedExhib.trim() + "%'";
      }
      
      String relatedEvent = map.get("relatedEvent");
      if (relatedExhib != null && relatedExhib.trim().length() > 0) {
         paras += " AND literature.related_event like '%" + relatedEvent.trim() + "%'";
      }
      
      if (paras.length() > 0) page.getParameters().put("paras", paras);
   
   
	}
	
    @Resource
	private ArtWorksMediumDao	artWorksMediumDao;

	public void setArtWorksMediumDao(ArtWorksMediumDao artWorksMediumDao) {
		this.artWorksMediumDao = artWorksMediumDao;
	}
}


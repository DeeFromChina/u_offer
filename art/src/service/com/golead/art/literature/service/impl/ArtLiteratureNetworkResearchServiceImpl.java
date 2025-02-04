package com.golead.art.literature.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

import com.golead.art.literature.dao.ArtLiteratureNetworkResearchDao;
import com.golead.art.literature.model.ArtLiteratureNetworkResearch;
import com.golead.art.literature.service.ArtLiteratureNetworkResearchService;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;

@Service
public class ArtLiteratureNetworkResearchServiceImpl extends BaseServiceImpl implements ArtLiteratureNetworkResearchService {
	private static final long serialVersionUID = 1L;

	public ArtLiteratureNetworkResearch getArtLiteratureNetworkResearch(Serializable id) throws ServiceException {
		try {
			return artLiteratureNetworkResearchDao.get(id);
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

	public void createArtLiteratureNetworkResearch(ArtLiteratureNetworkResearch artLiteratureNetworkResearch) throws ServiceException {
		try {
			artLiteratureNetworkResearchDao.save(artLiteratureNetworkResearch);
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

	public void updateArtLiteratureNetworkResearch(Map<String, String> artLiteratureNetworkResearchMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artLiteratureNetworkResearchMap.get(ArtLiteratureNetworkResearch.PROP_ID));
			ArtLiteratureNetworkResearch tmp = artLiteratureNetworkResearchDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artLiteratureNetworkResearchMap, true);			
			artLiteratureNetworkResearchDao.update(tmp);
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

	public void deleteArtLiteratureNetworkResearch(Serializable id) throws ServiceException {
		try {
			artLiteratureNetworkResearchDao.delete(id);
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

	public void deleteArtLiteratureNetworkResearchs(Serializable[] ids) throws ServiceException {
		try {
			artLiteratureNetworkResearchDao.deleteAll(ids);
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

	public PageQuery queryArtLiteratureNetworkResearch(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artLiteratureNetworkResearchList", pageQuery);
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
	}
	
	public ArtLiteratureNetworkResearch findByNetworkId(Serializable id) throws ServiceException{
	   try {
         List<ArtLiteratureNetworkResearch> artLiteratureNetworkResearchs = artLiteratureNetworkResearchDao.findByField(ArtLiteratureNetworkResearch.PROP_NETWORK_ID, id);
         if(artLiteratureNetworkResearchs.size() == 1){
            return artLiteratureNetworkResearchs.get(0);
         }
         return null;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
	public void updateArtLiteratureNetworkResearch(ArtLiteratureNetworkResearch artLiteratureNetworkResearch) throws ServiceException{
	   try {
         artLiteratureNetworkResearchDao.update(artLiteratureNetworkResearch);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
	public void deleteByNetworkId(Serializable id) throws ServiceException{
	   try {
         artLiteratureNetworkResearchDao.deleteByField(ArtLiteratureNetworkResearch.PROP_NETWORK_ID, id);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
    @Resource
	private ArtLiteratureNetworkResearchDao	artLiteratureNetworkResearchDao;

	public void setArtLiteratureNetworkResearchDao(ArtLiteratureNetworkResearchDao artLiteratureNetworkResearchDao) {
		this.artLiteratureNetworkResearchDao = artLiteratureNetworkResearchDao;
	}
}


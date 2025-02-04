package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.works.dao.ArtWorksNetworkDao;
import com.golead.art.works.model.ArtWorksNetwork;
import com.golead.art.works.service.ArtWorksNetworkService;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;

@Service
public class ArtWorksNetworkServiceImpl extends BaseServiceImpl implements ArtWorksNetworkService {
	private static final long serialVersionUID = 1L;

	public ArtWorksNetwork getArtWorksNetwork(Serializable id) throws ServiceException {
		try {
			return artWorksNetworkDao.get(id);
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

	public void createArtWorksNetwork(ArtWorksNetwork artWorksNetwork) throws ServiceException {
		try {
			artWorksNetworkDao.save(artWorksNetwork);
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
	
	public void createArtWorksNetwork(int id, int[] ids) throws ServiceException{
	   try {
         for(int i = 0; i<ids.length; i++){
            ArtWorksNetwork artWorksNetwork = new ArtWorksNetwork();
            artWorksNetwork.setWorksId(id);
            artWorksNetwork.setNetworkId(ids[i]);
            createArtWorksNetwork(artWorksNetwork);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}

	public void updateArtWorksNetwork(Map<String, String> artWorksNetworkMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artWorksNetworkMap.get(ArtWorksNetwork.PROP_ID));
			ArtWorksNetwork tmp = artWorksNetworkDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artWorksNetworkMap, true);			
			artWorksNetworkDao.update(tmp);
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

	public void deleteArtWorksNetwork(Serializable id) throws ServiceException {
		try {
			artWorksNetworkDao.delete(id);
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

	public void deleteArtWorksNetworks(Serializable[] ids) throws ServiceException {
		try {
			artWorksNetworkDao.deleteAll(ids);
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

	public PageQuery queryArtWorksNetwork(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artWorksNetworkList", pageQuery);
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
         paras += " AND network.works_id=" + worksId.trim();
      }
      
      String literatureAuther = map.get("literatureAuther");
      if (literatureAuther != null && literatureAuther.trim().length() > 0) {
         paras += " AND literature.literature_auther like '%" + literatureAuther.trim() + "%'";
      }
      
      String literatureTitle = map.get("literatureTitle");
      if (literatureTitle != null && literatureTitle.trim().length() > 0) {
         paras += " AND literature.literature_title like '%" + literatureTitle.trim() + "%'";
      }

      String literatureWorks = map.get("literatureWorks");
      if (literatureWorks != null && literatureWorks.trim().length() > 0) {
         paras += " AND literature.literature_works like '%" + literatureWorks.trim() + "%'";
      }
      
      String quoteLiterature = map.get("quoteLiterature");
      if (quoteLiterature != null && quoteLiterature.trim().length() > 0) {
         paras += " AND literature.quote_literature like '%" + quoteLiterature.trim() + "%'";
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
	private ArtWorksNetworkDao	artWorksNetworkDao;

	public void setArtWorksNetworkDao(ArtWorksNetworkDao artWorksNetworkDao) {
		this.artWorksNetworkDao = artWorksNetworkDao;
	}
}


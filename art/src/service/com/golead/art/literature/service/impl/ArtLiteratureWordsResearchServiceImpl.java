package com.golead.art.literature.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

import com.golead.art.literature.dao.ArtLiteratureWordsResearchDao;
import com.golead.art.literature.model.ArtLiteratureWordsResearch;
import com.golead.art.literature.service.ArtLiteratureWordsResearchService;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;

@Service
public class ArtLiteratureWordsResearchServiceImpl extends BaseServiceImpl implements ArtLiteratureWordsResearchService {
	private static final long serialVersionUID = 1L;

	public ArtLiteratureWordsResearch getArtLiteratureWordsResearch(Serializable id) throws ServiceException {
		try {
			return artLiteratureWordsResearchDao.get(id);
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

	public void createArtLiteratureWordsResearch(ArtLiteratureWordsResearch artLiteratureWordsResearch) throws ServiceException {
		try {
			artLiteratureWordsResearchDao.save(artLiteratureWordsResearch);
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

	public void updateArtLiteratureWordsResearch(Map<String, String> artLiteratureWordsResearchMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artLiteratureWordsResearchMap.get(ArtLiteratureWordsResearch.PROP_ID));
			ArtLiteratureWordsResearch tmp = artLiteratureWordsResearchDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artLiteratureWordsResearchMap, true);			
			artLiteratureWordsResearchDao.update(tmp);
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

	public void deleteArtLiteratureWordsResearch(Serializable id) throws ServiceException {
		try {
			artLiteratureWordsResearchDao.delete(id);
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

	public void deleteArtLiteratureWordsResearchs(Serializable[] ids) throws ServiceException {
		try {
			artLiteratureWordsResearchDao.deleteAll(ids);
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

	public PageQuery queryArtLiteratureWordsResearch(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artLiteratureWordsResearchList", pageQuery);
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
	
	public ArtLiteratureWordsResearch findByWordsId(Serializable id) throws ServiceException{
	   try {
         List<ArtLiteratureWordsResearch> artLiteratureWordsResearchs = artLiteratureWordsResearchDao.findByField(ArtLiteratureWordsResearch.PROP_WORDS_ID, id);
         if(artLiteratureWordsResearchs.size() == 1){
            return artLiteratureWordsResearchs.get(0);
         }
         return null;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
	public void deleteByWordsId(Serializable id) throws ServiceException{
	   try {
         artLiteratureWordsResearchDao.deleteByField(ArtLiteratureWordsResearch.PROP_WORDS_ID, id);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
	public void updateArtLiteratureWordsResearch(ArtLiteratureWordsResearch artLiteratureWordsResearch) throws ServiceException{
	   try {
	      artLiteratureWordsResearchDao.update(artLiteratureWordsResearch);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
    @Resource
	private ArtLiteratureWordsResearchDao	artLiteratureWordsResearchDao;

	public void setArtLiteratureWordsResearchDao(ArtLiteratureWordsResearchDao artLiteratureWordsResearchDao) {
		this.artLiteratureWordsResearchDao = artLiteratureWordsResearchDao;
	}
}


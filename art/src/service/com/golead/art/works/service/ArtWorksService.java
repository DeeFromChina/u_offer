package com.golead.art.works.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.golead.art.works.model.ArtWorks;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.form.BaseForm;
import com.golead.core.web.form.QueryForm;

public interface ArtWorksService {
   
	public ArtWorks getArtWorks(Serializable id) throws ServiceException;

	public void createArtWorks(ArtWorks artWorks) throws ServiceException;

	public void updateArtWorks(Map<String, String> artWorksMap,List<File> files,String fileFileName,String FILE_PATH) throws ServiceException;
	
	public void updateArtWorks(ArtWorks artWorks) throws ServiceException;

	public void deleteArtWorks(Serializable id) throws ServiceException;

	public PageQuery queryArtWorks(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorkss(Serializable[] ids) throws ServiceException;  
	
	public String importArtWorks(String path) throws ServiceException;
	
	public List<ArtWorks> findAll() throws ServiceException;
	
	public Map<String, String> changeArtWorks(QueryForm form) throws ServiceException;
	
	public void saveThumbnail(ArtWorks artWorks,String filesFileName,String FILE_PATH,List<File> files) throws ServiceException;
	
	public int findByCountry(String ids) throws ServiceException;
	
	public String getMaxWorksNo(int artistId) throws ServiceException;
	
	public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException;
	
	public int findByMedium(String id) throws ServiceException;
	
	public List<Map<String, Object>> findAllWorks() throws ServiceException;
	
	public void batchUpdateWorks(String[] sqls) throws ServiceException;
}


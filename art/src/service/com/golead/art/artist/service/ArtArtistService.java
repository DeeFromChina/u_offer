package com.golead.art.artist.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.golead.core.exception.ServiceException;
import com.golead.core.web.form.BaseForm;
import com.golead.core.web.form.QueryForm;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.artist.model.ArtArtist;

public interface ArtArtistService {

	public ArtArtist getArtArtist(Serializable id) throws ServiceException;

	public void createArtArtist(ArtArtist artArtist, File files, String filesName) throws ServiceException;

	public void updateArtArtist(Map<String, String> artArtistMap, File files, String filesName, String path) throws ServiceException;

	public void deleteArtArtist(Serializable id) throws ServiceException;

	public PageQuery queryArtArtist(PageQuery pageQuery) throws ServiceException;

	public void deleteArtArtists(Serializable[] ids) throws ServiceException;  
	
	public String deleteAllWithArtArtists(Serializable[] ids) throws ServiceException;
	
	public List<Map<String, Object>> queryArtArtistInformation(Map<String, String> parameters) throws ServiceException;
	
	public List<ArtArtist> findAllArtArtist() throws ServiceException;  
	
	public String importArtArtist(String path) throws ServiceException;
	
	public Map<String, String> changeArtWorks(QueryForm form) throws ServiceException;
	
	public int findByCountry(String ids) throws ServiceException;
	
	public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException;
}


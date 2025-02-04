package com.golead.art.works.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.works.model.ArtWorksExhibit;

public interface ArtWorksExhibitService {

	public ArtWorksExhibit getArtWorksExhibit(Serializable id) throws ServiceException;

	public void createArtWorksExhibit(ArtWorksExhibit artWorksExhibit) throws ServiceException;
	
	public void createArtWorksExhibit(int worksId, int[] exhibitId) throws ServiceException;
	
	public void createArtWorksExhibit2(int exhibitId, int[] worksId) throws ServiceException;

	public void updateArtWorksExhibit(Map<String, String> artWorksExhibitMap) throws ServiceException;

	public void deleteArtWorksExhibit(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksExhibit(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksExhibits(Serializable[] ids) throws ServiceException;  
	
	public String findArtistNameByExhibit(String id) throws ServiceException;  
}


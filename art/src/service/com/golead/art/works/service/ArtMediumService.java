package com.golead.art.works.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.works.model.ArtMedium;

public interface ArtMediumService {

	public ArtMedium getArtMedium(Serializable id) throws ServiceException;

	public void createArtMedium(ArtMedium artMedium) throws ServiceException;

	public void updateArtMedium(Map<String, String> artMediumMap) throws ServiceException;

	public void deleteArtMedium(Serializable id) throws ServiceException;

	public PageQuery queryArtMedium(PageQuery pageQuery) throws ServiceException;

	public void deleteArtMediums(Serializable[] ids) throws ServiceException;
	
	public List<ArtMedium> findBySide(String side) throws ServiceException;
	
	public ArtMedium findByTreeCode(String treeCode) throws ServiceException;
	
	public List<ArtMedium> findAll() throws ServiceException;
	
	public List<ArtMedium> findByCategory(String category) throws ServiceException;
	
	public List<ArtMedium> findChild(Serializable id) throws ServiceException;
}


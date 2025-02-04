package com.golead.art.works.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.works.model.ArtWorksStyle;

public interface ArtWorksStyleService {

	public ArtWorksStyle getArtWorksStyle(Serializable id) throws ServiceException;

	public void createArtWorksStyle(ArtWorksStyle artWorksStyle) throws ServiceException;

	public void updateArtWorksStyle(Map<String, String> artWorksStyleMap) throws ServiceException;

	public void deleteArtWorksStyle(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksStyle(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksStyles(Serializable[] ids) throws ServiceException;
	
	public List<ArtWorksStyle> findAll() throws ServiceException;  
}


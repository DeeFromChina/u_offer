package com.golead.art.works.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.works.model.ArtWorksTheme;

public interface ArtWorksThemeService {

	public ArtWorksTheme getArtWorksTheme(Serializable id) throws ServiceException;

	public void createArtWorksTheme(ArtWorksTheme artWorksTheme) throws ServiceException;

	public void updateArtWorksTheme(Map<String, String> artWorksThemeMap) throws ServiceException;

	public void deleteArtWorksTheme(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksTheme(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksThemes(Serializable[] ids) throws ServiceException;  
	
	public List<ArtWorksTheme> findAll() throws ServiceException;
}


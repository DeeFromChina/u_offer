package com.golead.art.works.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.works.model.ArtWorksCase;

public interface ArtWorksCaseService {

	public ArtWorksCase getArtWorksCase(Serializable id) throws ServiceException;

	public void createArtWorksCase(ArtWorksCase artWorksCase, List<File> files, String fileNames, String path) throws ServiceException;

	public void updateArtWorksCase(Map<String, String> artWorksCaseMap, List<File> files, String fileNames, String addFileName, String path) throws ServiceException;

	public void deleteArtWorksCase(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksCase(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksCases(Serializable[] ids, String path) throws ServiceException;
	
	public void deleteArtWorksCases(Serializable[] ids) throws ServiceException; 
	
	public String importArtWorksCase(String path, Integer id) throws ServiceException;
	
	public void createArtWorksCase(ArtWorksCase artWorksCase) throws ServiceException;
	
	public List<ArtWorksCase> findByWorksId(Serializable id) throws ServiceException;
}


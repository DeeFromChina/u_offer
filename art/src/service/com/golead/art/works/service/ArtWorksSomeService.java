package com.golead.art.works.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.works.model.ArtWorksSome;

public interface ArtWorksSomeService {

	public ArtWorksSome getArtWorksSome(Serializable id) throws ServiceException;

	public void createArtWorksSome(ArtWorksSome artWorksSome) throws ServiceException;

	public void updateArtWorksSome(Map<String, String> artWorksSomeMap) throws ServiceException;

	public void deleteArtWorksSome(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksSome(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksSomes(Serializable[] ids) throws ServiceException; 
	
	public List<ArtWorksSome> findByWorksId(Serializable id) throws ServiceException;
	
	public void createArtWorksSome(Serializable[] ids, String worksId) throws ServiceException; 
	
	public String importArtWorksSome(String path, Integer id) throws ServiceException;
}


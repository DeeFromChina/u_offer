package com.golead.art.works.service;

import java.io.Serializable;
import java.util.Map;

import org.apache.poi.ss.formula.ptg.IntPtg;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.works.model.ArtWorksMedium;

public interface ArtWorksMediumService {

	public ArtWorksMedium getArtWorksMedium(Serializable id) throws ServiceException;

	public void createArtWorksMedium(ArtWorksMedium artWorksMedium) throws ServiceException;
	
	public void createArtWorksMedium(int id, int[] ids) throws ServiceException;

	public void updateArtWorksMedium(Map<String, String> artWorksMediumMap) throws ServiceException;

	public void deleteArtWorksMedium(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksMedium(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksMediums(Serializable[] ids) throws ServiceException;  
}


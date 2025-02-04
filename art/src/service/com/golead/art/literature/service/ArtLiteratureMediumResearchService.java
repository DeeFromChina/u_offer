package com.golead.art.literature.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.literature.model.ArtLiteratureMediumResearch;

public interface ArtLiteratureMediumResearchService {

	public ArtLiteratureMediumResearch getArtLiteratureMediumResearch(Serializable id) throws ServiceException;

	public void createArtLiteratureMediumResearch(ArtLiteratureMediumResearch artLiteratureMediumResearch) throws ServiceException;

	public void updateArtLiteratureMediumResearch(Map<String, String> artLiteratureMediumResearchMap) throws ServiceException;

	public void deleteArtLiteratureMediumResearch(Serializable id) throws ServiceException;

	public PageQuery queryArtLiteratureMediumResearch(PageQuery pageQuery) throws ServiceException;

	public void deleteArtLiteratureMediumResearchs(Serializable[] ids) throws ServiceException;
	
	public ArtLiteratureMediumResearch findByMediumId(Serializable id) throws ServiceException;
	
	public void updateArtLiteratureMediumResearch(ArtLiteratureMediumResearch artLiteratureMediumResearch) throws ServiceException;
	
	public void deleteByMediumId(Serializable id) throws ServiceException;
}


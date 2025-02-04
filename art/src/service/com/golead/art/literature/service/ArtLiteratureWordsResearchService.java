package com.golead.art.literature.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.literature.model.ArtLiteratureWordsResearch;

public interface ArtLiteratureWordsResearchService {

	public ArtLiteratureWordsResearch getArtLiteratureWordsResearch(Serializable id) throws ServiceException;

	public void createArtLiteratureWordsResearch(ArtLiteratureWordsResearch artLiteratureWordsResearch) throws ServiceException;

	public void updateArtLiteratureWordsResearch(Map<String, String> artLiteratureWordsResearchMap) throws ServiceException;

	public void deleteArtLiteratureWordsResearch(Serializable id) throws ServiceException;

	public PageQuery queryArtLiteratureWordsResearch(PageQuery pageQuery) throws ServiceException;

	public void deleteArtLiteratureWordsResearchs(Serializable[] ids) throws ServiceException;
	
	public ArtLiteratureWordsResearch findByWordsId(Serializable id) throws ServiceException;
	
	public void deleteByWordsId(Serializable id) throws ServiceException;
	
	public void updateArtLiteratureWordsResearch(ArtLiteratureWordsResearch artLiteratureWordsResearch) throws ServiceException;
}


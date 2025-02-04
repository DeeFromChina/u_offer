package com.golead.art.literature.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.literature.model.ArtLiteratureNetworkResearch;

public interface ArtLiteratureNetworkResearchService {

	public ArtLiteratureNetworkResearch getArtLiteratureNetworkResearch(Serializable id) throws ServiceException;

	public void createArtLiteratureNetworkResearch(ArtLiteratureNetworkResearch artLiteratureNetworkResearch) throws ServiceException;

	public void updateArtLiteratureNetworkResearch(Map<String, String> artLiteratureNetworkResearchMap) throws ServiceException;

	public void deleteArtLiteratureNetworkResearch(Serializable id) throws ServiceException;

	public PageQuery queryArtLiteratureNetworkResearch(PageQuery pageQuery) throws ServiceException;

	public void deleteArtLiteratureNetworkResearchs(Serializable[] ids) throws ServiceException;
	
	public ArtLiteratureNetworkResearch findByNetworkId(Serializable id) throws ServiceException;
	
	public void updateArtLiteratureNetworkResearch(ArtLiteratureNetworkResearch artLiteratureNetworkResearch) throws ServiceException;
	
	public void deleteByNetworkId(Serializable id) throws ServiceException;
}


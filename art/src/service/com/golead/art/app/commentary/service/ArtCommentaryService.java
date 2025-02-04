package com.golead.art.app.commentary.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.app.commentary.model.ArtCommentary;

public interface ArtCommentaryService {

	public ArtCommentary getArtCommentary(Serializable id) throws ServiceException;

	public void createArtCommentary(ArtCommentary artCommentary) throws ServiceException;

	public void updateArtCommentary(Map<String, String> artCommentaryMap) throws ServiceException;
	
	public void updateArtCommentary(ArtCommentary artCommentary) throws ServiceException;

	public void deleteArtCommentary(Serializable id) throws ServiceException;

	public PageQuery queryArtCommentary(PageQuery pageQuery) throws ServiceException;

	public PageQuery queryArtFeedback(PageQuery pageQuery) throws ServiceException;
	
	public void deleteArtCommentarys(Serializable[] ids) throws ServiceException;  
}


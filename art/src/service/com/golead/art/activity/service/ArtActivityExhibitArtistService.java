package com.golead.art.activity.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.activity.model.ArtActivityExhibitArtist;

public interface ArtActivityExhibitArtistService {

	public ArtActivityExhibitArtist getArtActivityExhibitArtist(Serializable id) throws ServiceException;

	public void createArtActivityExhibitArtist(Integer id, int[] ids) throws ServiceException;

	public void updateArtActivityExhibitArtist(Map<String, String> artActivityExhibitArtistMap) throws ServiceException;

	public void deleteArtActivityExhibitArtist(Serializable id) throws ServiceException;

	public PageQuery queryArtActivityExhibitArtist(PageQuery pageQuery) throws ServiceException;

	public void deleteArtActivityExhibitArtists(Serializable[] ids) throws ServiceException;  
	
	public ArtActivityExhibitArtist findByExhibitId(Serializable id) throws ServiceException;
}


package com.golead.art.artist.service;

import java.io.Serializable;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.artist.model.ArtArtistHonors;

public interface ArtArtistHonorsService {

	public ArtArtistHonors getArtArtistHonors(Serializable id) throws ServiceException;

	public void createArtArtistHonors(ArtArtistHonors artArtistHonors) throws ServiceException;

	public void updateArtArtistHonors(Map<String, String> artArtistHonorsMap) throws ServiceException;

	public void deleteArtArtistHonors(Serializable id) throws ServiceException;

	public PageQuery queryArtArtistHonors(PageQuery pageQuery) throws ServiceException;

	public void deleteArtArtistHonorss(Serializable[] ids) throws ServiceException;  
	
	public void deleteArtArtistWithHonorss(Serializable[] ids) throws ServiceException;
	
	public String importHonors(String path,int artistId) throws ServiceException;
}


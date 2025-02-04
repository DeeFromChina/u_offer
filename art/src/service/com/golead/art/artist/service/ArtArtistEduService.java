package com.golead.art.artist.service;

import java.io.Serializable;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.artist.model.ArtArtistEdu;

public interface ArtArtistEduService {

	public ArtArtistEdu getArtArtistEdu(Serializable id) throws ServiceException;

	public void createArtArtistEdu(ArtArtistEdu artArtistEdu) throws ServiceException;

	public void updateArtArtistEdu(Map<String, String> artArtistEduMap) throws ServiceException;

	public void deleteArtArtistEdu(Serializable id) throws ServiceException;

	public PageQuery queryArtArtistEdu(PageQuery pageQuery) throws ServiceException;

	public void deleteArtArtistEdus(Serializable[] ids) throws ServiceException;  
	
	public void deleteArtArtistWithEdus(Serializable[] ids) throws ServiceException;
	
	public String importEdu(String path,int artistId) throws ServiceException;
}


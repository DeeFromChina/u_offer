package com.golead.art.app.appUser.service;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.app.appUser.model.ArtAppUser;

public interface ArtAppUserService {

	public ArtAppUser getArtAppUser(Serializable id) throws ServiceException;

	public void createArtAppUser(ArtAppUser artAppUser) throws ServiceException;

	public void updateArtAppUser(Map<String, String> artAppUserMap) throws ServiceException;

	public void updateArtAppUser(ArtAppUser artAppUser) throws ServiceException;

	public void deleteArtAppUser(Serializable id) throws ServiceException;

	public PageQuery queryArtAppUser(PageQuery pageQuery) throws ServiceException;

	public void deleteArtAppUsers(Serializable[] ids) throws ServiceException;  
	
	public boolean existsArtAppUsers(String account) throws ServiceException;
	
	public void createArtAppUser(Map<String, String> artAppUserMap, File file) throws ServiceException;  
}


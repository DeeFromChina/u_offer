package com.golead.art.photo.service;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.photo.model.ArtPhoto;

public interface ArtPhotoService {

	public ArtPhoto getArtPhoto(Serializable id) throws ServiceException;

	public void createArtPhoto(Map<String, String> artPhotoMap, File file, String fileName, String path) throws ServiceException;

	public void updateArtPhoto(Map<String, String> artPhotoMap, File file, String fileName, String path) throws ServiceException;

	public void deleteArtPhoto(Serializable id) throws ServiceException;

	public PageQuery queryArtPhoto(PageQuery pageQuery) throws ServiceException;

	public void deleteArtPhotos(Serializable[] ids, String path) throws ServiceException;  
}


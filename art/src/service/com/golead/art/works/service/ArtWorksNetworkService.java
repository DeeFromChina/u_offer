package com.golead.art.works.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.works.model.ArtWorksNetwork;

public interface ArtWorksNetworkService {

	public ArtWorksNetwork getArtWorksNetwork(Serializable id) throws ServiceException;

	public void createArtWorksNetwork(ArtWorksNetwork artWorksNetwork) throws ServiceException;
	
	public void createArtWorksNetwork(int id, int[] ids) throws ServiceException;

	public void updateArtWorksNetwork(Map<String, String> artWorksNetworkMap) throws ServiceException;

	public void deleteArtWorksNetwork(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksNetwork(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksNetworks(Serializable[] ids) throws ServiceException;  
}


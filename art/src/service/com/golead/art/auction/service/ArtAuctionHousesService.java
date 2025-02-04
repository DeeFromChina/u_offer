package com.golead.art.auction.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.golead.core.exception.ServiceException;
import com.golead.core.web.form.BaseForm;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.auction.model.ArtAuctionHouses;

public interface ArtAuctionHousesService {

	public ArtAuctionHouses getArtAuctionHouses(Serializable id) throws ServiceException;

	public void createArtAuctionHouses(ArtAuctionHouses artAuctionHouses) throws ServiceException;

	public void updateArtAuctionHouses(Map<String, String> artAuctionHousesMap) throws ServiceException;

	public void deleteArtAuctionHouses(Serializable id) throws ServiceException;

	public PageQuery queryArtAuctionHouses(PageQuery pageQuery) throws ServiceException;

	public String deleteArtAuctionHousess(Serializable[] ids) throws ServiceException;  
	
	public String importHouses(String path) throws ServiceException;
	
	public List<ArtAuctionHouses> getAllHouses() throws ServiceException;
	
	public int findByCountry(String ids) throws ServiceException;
	
	public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException;
}


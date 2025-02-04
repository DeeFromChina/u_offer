package com.golead.art.activity.service;

import java.io.Serializable;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.golead.core.exception.ServiceException;
import com.golead.core.web.form.BaseForm;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.activity.model.ArtActivityExhibit;

public interface ArtActivityExhibitService {

	public ArtActivityExhibit getArtActivityExhibit(Serializable id) throws ServiceException;

	public int createArtActivityExhibit(Map<String, String> artActivityExhibitMap) throws ServiceException;

	public void updateArtActivityExhibit(Map<String, String> artActivityExhibitMap) throws ServiceException;

	public void deleteArtActivityExhibit(Serializable id) throws ServiceException;

	public PageQuery queryArtActivityExhibit(PageQuery pageQuery) throws ServiceException;

	public void deleteArtActivityExhibits(Serializable[] ids) throws ServiceException;
	
	public int findByCountry(String ids) throws ServiceException;
	
	public int findByAgency(String ids) throws ServiceException;
	
	public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException;
}


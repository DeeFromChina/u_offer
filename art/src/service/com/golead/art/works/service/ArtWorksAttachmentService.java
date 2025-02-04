package com.golead.art.works.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.works.model.ArtWorksAttachment;

public interface ArtWorksAttachmentService {

	public ArtWorksAttachment getArtWorksAttachment(Serializable id) throws ServiceException;

	public void createArtWorksAttachment(ArtWorksAttachment artWorksAttachment) throws ServiceException;

	public void updateArtWorksAttachment(Map<String, String> artWorksAttachmentMap) throws ServiceException;

	public void deleteArtWorksAttachment(Serializable id) throws ServiceException;

	public PageQuery queryArtWorksAttachment(PageQuery pageQuery) throws ServiceException;

	public void deleteArtWorksAttachments(Serializable[] ids) throws ServiceException;
	
	public ArtWorksAttachment findByWorksId(Serializable id) throws ServiceException;
	
	public void deleteByWorksId(Serializable id) throws ServiceException;
}


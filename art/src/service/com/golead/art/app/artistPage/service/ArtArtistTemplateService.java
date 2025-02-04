package com.golead.art.app.artistPage.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.art.app.artistPage.model.ArtArtistTemplate;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;

public interface ArtArtistTemplateService {

   public ArtArtistTemplate getArtArtistTemplate(Serializable id) throws ServiceException;

   public void createArtArtistTemplate(ArtArtistTemplate artArtistTemplate) throws ServiceException;

   public void updateArtArtistTemplate(Map<String, String> artArtistTemplateMap) throws ServiceException;

   public void deleteArtArtistTemplate(Serializable id) throws ServiceException;

   public PageQuery queryArtArtistTemplate(PageQuery pageQuery) throws ServiceException;

   public void deleteArtArtistTemplates(Serializable[] ids) throws ServiceException;

}

package com.golead.art.publication.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.publication.model.ArtPublicationWorks;

public interface ArtPublicationWorksService {

   public ArtPublicationWorks getArtPublicationWorks(Serializable id) throws ServiceException;

   public void createArtPublicationWorks(ArtPublicationWorks artPublicationWorks) throws ServiceException;

   public void updateArtPublicationWorks(Map<String, String> artPublicationWorksMap) throws ServiceException;

   public void deleteArtPublicationWorks(Serializable id) throws ServiceException;

   public PageQuery queryArtPublicationWorks(PageQuery pageQuery) throws ServiceException;

   public void deleteArtPublicationWorkss(Serializable[] ids) throws ServiceException;

   public void createArtPublicationWorks(int[] ids, int pubId) throws ServiceException;
}

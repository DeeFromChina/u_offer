package com.golead.art.works.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.works.model.ArtPublicationArtist;

public interface ArtPublicationArtistService {

   public ArtPublicationArtist getArtPublicationArtist(Serializable id) throws ServiceException;

   public void createArtPublicationArtist(ArtPublicationArtist artPublicationArtist) throws ServiceException;

   public void createArtPublicationArtist(List<ArtPublicationArtist> artPublicationArtist) throws ServiceException;

   public void updateArtPublicationArtist(Map<String, String> artPublicationArtistMap) throws ServiceException;

   public void deleteArtPublicationArtist(Serializable id) throws ServiceException;

   public PageQuery queryArtPublicationArtist(PageQuery pageQuery) throws ServiceException;

   public PageQuery queryArtist(PageQuery pageQuery) throws ServiceException;

   public void deleteArtPublicationArtists(Serializable[] ids) throws ServiceException;
}

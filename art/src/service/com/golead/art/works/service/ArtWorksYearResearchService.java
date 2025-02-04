package com.golead.art.works.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.works.model.ArtWorksYearResearch;

public interface ArtWorksYearResearchService {

   public ArtWorksYearResearch getArtWorksYearResearch(Serializable id) throws ServiceException;

   public void createArtWorksYearResearch(ArtWorksYearResearch artWorksYearResearch) throws ServiceException;

   public void createArtWorksYearResearch(List<ArtWorksYearResearch> artWorksYearResearch) throws ServiceException;

   public void updateArtWorksYearResearch(Map<String, String> artWorksYearResearchMap) throws ServiceException;

   public void deleteArtWorksYearResearch(Serializable id) throws ServiceException;

   public PageQuery queryArtWorksYearResearch(PageQuery pageQuery) throws ServiceException;

   public void deleteArtWorksYearResearchs(Serializable[] ids) throws ServiceException;
}

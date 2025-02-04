package com.golead.art.literature.service;

import java.io.Serializable;
import java.util.Map;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.literature.model.ArtLiteratureOtherResearch;

public interface ArtLiteratureOtherResearchService {

   public ArtLiteratureOtherResearch getArtLiteratureOtherResearchByArtLiteratureOtherId(Serializable artLiteratureOtherId) throws ServiceException;

   public ArtLiteratureOtherResearch getArtLiteratureOtherResearch(Serializable id) throws ServiceException;

   public void createArtLiteratureOtherResearch(ArtLiteratureOtherResearch artLiteratureOtherResearch) throws ServiceException;

   public void updateArtLiteratureOtherResearch(Map<String, String> artLiteratureOtherResearchMap) throws ServiceException;

   public void updateArtLiteratureOtherResearch(ArtLiteratureOtherResearch artLiteratureOtherResearch) throws ServiceException;

   public void deleteArtLiteratureOtherResearch(Serializable id) throws ServiceException;

   public void deleteArtLiteratureOtherResearchByOtherId(Serializable otherId) throws ServiceException;

   public PageQuery queryArtLiteratureOtherResearch(PageQuery pageQuery) throws ServiceException;

   public PageQuery queryArtLiteratureOtherRelatedWorks(PageQuery pageQuery) throws ServiceException;

   public void deleteArtLiteratureOtherResearchs(Serializable[] ids) throws ServiceException;

}

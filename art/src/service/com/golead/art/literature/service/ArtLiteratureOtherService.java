package com.golead.art.literature.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.art.literature.model.ArtLiteratureMedium;
import com.golead.art.literature.model.ArtLiteratureMediumResearch;
import com.golead.art.literature.model.ArtLiteratureOtherResearch;
import com.golead.core.exception.ServiceException;
import com.golead.core.dao.model.PageQuery;

import com.golead.art.literature.model.ArtLiteratureOther;

public interface ArtLiteratureOtherService {

   public ArtLiteratureOther getArtLiteratureOther(Serializable id) throws ServiceException;

   public void createArtLiteratureOther(ArtLiteratureOther artLiteratureOther) throws ServiceException;

   public void updateArtLiteratureOther(Map<String, String> artLiteratureOtherMap) throws ServiceException;

   public void updateArtLiteratureOther(ArtLiteratureOther artLiteratureOther) throws ServiceException;

   public void updateArtLiteratureOther(Map<String, String> record, List<File> files, String[] filesFileName, String[] attachmentName, String path)
         throws ServiceException;

   public void deleteArtLiteratureOther(Serializable id) throws ServiceException;

   public PageQuery queryArtLiteratureOther(PageQuery pageQuery) throws ServiceException;

   public void deleteArtLiteratureOthers(Serializable[] ids) throws ServiceException;

   public boolean saveFile(ArtLiteratureOther artLiteratureOther, List<File> files, String filesFileName, String FILE_PATH) throws ServiceException;

   public void updateFile(ArtLiteratureOtherResearch artLiteratureOtherResearch, ArtLiteratureOther artLiteratureOther, List<File> files, String filesFileName,
         String FILE_PATH, String fileName) throws ServiceException;
}

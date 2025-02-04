package com.golead.art.publication.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.golead.core.exception.ServiceException;
import com.golead.core.web.form.BaseForm;
import com.golead.core.dao.model.PageQuery;
import com.golead.art.publication.model.ArtPublication;

public interface ArtPublicationService {

   public ArtPublication getArtPublication(Serializable id) throws ServiceException;

   public void createArtPublication(ArtPublication artPublication) throws ServiceException;

   public void createArtPublication(ArtPublication artPublication, File cover, String coverFileName, File backCover, String backCoverFileName, List<File> files,
         String[] filesFileName, String filePath) throws ServiceException;

   public void updateArtPublication(Map<String, String> artPublicationMap, File cover, String coverFileName, File backCover, String backCoverFileName,
         List<File> files, String[] filesFileName, String oldCatalogOther, String filePath) throws ServiceException;

   public void deleteArtPublication(Serializable id) throws ServiceException;

   public void deleteArtPublications(Serializable[] ids, String path) throws ServiceException;

   public PageQuery queryArtPublication(PageQuery pageQuery) throws ServiceException;

   public List<ArtPublication> findAll() throws ServiceException;

   public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException;
}

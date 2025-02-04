package com.golead.art.artist.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.artist.dao.ArtArtistExperienceDao;
import com.golead.art.artist.model.ArtArtistExperience;
import com.golead.art.artist.service.ArtArtistExperienceService;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class ArtArtistExperienceServiceImpl extends BaseServiceImpl implements ArtArtistExperienceService {
	private static final long serialVersionUID = 1L;

	public ArtArtistExperience getArtArtistExperience(Serializable id) throws ServiceException {
		try {
			return artArtistExperienceDao.get(id);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void createArtArtistExperience(ArtArtistExperience artArtistExperience) throws ServiceException {
		try {
			artArtistExperienceDao.save(artArtistExperience);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void updateArtArtistExperience(Map<String, String> artArtistExperienceMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artArtistExperienceMap.get(ArtArtistExperience.PROP_ID));
			ArtArtistExperience tmp = artArtistExperienceDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artArtistExperienceMap, true);			
			artArtistExperienceDao.update(tmp);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void deleteArtArtistExperience(Serializable id) throws ServiceException {
		try {
			artArtistExperienceDao.delete(id);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void deleteArtArtistExperiences(Serializable[] ids) throws ServiceException {
		try {
			artArtistExperienceDao.deleteAll(ids);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void deleteArtArtistWithExperiences(Serializable[] ids) throws ServiceException {
		try {
			for(Serializable id : ids) {
				artArtistExperienceDao.deleteByField("artId", id);
			}
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("数据库操作错误。");
		} catch (ServiceException se) {
			se.printStackTrace();
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("系统错误。");
		}
	}
	
	public String importExperience(String path,int artistId) throws ServiceException {
		String message = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(path);
			HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
			HSSFSheet sht = wb.getSheetAt(0);
			if (sht != null) {
				for (int rowNum = 2; rowNum < sht.getLastRowNum(); rowNum++) {
					HSSFRow hssfRow = sht.getRow(rowNum);
					if(hssfRow == null) continue;
					if (hssfRow.getCell(0) == null) {
						message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
						break;
					}
					if(("").equals(getValue(hssfRow.getCell(0)))) {
						message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
						break;
					}
					ArtArtistExperience artArtistExperience = new ArtArtistExperience();
					String str = "";
					artArtistExperience.setArtId(artistId);
					str = getValue(hssfRow.getCell(0));
					if ("".equals(str)) {
						message = "第" + String.valueOf(rowNum) + "行找不到时间";
						break;
					}
					artArtistExperience.setExpeTime(str);
					str = getValue(hssfRow.getCell(1));
					artArtistExperience.setLifeExperience(str);
					str = getValue(hssfRow.getCell(2));
					artArtistExperience.setHistoryExperience(str);
					artArtistExperienceDao.save(artArtistExperience);
				}
			}
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(path);
		if(file.exists() && file.isFile()){
			file.delete();
		}
		return message;
	}
	
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			DecimalFormat df = new DecimalFormat("0");
			return df.format(hssfCell.getNumericCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	public PageQuery queryArtArtistExperience(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artArtistExperienceList", pageQuery);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}
	
	private void createSqlFilter(PageQuery page) {
		String paras = "";
		Map<String, String> parameters = page.getParameters();
		String art_id = parameters.get("artistId");
		paras = " aae.art_id = " + art_id + " ";
		String year = parameters.get("year");
		if (year != null && year.trim().length() > 0) {
			if (paras.length() > 0)
				paras += " and ";
			paras += " aae.year like '%" + year.trim() + "%'";
		}
		if (paras.length() > 0)
			page.getParameters().put("paras", paras);
	}
	
    @Resource
	private ArtArtistExperienceDao	artArtistExperienceDao;

	public void setArtArtistExperienceDao(ArtArtistExperienceDao artArtistExperienceDao) {
		this.artArtistExperienceDao = artArtistExperienceDao;
	}
}


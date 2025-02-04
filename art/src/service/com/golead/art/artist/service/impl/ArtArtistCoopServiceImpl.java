package com.golead.art.artist.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.golead.art.activity.model.ArtAgency;
import com.golead.art.activity.service.ArtAgencyService;
import com.golead.art.artist.dao.ArtArtistCoopDao;
import com.golead.art.artist.model.ArtArtistCoop;
import com.golead.art.artist.service.ArtArtistCoopService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtArtistCoopServiceImpl extends BaseServiceImpl implements ArtArtistCoopService {
	private static final long serialVersionUID = 1L;

	public ArtArtistCoop getArtArtistCoop(Serializable id) throws ServiceException {
		try {
			return artArtistCoopDao.get(id);
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

	public void createArtArtistCoop(ArtArtistCoop artArtistCoop) throws ServiceException {
		try {
			artArtistCoopDao.save(artArtistCoop);
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

	public void updateArtArtistCoop(Map<String, String> artArtistCoopMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artArtistCoopMap.get(ArtArtistCoop.PROP_ID));
			ArtArtistCoop tmp = artArtistCoopDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artArtistCoopMap, true);			
			artArtistCoopDao.update(tmp);
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

	public void deleteArtArtistCoop(Serializable id) throws ServiceException {
		try {
			artArtistCoopDao.delete(id);
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

	public void deleteArtArtistCoops(Serializable[] ids) throws ServiceException {
		try {
			artArtistCoopDao.deleteAll(ids);
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
	
	public void deleteArtArtistWithCoops(Serializable[] ids) throws ServiceException {
		try {
			for (Serializable id : ids) {
				artArtistCoopDao.deleteByField("artistId", id);
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
	
	public String importCoops(String path, int artistId) throws ServiceException {
		String message = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(path);
			HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
			HSSFSheet sht = wb.getSheetAt(0);
         List<ArtAgency> artAgencies = artAgencyService.findAll();
         Map<String, String> agencyMap = new HashMap<String, String>();
         for (ArtAgency agency : artAgencies) {
            agencyMap.put(agency.getAgencyCName(), agency.getId().toString());
            agencyMap.put(agency.getAgencyEName(), agency.getId().toString());
         }
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
					ArtArtistCoop artArtistCoop = new ArtArtistCoop();
					String str = "";
					artArtistCoop.setArtistId(artistId);
					str = getValue(hssfRow.getCell(0));
					if (agencyMap.get(str) == null) {
                  message = "第" + String.valueOf(rowNum) + "行找不到机构名称";
                  break;
               }
               Integer agencyId = Integer.valueOf(agencyMap.get(str));
					artArtistCoop.setAgencyId(agencyId);
					str = getValue(hssfRow.getCell(1));
					if ("".equals(str)) {
						message = "第" + String.valueOf(rowNum) + "行找不到时间";
						break;
					}
					artArtistCoop.setCoopTime(str);
					str = getValue(hssfRow.getCell(2));
					artArtistCoop.setCoopDesc(str);
					createArtArtistCoop(artArtistCoop);
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

	public PageQuery queryArtArtistCoop(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artArtistCoopList", pageQuery);
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
		String artistId = page.getParameters().get("artistId");
		String paras = "";
		paras = " aac.artist_id = " + artistId + " ";
		if (paras.length() > 0) {
			page.getParameters().put("paras", paras);
		}
	}
	
    @Resource
	private ArtArtistCoopDao	artArtistCoopDao;
    
    @Resource
    private ArtAgencyService	artAgencyService;

	public void setArtArtistCoopDao(ArtArtistCoopDao artArtistCoopDao) {
		this.artArtistCoopDao = artArtistCoopDao;
	}
}


package com.golead.art.works.service.impl;



public class ArtWorksAuctionServiceImpl  {
	/**private static final long serialVersionUID = 1L;

	public ArtWorksAuction getArtWorksAuction(Serializable id) throws ServiceException {
		try {
			return artWorksAuctionDao.get(id);
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

	public void createArtWorksAuction(ArtWorksAuction artWorksAuction) throws ServiceException {
		try {
			artWorksAuctionDao.save(artWorksAuction);
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

	public void updateArtWorksAuction(Map<String, String> artWorksAuctionMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artWorksAuctionMap.get(ArtWorksAuction.PROP_ID));
			ArtWorksAuction tmp = artWorksAuctionDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artWorksAuctionMap, true);			
			artWorksAuctionDao.update(tmp);
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

	public void deleteArtWorksAuction(Serializable id) throws ServiceException {
		try {
			artWorksAuctionDao.delete(id);
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

	public void deleteArtWorksAuctions(Serializable[] ids) throws ServiceException {
		try {
			PageQuery pq = new PageQuery();
			List<Integer> words = new ArrayList<Integer>();
			List<Integer> video = new ArrayList<Integer>();
			for (Serializable id : ids) {
				pq.getParameters().put("auctionId", id.toString());
				pq = artAuctionVideoService.queryArtAuctionVideo(pq);
				if (pq.getRecordSet() != null) {
					for (Map<String, Object> item : pq.getRecordSet()) {
						video.add(Integer.valueOf(item.get("id").toString()));
					}
				}
				pq = artAuctionWordsService.queryArtAuctionWords(pq);
				if (pq.getRecordSet() != null) {
					for (Map<String, Object> item : pq.getRecordSet()) {
						words.add(Integer.valueOf(item.get("id").toString()));
					}
				}
				String path = ServletActionContext.getServletContext().getRealPath("");
				String pathOfWords = path + File.separator + "upload" + File.separator + "auction" + File.separator + "words" + File.separator + id.toString();
				String pathOfVideo = path + File.separator + "upload" + File.separator + "auction" + File.separator + "video" + File.separator + id.toString();
				deleteFile(pathOfWords);
				deleteFile(pathOfVideo);
			}
			Integer[] videoIds = delete(video);
			Integer[] wordsIds = delete(words);
			artAuctionVideoService.deleteArtAuctionVideos(videoIds);
			artAuctionWordsService.deleteArtAuctionWordss(wordsIds);
			artWorksAuctionDao.deleteAll(ids);
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
	
	public Integer[] delete(List<Integer> ids){
		Integer[] del_ids = new Integer[ids.size()];
		int i = 0;
		for (Integer integer : ids) {
			del_ids[i] = integer;
			i++;
		}
		return del_ids;
	}
	
	private void deleteFile(String path) throws Exception{
		   try {
	         File file = new File(path);
	         if(file.exists()){
	            File[] files = file.listFiles();
	            for(File childFile : files){
	               if(childFile.isFile()){
	                  childFile.delete();
	               }else{
	                  deleteFile(childFile.getPath());
	               }
	            }
	            File[] files2 = new File(path).listFiles();
	            if(files2.length == 0){
	               file.delete();
	            }
	         }
	      }
	      catch (Exception e) {
	         e.printStackTrace();
	      }
		}

	public PageQuery queryArtWorksAuction(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artWorksAuctionList", pageQuery);
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
		Map<String, String> map = page.getParameters();
		String saleName = map.get("saleName");
		if (saleName != null && saleName.length() >0) {
			if (paras.length() > 0)
				paras += " and ";
			paras += " worksAuction.sale_name like '%" + saleName + "%' ";
		}
		
		String worksId = map.get("worksId");
		if (worksId != null && worksId.length() >0) {
			if (paras.length() > 0)
				paras += " and ";
			paras += " worksAuction.auction_houses_id = '" + worksId + "' ";
		}
		if (paras.length() > 0) 
			page.getParameters().put("paras", paras);
		System.out.println("paras==" + paras);
	}
	
    @Resource
	private ArtWorksAuctionDao	artWorksAuctionDao;
    
    @Resource
    private ArtAuctionVideoService artAuctionVideoService;
    
    @Resource
    private ArtAuctionWordsService artAuctionWordsService;

	public void setArtWorksAuctionDao(ArtWorksAuctionDao artWorksAuctionDao) {
		this.artWorksAuctionDao = artWorksAuctionDao;
	}**/
}


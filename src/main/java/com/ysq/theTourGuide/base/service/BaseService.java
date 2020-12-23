package com.ysq.theTourGuide.base.service;


import com.ysq.theTourGuide.base.dto.BaseDTO;
import com.ysq.theTourGuide.base.page.PageList;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface BaseService<T> {

	public String getType();

	String getId(T model)throws Exception;

	/**
	 * 通过id获取实体
	 * 
	 * @param id
	 * @return T 范型
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public T get(Serializable id) throws Exception;

	/**
	 * 通过id获取DTO
	 * 
	 * @param id
	 * @param clazz
	 * @return T 范型
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> D getDTO(Serializable id, Class<D> clazz) throws Exception;

	/**
	 * 保存实体
	 * 
	 * @param model
	 * @return T 范型
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public T save(T model) throws Exception;

	/**
	 * 保存DTO
	 * 
	 * @param clazz 
	 * @param model
	 * @return D 范型
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> D saveDTO(D model, Class<T> clazz) throws Exception;

	/**
	 * 更新实体
	 * 
	 * @param model
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public void update(T model) throws Exception;

	/**
	 * 更新DTO
	 * 
	 * @param model
	 * @param clazz 
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> void updateDTO(D model, Class<T> clazz) throws Exception;

	/**
	 * 通过id删除实体
	 * 
	 * @param id
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public void deleteById(long id) throws Exception;

	/**
	 * 删除实体
	 * 
	 * @param model
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public void delete(T model) throws Exception;

	/**
	 * 删除DTO
	 * 
	 * @param clazz
	 * @param model
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> void deleteDTO(D model, Class<T> clazz) throws Exception;

	/**
	 * 获取实体总数量
	 * 
	 * @param entity
	 * @return Long
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public Long countAll(T entity) throws Exception;

	/**
	 * 获取实体列表(全部)
	 * 
	 * @return List<T> 范型
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public List<T> findAll() throws Exception;

	/**
	 * 获取DTO列表(全部)
	 * 
	 * @param clazz
	 * @return List<D> 范型
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> List<D> findAllDTO(Class<D> clazz) throws Exception;
	
	
	/**
	 * 获取实体列表(条件)
	 * 
	 * @param params 条件参数
	 * @return List<D>(返回值说明)
	 * 
	 * @author Alphones  2018年7月25日
	 */
	public List<T> findByParams(T params) throws Exception;
	
	/**
	 * 获取实体列表(条件)
	 * 
	 * @param params 条件参数
	 * @param orderBy 排序
	 * @return List<D>(返回值说明)
	 * 
	 * @author Alphones  2018年7月25日
	 */
	public List<T> findByParams(T params, String orderBy) throws Exception;
	
	/**
	 * 获取DTO列表(条件)
	 * 
	 * @param params 条件参数
	 * @param clazz DTO类型
	 * @return List<D>(返回值说明)
	 * 
	 * @author Alphones  2018年7月25日
	 */
	public <D extends BaseDTO> List<D> findByParams(D params, Class<D> clazz) throws Exception;

	/**
	 * 获取DTO列表(条件)
	 * 
	 * @param params 条件参数
	 * @param clazz DTO类型
	 * @param orderBy 排序
	 * @return List<D>(返回值说明)
	 *
	 * @author Alphones  2018年7月25日
	 */
	public <D extends BaseDTO> List<D> findByParams(D params, Class<D> clazz, String orderBy) throws Exception;
	
	/**
	 * 获取实体分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public PageList<T> findPageData(T params, int pageNum, int pageSize) throws Exception;

	/**
	 * 获取实体分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @param orderBy 排序
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public PageList<T> findPageData(T params, int pageNum, int pageSize, String orderBy) throws Exception;
	
	/**
	 * 获取实体分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public PageList<T> findPageData(Map<String, Object> params, int pageNum, int pageSize) throws Exception;

	/**
	 * 获取实体分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @param orderBy 排序
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public PageList<T> findPageData(Map<String, Object> params, int pageNum, int pageSize, String orderBy) throws Exception;
	
	/**
	 * 获取DTO分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> PageList<D> findPageDataDTO(D params, Class<D> clazzD, int pageNum, int pageSize) throws Exception;

	/**
	 * 获取DTO分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @param orderBy 排序
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> PageList<D> findPageDataDTO(D params, Class<D> clazzD, int pageNum, int pageSize, String orderBy) throws Exception;
	
	/**
	 * 获取DTO分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> PageList<D> findPageDataDTO(Map<String, Object> params, Class<D> clazz, int pageNum, int pageSize) throws Exception;

	/**
	 * 获取DTO分页列表
	 * 
	 * @param params 查询参数
	 * @param pageNum 页码
	 * @param pageSize 数量
	 * @param orderBy 排序
	 * @return PageList<T> 分页列表
	 * @author: Alphones
	 * @date: 2018年10月05日
	 */
	public <D extends BaseDTO> PageList<D> findPageDataDTO(Map<String, Object> params, Class<D> clazz, int pageNum, int pageSize, String orderBy) throws Exception;

}

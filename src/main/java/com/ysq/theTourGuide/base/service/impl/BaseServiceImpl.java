package com.ysq.theTourGuide.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.ysq.theTourGuide.base.dto.BaseDTO;
import com.ysq.theTourGuide.base.page.PageList;
import com.ysq.theTourGuide.base.page.Paginator;
import com.ysq.theTourGuide.base.service.BaseService;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class BaseServiceImpl<M extends Mapper<T>, T> implements BaseService<T> {

	@Autowired
	protected M mapper;

	@Resource
	private RedisTemplate<Serializable, Object> redisTemplate;

	public void setMapper(M mapper) {
		this.mapper = mapper;
	}

	public String getType(){
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		String str = parameterizedType.getActualTypeArguments()[1].getTypeName();
		return str.substring(str.lastIndexOf(".")+1);
	}

	public String getId(T model)throws Exception{
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> clazz = (Class<T>) parameterizedType.getActualTypeArguments()[1];

		PropertyDescriptor pd = new PropertyDescriptor(clazz.getDeclaredFields()[0].getName(), clazz);
		Method getMethod = pd.getReadMethod();//获得get方法
		Object fieldValue = ReflectionUtils.invokeMethod(getMethod, model);
		return fieldValue.toString();
	}

	@Override
	public T get(Serializable id) throws Exception {
		String key = getType() + "-" + id.toString();
		T eneity = (T) redisTemplate.opsForValue().get(key);
		if( eneity == null){
			eneity = mapper.selectByPrimaryKey(id);
			if(eneity == null){
				return null;
			}
			redisTemplate.opsForValue().set(key,eneity,240, TimeUnit.SECONDS);
		}
		return eneity;
	}

	@Override
	public <D extends BaseDTO> D getDTO(Serializable id, Class<D> clazz) throws Exception {
		T entity = mapper.selectByPrimaryKey(id);
		return transToDTO(entity, clazz);
	}

	@Override
	public T save(T model) throws Exception {
		mapper.insert(model);
		return model;
	}

	@Override
	public <D extends BaseDTO> D saveDTO(D model, Class<T> clazz) throws Exception {
		T entity = transToEntity(model, clazz);
		mapper.insert(entity);
		reflexSetId(entity, model);
		return model;
	}

	@Override
	public void update(T model) throws Exception {
		String key = getType() + "-" + getId(model);
		redisTemplate.delete(key);
		mapper.updateByPrimaryKeySelective(model);
	}

	@Override
	public <D extends BaseDTO> void updateDTO(D model, Class<T> clazz) throws Exception {
		T entity = transToEntity(model, clazz);
		mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public Long countAll(T entity) throws Exception {
		return new Long(mapper.selectCount(entity));
	}

	@Override
	public List<T> findAll() throws Exception {
		return mapper.selectAll();
	}

	@Override
	public void delete(T entity) throws Exception {
		String key = getType() + "-" + getId(findByParams(entity).get(0));
		redisTemplate.delete(key);
		mapper.delete(entity);
	}

	@Override
	public <D extends BaseDTO> void deleteDTO(D model, Class<T> clazz) throws Exception {
		mapper.delete(transToEntity(model, clazz));
	}

	@Override
	public void deleteById(long id) throws Exception {
		String key = getType() + "-" + id;
		redisTemplate.delete(key);
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public <D extends BaseDTO> List<D> findAllDTO(Class<D> clazz) throws Exception {
		List<T> entityList = mapper.selectAll();
		return transToDTOList(entityList, clazz);
	}

	@Override
	public List<T> findByParams(T params) throws Exception {
		return mapper.select(params);
	}

	@Override
	public List<T> findByParams(T params, String orderBy) throws Exception {
		Map<String, Object> map = reflexObject(params);
		return selectList(map, orderBy);
	}

	@Override
	public <D extends BaseDTO> List<D> findByParams(D params, Class<D> clazz) throws Exception {
		return findByParams(params, clazz, null);
	}

	@Override
	public <D extends BaseDTO> List<D> findByParams(D params, Class<D> clazz, String orderBy) throws Exception {
		Map<String, Object> map = reflexObject(params);
		return transToDTOList(selectList(map, orderBy), clazz);
	}

	@Override
	public PageList<T> findPageData(Map<String, Object> params, int pageNum, int pageSize) throws Exception {
		return selectByPage(params, pageNum, pageSize, null);
	}

	@Override
	public PageList<T> findPageData(Map<String, Object> params, int pageNum, int pageSize, String orderBy) throws Exception {
		return selectByPage(params, pageNum, pageSize, orderBy);
	}

	@Override
	public PageList<T> findPageData(T params, int pageNum, int pageSize) throws Exception {
		return findPageData(params, pageNum, pageSize, null);
	}

	@Override
	public PageList<T> findPageData(T params, int pageNum, int pageSize, String orderBy) throws Exception {
		Map<String, Object> map = reflexObject(params);
		return selectByPage(map, pageNum, pageSize, orderBy);
	}

	@Override
	public <D extends BaseDTO> PageList<D> findPageDataDTO(Map<String, Object> params, Class<D> clazz, int pageNum, int pageSize) throws Exception {
		return findPageDataDTO(params, clazz, pageNum, pageSize, null);
	}

	@Override
	public <D extends BaseDTO> PageList<D> findPageDataDTO(Map<String, Object> params, Class<D> clazz, int pageNum, int pageSize, String orderBy) throws Exception {
		PageList<T> pageList = selectByPage(params, pageNum, pageSize, orderBy);
		return transToPageList(pageList, clazz);
	}

	@Override
	public <D extends BaseDTO> PageList<D> findPageDataDTO(D params, Class<D> clazzD, int pageNum, int pageSize) throws Exception {
		return findPageDataDTO(params, clazzD, pageNum, pageSize, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <D extends BaseDTO> PageList<D> findPageDataDTO(D params, Class<D> clazzD, int pageNum, int pageSize, String orderBy) throws Exception {
		Class<T> clazzT = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		Map<String, Object> map = reflexObject(transToEntity(params, clazzT));
		Example example = new Example(clazzT);
		if (map.entrySet().size() > 0) {
			Example.Criteria criteria = example.createCriteria();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() != null) {
					if (entry.getValue() instanceof String) {
						if (!entry.getValue().equals("")) {
							criteria.andEqualTo(entry.getKey(), entry.getValue());
						}
					} else {
						criteria.andEqualTo(entry.getKey(), entry.getValue());
					}
				}
			}
		}

		if (orderBy != null) {
			example.setOrderByClause(orderBy); // 参数名 + ASC 或 DESC, 多个用","隔开
		}
		com.github.pagehelper.Page<Object> result = PageHelper.startPage(pageNum, pageSize);
		List<D> list = transToDTOList(mapper.selectByExample(example), clazzD);
		Paginator paginator = new Paginator(pageNum, pageSize == 0?10:pageSize, (int) result.getTotal());
		PageList<D> pageList = new PageList<D>(paginator);
		pageList.setData(list);
		return pageList;
	}

	@SuppressWarnings("unchecked")
	private PageList<T> selectByPage(Map<String, Object> params, int pageNum, int pageSize, String orderBy) throws Exception {
		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		Example example = new Example(clazz);
		if (params.entrySet().size() > 0) {
			Example.Criteria criteria = example.createCriteria();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() != null) {
					if (entry.getValue() instanceof String) {
						if (!entry.getValue().equals("")) {
							criteria.andEqualTo( entry.getKey(),entry.getValue());
						}
					} else {
						criteria.andEqualTo(entry.getKey(), entry.getValue());
					}
				}
			}
		}
		if (orderBy != null) {
			example.setOrderByClause(orderBy.toString()); // 参数名 + ASC 或 DESC,
															// 多个用","隔开
		}
		com.github.pagehelper.Page<Object> result = PageHelper.startPage(pageNum, pageSize);
		List<T> list = mapper.selectByExample(example);
		Paginator paginator = new Paginator(pageNum, pageSize, (int) result.getTotal());
		PageList<T> pageList = new PageList<T>(paginator);
		pageList.setData(list);
		return pageList;
	}

	@SuppressWarnings("unchecked")
	public List<T> selectList(Map<String, Object> params, String orderBy) throws Exception {
		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		Example example = new Example(clazz);
		if (params.entrySet().size() > 0) {
			Example.Criteria criteria = example.createCriteria();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if (entry.getValue() != null) {
					if (entry.getValue() instanceof String) {
						if (!entry.getValue().equals("")) {
							criteria.andEqualTo(entry.getKey(), entry.getValue());
						}
					} else {
						criteria.andEqualTo(entry.getKey(), entry.getValue());
					}
				}
			}
		}
		if (orderBy != null) {
			example.setOrderByClause(orderBy.toString()); // 参数名 + ASC 或 DESC,
															// 多个用","隔开
		}
		List<T> list = mapper.selectByExample(example);
		return list;
	}

	public <D extends BaseDTO> PageList<D> transToPageList(PageList<T> pageList, Class<D> clazz) throws Exception {
		Paginator paginator = new Paginator(pageList.getPageNum(), pageList.getPageSize(), pageList.getTotalItems());
		PageList<D> page = new PageList<D>(paginator);
		page.setData(transToDTOList(pageList.getData(), clazz));
		return page;
	}

	public <D extends BaseDTO> List<T> transToEntityList(List<D> dtoList, Class<T> clazz) throws Exception {
		List<T> entityList = new ArrayList<T>();
		if (dtoList != null && dtoList.size() > 0) {
			for (D dto : dtoList) {
				T entity = transToEntity(dto, clazz);
				entityList.add(entity);
			}
		}
		return entityList;
	}

	public <D extends BaseDTO> T transToEntity(D dto, Class<T> clazz) throws Exception {
		if (dto == null) {
			return null;
		}
		T entity = clazz.newInstance();
		ConvertUtils.register(new DateConverter(null), Date.class);
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BooleanConverter(null), Boolean.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		BeanUtils.copyProperties(entity, dto);
		return entity;
	}

	public <D extends BaseDTO> List<D> transToDTOList(List<T> entityList, Class<D> clazz) throws Exception {
		List<D> dtoList = new ArrayList<D>();
		if (entityList != null && entityList.size() > 0) {
			for (T entity : entityList) {
				D dto = transToDTO(entity, clazz);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	public <D extends BaseDTO> D transToDTO(T entity, Class<D> clazz) throws Exception {
		if (entity == null) {
			return null;
		}
		D dto = clazz.newInstance();
		ConvertUtils.register(new DateConverter(null), Date.class);
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);
		ConvertUtils.register(new BooleanConverter(null), Boolean.class);
		ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
		BeanUtils.copyProperties(dto, entity);
		return dto;
	}

	private Map<String, Object> reflexObject(Object object) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Class<? extends Object> clazz = object.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true);
			Object val = new Object();
			val = f.get(object);
			f.getModifiers();
			if (!f.getName().equals("serialVersionUID") && f.getModifiers() == 2) {
				resultMap.put(f.getName(), val);
			}
		}
		return resultMap;
	}

	private Object reflexGetId(Object object) throws Exception {
		Class<? extends Object> clazz = object.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true);
			if (f.getName().equals("id")) {
				Object val = new Object();
				val = f.get(object);
				return val;
			}
		}
		return null;
	}

	private void reflexSetId(Object entity, Object dto) throws Exception {
		Class<? extends Object> clazz = entity.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true);
			if (f.getName().equals("id")) {
				Method setIdMethod = dto.getClass().getMethod("setId", Long.class);
				setIdMethod.invoke(dto, reflexGetId(entity));
			}
		}
	}

}

package com.turnolibre.service.impl;

import com.turnolibre.persistence.dao.impl.SharedDao;
import com.turnolibre.service.SharedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SharedServiceImpl implements SharedService {

	@Autowired
	private SharedDao sharedDao;

	@Override
	@Transactional
	public <T> Long save(T entity) {
		return sharedDao.save(entity);
	}

	@Override
	@Transactional
	public <T> void update(T entity) {
		sharedDao.update(entity);
	}

	@Override
	@Transactional
	public <T> void saveOrUpdate(T entity) {
		sharedDao.saveOrUpdate(entity);
	}

	@Override
	@Transactional
	public <T> void delete(T entity) {
		sharedDao.delete(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public <T> T get(Class<T> entityClass, Long id) {
		return sharedDao.get(entityClass, id);
	}

	@Override
	@Transactional(readOnly = true)
	public <T> T load(Class<T> entityClass, Long id) {
		return sharedDao.load(entityClass, id);
	}

	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAll(Class<T> entityClass) {
		return sharedDao.findAll(entityClass);
	}

}

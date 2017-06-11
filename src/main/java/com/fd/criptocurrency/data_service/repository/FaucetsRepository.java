package com.fd.criptocurrency.data_service.repository;

import com.fd.criptocurrency.model.criteria.FaucetsSearchCriteria;
import com.fd.criptocurrency.model.entity.FaucetEntity;
import com.fd.criptocurrency.model.result.FaucetsSearchResult;

/**
 * 
 * @author Muguruza
 *
 */
public interface FaucetsRepository {
	public FaucetsSearchResult retriveFaucets(FaucetsSearchCriteria sc);
	public boolean saveFaucet(FaucetEntity fEntity);
}
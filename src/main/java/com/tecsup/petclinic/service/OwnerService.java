package com.tecsup.petclinic.service;

import java.util.List;
import com.tecsup.petclinic.domain.Owner;
import com.tecsup.petclinic.exception.OwnerNotFoundException;

/**
 * 
 * @author jgomezm
 *
 */
public interface OwnerService {

	Owner create(Owner owner);
	
	Owner update(Owner owner);
	
	void delete(Long id) throws OwnerNotFoundException;
	
	Owner findById(long id) throws OwnerNotFoundException;
	
	List<Owner> findByFirstName(String firstName);
	
	Iterable<Owner> findAll();
	
}
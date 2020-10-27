package com.sanroxcode.accountswitcher.controller;

import java.util.ArrayList;

import com.sanroxcode.accountswitcher.dao.CountryDao;
import com.sanroxcode.accountswitcher.dto.Country;

public class CountryController {
	private final ArrayList<Country> listCountries = new ArrayList<Country>();
	private final CountryDao countryDao = new CountryDao();
	
	public CountryController() {
		listCountriesRefresh();
	}
	/*
	public Country findByName(String countryname) {
		Country country = null; 
		countryname = countryname.trim();
		
		if(countryname.equals(""))
			throw new Error("Invalid country name !!!");
		
		country = countryDao.findByCountryName(countryname);
		
		return country;
	}
	*/
	
	public Country findByName(String countryname) {		
		countryname = countryname.trim();
		
		if(countryname.equals(""))
			throw new Error("Invalid country name !!!");
		
		for(Country c: listCountries) {
			if(c.getName().equals(countryname))
				return c;
		}
		
		return null;
	}
	
	public Country findByDomain(String countrydomain) {		
		countrydomain = countrydomain.trim();
		
		if(countrydomain.equals(""))
			throw new Error("Invalid country domain !!!");
		
		for(Country c: listCountries) {
			if(c.getDomain().equals(countrydomain))
				return c;
		}
		
		return null;
	}
	
	public ArrayList<Country> getListCountries() {
		return listCountries;
	}

	private ArrayList<Country> listCountriesRefresh() {

		listCountries.clear();
		listCountries.addAll(countryDao.findAll());
		return listCountries;
		//refreshList();
	}
	
}

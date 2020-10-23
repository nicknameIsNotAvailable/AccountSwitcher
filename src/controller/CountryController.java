package controller;

import java.util.ArrayList;

import dao.CountryDao;
import dto.Country;

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

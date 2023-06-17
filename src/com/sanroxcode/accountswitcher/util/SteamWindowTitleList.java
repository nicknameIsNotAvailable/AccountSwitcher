package com.sanroxcode.accountswitcher.util;

import java.util.ArrayList;
import java.util.List;
/**
 * Prováveis nomes do título da janela da Steam
 * @author Rodrigo Rolim
 *
 */
public class SteamWindowTitleList {

	public static List<String> getWindowTitles() {
		List<String> list = new ArrayList<String>();
		list.add("steam");
		list.add("Servidores");		

		return list;
	}

}

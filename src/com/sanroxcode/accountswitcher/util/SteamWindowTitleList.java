package com.sanroxcode.accountswitcher.util;

import java.util.ArrayList;
import java.util.List;
/**
 * Prov�veis nomes do t�tulo da janela da Steam
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

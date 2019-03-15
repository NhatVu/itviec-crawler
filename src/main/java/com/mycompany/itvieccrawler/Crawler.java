/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.itvieccrawler;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author nhatvm
 */
public class Crawler {

	private static final String ALL_COMPANY_URL = "https://itviec.com/jobs-company-index/s-z";
	private static final String HOST = "https://itviec.com";
	private static final String IT_VIEC = "itviec";
	private static final String OUT_FILE_CSV = "itviec_result_s_z.csv";

	public void crawl() {
		Document doc = Helper.jsoupConnect(ALL_COMPANY_URL, IT_VIEC);
		Elements allCompanies = doc.select(".skill-tag__item");
		List<ITViecDataModel> result = new LinkedList<>();
		
		for (Element element : allCompanies) {
			
			result.add(parseOneCompany(HOST + element.select("a").attr("href")));
		}
		
		writeDataLineByLine(OUT_FILE_CSV, result);

		System.out.println("done");

	}

	public ITViecDataModel parseOneCompany(String url) {
		Document document = Helper.jsoupConnect(url, IT_VIEC);

		Element nameAndInfo = document.selectFirst(".name-and-info");
		String companyName = nameAndInfo.selectFirst("h1").text();
		String address = nameAndInfo.selectFirst("span").text();
		String companyType = nameAndInfo.select(".company-info .gear-icon").text();
		String size = nameAndInfo.select(".company-info .group-icon").text();
		String country = nameAndInfo.select(".company-info .country").text();
		float rating = -1;
		if (document.select(".company-ratings__star-point").size() > 0) {
			rating = Float.parseFloat(document.select(".company-ratings__star-point").text());
		}

		ITViecDataModel comDataModel = new ITViecDataModel();
		comDataModel.setAddress(address);
		comDataModel.setCompany(companyName);
		comDataModel.setCompanyType(companyType);
		comDataModel.setCountry(country);
		comDataModel.setRating(rating);
		comDataModel.setSize(size);
		comDataModel.setUrl(url);
		System.out.println(comDataModel);
		return comDataModel;
	}

	private void writeDataLineByLine(String filePath, List<ITViecDataModel> listData) {
		// first create file object for file placed at location 
		// specified by filepath 
		File file = new File(filePath);
		try {
			// create FileWriter object with file as parameter 
			FileWriter writer = new FileWriter(file);

			// Create Mapping Strategy to arrange the  
			// column name in order 
			ColumnPositionMappingStrategy mappingStrategy
					= new ColumnPositionMappingStrategy();
			mappingStrategy.setType(ITViecDataModel.class);

			// Arrange column name as provided in below array. 
			/*
				private String company;
	private String country;
	private String url;
	private String size;
	private String address;
	private float rating;
	private String companyType;
			*/
			String[] columns = new String[]{"Country", "Company", "Rating", "CompanyType", "Size", "Address", "Url"};
			mappingStrategy.setColumnMapping(columns);
			

			// Createing StatefulBeanToCsv object 
			StatefulBeanToCsvBuilder<ITViecDataModel> builder
					= new StatefulBeanToCsvBuilder(writer);
		
			StatefulBeanToCsv beanWriter
					= builder.withMappingStrategy(mappingStrategy).build();

			// Write list to StatefulBeanToCsv object 
			
			beanWriter.write(listData);

			// closing the writer object 
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String url = "https://itviec.com/companies/fpt-shop";
		Crawler crawler = new Crawler();
		crawler.crawl();
	}
}

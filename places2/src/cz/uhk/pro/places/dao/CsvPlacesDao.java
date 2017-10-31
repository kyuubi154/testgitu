package cz.uhk.pro.places.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cz.uhk.pro.places.model.Place;

public class CsvPlacesDao implements PlacesDao {
	private String fileName;
	
	public CsvPlacesDao(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void save(Place p) {
		if(p.getId() == 0){
			// nove misto INSERT
			int newID = getSequenceNumber() + 1;
			p.setId(newID);
			// nacteme vsechny existujici mista ze souboru
			List<Place> places = findAll();
			//pridame nove misto
			places.add(p);
			// vsechno ulozime zpet do souboru
			saveAll(places,newID);
		} else{
			// existujici misto UPDATE
			List<Place> places = findAll();
			//najdeme v seznamu objekt s daným ID
			int index = 0;
			for (Place place : places) {
				if (place.getId() == p.getId()) {
					// zaznam s danym ID uz v souboru existuje
					// zamenime ho v listu za novy (odkaz na instanci)
					places.set(index, p);
					// ulozime upraveny seznam do souboru 
					saveAll(places, getSequenceNumber());
					return; // skoncime metodu
				}
				index++;
			}
			// ID jsme nenašli
			throw new RuntimeException("ID nenalezeno" + p.getId());
		}
	}

	@Override
	public void delete(int id) {
		// nacteme vsechna data ze souboru
		List<Place> places = findAll();
		// projdeme vsechna mista a smazeme to ze seznamu, jehoz id se shoduje
		for (Place p : places) {
			if (p.getId() == id) {
				places.remove(p);
				// ulozime upraveny seznam do souboru 
				saveAll(places, getSequenceNumber());
				return; // skoncime metodu
			}
		}
		// nenalezeno
		throw new RecordNotFoundException("Record not found, ID=" + id);
	}
	
	private void saveAll(List<Place> places, int seqNumber) {		
		// ulozime cely seznam do souboru (prepiseme)
		try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
			pw.println(seqNumber); // ulozime puvodni (zapamatovane) sekv. cislo
			for (Place p : places) {				
				pw.println(p.getId() + ";" + p.getLatitude() + ";" + 
								p.getLongitude() + ";" + p.getTitle());
			}
		} catch (IOException e) {
			throw new DaoException(e);
		}
	}
	
	private int getSequenceNumber() {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String fistLine = br.readLine();
			return Integer.parseInt(fistLine);
		} catch (FileNotFoundException e) {
			throw new DaoException(e);
		} catch (IOException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Place getById(int id) {
		// nacteme vsechna data ze souboru (neni to uplne optimalni, ale nevadi nam to)
		List<Place> places = findAll();
		// projdeme vsechna mista a vratime to, jehoz id se shoduje
		for (Place p : places) {
			if (p.getId() == id) return p;
		}
		// nenalezeno
		throw new RecordNotFoundException("Record not found, ID=" + id);
	}

	@Override
	public List<Place> findAll() {
		List<Place> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			br.readLine(); // precteme prvni radek a ignorujeme ho (obsahuje pocitadlo pro generovani ID)
			String line;
			// prochazime po radcich soubor, nez dorazime na konec
			while ((line = br.readLine()) != null) {
				// rozsekame radek podle stredniku
				String[] parts = line.split(";", 4);
				// vyrobime Place a nastavime mu vlastnosti
				Place p = new Place();
				p.setId(Integer.parseInt(parts[0]));
				p.setLatitude(Double.parseDouble(parts[1]));
				p.setLongitude(Double.parseDouble(parts[2]));
				p.setTitle(parts[3]);
				// pridame place do listu
				list.add(p);
			}
		} catch (FileNotFoundException e) {
			throw new DaoException(e);
		} catch (IOException e) {
			throw new DaoException(e);
		}
		return list;
	}

}

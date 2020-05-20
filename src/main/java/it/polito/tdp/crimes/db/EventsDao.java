package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Crimini;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public void listAllEvents(Map<Long,Event> crimini){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					crimini.put(res.getLong("incident_id"),new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
		

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	public List<String> getVertex(String categoria, int mese){
		List<String> result = new ArrayList<>();
		String sql = "SELECT distinct(offense_type_id) as result FROM EVENTS WHERE Month(reported_date)=? AND offense_category_id=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1,mese);
			st.setString(2, categoria);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					result.add(res.getString("result"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
	
	
			conn.close();
			return result;


	} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	return null;
	
	}
	
	
	
	}
	
	
	public List<Crimini> getArchi(String categoria, int mese){
		List<Crimini> result = new ArrayList<>();
		String sql = " SELECT e1.offense_type_id as v1, e2.offense_type_id as v2, COUNT(DISTINCT(e1.neighborhood_id)) as peso " + 
				"FROM EVENTS e1,EVENTS e2 " + 
				"WHERE MONTH(e1.reported_date)=? AND e1.offense_category_id=? AND MONTH(e2.reported_date)=? AND e2.offense_category_id=? " + 
				"AND e1.neighborhood_id=e2.neighborhood_id AND e1.offense_type_id!=e2.offense_type_id GROUP BY e1.offense_type_id, e2.offense_type_id" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, mese);
			st.setInt(3, mese);
			st.setString(2, categoria);
			st.setString(4, categoria);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Crimini c = new Crimini(res.getString("v1"),res.getString("v2"),res.getInt("peso"));
				result.add(c);
			}
			
			conn.close();
			return result;


		} catch (SQLException e) {
	// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
	
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

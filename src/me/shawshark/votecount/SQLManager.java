package me.shawshark.votecount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class SQLManager {

	public static Connection con;
	public static String hostname, username, password, database, tableName;
	public static Boolean status = false;
	
	public static void startConnection() {
		
		if(!(VoteCount.config.getString("mysql.hostname") != null)) { 
			System.out.println("[VoteCount] Mysql support disabled!");
			status = false;
			return;
		}
		
		hostname = VoteCount.config.getString("mysql.hostname");
		username = VoteCount.config.getString("mysql.username");
		password = VoteCount.config.getString("mysql.password");
		database = VoteCount.config.getString("mysql.database");
		tableName = VoteCount.config.getString("mysql.tableName");
		
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					con = DriverManager.getConnection("jdbc:mysql://" + hostname + ":3306/" + database, username, password);
					Statement str = con.createStatement();
					String query = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (" + 
							"  `UUID` varchar(100) NOT NULL," + 
							"  `player` varchar(100) NOT NULL," + 
							"  `votes` varchar(100) NOT NULL," + 
							"  UNIQUE KEY `UUID` (`UUID`)" + 
							") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
					str.executeUpdate(query);
					str.close();
					
					status = true;
					
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}
			}
		}.runTaskAsynchronously(VoteCount.plugin);
	}
	
	public static Integer loadPlayersVotes(final String UUID) {
		
		final HashMap<String, Integer> vote = new HashMap<String, Integer>();
		
		
				
			if(!(con != null)) {
				startConnection();
			}
				
			try {
					
				String query = "SELECT * FROM " + tableName + " WHERE UUID = '" + UUID + "';";
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				
				boolean t = false;
				
				if(!rs.first()) {
					vote.put(UUID, 0);
					st.close();
					t = true;
				}  
				
				if(!t) {
					vote.put(UUID, Integer.parseInt(rs.getString("votes")));	
				}
				    
				st.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		
		return vote.get(UUID);
	}
	
	void remind() {
		if(!status) {
			return;
		}
		Bukkit.broadcastMessage("connor config the fucking plugin correctly!");
		remind();
	}
	
	public static void updatePlayer(final String UUID, final String player, final int votes) {
		
		new BukkitRunnable() {
			@Override
			public void run() {
				int t = votes;
				int tt = loadPlayersVotes(UUID);
				int votes = tt + t;
				
				try {
					Statement str;
					str = con.createStatement();
					
					String query = String.format("REPLACE INTO " + tableName + " (UUID, player, votes)VALUES('" + 
							UUID + "', '" + player + "', '" + votes + "');", new Object[0]);
					
					str.executeUpdate(query);
					
					str.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(VoteCount.plugin);
	}
}

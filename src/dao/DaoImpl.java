package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Card;
import model.Player;

public class DaoImpl implements Dao{

	Connection con ;
	
	@Override
	public void connect() throws SQLException {
		// TODO Auto-generated method stub
		try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/uno?serverTimezone=UTC", "root", "");
      
        } catch (SQLException e) {
        
            e.printStackTrace();
        }
		
	}

	@Override
    public void disconnect() throws SQLException {
        if (con != null) {
            con.close();
        }
    }

	@Override
    public int getLastIdCard(int playerId) throws SQLException {
        int lastId = 0;
        String query = "SELECT IFNULL(MAX(id), 0) + 1 AS nextId FROM Card WHERE id_player = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lastId = rs.getInt("nextId");
                }
            }
        }
        return lastId;
    }
	

	    @Override
	    public Card getLastCard() throws SQLException {
	        Card lastCard = null;
	        String query = "SELECT * FROM Card ORDER BY id DESC LIMIT 1";
	        try (PreparedStatement stmt = con.prepareStatement(query);
	             ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                lastCard = new Card(rs.getInt("id"), rs.getString("number"), rs.getString("color"), rs.getInt("id_player"));
	            }
	        }
	        return lastCard;
	    }

	    @Override
	    public Player getPlayer(String user, String pass) throws SQLException {
	        Player player = null;
	        String query = "SELECT * FROM Player WHERE user = ? AND password = ?";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setString(1, user);
	            stmt.setString(2, pass);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                	
	         
	                    player = new Player(rs.getInt("id"), rs.getString("name"), rs.getInt("games"), rs.getInt("victories"));
	                    
	                }
	            }
	        }
	        return player;
	    }

	    @Override
	    public ArrayList<Card> getCards(int playerId) throws SQLException {
	        ArrayList<Card> cards = new ArrayList<>();
	        String query = "SELECT * FROM Card LEFT JOIN GAME ON Card.id = GAME.id_card where id_player=? AND GAME.id is null";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, playerId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    cards.add(new Card(rs.getInt("id"), rs.getString("number"), rs.getString("color"), rs.getInt("id_player")));
	                }
	            }
	        }
	        System.out.println(cards);
	        return cards;
	    }

	    @Override
	    public Card getCard(int cardId) throws SQLException {
	        Card card = null;
	        String query = "SELECT * FROM Card WHERE id = ?";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, cardId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    card = new Card(rs.getInt("id"), rs.getString("number"), rs.getString("color"), rs.getInt("id_player"));
	                }
	            }
	        }
	        return card;
	    }

	    @Override
	    public void saveGame(Card card) throws SQLException {
	        String query = "INSERT INTO GAME (id_card) VALUES (?)";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, card.getId());
	            stmt.executeUpdate();
	        }
	    }

	    @Override
	    public void saveCard(Card card) throws SQLException {
	        String query = "INSERT INTO Card (id, number, color, id_player) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, card.getId());
	            stmt.setString(2, card.getNumber().toString());
	            stmt.setString(3, card.getColor().toString());
	            stmt.setInt(4, card.getPlayerId());
	            stmt.executeUpdate();
	        }
	    }

	    @Override
	    public void deleteCard(Card card) throws SQLException {
	        String query = "DELETE FROM Game WHERE id_card = ?";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, card.getId());
	            stmt.executeUpdate();
	        }
	    }

	    @Override
	    public void clearDeck(int playerId) throws SQLException {
	        String query = "DELETE FROM Card WHERE id_player = ?";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, playerId);
	            stmt.executeUpdate();
	        }
	        query = "DELETE FROM Game WHERE id_card IN (SELECT id FROM Card WHERE id_player = ?)";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, playerId);
	            stmt.executeUpdate();
	        }
	    }

	    @Override
	    public void addVictories(int playerId) throws SQLException {
	        String query = "UPDATE Player SET victories = victories + 1 WHERE id = ?";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, playerId);
	            stmt.executeUpdate();
	        }
	    }

	    @Override
	    public void addGames(int playerId) throws SQLException {
	        String query = "UPDATE Player SET games = games + 1 WHERE id = ?";
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setInt(1, playerId);
	            stmt.executeUpdate();
	        }
	
	
}
}

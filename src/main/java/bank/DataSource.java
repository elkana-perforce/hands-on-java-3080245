package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {
  public static Connection connect() {
    String db_file = "jdbc:sqlite:resources/bank.db";

    Connection connection = null;

    try {
      connection = DriverManager.getConnection(db_file);
      System.out.println("wer'e connected");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;

  }

  public static Account getAccount(int id) {
    String sql = "SELECT * FROM accounts where id=?";
    Account account = null;

    try (Connection connection = connect();
    PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      try (ResultSet result = statement.executeQuery()) {
        account = new Account(
          result.getInt("id"),
          result.getString("type"),
          result.getDouble("balance"));
        
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }

    return account;
  }

  public static Customer getCustomer(String username) {
    String sql = "SELECT * FROM customers where username=?";
    Customer customer = null;

    try (Connection connection = connect(); 
    PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        customer = new Customer(
          resultSet.getInt("id"),
          resultSet.getString("name"),
          resultSet.getString("username"),
          resultSet.getString("password"),
          resultSet.getInt("account_id"));
      } catch (Exception e) {
        e.printStackTrace();
      }      
    } catch (Exception e) {
      e.printStackTrace();
    }

    return customer;
  }

  public static void main(String[] args) {
    Customer customer = getCustomer("twest8o@friendfeed.com");
    System.out.println(customer.getName());

    Account account = getAccount(customer.getAccountId());
    System.out.println(account.getBalance());
  }
}

package banking;

import java.io.File;
import java.sql.*;

public class Database {

    private String url = "jdbc:sqlite:";
    private final String fileName;

    public Database(String fileName) {
        this.url = this.url + fileName;
        this.fileName = fileName;

        initializeDatabase();
    }

    private void initializeDatabase() {

        if (!isFilePresent(fileName)) {
            createDatabase(url);
        }

        if (!isValidTablePresent(url)) {
            repairTableStructure(url);
        }

        //System.out.println(checkIfTableIsEmpty(url));
    }

    private void createDatabase(String url) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                createTable(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void repairTableStructure(String url) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                dropTable(conn);
                createTable(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAccount(Card card) {
        String insert = "INSERT INTO CARD(number, pin) VALUES (?, ?)";
        accountOperation(card, insert);
    }

    public void deleteAccount(Card card) {
        String insert = "DELETE FROM CARD WHERE number = ? AND pin = ?";
        accountOperation(card, insert);
    }

    private void accountOperation(Card card, String insert) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (PreparedStatement preparedStatement = conn.prepareStatement(insert)) {
                    preparedStatement.setObject(1, card.getCardNumber());
                    preparedStatement.setObject(2, card.getPinNumber());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isAccountPresent(Card card) {
        String number = card.getCardNumber();
        String pin = card.getPinNumber();
        boolean x = false;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (Statement statement = conn.createStatement()) {
                    // Statement execution
                    try (ResultSet result = statement.executeQuery("SELECT COUNT(1) FROM CARD WHERE " +
                                                                   "number = " + number +
                                                                   " AND pin = " + pin
                    )) {
                        while (result.next()) {
                            x = result.getBoolean(1);
                        }


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return x;
    }

    public boolean isAccountPresent(String accountNumber) {
        boolean x = false;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (Statement statement = conn.createStatement()) {
                    // Statement execution
                    try (ResultSet result = statement.executeQuery("SELECT COUNT(1) FROM CARD WHERE " +
                                                                   "number = " + accountNumber
                    )) {
                        while (result.next()) {
                            x = result.getBoolean(1);
                        }


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return x;
    }

    public int getAccountBalance(Card card) {
        int balance = 0;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (Statement statement = conn.createStatement()) {
                    // Statement execution
                    try (ResultSet results = statement.executeQuery("SELECT balance FROM CARD WHERE " +
                                                                    "number = " + card.getCardNumber() +
                                                                    " AND pin = " + card.getPinNumber()
                    )) {
                        while (results.next()) {
                            balance = results.getInt(1);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return balance;
    }

    public void printTableContent() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (Statement statement = conn.createStatement()) {
                    try (ResultSet result = statement.executeQuery("SELECT * FROM CARD")) {
                        while (result.next()) {
                            String number = result.getString("number");
                            String pin = result.getString("pin");
                            int balance = result.getInt("balance");

                            System.out.println(" | " + number + " | " + pin + " | " + balance + " | ");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferBalance(Card card, String targetAccount, int balance) {
        String subtract = "UPDATE card SET balance = balance - ? WHERE number = ? AND pin = ?";
        String enrich = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                try (PreparedStatement preparedStatement = conn.prepareStatement(subtract)) {
                    preparedStatement.setObject(1, balance);
                    preparedStatement.setObject(2, card.getCardNumber());
                    preparedStatement.setObject(3, card.getPinNumber());

                    preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    conn.rollback();
                    System.out.println(e.getMessage());
                }
                try (PreparedStatement preparedStatement = conn.prepareStatement(enrich)) {
                    preparedStatement.setObject(1, balance);
                    preparedStatement.setObject(2, targetAccount);

                    preparedStatement.executeUpdate();

                } catch (SQLException e) {
                    conn.rollback();
                    System.out.println(e.getMessage());
                }
                conn.commit();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBalance(Card card, int balance) {
        String insert = "UPDATE card SET balance = balance + ? WHERE number = ? AND pin = ?";
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                try (PreparedStatement preparedStatement = conn.prepareStatement(insert)) {
                    preparedStatement.setObject(1, balance);
                    preparedStatement.setObject(2, card.getCardNumber());
                    preparedStatement.setObject(3, card.getPinNumber());

                    preparedStatement.executeUpdate();

                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTable(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            // Statement execution
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT NOT NULL," +
                    "pin TEXT NOT NULL," +
                    "balance INTEGER DEFAULT 0)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropTable(Connection conn) {
        try (Statement statement = conn.createStatement()) {
            // Statement execution
            statement.executeUpdate(
                    "DROP TABLE card;"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isTableEmpty(String url) {
        int x = 0;
        try (Connection conn = DriverManager.getConnection(url)) {
            try (Statement statement = conn.createStatement()) {
                // Statement execution
                x = statement.executeUpdate(
                        "SELECT COUNT(*) FROM card;"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return x == 0;
    }


    private boolean isValidTablePresent(String url) {
        String[] columnsNames = {"id", "number", "pin", "balance"};
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData dbm = conn.getMetaData();
                // check if "employee" table is there
                ResultSet tables = dbm.getTables(null,
                        null,
                        "card",
                        null
                );

                if (tables.next()) {
                    DatabaseMetaData md = conn.getMetaData();
                    for (String columnsName : columnsNames) {
                        ResultSet rs = md.getColumns(null, null, "card", columnsName);
                        if (!rs.next()) {
                            return false;
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean isFilePresent(String fileName) {
        File file = new File(fileName);
        return file.isFile();
    }
}

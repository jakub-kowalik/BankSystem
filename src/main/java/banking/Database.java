package banking;

import java.io.File;
import java.sql.*;

public class Database {

    private String url = "jdbc:sqlite:";
    private final String fileName;

    public Database(String fileName) {
        this.url = this.url + fileName;
        this.fileName = fileName;
    }

    void initializeDatabase() {


        if (!checkIfFileExists(fileName)) {
            createDatabase(url);
        }

        if (!checkIfTableExists(url)) {
            repairTableStructure(url);
        }

        //System.out.println(checkIfTableIsEmpty(url));

    }

    void createDatabase(String url) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                createTable(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void repairTableStructure(String url) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                dropTable(conn);
                createTable(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addRecordToTable(Card card) {
        String insert = "INSERT INTO CARD(number, pin) VALUES (?, ?)";
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

    public void deleteRecordFromTable(Card card) {
        String insert = "DELETE FROM CARD WHERE number = ? AND pin = ?";
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

    public boolean checkIfAccountExistInTable(Card card) {
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

    public boolean checkIfAccountExistInTable(String accountNumber) {
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

    public int checkAccountBalance(Card card) {
        int balance = 0;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (Statement statement = conn.createStatement()) {
                    // Statement execution
                    try (ResultSet results = statement.executeQuery("SELECT * FROM CARD WHERE " +
                            "number = " + card.getCardNumber() +
                            " AND pin = " + card.getPinNumber()
                    )) {
                        while (results.next()) {
                            balance = results.getInt(4);
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

    public void readFromTable() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                try (Statement statement = conn.createStatement()) {
                    try (ResultSet greatHouses = statement.executeQuery("SELECT * FROM CARD")) {
                        while (greatHouses.next()) {
                            // Retrieve column values

                            String number = greatHouses.getString("number");
                            String pin = greatHouses.getString("pin");
                            int balance = greatHouses.getInt("balance");

                            System.out.println(" | " + number + " | " + pin + " | " + balance + "\t | ");
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

    public void addToBalance(Card card, int balance) {
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

    private boolean checkIfTableIsEmpty(String url) {
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


    private boolean checkIfTableExists(String url) {
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

    private boolean checkIfFileExists(String fileName) {
        File file = new File(fileName);
        return file.isFile();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Voting {
    private static final String ADMIN_PASSWORD="12345";
    private JTextField inputNameField;
    private JTextField inputNimField;
    private JComboBox<String> inputProdiComboBox;
    private JButton adminButton;
    private JFrame adminFrame;
    private JTextArea adminResultTextArea;

    private void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon backgroundImage = new ImageIcon("D:\\TUGAS\\PBO\\bg1.jpg");
        frame.setContentPane(new JLabel(backgroundImage));
        frame.setLayout(null);

        ImageIcon frontImage = new ImageIcon("D:\\TUGAS\\PBO\\front.png");
        JLabel frontLabel = new JLabel(frontImage);
        frontLabel.setBounds(100, 50, frontImage.getIconWidth(), frontImage.getIconHeight());
        frame.add(frontLabel);

        JLabel titleLabel = new JLabel("VOTENESA");
        titleLabel.setFont(new Font("ELEPHANT", Font.BOLD, 20));
        titleLabel.setBounds(150, 10, 250, 30);
        frame.add(titleLabel);

        JLabel nameLabel = new JLabel("Nama                   :");
        nameLabel.setBounds(80, 50, 100, 30);
        frame.add(nameLabel);

        inputNameField = new JTextField();
        inputNameField.setBounds(180, 50, 250, 30);
        frame.add(inputNameField);

        JLabel nimLabel = new JLabel("NIM                      :");
        nimLabel.setBounds(80, 90, 100, 30);
        frame.add(nimLabel);

        inputNimField = new JTextField();
        inputNimField.setBounds(180, 90, 250, 30);
        frame.add(inputNimField);

        JLabel prodiLabel = new JLabel("Program Studi    :");
        prodiLabel.setBounds(80, 130, 100, 30);
        frame.add(prodiLabel);

        // Assuming you have a method to retrieve Prodi options from the database
        String[] prodiOptions = getProdiOptionsFromDatabase();
        inputProdiComboBox = new JComboBox<>(prodiOptions);
        inputProdiComboBox.setBounds(180    , 130, 250, 30);
        frame.add(inputProdiComboBox);

        JButton viewProfileButton1 = new JButton("Profil Calon 1");
        viewProfileButton1.setBounds(80, 340, 120, 30);
        viewProfileButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCandidateProfile(0, "Hermawan sudrajat", "Sistem Informasi");
            }
        });
        viewProfileButton1.setBackground(Color.CYAN);
        frame.add(viewProfileButton1);

        JButton viewProfileButton2 = new JButton("Profil Calon 2");
        viewProfileButton2.setBounds(250, 340, 120, 30);
        viewProfileButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCandidateProfile(1, "Riyan babayo", "Teknik Informatika");
            }
        });
        viewProfileButton2.setBackground(Color.CYAN);
        frame.add(viewProfileButton2);

        JLabel ketLabel = new JLabel("*Tekan Tombol Reset Sebelum Mengisi!");
        ketLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
        ketLabel.setBounds(385, 325, 500, 30);
        frame.add(ketLabel);

        JLabel infopilihLabel = new JLabel("*Tekan Gambar jika ingin memilih calon!");
        infopilihLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
        infopilihLabel.setBounds(385, 345, 500, 30);
        frame.add(infopilihLabel);

        JLabel kandidat1Label = new JLabel("CALON 1");
        kandidat1Label.setFont(new Font("Times New Roman", Font.BOLD, 13));
        kandidat1Label.setBounds(110, 180, 120, 30);
        frame.add(kandidat1Label);

        JLabel kandidat2Label = new JLabel("CALON 2");
        kandidat2Label.setFont(new Font("Times New Roman", Font.BOLD, 13));
        kandidat2Label.setBounds(285, 180, 120, 30);
        frame.add(kandidat2Label);

        JButton cancelButton = new JButton("Reset");
        cancelButton.setBounds(460, 90, 120, 30);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
        cancelButton.setBackground(Color.cyan);
        frame.add(cancelButton);

        JButton saveButton = new JButton("Simpan");
        saveButton.setBackground(Color.GREEN);
        saveButton.setBounds(460, 130, 120, 30);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveVote();
            }
        });
        frame.add(saveButton);

        adminButton = new JButton("Admin");
        adminButton.setBounds(460, 50, 120, 30);
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPasswordDialog(ADMIN_PASSWORD, true);
            }
        });
        adminButton.setBackground(Color.CYAN);
        frame.add(adminButton);

        JButton candidate1ImageLabel = new JButton();
        candidate1ImageLabel.setBounds(89, 220, 100, 100); // Adjust the position and size as needed
        candidate1ImageLabel.setIcon(new ImageIcon("D:\\TUGAS\\PBO\\iconn.png")); // Replace "path/to/candidate1-icon.png" with the actual path to candidate 1's icon file
        candidate1ImageLabel.addActionListener(new VoteButtonActionListener(0));
        frame.add(candidate1ImageLabel);

        JButton candidate2ImageLabel = new JButton();
        candidate2ImageLabel.setBounds(259, 220, 100, 100); // Adjust the position and size as needed
        candidate2ImageLabel.setIcon(new ImageIcon("D:\\TUGAS\\PBO\\iconn.png")); // Replace "path/to/candidate2-icon.png" with the actual path to candidate 2's icon file
        candidate2ImageLabel.addActionListener(new VoteButtonActionListener(1));
        frame.add(candidate2ImageLabel);

        frame.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private String[] getProdiOptionsFromDatabase() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Connect to MySQL database
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
            String username = "root"; // Change with your MySQL username
            String password = ""; // Change with your MySQL password
            connection = DriverManager.getConnection(url, username, password);

            // Select all Program Studi options from the prodi table
            String selectQuery = "SELECT nama_prodi FROM prodi";
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            // Collect Prodi options into a list
            List<String> prodiList = new ArrayList<>();
            while (resultSet.next()) {
                String prodi = resultSet.getString("nama_prodi");
                prodiList.add(prodi);
            }

            // Convert the list to an array
            return prodiList.toArray(new String[0]);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve Program Studi options. Please try again.");
            return new String[]{"Error"};
        } finally {
            // Close the connection and statement
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to close database connections. Please try again.");
            }
        }
    }

    private void showAdminPanel() {
        // Create and show the Admin panel
        adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 540);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon backgroundImage = new ImageIcon("D:\\TUGAS\\PBO\\bg5.jpg");
        adminFrame.setContentPane(new JLabel(backgroundImage));
        adminFrame.setLayout(null);


        JLabel titleLabel = new JLabel("DATA PEMILIH");
        titleLabel.setFont(new Font("ELEPHANT", Font.BOLD, 18));
        titleLabel.setBounds(120, 10, 200, 30);
        adminFrame.add(titleLabel);

        // Add components to the Admin panel
        adminResultTextArea = new JTextArea();
        adminResultTextArea.setEditable(false);
        JScrollPane adminScrollPane = new JScrollPane(adminResultTextArea);
        adminScrollPane.setBounds(50, 50, 300, 300);
        adminFrame.add(adminScrollPane);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(50, 360, 100, 30);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
        refreshButton.setBackground(Color.green);
        adminFrame.add(refreshButton);

        JButton closeAdminButton = new JButton("Close");
        closeAdminButton.setBounds(250, 360, 100, 30);
        closeAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
            }
        });
        closeAdminButton.setBackground(Color.green);
        adminFrame.add(closeAdminButton);
        JButton showWinnerButton = new JButton("Winner");
        showWinnerButton.setBounds(150, 460, 100, 30);
        showWinnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String winner = calculateWinner();
                JOptionPane.showMessageDialog(null, "Pemenang Pemilihan \nKetua Himpunan Mahasiswa Teknik Informatika \n" + winner, "Winner", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        showWinnerButton.setBackground(Color.GREEN);
        adminFrame.add(showWinnerButton);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(50, 410, 100, 30);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVote();
            }
        });
        updateButton.setBackground(Color.green);
        adminFrame.add(updateButton);

        JButton deleteButton = new JButton("Hapus");
        deleteButton.setBounds(250, 410, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVote();
            }
        });
        deleteButton.setBackground(Color.green);
        adminFrame.add(deleteButton);


        // Retrieve and display data in the Admin panel
        retrieveAndDisplayData();

        adminFrame.setLocationRelativeTo(null);
        adminFrame.setVisible(true);
    }
    private int getTotalVoters() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int totalVotes = 0;

        try {
            // Connect to MySQL database
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
            String username = "root"; // Change with your MySQL username
            String password = ""; // Change with your MySQL password
            connection = DriverManager.getConnection(url, username, password);

            // Count the total number of voters
            String countQuery = "SELECT COUNT(*) FROM voting";
            preparedStatement = connection.prepareStatement(countQuery);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalVotes = resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve total voters. Please try again.");
        } finally {
            // Close the connection and statement
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to close database connections. Please try again.");
            }
        }

        return totalVotes;
    }
    private void refreshData() {
        // This method should contain the logic to refresh the displayed data
        // You can reuse the existing logic from the retrieveAndDisplayData method
        retrieveAndDisplayData();
    }

    private void showPasswordDialog(String correctPassword, boolean isAdmin) {
        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(null, passwordField, "Masukkan Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            char[] enteredPassword = passwordField.getPassword();
            if (checkPasswordAndOpenPanel(correctPassword, enteredPassword, isAdmin)) {
                SwingUtilities.invokeLater(() -> showAdminPanel());
            } else {
                JOptionPane.showMessageDialog(null, "Password salah. Coba lagi!", "Peringatan!", JOptionPane.ERROR_MESSAGE);
            }

            Arrays.fill(enteredPassword, ' ');
        }
    }

    private boolean checkPasswordAndOpenPanel(String correctPassword, char[]enteredPassword, boolean isAdmin){
        String enteredPasswordString= new String(enteredPassword);
        return correctPassword.equals(enteredPasswordString);
    }
    private void retrieveAndDisplayData() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Connect to MySQL database
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
            String username = "root"; // Change with your MySQL username
            String password = ""; // Change with your MySQL password
            connection = DriverManager.getConnection(url, username, password);

            // Select all data from the voting table
            String selectQuery = "SELECT Nama, Nim, Prodi, Sudah_memilih FROM voting";
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            // Count the votes for each candidate
            int candidate1Votes = 0;
            int candidate2Votes = 0;
            int totalVotes = 0;


            StringBuilder dataText = new StringBuilder("");
            while (resultSet.next()) {
                String sudahMemilih = resultSet.getString("Sudah_memilih");

                if ("Calon 1".equals(sudahMemilih)) {
                    candidate1Votes++;
                } else if ("Calon 2".equals(sudahMemilih)) {
                    candidate2Votes++;
                }

                String nama = resultSet.getString("Nama");
                String nim = resultSet.getString("Nim");
                String prodi = resultSet.getString("Prodi");

                dataText.append("Nama: ").append(nama)
                        .append("\nNIM: ").append(nim)
                        .append("\nProdi: ").append(prodi)
                        .append("\nSudah Memilih: ").append(sudahMemilih)
                        .append("\n\n");

                totalVotes++;
            }
            dataText.append("----------------------------------------------------------------------");
            dataText.append("\n");
            dataText.append("                               TOTAL PEMILIH : "+totalVotes);
            dataText.append("\n");

            adminResultTextArea.setText(dataText.toString());

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data pemilih. Silakan coba lagi.");
        } finally {
            // Close the connection and statement
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal menampilkan data pemilih. Pesan kesalahan: " + ex.getMessage());
            }
        }
    }

    private static final String[] candidates = {
            "Visi:\nMembangun masa depan yang cerah untuk semua mahasiswa.\n\nMisi:\n1. Meningkatkan kualitas pendidikan.\n2. Meningkatkan fasilitas kampus.\n3. Mendukung pengembangan bakat mahasiswa.",
            "Visi:\nMenjadi agen perubahan dalam dunia pendidikan.\n\nMisi:\n1. Meningkatkan kolaborasi dengan industri.\n2. Memperkenalkan program pembelajaran inovatif.\n3. Memastikan keberlanjutan lingkungan kampus."
    };
    private int selectedCandidate;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Voting evotingApp = new Voting();
            evotingApp.createAndShowGUI();
        });
    }

    private void showCandidateProfile(int candidateNumber, String candidateName, String candidateProdi) {
        // Show candidate profile in a dialog
        String candidateInfo = candidates[candidateNumber];
        String profileText = "Nama : " + candidateName +
                "\nProgram Studi : " + candidateProdi +
                "\n\n" + candidateInfo;

        JOptionPane.showMessageDialog(null, profileText, "Profil Calon " + (candidateNumber + 1), JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetForm() {
        inputNameField.setText("");
        inputNimField.setText("");
        inputProdiComboBox.getAction();
        selectedCandidate = -1; // Reset selected candidate
    }
    private String calculateWinner() {
        int candidate1Votes = 0;
        int candidate2Votes = 0;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Connect to MySQL database
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
            String username = "root"; // Change with your MySQL username
            String password = ""; // Change with your MySQL password
            connection = DriverManager.getConnection(url, username, password);

            // Select all data from the voting table
            String selectQuery = "SELECT Sudah_memilih FROM voting";
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String sudahMemilih = resultSet.getString("Sudah_memilih");

                if ("Calon 1".equals(sudahMemilih)) {
                    candidate1Votes++;
                } else if ("Calon 2".equals(sudahMemilih)) {
                    candidate2Votes++;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to calculate the winner. Please try again.");
        } finally {
            // Close the connection and statement
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to close database connections. Please try again.");
            }
        }

        if (candidate1Votes > candidate2Votes) {
            return "Calon 1";
        } else if (candidate2Votes > candidate1Votes) {
            return "Calon 2";
        } else {
            return "Hasil Vote Seri";
        }
    }


    private void saveVote() {
        if (selectedCandidate == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih calon terlebih dahulu.");
        } else {
            String name = inputNameField.getText();
            String nim = inputNimField.getText();
            String prodi = inputProdiComboBox.getSelectedItem().toString();
            String calonTerpilih = (selectedCandidate == 0) ? "Calon 1" : "Calon 2";

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Connect to MySQL database
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
                String username = "root"; // Change with your MySQL username
                String password = ""; // Change with your MySQL password
                connection = DriverManager.getConnection(url, username, password);

                // Check if NIM already exists in the table
                String checkNimQuery = "SELECT COUNT(*) FROM voting WHERE Nim=?";
                preparedStatement = connection.prepareStatement(checkNimQuery);
                preparedStatement.setString(1, nim);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    // If NIM already exists, update the record
                    String updateQuery = "UPDATE voting SET Nama=?, Prodi=?, Sudah_memilih=? WHERE Nim=?";
                    preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, prodi);
                    preparedStatement.setString(3, calonTerpilih);
                    preparedStatement.setString(4, nim);
                    preparedStatement.executeUpdate();
                } else {
                    // If NIM doesn't exist, insert a new record
                    String insertQuery = "INSERT INTO voting (Nama, Nim, Prodi, Sudah_memilih) VALUES (?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, nim);
                    preparedStatement.setString(3, prodi);
                    preparedStatement.setString(4, calonTerpilih);
                    preparedStatement.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Vote Berhasil");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal menyimpan vote. Silakan coba lagi.");
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan vote. Pesan kesalahan: " + e.getMessage());
                }
            }
        }
    }

    private class VoteButtonActionListener implements ActionListener {
        private int candidateNumber;

        public VoteButtonActionListener(int candidateNumber) {
            this.candidateNumber = candidateNumber;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedCandidate != -1) {
                JOptionPane.showMessageDialog(null, "Maaf, Anda sudah memilih.");
            } else {
                selectedCandidate = candidateNumber;
                String name = inputNameField.getText();
                String nim = inputNimField.getText();
                String prodi = inputProdiComboBox.getSelectedItem().toString();

                String candidateName = (selectedCandidate == 0) ? "Hermawan sudrajat" : "Riyan babayo";
                JOptionPane.showMessageDialog(null, "Terima kasih, " + name + " (" + nim + ", " + prodi + "), Anda memilih " + candidateName + "!");
            }
        }
    }
    private void updateVote() {
        if (selectedCandidate == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih calon terlebih dahulu.");
        } else {
            String name = inputNameField.getText();
            String nim = inputNimField.getText();
            String prodi = inputProdiComboBox.getSelectedItem().toString();
            String calonTerpilih = (selectedCandidate == 0) ? "Calon 1" : "Calon 2";

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Connect to MySQL database
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
                String username = "root"; // Change with your MySQL username
                String password = ""; // Change with your MySQL password
                connection = DriverManager.getConnection(url, username, password);

                // Check if NIM already exists in the table
                String checkNimQuery = "SELECT COUNT(*) FROM voting WHERE Nim=?";
                preparedStatement = connection.prepareStatement(checkNimQuery);
                preparedStatement.setString(1, nim);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    // If NIM already exists, update the record
                    String updateQuery = "UPDATE voting SET Nama=?, Prodi=?, Sudah_memilih=? WHERE Nim=?";
                    preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, prodi);
                    preparedStatement.setString(3, calonTerpilih);
                    preparedStatement.setString(4, nim);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Data pemilih dengan NIM " + nim + " berhasil diperbarui.");
                } else {
                    JOptionPane.showMessageDialog(null, "NIM " + nim + " tidak ditemukan. Gunakan tombol Simpan untuk menambahkan data baru.");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal memperbarui data pemilih. Silakan coba lagi.");
            } finally {
                // Close the connection and statement
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Gagal memperbarui data pemilih. Pesan kesalahan: " + e.getMessage());
                }
            }
        }
    }
    private void lihatData() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Connect to MySQL database
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
            String username = "root"; // Change with your MySQL username
            String password = ""; // Change with your MySQL password
            connection = DriverManager.getConnection(url, username, password);

            // Select all data from the voting table
            String selectQuery = "SELECT Nama, Nim, Prodi, Sudah_memilih FROM voting";
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            // Display the data in a dialog
            StringBuilder dataText = new StringBuilder("DATA PEMILIH\n\n");
            while (resultSet.next()) {
                String nama = resultSet.getString("Nama");
                String nim = resultSet.getString("Nim");
                String prodi = resultSet.getString("Prodi");
                String sudahMemilih = resultSet.getString("Sudah_memilih");

                dataText.append("Nama: ").append(nama)
                        .append("\nNIM: ").append(nim)
                        .append("\nProdi: ").append(prodi)
                        .append("\nSudah Memilih: ").append(sudahMemilih)
                        .append("\n\n");
            }

            JOptionPane.showMessageDialog(null, dataText.toString(), "Data Pemilih", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data pemilih. Silakan coba lagi.");
        } finally {
            // Close the connection and statement
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal menampilkan data pemilih. Pesan kesalahan: " + ex.getMessage());
            }
        }
    }
    private void deleteVote() {
        if (selectedCandidate == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih calon terlebih dahulu.");
        } else {
            String nim = inputNimField.getText();

            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                // Connect to MySQL database
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/vnesa"; // Change with your MySQL database URL
                String username = "root"; // Change with your MySQL username
                String password = ""; // Change with your MySQL password
                connection = DriverManager.getConnection(url, username, password);

                // Delete the voting record based on NIM
                String sql = "DELETE FROM voting WHERE Nim=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nim);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Data pemilih dengan NIM " + nim + " berhasil dihapus.");
                resetForm(); // Reset the form after deletion
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Gagal menghapus data pemilih. Silakan coba lagi.");
            } finally {
                // Close the connection and statement
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Gagal menghapus data pemilih. Pesan kesalahan: " + ex.getMessage());
                }
            }
        }
    }

}
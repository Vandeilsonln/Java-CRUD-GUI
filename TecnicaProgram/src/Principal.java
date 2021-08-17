import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

public class Principal {

    private JPanel panel1;
    private JButton atualizarButton;
    private JButton primeiroButton;
    private JButton anteriorButton;
    private JButton proximoButton;
    private JButton ultimoButton;
    private JButton limparCamposButton;
    private JButton deletarButton;
    private JButton inserirButton;
    private JTextField txtDescricao;
    private JTextField txtNome;
    private JTextField txtSigla;
    private JTextArea navegacao;
    private JLabel labelSigla;
    private JLabel labelNome;
    private JLabel labelDescr;

    public Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);;
    public Statement stmt;
    public ResultSet rs;

    private static final String URL = "jdbc:mysql://10.151.16.55:3309/my_database";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Principal() throws SQLException {
        inserirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                try {
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                    String query = "INSERT INTO curso VALUES(" +
                        "'" + txtSigla.getText().toLowerCase() + "', '" + txtNome.getText() + "', '" + txtDescricao.getText() + "')";

                    JOptionPane.showMessageDialog(null, query);
                    int i = stmt.executeUpdate(query);
                    stmt.close();

                    if (i > 0) {
                        JOptionPane.showMessageDialog(null, "Curso Cadastrado com sucesso!");
                        abrirTabela();
                    }

                } catch (ClassNotFoundException | SQLException e) {
                    System.out.println(e);

                }
            }
        });

        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                String response = null;

                try {
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                    String query = "DELETE FROM curso WHERE(sigla='" + txtSigla.getText().toLowerCase() + "')";
                    JOptionPane.showMessageDialog(null, query);

                    int i = stmt.executeUpdate(query);
                    stmt.close();

                    if (i > 0) {
                        response = "Curso deletado com sucesso";
                        txtSigla.setText("");
                        txtNome.setText("");
                        txtDescricao.setText("");
                        abrirTabela();
                    } else {
                        response = "Não foi encontrado curso com a sigla " + txtSigla.getText();
                    }

                    JOptionPane.showMessageDialog(null, response);

                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {

                String query = "UPDATE curso SET sigla='" + txtSigla.getText().toLowerCase() +
                                "', nome='" + txtNome.getText().toLowerCase() +
                                "', descricao='" + txtDescricao.getText().toLowerCase() +
                                "' WHERE sigla='" + txtSigla.getText().toLowerCase() + "'";
                try {
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    JOptionPane.showMessageDialog(null, query);

                    int i = stmt.executeUpdate(query);
                    stmt.close();

                    if (i > 0) {
                        JOptionPane.showMessageDialog(null, "Curso alterado com sucesso");
                        abrirTabela();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Falha no engano");
                    System.out.println(e);
                }

            }
        });
        limparCamposButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                txtSigla.setText("");
                txtNome.setText("");
                txtDescricao.setText("");
            }
        });
        primeiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                try {
                    rs.first();
                    atualizaCampos();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                try {
                    rs.previous();
                    atualizaCampos();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
        proximoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                try {
                    rs.next();
                    atualizaCampos();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
        ultimoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                try {
                    rs.last();
                    atualizaCampos();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        });
    }

    public void atualizaCampos() {
        try {
            txtSigla.setText("" + rs.getString("sigla"));
            txtNome.setText("" + rs.getString("nome"));
            txtDescricao.setText("" + rs.getString("descricao"));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void abrirTabela() {
        String query = "Select * from curso";
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery(query);
            rs.first();
            atualizaCampos();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        JFrame jFrame = new JFrame("CRUD - Vandeilson");
        jFrame.setContentPane(new Principal().panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}

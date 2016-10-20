package exemplobd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class BancoDados {

    public static Connection cn;

    public static void conectarBanco() {
        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            cn = DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/AulaEJB", "APP", "app");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Driver não encontrado.");
            System.exit(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar no BD.");
            System.exit(0);

        }

    }

    public static void desconectarBanco() {
        try {
            cn.close();
        } catch (SQLException ex) {
        }
        System.exit(0);

    }

    public static Cliente pesquisarCliente(int codigo) {

        Cliente cliente = null;

        try {

            PreparedStatement st = cn.prepareCall(
                    "select * from cliente where codigo = ?");
            st.setInt(1, codigo);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setCodigo(rs.getInt("codigo"));
                cliente.setNome(rs.getString("nome"));
                cliente.setIdade(rs.getInt("idade"));
                cliente.setSexo(rs.getString("sexo"));
            }

            rs.close();
            st.close();

        } catch (SQLException ex) {
            Logger.getLogger(FormCadastro.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cliente;

    }

    public static void excluirCliente(int codigo) {
        try {
            PreparedStatement st = cn.prepareCall("delete from cliente where codigo = ?");
            st.setInt(1, codigo);
            st.execute();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void alterarCliente(Cliente cliente) {
        try {
            PreparedStatement st;
            st = cn.prepareCall(
                    "update cliente set nome = ?, idade = ?, sexo = ?"
                    + " where codigo = ?");

            st.setInt(4, cliente.getCodigo());
            st.setString(1, cliente.getNome());
            st.setInt(2, cliente.getIdade());
            st.setString(3, cliente.getSexo());
            
            st.execute();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void inserirCliente(Cliente cliente) {
        try {
            PreparedStatement st;
            st = cn.prepareCall(
                    "insert into cliente (codigo,nome,idade,sexo)"
                    + " values (?,?,?,?)");

            st.setInt(1, cliente.getCodigo());
            st.setString(2, cliente.getNome());
            st.setInt(3, cliente.getIdade());
            st.setString(4, cliente.getSexo());
            
            st.execute();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}

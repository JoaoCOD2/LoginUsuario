package telas;

import java.sql.*;
import DAO.Mod_conexao;
import javax.swing.JOptionPane;

public class TelaUsuarios extends javax.swing.JInternalFrame {

    Connection conexao = null;

    public TelaUsuarios() {
        initComponents();
        conexao = Mod_conexao.conector();
        addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                try {
                    if (conexao != null) {
                        conexao.close();
                    }
                } catch (SQLException ignored) {
                }
            }
        });
    }

    private String getSenha() {
        return new String(pwdSenha.getPassword());
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        pwdSenha.setText("");
    }

    private boolean validarId() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o ID.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtId.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }
        if (!txtEmail.getText().trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            JOptionPane.showMessageDialog(this, "E-mail inválido.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        if (getSenha().length() < 6) {
            JOptionPane.showMessageDialog(this, "A senha deve ter ao menos 6 caracteres.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            pwdSenha.requestFocus();
            return false;
        }
        return true;
    }

    private void consultar() {
        if (conexao == null || !validarId()) {
            return;
        }
        String sql = "SELECT nome, email FROM tb_usuarios WHERE id = ?";
        try (PreparedStatement pst = conexao.prepareStatement(sql)) {
            pst.setString(1, txtId.getText().trim());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    txtNome.setText(rs.getString("nome"));
                    txtEmail.setText(rs.getString("email"));
                    pwdSenha.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário não cadastrado.");
                    limparCampos();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionar() {
        if (conexao == null || !validarCampos()) {
            return;
        }
        String sql = "INSERT INTO tb_usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try (PreparedStatement pst = conexao.prepareStatement(sql)) {
            pst.setString(1, txtNome.getText().trim());
            pst.setString(2, txtEmail.getText().trim());
            pst.setString(3, getSenha());
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso.");
                limparCampos();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Este e-mail já está cadastrado.",
                        "Duplicado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alterar() {
        if (conexao == null || !validarId() || !validarCampos()) {
            return;
        }
        String sql = "UPDATE tb_usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
        try (PreparedStatement pst = conexao.prepareStatement(sql)) {
            pst.setString(1, txtNome.getText().trim());
            pst.setString(2, txtEmail.getText().trim());
            pst.setString(3, getSenha());
            pst.setString(4, txtId.getText().trim());
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Usuário alterado com sucesso.");
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Este e-mail já está cadastrado.",
                        "Duplicado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void apagar() {
        if (conexao == null || !validarId()) {
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este usuário?", "Atenção",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirma != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM tb_usuarios WHERE id = ?";
        try (PreparedStatement pst = conexao.prepareStatement(sql)) {
            pst.setString(1, txtId.getText().trim());
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Usuário apagado com sucesso.");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao apagar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblId = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblSenha = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        lblTitulo = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnVisualizar = new javax.swing.JButton();
        btnApagar = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        pwdSenha = new javax.swing.JPasswordField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        lblId.setText("ID:");

        lblNome.setText("Nome:");

        lblEmail.setText("Email:");

        lblSenha.setText("Senha:");

        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitulo.setText("Cadastro de usuários");

        btnAdd.setText("Adicionar");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnVisualizar.setText("Visualizar");
        btnVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarActionPerformed(evt);
            }
        });

        btnApagar.setText("Apagar");
        btnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarActionPerformed(evt);
            }
        });

        btnEdit.setText("Editar");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(lblTitulo)
                        .addGap(0, 103, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEmail)
                            .addComponent(lblSenha)
                            .addComponent(lblNome))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNome)
                            .addComponent(txtEmail)
                            .addComponent(txtId)
                            .addComponent(pwdSenha)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAdd)
                            .addComponent(btnVisualizar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(61, 61, 61)))
                .addGap(110, 110, 110))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTitulo)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSenha)
                    .addComponent(pwdSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVisualizar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnApagar)))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnVisualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisualizarActionPerformed
        // TODO add your handling code here:
        consultar();
    }//GEN-LAST:event_btnVisualizarActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        alterar();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarActionPerformed
        // TODO add your handling code here:
        apagar();
    }//GEN-LAST:event_btnApagarActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtEmailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnApagar;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnVisualizar;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPasswordField pwdSenha;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}

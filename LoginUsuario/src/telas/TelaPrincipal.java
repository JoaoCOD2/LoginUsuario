package telas;

import java.awt.BorderLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TelaPrincipal extends javax.swing.JFrame {

    private TelaCliente telaCliente;
    private TelaUsuarios telaUsuarios;
    private JLabel lblBoasVindas;
    private JLabel lblRodape;
    private final String usuarioLogado;

    public TelaPrincipal() {
        this("Usuário");
    }

    public TelaPrincipal(String nomeUsuario) {
        this.usuarioLogado = nomeUsuario;
        initComponents();
        montarLayout();
        iniciarRelogio();
        menuSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke("control Q"));
        menuCadClientes.setAccelerator(javax.swing.KeyStroke.getKeyStroke("control shift C"));
        setTitle("Sistema de Cadastro");
        setExtendedState(MAXIMIZED_BOTH);
    }

    private void montarLayout() {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        desktop.setLayout(null);
        desktop.setBackground(new java.awt.Color(236, 240, 245));
        getContentPane().add(desktop, BorderLayout.CENTER);

        lblRodape = new JLabel();
        lblRodape.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12));
        getContentPane().add(lblRodape, BorderLayout.SOUTH);

        lblBoasVindas = new JLabel("<html><div style='text-align:center'>"
                + "<span style='font-size:28px'>Bem-vindo, " + usuarioLogado + "!</span><br><br>"
                + "<span style='font-size:14px;color:gray'>Use o menu Cadastro para começar</span>"
                + "</div></html>", JLabel.CENTER);
        desktop.add(lblBoasVindas, javax.swing.JLayeredPane.FRAME_CONTENT_LAYER);

        desktop.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                lblBoasVindas.setBounds(0, 0, desktop.getWidth(), desktop.getHeight());
            }
        });
    }

    private void iniciarRelogio() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        javax.swing.Timer timer = new javax.swing.Timer(1000, e
                -> lblRodape.setText("Usuário: " + usuarioLogado + "     |     "
                        + LocalDateTime.now().format(fmt)));
        timer.setInitialDelay(0);
        timer.start();
    }

    private void abrirTela(javax.swing.JInternalFrame tela) {
        desktop.add(tela);
        tela.setLocation((desktop.getWidth() - tela.getWidth()) / 2,
                (desktop.getHeight() - tela.getHeight()) / 2);
        tela.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        menu = new javax.swing.JMenuBar();
        menuCad = new javax.swing.JMenu();
        menuCadClientes = new javax.swing.JMenuItem();
        usuario = new javax.swing.JMenuItem();
        menuOpcao = new javax.swing.JMenu();
        menuSair = new javax.swing.JMenuItem();
        menuAjuda = new javax.swing.JMenu();
        menuSobre = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 626, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 554, Short.MAX_VALUE)
        );

        jLabel1.setText("Bem Vindo");

        menuCad.setText("Cadastro");
        menuCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadActionPerformed(evt);
            }
        });

        menuCadClientes.setText("Cliente");
        menuCadClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadClientesActionPerformed(evt);
            }
        });
        menuCad.add(menuCadClientes);

        usuario.setText("Usuario");
        usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usuarioActionPerformed(evt);
            }
        });
        menuCad.add(usuario);

        menu.add(menuCad);

        menuOpcao.setText("Opções");

        menuSair.setText("Sair");
        menuSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSairActionPerformed(evt);
            }
        });
        menuOpcao.add(menuSair);

        menu.add(menuOpcao);

        menuAjuda.setText("Ajuda");
        menuAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAjudaActionPerformed(evt);
            }
        });

        menuSobre.setText("Sobre");
        menuSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSobreActionPerformed(evt);
            }
        });
        menuAjuda.add(menuSobre);

        menu.add(menuAjuda);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(desktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(desktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuCadClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadClientesActionPerformed
        if (telaCliente == null || telaCliente.isClosed()) {
            telaCliente = new TelaCliente();
            abrirTela(telaCliente);
        } else {
            telaCliente.toFront();
        }
    }//GEN-LAST:event_menuCadClientesActionPerformed

    private void menuAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAjudaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuAjudaActionPerformed

    private void menuSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSairActionPerformed
        int sair = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja sair?",
                "Atenção", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_menuSairActionPerformed

    private void menuSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSobreActionPerformed
        new TelaSobre().setVisible(true);
    }//GEN-LAST:event_menuSobreActionPerformed

    private void usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usuarioActionPerformed
        if (telaUsuarios == null || telaUsuarios.isClosed()) {
            telaUsuarios = new TelaUsuarios();
            abrirTela(telaUsuarios);
        } else {
            telaUsuarios.toFront();
        }
    }//GEN-LAST:event_usuarioActionPerformed

    private void menuCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuCadActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenu menuAjuda;
    private javax.swing.JMenu menuCad;
    private javax.swing.JMenuItem menuCadClientes;
    private javax.swing.JMenu menuOpcao;
    private javax.swing.JMenuItem menuSair;
    private javax.swing.JMenuItem menuSobre;
    private javax.swing.JMenuItem usuario;
    // End of variables declaration//GEN-END:variables
}

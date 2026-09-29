package telas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import javax.swing.JOptionPane;
import DAO.Mod_conexao;

public class TelaCliente extends javax.swing.JInternalFrame {

    private Connection conexao;

    private final DateTimeFormatter FORMATO_DATA
            = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public TelaCliente() {
        initComponents();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        conexao = Mod_conexao.conector();
        configurarMascaras();
        rbCPF.setSelected(true);
        configurarMascaraDocumento("CPF");
    }

    private void configurarMascaras() {
        try {
            javax.swing.text.MaskFormatter telefone
                    = new javax.swing.text.MaskFormatter("(##) #####-####");
            telefone.setPlaceholderCharacter('_');

            javax.swing.text.MaskFormatter data
                    = new javax.swing.text.MaskFormatter("##/##/####");
            data.setPlaceholderCharacter('_');

            javax.swing.text.MaskFormatter cpf
                    = new javax.swing.text.MaskFormatter("###.###.###-##");
            cpf.setPlaceholderCharacter('_');

            txtTeleCliente.setFormatterFactory(
                    new javax.swing.text.DefaultFormatterFactory(telefone)
            );

            txtDatNascCliente.setFormatterFactory(
                    new javax.swing.text.DefaultFormatterFactory(data)
            );

            TXTdocumento.setFormatterFactory(
                    new javax.swing.text.DefaultFormatterFactory(cpf)
            );

        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao configurar máscaras."
            );
        }
    }

    private void configurarMascaraDocumento(String tipo) {
        try {
            javax.swing.text.MaskFormatter mascara;

            if (tipo.equals("CPF")) {
                mascara = new javax.swing.text.MaskFormatter("###.###.###-##");
            } else {
                mascara = new javax.swing.text.MaskFormatter("##.###.###/####-##");
            }

            mascara.setPlaceholderCharacter('_');

            TXTdocumento.setFormatterFactory(
                    new javax.swing.text.DefaultFormatterFactory(mascara)
            );

            TXTdocumento.setValue(null);

        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao configurar máscara do documento."
            );
        }
    }

    // =========================
    // LIMPAR CAMPOS
    // =========================
    private void limparCampos() {
        txtIdCliente.setText("");
        txtNomeCliente.setText("");
        txtEndeCliente.setText("");
        cmbCidade.setSelectedIndex(0);
        cmbUf.setSelectedIndex(0);
        TXTdocumento.setText("");
        txtTeleCliente.setText("");
        txtDatNascCliente.setText("");
        rbCPF.setSelected(true);
        configurarMascaraDocumento("CPF");
    }

    // =========================
    // VERIFICAR CONEXÃO
    // =========================
    private boolean verificarConexao() {
        if (conexao == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível conectar ao banco de dados."
            );
            return false;
        }

        return true;
    }

    // =========================
    // PEGAR TIPO DO CLIENTE
    // =========================
    private String getTipoCliente() {

        if (rbCPF.isSelected()) {
            return "PF";
        }

        if (RB.isSelected()) {
            return "PJ";
        }

        JOptionPane.showMessageDialog(
                this,
                "Selecione CPF ou CNPJ."
        );

        return null;
    }

    private String digitos(String s) {
        return s.replaceAll("\\D", "");
    }

    private boolean cpfValido(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        for (int t = 9; t < 11; t++) {
            int soma = 0;
            for (int i = 0; i < t; i++) {
                soma += (cpf.charAt(i) - '0') * (t + 1 - i);
            }
            int dv = (soma * 10) % 11;
            if (dv == 10) {
                dv = 0;
            }
            if (dv != cpf.charAt(t) - '0') {
                return false;
            }
        }
        return true;
    }

    private boolean cnpjValido(String cnpj) {
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return cnpj.charAt(12) - '0' == dvCnpj(cnpj, pesos1)
                && cnpj.charAt(13) - '0' == dvCnpj(cnpj, pesos2);
    }

    private int dvCnpj(String cnpj, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos[i];
        }
        int r = soma % 11;
        return r < 2 ? 0 : 11 - r;
    }

    // =========================
// VALIDAR CAMPOS
// =========================
    private boolean validarCampos() {

        if (txtNomeCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do cliente.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtNomeCliente.requestFocus();
            return false;
        }

        if (txtEndeCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o endereço do cliente.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtEndeCliente.requestFocus();
            return false;
        }

        if (cmbCidade.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma cidade.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            cmbCidade.requestFocus();
            return false;
        }

        if (cmbUf.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma UF.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            cmbUf.requestFocus();
            return false;
        }

        String tipo = getTipoCliente();
        if (tipo == null) {
            return false;
        }

        String doc = digitos(TXTdocumento.getText());
        boolean docOk = tipo.equals("PF") ? cpfValido(doc) : cnpjValido(doc);
        if (!docOk) {
            JOptionPane.showMessageDialog(this, "CPF/CNPJ inválido.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            TXTdocumento.requestFocus();
            return false;
        }

        if (digitos(txtTeleCliente.getText()).length() != 11) {
            JOptionPane.showMessageDialog(this, "Telefone incompleto.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtTeleCliente.requestFocus();
            return false;
        }

        if (!validarData()) {
            return false;
        }

        return true;
    }

// =========================
// VALIDAR DATA
// =========================
    private boolean validarData() {

        String data = txtDatNascCliente.getText().trim();

        try {
            LocalDate nasc = LocalDate.parse(data, FORMATO_DATA);

            if (nasc.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "A data não pode ser futura.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
                txtDatNascCliente.requestFocus();
                return false;
            }

            return true;

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Data inválida.\nDigite no formato DD/MM/AAAA.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtDatNascCliente.requestFocus();
            return false;
        }
    }

    // =========================
    // CONVERTER DATA
    // =========================
    private String converterDataParaMySQL() {

        LocalDate data = LocalDate.parse(
                txtDatNascCliente.getText().trim(),
                FORMATO_DATA
        );

        return data.toString();
    }

    // =========================
// CONSULTAR
// =========================
    private void consultar() {

        if (!verificarConexao()) {
            return;
        }

        String id = txtIdCliente.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o ID do cliente.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            txtIdCliente.requestFocus();
            return;
        }

        String sql = "SELECT * FROM tb_cliente WHERE id_cliente = ?";

        try (PreparedStatement pst = conexao.prepareStatement(sql)) {

            pst.setString(1, id);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    txtNomeCliente.setText(rs.getString("nome_cliente"));
                    txtEndeCliente.setText(rs.getString("endereco_cliente"));
                    cmbCidade.setSelectedItem(rs.getString("cidade_cliente"));
                    cmbUf.setSelectedItem(rs.getString("uf_cliente"));

                    String tipo = rs.getString("tipo_cliente");

                    if ("PF".equalsIgnoreCase(tipo)) {
                        rbCPF.setSelected(true);
                        configurarMascaraDocumento("CPF");
                    } else if ("PJ".equalsIgnoreCase(tipo)) {
                        RB.setSelected(true);
                        configurarMascaraDocumento("CNPJ");
                    }

                    String docMascara = "PF".equalsIgnoreCase(tipo)
                            ? "###.###.###-##"
                            : "##.###.###/####-##";

                    TXTdocumento.setText(
                            aplicarMascara(rs.getString("cpf_cnpj_cliente"), docMascara)
                    );

                    txtTeleCliente.setText(
                            aplicarMascara(rs.getString("telefone_cliente"), "(##) #####-####")
                    );

                    String data = rs.getString("data_nasc_cliente");

                    if (data != null && !data.isEmpty()) {
                        try {
                            LocalDate dataNascimento = LocalDate.parse(data);
                            txtDatNascCliente.setText(dataNascimento.format(FORMATO_DATA));
                        } catch (DateTimeParseException e) {
                            txtDatNascCliente.setText(data);
                        }
                    }

                } else {

                    JOptionPane.showMessageDialog(this, "Cliente não cadastrado.",
                            "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar cliente:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Metodo para aplicar a mascara
    private String aplicarMascara(String valor, String mascara) {
        if (valor == null) {
            return "";
        }
        String d = digitos(valor);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (char c : mascara.toCharArray()) {
            if (c == '#') {
                if (i >= d.length()) {
                    break;
                }
                sb.append(d.charAt(i++));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // =========================
    // ADICIONAR
    // =========================
    private void adicionar() {

        if (!verificarConexao()) {
            return;
        }

        if (!validarCampos()) {
            return;
        }

        String tipo = getTipoCliente();

        if (tipo == null) {
            return;
        }

        String sql
                = "INSERT INTO tb_cliente "
                + "(nome_cliente, endereco_cliente, cidade_cliente, "
                + "uf_cliente, cpf_cnpj_cliente, tipo_cliente, "
                + "telefone_cliente, data_nasc_cliente) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst
                = conexao.prepareStatement(sql)) {

            pst.setString(
                    1,
                    txtNomeCliente.getText().trim()
            );

            pst.setString(
                    2,
                    txtEndeCliente.getText().trim()
            );

            pst.setString(
                    3,
                    cmbCidade.getSelectedItem().toString()
            );

            pst.setString(
                    4,
                    cmbUf.getSelectedItem().toString()
            );

            pst.setString(
                    5,
                    digitos(TXTdocumento.getText().trim())
            );

            pst.setString(
                    6,
                    tipo
            );

            pst.setString(
                    7,
                    digitos(txtTeleCliente.getText().trim())
            );

            pst.setString(
                    8,
                    converterDataParaMySQL()
            );

            int resultado = pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente cadastrado com sucesso!"
                );

                limparCampos();
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Já existe um cliente com este CPF/CNPJ.",
                        "Duplicado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar cliente:\n" + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================
    // ALTERAR
    // =========================
    private void alterar() {

        if (!verificarConexao()) {
            return;
        }

        String id = txtIdCliente.getText().trim();

        if (id.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o ID do cliente."
            );

            txtIdCliente.requestFocus();
            return;
        }

        if (!validarCampos()) {
            return;
        }

        String tipo = getTipoCliente();

        if (tipo == null) {
            return;
        }

        String sql
                = "UPDATE tb_cliente SET "
                + "nome_cliente = ?, "
                + "endereco_cliente = ?, "
                + "cidade_cliente = ?, "
                + "uf_cliente = ?, "
                + "cpf_cnpj_cliente = ?, "
                + "tipo_cliente = ?, "
                + "telefone_cliente = ?, "
                + "data_nasc_cliente = ? "
                + "WHERE id_cliente = ?";

        try (PreparedStatement pst
                = conexao.prepareStatement(sql)) {

            pst.setString(
                    1,
                    txtNomeCliente.getText().trim()
            );

            pst.setString(
                    2,
                    txtEndeCliente.getText().trim()
            );

            pst.setString(
                    3,
                    cmbCidade.getSelectedItem().toString()
            );

            pst.setString(
                    4,
                    cmbUf.getSelectedItem().toString()
            );

            pst.setString(
                    5,
                    digitos(TXTdocumento.getText().trim())
            );

            pst.setString(
                    6,
                    tipo
            );

            pst.setString(
                    7,
                    digitos(txtTeleCliente.getText().trim())
            );

            pst.setString(
                    8,
                    converterDataParaMySQL()
            );

            pst.setString(
                    9,
                    id
            );

            int resultado = pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente alterado com sucesso!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente não encontrado."
                );
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Já existe um cliente com este CPF/CNPJ.",
                        "Duplicado", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar cliente:\n" + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================
    // APAGAR
    // =========================
    private void apagar() {

        if (!verificarConexao()) {
            return;
        }

        String id = txtIdCliente.getText().trim();

        if (id.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o ID do cliente."
            );

            txtIdCliente.requestFocus();
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este cliente?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirma != JOptionPane.YES_OPTION) {
            return;
        }

        String sql
                = "DELETE FROM tb_cliente "
                + "WHERE id_cliente = ?";

        try (PreparedStatement pst
                = conexao.prepareStatement(sql)) {

            pst.setString(1, id);

            int resultado = pst.executeUpdate();

            if (resultado > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente apagado com sucesso!"
                );

                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Cliente não encontrado."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao apagar cliente:\n"
                    + e.getMessage()
            );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
        txtNomeCliente = new javax.swing.JTextField();
        txtEndeCliente = new javax.swing.JTextField();
        btnAdicionarCliente = new javax.swing.JButton();
        btnEditarCliente = new javax.swing.JButton();
        btnVizualizarCliente = new javax.swing.JButton();
        btnApagarCliente = new javax.swing.JButton();
        cmbCidade = new javax.swing.JComboBox<>();
        RB = new javax.swing.JRadioButton();
        rbCPF = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbUf = new javax.swing.JComboBox<>();
        TXTdocumento = new javax.swing.JFormattedTextField();
        txtTeleCliente = new javax.swing.JFormattedTextField();
        txtDatNascCliente = new javax.swing.JFormattedTextField();

        jLabel10.setText("jLabel10");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Cadastro cliente");

        jLabel2.setText("ID:");

        jLabel3.setText("Nome:");

        jLabel4.setText("Endereco:");

        jLabel5.setText("Cidade:");

        jLabel6.setText("UF:");

        jLabel8.setText("Telefone:");

        jLabel9.setText("Data nascimento:");

        txtIdCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdClienteActionPerformed(evt);
            }
        });

        txtNomeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeClienteActionPerformed(evt);
            }
        });

        txtEndeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEndeClienteActionPerformed(evt);
            }
        });

        btnAdicionarCliente.setText("Adicionar");
        btnAdicionarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarClienteActionPerformed(evt);
            }
        });

        btnEditarCliente.setText("Editar");
        btnEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarClienteActionPerformed(evt);
            }
        });

        btnVizualizarCliente.setText("Vizualizar");
        btnVizualizarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVizualizarClienteActionPerformed(evt);
            }
        });

        btnApagarCliente.setText("Apagar");
        btnApagarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarClienteActionPerformed(evt);
            }
        });

        cmbCidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione a cidade", "Taquara", "Parobé", "Igrejinha", "Três Coroas", "Gramado", "Canela" }));
        cmbCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCidadeActionPerformed(evt);
            }
        });

        buttonGroup1.add(RB);
        RB.setText("CNPJ");
        RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RBActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbCPF);
        rbCPF.setText("CPF");
        rbCPF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCPFActionPerformed(evt);
            }
        });

        jLabel7.setText("CPF/CNPJ:");

        jLabel11.setText("Tipo de pessoa:");

        cmbUf.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione uma UF", "RS", "SC", "PR", "SP", "RJ", "MG" }));
        cmbUf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel11))
                        .addGap(29, 29, 29)
                        .addComponent(rbCPF)
                        .addGap(59, 59, 59)
                        .addComponent(RB)
                        .addContainerGap(357, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel8)
                                .addGap(42, 42, 42))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEndeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TXTdocumento)
                            .addComponent(txtTeleCliente)
                            .addComponent(txtDatNascCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAdicionarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnVizualizarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEditarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnApagarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtEndeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbCPF)
                    .addComponent(RB)
                    .addComponent(jLabel11))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(TXTdocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtTeleCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtDatNascCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdicionarCliente)
                    .addComponent(btnEditarCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVizualizarCliente)
                    .addComponent(btnApagarCliente))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdClienteActionPerformed
        // TODO add your handling code here:
        consultar();
    }//GEN-LAST:event_txtIdClienteActionPerformed

    private void txtNomeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeClienteActionPerformed

    private void txtEndeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEndeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEndeClienteActionPerformed

    private void btnAdicionarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarClienteActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdicionarClienteActionPerformed

    private void btnEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarClienteActionPerformed
        alterar();
    }//GEN-LAST:event_btnEditarClienteActionPerformed

    private void btnVizualizarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVizualizarClienteActionPerformed
        consultar();
    }//GEN-LAST:event_btnVizualizarClienteActionPerformed

    private void btnApagarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarClienteActionPerformed
        apagar();
    }//GEN-LAST:event_btnApagarClienteActionPerformed

    private void cmbCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCidadeActionPerformed

    private void RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RBActionPerformed

        configurarMascaraDocumento("CNPJ");
    }//GEN-LAST:event_RBActionPerformed

    private void rbCPFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCPFActionPerformed
        // TODO add your handling code here:

        configurarMascaraDocumento("CPF");
    }//GEN-LAST:event_rbCPFActionPerformed

    private void cmbUfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbUfActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton RB;
    private javax.swing.JFormattedTextField TXTdocumento;
    private javax.swing.JButton btnAdicionarCliente;
    private javax.swing.JButton btnApagarCliente;
    private javax.swing.JButton btnEditarCliente;
    private javax.swing.JButton btnVizualizarCliente;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbCidade;
    private javax.swing.JComboBox<String> cmbUf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbCPF;
    private javax.swing.JFormattedTextField txtDatNascCliente;
    private javax.swing.JTextField txtEndeCliente;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JFormattedTextField txtTeleCliente;
    // End of variables declaration//GEN-END:variables
}

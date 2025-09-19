/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vistas;

import Clases.Categoria;
import Clases.Producto;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author TuMachGalan
 */
public class IFGestionProductos extends javax.swing.JInternalFrame {

    /**
     * Creates new form IFGestionProductoss
     */
    public IFGestionProductos() {
        initComponents();
        
        // No pude generar por cada boton, asi es que los filtro y relleno antes de cada evento
        cbRubro.removeAllItems();
        for (Categoria cat : Categoria.values()) {
            cbRubro.addItem(cat.toString());
        }
        CbFiltrarCtgria.removeAllItems();
        CbFiltrarCtgria.addItem("Todos");
        for (Categoria cat : Categoria.values()) {
            CbFiltrarCtgria.addItem(cat.toString());
        }
        cbRubro.setSelectedIndex(0);
        CbFiltrarCtgria.setSelectedIndex(0);

        // Defino modelo de tabla
        model = new DefaultTableModel(
                new Object[]{"Codigo", "Descripcion", "Precio", "Categoria", "Stock"}, 0
        );
        jTableDescripcion.setModel(model);
        cargarTablaDesdeTreeSet();

        // estado inicial de botones por que al guardar un item los otros se bloqueaban permanente
        BtonGuardar.setEnabled(true);
        BtonActualizar.setEnabled(false);
        BtonEliminar.setEnabled(false);

        //MouseListener para habilitar botones solo al seleccionar fila
        jTableDescripcion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = jTableDescripcion.getSelectedRow();
                if (fila >= 0) {
                    txtCodigo.setText(jTableDescripcion.getValueAt(fila, 0).toString());
                    txtDescripcion.setText(jTableDescripcion.getValueAt(fila, 1).toString());
                    txtPrecio.setText(jTableDescripcion.getValueAt(fila, 2).toString());
                    cbRubro.setSelectedItem(jTableDescripcion.getValueAt(fila, 3).toString());
                    spStock.setValue(Integer.parseInt(jTableDescripcion.getValueAt(fila, 4).toString()));

                    BtonActualizar.setEnabled(true);
                    BtonEliminar.setEnabled(true);
                }
            }
        });

        // Evento Bton Guardar
        BtonGuardar.addActionListener(e -> {
            if (validarCampos()) {
                int codigo = Integer.parseInt(txtCodigo.getText());
                String descripcion = txtDescripcion.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int stock = (Integer) spStock.getValue();
                Categoria rubro = Categoria.valueOf(cbRubro.getSelectedItem().toString());

                Producto nuevo = new Producto(codigo, descripcion, precio, stock, rubro);
                VistaPrincipal.listaProductos.add(nuevo);

                cargarTablaDesdeTreeSet();

                // Limpiar campos
                txtCodigo.setText("");
                txtDescripcion.setText("");
                txtPrecio.setText("");
                spStock.setValue(0);
                if (cbRubro.getItemCount() > 0) {
                    cbRubro.setSelectedIndex(0);
                }

                JOptionPane.showMessageDialog(this, "Producto guardado con Exito.");
            }
        });

        // Evento BtonActualizar
        BtonActualizar.addActionListener(e -> {
            int fila = jTableDescripcion.getSelectedRow();
            if (fila >= 0 && validarCampos()) {
                int codigo = Integer.parseInt(txtCodigo.getText());

                Producto buscado = null;
                for (Producto p : VistaPrincipal.listaProductos) {
                    if (p.getCodigo() == codigo) {
                        buscado = p;
                        break;
                    }
                }
                if (buscado != null) {
                    VistaPrincipal.listaProductos.remove(buscado);
                }

                Producto modificado = new Producto(
                        codigo,
                        txtDescripcion.getText(),
                        Double.parseDouble(txtPrecio.getText()),
                        (Integer) spStock.getValue(),
                        Categoria.valueOf(cbRubro.getSelectedItem().toString())
                );
                VistaPrincipal.listaProductos.add(modificado);

                cargarTablaDesdeTreeSet();

                JOptionPane.showMessageDialog(this, "Producto actualizado.");

                // Limpiar campos y deshabilitar botones hasta nueva selección
                txtCodigo.setText("");
                txtDescripcion.setText("");
                txtPrecio.setText("");
                spStock.setValue(0);
                if (cbRubro.getItemCount() > 0) {
                    cbRubro.setSelectedIndex(0);
                }
                BtonActualizar.setEnabled(false);
                BtonEliminar.setEnabled(false);
            }
        });

        // Evento BtonEliminar
        BtonEliminar.addActionListener(e -> {
            int fila = jTableDescripcion.getSelectedRow();
            if (fila >= 0) {
                int codigo = Integer.parseInt(jTableDescripcion.getValueAt(fila, 0).toString());

                Producto aEliminar = null;
                for (Producto p : VistaPrincipal.listaProductos) {
                    if (p.getCodigo() == codigo) {
                        aEliminar = p;
                        break;
                    }
                }
                if (aEliminar != null) {
                    VistaPrincipal.listaProductos.remove(aEliminar);
                }

                cargarTablaDesdeTreeSet();

                JOptionPane.showMessageDialog(this, "Producto eliminado.");

                // Limpiar campos y deshabilitar botones hasta nueva selección
                txtCodigo.setText("");
                txtDescripcion.setText("");
                txtPrecio.setText("");
                spStock.setValue(0);
                if (cbRubro.getItemCount() > 0) {
                    cbRubro.setSelectedIndex(0);
                }
                BtonActualizar.setEnabled(false);
                BtonEliminar.setEnabled(false);
            }
        });

        // Filtar por categoria
        CbFiltrarCtgria.addActionListener(e -> {
            String seleccion = CbFiltrarCtgria.getSelectedItem().toString();
            model.setRowCount(0);
            for (Producto p : VistaPrincipal.listaProductos) {
                if (seleccion.equals("Todos") || p.getRubro().toString().equals(seleccion)) {
                    model.addRow(new Object[]{p.getCodigo(), p.getDescripcion(), p.getPrecio(), p.getRubro(), p.getStock()});
                }
            }
        });

        // Cerrar ventana
        BtonCerrar.addActionListener(e -> this.dispose());
        BtondNuevo.addActionListener(e -> {
            // Limpiar campos
            txtCodigo.setText("");
            txtDescripcion.setText("");
            txtPrecio.setText("");
            spStock.setValue(0);

            if (cbRubro.getItemCount() > 0) {
                cbRubro.setSelectedIndex(0);
            }

            // Limpiar selección en tabla
            jTableDescripcion.clearSelection();

            // Habilitar solo guardar
            BtonGuardar.setEnabled(true);
            BtonActualizar.setEnabled(false);
            BtonEliminar.setEnabled(false);
        });
        
        //Bton Buscar
        BtonBuscar.addActionListener(e -> {
            String codigoBuscado = txtCodigo.getText().trim();
            if (codigoBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(IFGestionProductos.this, "Ingrese un codigo para buscar.");
                return;
            }

            boolean encontrado = false;
            for (int i = 0; i < jTableDescripcion.getRowCount(); i++) {
                if (jTableDescripcion.getValueAt(i, 0).toString().equals(codigoBuscado)) {
                    jTableDescripcion.setRowSelectionInterval(i, i);
                    jTableDescripcion.scrollRectToVisible(jTableDescripcion.getCellRect(i, 0, true));
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                JOptionPane.showMessageDialog(IFGestionProductos.this, "No se encontro un producto con ese codigo.");
                jTableDescripcion.clearSelection();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtCodigo = new java.awt.TextField();
        txtDescripcion = new java.awt.TextField();
        txtPrecio = new java.awt.TextField();
        spStock = new javax.swing.JSpinner();
        jtxtCdgo = new javax.swing.JLabel();
        jtxtDcrpcion = new javax.swing.JLabel();
        jtxtPrice = new javax.swing.JLabel();
        jtxtRbro = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbRubro = new javax.swing.JComboBox<>();
        txtFiltrar = new javax.swing.JLabel();
        CbFiltrarCtgria = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDescripcion = new javax.swing.JTable();
        txtTitulo = new javax.swing.JLabel();
        BtondNuevo = new javax.swing.JButton();
        BtonGuardar = new javax.swing.JButton();
        BtonActualizar = new javax.swing.JButton();
        BtonEliminar = new javax.swing.JButton();
        BtonBuscar = new javax.swing.JButton();
        BtonCerrar = new javax.swing.JButton();

        txtCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoActionPerformed(evt);
            }
        });

        jtxtCdgo.setText("Codigo:");

        jtxtDcrpcion.setText("Descripcion:");

        jtxtPrice.setText("Precio:");

        jtxtRbro.setText("Rubro:");

        jLabel5.setText("Stock:");

        cbRubro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRubroActionPerformed(evt);
            }
        });

        txtFiltrar.setText("Filtrar por Categoría:");

        CbFiltrarCtgria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbFiltrarCtgriaActionPerformed(evt);
            }
        });

        jTableDescripcion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Descripcion", "Precio", "Categoria", "Stock"
            }
        ));
        jScrollPane1.setViewportView(jTableDescripcion);

        txtTitulo.setFont(new java.awt.Font("Dialog", 0, 27)); // NOI18N
        txtTitulo.setText("Gestión de Productos");

        BtondNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-producto-usado-50.png"))); // NOI18N
        BtondNuevo.setText("Nuevo");

        BtonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-caja-de-producto-de-pelo-corto-50.png"))); // NOI18N
        BtonGuardar.setText("Guardar");

        BtonActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8-marca-doble-30.png"))); // NOI18N
        BtonActualizar.setText("Actualizar");

        BtonEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png"))); // NOI18N
        BtonEliminar.setText("Eliminar");

        BtonBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lupa.png"))); // NOI18N
        BtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtonBuscarActionPerformed(evt);
            }
        });

        BtonCerrar.setText("Cerrar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(txtTitulo))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtFiltrar)
                                .addGap(18, 18, 18)
                                .addComponent(CbFiltrarCtgria, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtxtDcrpcion)
                                    .addComponent(jtxtPrice)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtRbro))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtPrecio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                        .addComponent(txtDescripcion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtCodigo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(32, 32, 32)
                                .addComponent(BtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtondNuevo)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jtxtCdgo)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtonGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtonActualizar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtonCerrar)
                                    .addComponent(BtonEliminar)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(spStock, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(txtTitulo)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CbFiltrarCtgria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiltrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtxtCdgo)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtDcrpcion, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtPrice))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtRbro))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(BtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(BtonCerrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(spStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtonEliminar)
                    .addComponent(BtonActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtonGuardar)
                    .addComponent(BtondNuevo))
                .addGap(55, 55, 55))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoActionPerformed

    private void CbFiltrarCtgriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbFiltrarCtgriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbFiltrarCtgriaActionPerformed

    private void cbRubroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRubroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRubroActionPerformed

    private void BtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtonBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtonBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtonActualizar;
    private javax.swing.JButton BtonBuscar;
    private javax.swing.JButton BtonCerrar;
    private javax.swing.JButton BtonEliminar;
    private javax.swing.JButton BtonGuardar;
    private javax.swing.JButton BtondNuevo;
    private javax.swing.JComboBox<String> CbFiltrarCtgria;
    private javax.swing.JComboBox<String> cbRubro;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableDescripcion;
    private javax.swing.JLabel jtxtCdgo;
    private javax.swing.JLabel jtxtDcrpcion;
    private javax.swing.JLabel jtxtPrice;
    private javax.swing.JLabel jtxtRbro;
    private javax.swing.JSpinner spStock;
    private java.awt.TextField txtCodigo;
    private java.awt.TextField txtDescripcion;
    private javax.swing.JLabel txtFiltrar;
    private java.awt.TextField txtPrecio;
    private javax.swing.JLabel txtTitulo;
    // End of variables declaration//GEN-END:variables
private DefaultTableModel model;

    private boolean validarCampos() {
        if (txtCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El codigo no puede estar vacio.");
            return false;
        }
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripcion no puede estar vacia.");
            return false;
        }
        if (txtPrecio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El precio no puede estar vacio.");
            return false;
        }
        if (cbRubro.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un rubro.");
            return false;
        }
        return true;
    }

    private void cargarTablaDesdeTreeSet() {
        model.setRowCount(0); // limpiar tabla
        for (Producto p : VistaPrincipal.listaProductos) {
            model.addRow(new Object[]{
                p.getCodigo(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getRubro(),
                p.getStock()
            });

        }
    }
}

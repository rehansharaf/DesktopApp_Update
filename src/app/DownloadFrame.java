package app;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.net.ftp.FTPFile;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.RowFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

public class DownloadFrame extends javax.swing.JDialog {

    private static String host;
    private static String user;
    private static String pass;
    private static String service;

    public DownloadFrame(String host, String user, String pass, String service) throws IOException {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.service = service;

        initComponents();
        getAllFiles();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setCurrentModel(true);
        jScrollPane1 = new javax.swing.JScrollPane();

        jDownloadList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        // ✅ updated table model: only checkbox editable
        jDownloadList.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Select", "File Names"}
        ) {
            Class[] columnTypes = new Class[] {Boolean.class, Object.class};

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0; // only checkbox column editable
            }
        });

        jScrollPane1.setViewportView(jDownloadList);

        // ✅ clicking row toggles checkbox if user clicks outside first column
        jDownloadList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int viewRow = jDownloadList.rowAtPoint(e.getPoint());
                int viewCol = jDownloadList.columnAtPoint(e.getPoint());

                if (viewRow >= 0 && viewCol != 0) {
                    int modelRow = jDownloadList.convertRowIndexToModel(viewRow);
                    DefaultTableModel model = (DefaultTableModel) jDownloadList.getModel();
                    boolean currentValue = (Boolean) model.getValueAt(modelRow, 0);
                    model.setValueAt(!currentValue, modelRow, 0);
                }
            }
        });

        JButton btnDownloadSelected = new JButton("Download");
        btnDownloadSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                DefaultTableModel model = (DefaultTableModel) jDownloadList.getModel();
                ArrayList<String> filename = new ArrayList<String>();

                // ✅ iterate over view rows and convert to model row
                for (int i = 0; i < jDownloadList.getRowCount(); i++) {
                    int modelRow = jDownloadList.convertRowIndexToModel(i);
                    boolean selected = (Boolean) model.getValueAt(modelRow, 0);
                    if (selected) {
                        filename.add(model.getValueAt(modelRow, 1).toString());
                    }
                }

                if (filename.size() > 0) {
                    setCurrentModel(false);
                    downloadSelectedRows(filename);
                }
            }
        });

        searchField = new JTextField("Search");
        searchField.setColumns(10);
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                } else
                    searchField.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search");
                }
            }
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String text = searchField.getText();
                if (text.trim().length() == 0 || text.equals("Search")) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(47)
                    .addComponent(searchField, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addGap(18)
                    .addComponent(btnDownloadSelected)
                    .addGap(31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDownloadSelected)
                        .addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        );
        getContentPane().setLayout(layout);

        pack();
        setLocationRelativeTo(null);
    }

    private void setCurrentModel(boolean val) {
        this.setModal(val);
    }

    public void downloadSelectedRows(ArrayList<String> filename) {
        JFileChooser f = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int r = f.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File localDir = f.getSelectedFile();

            for (int i = 0; i < filename.size(); i++) {
                try {
                    DownloadPopup downloadp = new DownloadPopup();
                    downloadp.setVisible(true);
                    DownloadWorker worker = new DownloadWorker(host, user, pass, new File(filename.get(i)), service,
                            localDir);
                    worker.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if (evt.getPropertyName().equals("progress")) {
                                downloadp.jbutton1ChangeVisiblity(false);
                                downloadp.setDefaultCloseOperation(0);
                                Integer progress = (Integer) evt.getNewValue();
                                try {
                                    downloadp.progressBarVal(progress);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                boolean checkBtnVisibile = downloadp.jbutton1CheckVisiblity();
                                if (!checkBtnVisibile) {
                                    downloadp.jbutton1ChangeVisiblity(true);
                                    downloadp.dispose();
                                } else
                                    downloadp.jbutton1ChangeVisiblity(true);
                            }
                        }
                    });
                    worker.execute();
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void getAllFiles() throws IOException {
        GetAllFiles getAll = new GetAllFiles();
        FTPFile[] files = getAll.getAllFilesFromFolder("/" + service, host, user, pass);
        DefaultTableModel model = (DefaultTableModel) jDownloadList.getModel();
        model.setColumnIdentifiers(new String[] {"Select", "File Names"});

        jDownloadList.getColumnModel().getColumn(0).setPreferredWidth(20);
        jDownloadList.getColumnModel().getColumn(0).setMaxWidth(20);

        int rowcount = 0;
        for (FTPFile file : files) {
            model.addRow(new Object[0]);
            model.setValueAt(false, rowcount, 0);
            model.setValueAt(file.getName(), rowcount, 1);
            rowcount++;
        }

        rowSorter = new TableRowSorter<>(jDownloadList.getModel());
        jDownloadList.setRowSorter(rowSorter);
    }

    // Variables declaration
    private javax.swing.JTable jDownloadList;
    private javax.swing.JScrollPane jScrollPane1;
    private JTextField searchField;
    private TableRowSorter<TableModel> rowSorter;
}

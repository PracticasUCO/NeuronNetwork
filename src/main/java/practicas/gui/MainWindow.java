/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicas.gui;

import practicas.controller.MainController;
import javax.swing.JFileChooser;

/**
 *
 * @author gowikel
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
	initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

	loadToolBar = new javax.swing.JToolBar();
	loadTrainDataButton = new javax.swing.JButton();
	loadTestDataButton = new javax.swing.JButton();
	rightGlue = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
		new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
	useBiasCheckBox = new javax.swing.JCheckBox();
	learningFactorLabel = new javax.swing.JLabel();
	learningFactorSlider = new javax.swing.JSlider();
	inertiaFactorLabel = new javax.swing.JLabel();
	inertiaFactorSlider = new javax.swing.JSlider();
	spinnerOptionsPanel = new javax.swing.JPanel();
	minimumImprovementPanel = new javax.swing.JPanel();
	minimumImprovementField = new javax.swing.JTextField();
	hiddenLayersPanel = new javax.swing.JPanel();
	hiddenLayersSpinner = new javax.swing.JSpinner();
	hiddenNeuronsPanel = new javax.swing.JPanel();
	hiddenNeuronsSpinner = new javax.swing.JSpinner();
	maxiterPanel = new javax.swing.JPanel();
	maxiterSpinner = new javax.swing.JSpinner();
	timesPanel = new javax.swing.JPanel();
	timesSpinner = new javax.swing.JSpinner();
	generalInfoPanel = new javax.swing.JPanel();
	finalTrainErrorLabel = new javax.swing.JLabel();
	finalTrainError = new javax.swing.JLabel();
	finalTestErrorLabel = new javax.swing.JLabel();
	finalTestError = new javax.swing.JLabel();
	actionPanel = new javax.swing.JPanel();
	trainButton = new javax.swing.JButton();
	statusPanel = new javax.swing.JPanel();
	statusLabel = new javax.swing.JLabel();
	infoLabel = new javax.swing.JLabel();

	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	setTitle("Multilayer Perceptron");
	getContentPane().setLayout(
		new javax.swing.BoxLayout(getContentPane(),
			javax.swing.BoxLayout.Y_AXIS));

	loadToolBar.setRollover(true);

	loadTrainDataButton.setText("Cargar fichero de entrenamiento");
	loadTrainDataButton.setFocusable(false);
	loadTrainDataButton
		.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	loadTrainDataButton
		.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
	loadTrainDataButton.addMouseListener(new java.awt.event.MouseAdapter() {
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
		loadTrainDataButtonMouseClicked(evt);
	    }
	});
	loadToolBar.add(loadTrainDataButton);

	loadTestDataButton.setText("Cargar fichero de test");
	loadTestDataButton.setFocusable(false);
	loadTestDataButton
		.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	loadTestDataButton
		.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
	loadTestDataButton.addMouseListener(new java.awt.event.MouseAdapter() {
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
		loadTestDataButtonMouseClicked(evt);
	    }
	});
	loadToolBar.add(loadTestDataButton);
	loadToolBar.add(rightGlue);

	useBiasCheckBox.setText("Usa bias");
	useBiasCheckBox.setFocusable(false);
	useBiasCheckBox
		.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
	useBiasCheckBox
		.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
	loadToolBar.add(useBiasCheckBox);

	getContentPane().add(loadToolBar);

	learningFactorLabel.setText("Factor de aprendizaje (%)");
	learningFactorLabel.setToolTipText("");
	getContentPane().add(learningFactorLabel);

	learningFactorSlider.setMajorTickSpacing(1);
	learningFactorSlider.setMinorTickSpacing(1);
	learningFactorSlider.setPaintTicks(true);
	learningFactorSlider.setSnapToTicks(true);
	learningFactorSlider.setValue(90);
	getContentPane().add(learningFactorSlider);

	inertiaFactorLabel.setText("Factor de inercia (%)");
	getContentPane().add(inertiaFactorLabel);

	inertiaFactorSlider.setMajorTickSpacing(1);
	inertiaFactorSlider.setMinorTickSpacing(1);
	inertiaFactorSlider.setPaintTicks(true);
	inertiaFactorSlider.setSnapToTicks(true);
	inertiaFactorSlider.setToolTipText("");
	inertiaFactorSlider.setValue(10);
	getContentPane().add(inertiaFactorSlider);

	spinnerOptionsPanel.setLayout(new java.awt.GridLayout(2, 3));

	minimumImprovementPanel.setBorder(javax.swing.BorderFactory
		.createTitledBorder("Mejora mínima"));
	minimumImprovementPanel.setLayout(new javax.swing.BoxLayout(
		minimumImprovementPanel, javax.swing.BoxLayout.Y_AXIS));

	minimumImprovementField
		.setHorizontalAlignment(javax.swing.JTextField.CENTER);
	minimumImprovementField.setText("1e-6");
	minimumImprovementField.setToolTipText("");
	minimumImprovementField.addKeyListener(new java.awt.event.KeyAdapter() {
	    public void keyTyped(java.awt.event.KeyEvent evt) {
		minimumImprovementFieldKeyTyped(evt);
	    }
	});
	minimumImprovementPanel.add(minimumImprovementField);

	spinnerOptionsPanel.add(minimumImprovementPanel);

	hiddenLayersPanel.setBorder(javax.swing.BorderFactory
		.createTitledBorder("Capas ocultas"));
	hiddenLayersPanel.setLayout(new javax.swing.BoxLayout(
		hiddenLayersPanel, javax.swing.BoxLayout.Y_AXIS));

	hiddenLayersSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer
		.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
	hiddenLayersPanel.add(hiddenLayersSpinner);

	spinnerOptionsPanel.add(hiddenLayersPanel);

	hiddenNeuronsPanel.setBorder(javax.swing.BorderFactory
		.createTitledBorder("Neuronas por capa oculta"));
	hiddenNeuronsPanel.setLayout(new javax.swing.BoxLayout(
		hiddenNeuronsPanel, javax.swing.BoxLayout.Y_AXIS));

	hiddenNeuronsSpinner.setModel(new javax.swing.SpinnerNumberModel(
		Integer.valueOf(1), Integer.valueOf(1), null, Integer
			.valueOf(1)));
	hiddenNeuronsPanel.add(hiddenNeuronsSpinner);

	spinnerOptionsPanel.add(hiddenNeuronsPanel);

	maxiterPanel.setBorder(javax.swing.BorderFactory
		.createTitledBorder("Iteraciones máximas"));
	maxiterPanel.setLayout(new javax.swing.BoxLayout(maxiterPanel,
		javax.swing.BoxLayout.Y_AXIS));

	maxiterSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer
		.valueOf(1000), Integer.valueOf(10), null, Integer.valueOf(1)));
	maxiterPanel.add(maxiterSpinner);

	spinnerOptionsPanel.add(maxiterPanel);

	timesPanel.setBorder(javax.swing.BorderFactory
		.createTitledBorder("Repeticiones"));
	timesPanel.setLayout(new javax.swing.BoxLayout(timesPanel,
		javax.swing.BoxLayout.Y_AXIS));

	timesSpinner.setModel(new javax.swing.SpinnerNumberModel(5, 5, 500, 1));
	timesPanel.add(timesSpinner);

	spinnerOptionsPanel.add(timesPanel);

	generalInfoPanel.setBorder(javax.swing.BorderFactory
		.createTitledBorder("Información"));
	generalInfoPanel.setLayout(new java.awt.GridLayout(2, 2, 2, 5));

	finalTrainErrorLabel.setFont(new java.awt.Font("Sans Serif", 1, 15)); // NOI18N
	finalTrainErrorLabel.setText("Error entrenamiento");
	generalInfoPanel.add(finalTrainErrorLabel);

	finalTrainError.setToolTipText("");
	generalInfoPanel.add(finalTrainError);

	finalTestErrorLabel.setFont(new java.awt.Font("Sans Serif", 1, 15)); // NOI18N
	finalTestErrorLabel.setText("Error en test");
	finalTestErrorLabel.setToolTipText("");
	generalInfoPanel.add(finalTestErrorLabel);

	finalTestError.setToolTipText("");
	generalInfoPanel.add(finalTestError);

	spinnerOptionsPanel.add(generalInfoPanel);

	getContentPane().add(spinnerOptionsPanel);

	actionPanel
		.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

	trainButton.setText("Entrenar");
	trainButton.setToolTipText("");
	trainButton.setEnabled(false);
	trainButton.addMouseListener(new java.awt.event.MouseAdapter() {
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
		trainButtonMouseClicked(evt);
	    }
	});
	actionPanel.add(trainButton);

	getContentPane().add(actionPanel);

	statusPanel
		.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

	statusLabel.setFont(new java.awt.Font("Sans Serif", 1, 15)); // NOI18N
	statusLabel.setForeground(new java.awt.Color(155, 2, 40));
	statusLabel.setText("Status: ");
	statusPanel.add(statusLabel);

	infoLabel.setForeground(new java.awt.Color(155, 2, 40));
	infoLabel.setText("No se han cargado los datos aún");
	statusPanel.add(infoLabel);

	getContentPane().add(statusPanel);

	pack();
    }

    private void loadTrainDataButtonMouseClicked(java.awt.event.MouseEvent evt) {
	int returnVal = fileChooser.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    MainController.setTrainInput(fileChooser.getSelectedFile()
		    .getAbsolutePath());
	}

    }

    private void loadTestDataButtonMouseClicked(java.awt.event.MouseEvent evt) {
	int returnVal = fileChooser.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    MainController.setTestInput(fileChooser.getSelectedFile()
		    .getAbsolutePath());
	}
    }

    private void minimumImprovementFieldKeyTyped(java.awt.event.KeyEvent evt) {
	MainController.setMinimumImprovement(getMinimumImprovement());
    }

    private void trainButtonMouseClicked(java.awt.event.MouseEvent evt) {

	if (trainButton.isEnabled()) {
	    MainController.trainData();
	}

    }

    /**
     * @return the learning factor of the network
     */
    public double getLearningFactor() {
	return ((double) learningFactorSlider.getValue())
		/ ((double) learningFactorSlider.getMaximum());
    }

    /**
     * @return the inertia factor of the network
     */
    public double getInertiaFactor() {
	return ((double) inertiaFactorSlider.getValue())
		/ ((double) inertiaFactorSlider.getMaximum());
    }

    public double getMinimumImprovement() {
	double m;

	try {
	    m = Double.valueOf(minimumImprovementField.getText());
	} catch (Exception e) {
	    m = -1;
	}

	return m;
    }

    public int getHiddenLayers() {
	return (Integer) hiddenLayersSpinner.getValue();
    }

    public int getHiddenNeurons() {
	return (Integer) hiddenNeuronsSpinner.getValue();
    }

    public int getMaxiter() {
	return (Integer) maxiterSpinner.getValue();
    }

    public int getTimes() {
	return (Integer) timesSpinner.getValue();
    }

    public void setTrainingError(String error) {
	finalTrainError.setText(error);
    }

    public void setTestError(String error) {
	finalTestError.setText(error);
    }

    public void setStatus(String status) {
	infoLabel.setText(status);
    }

    public boolean getUseBias() {
	return useBiasCheckBox.isSelected();
    }

    public void enableTrain() {
	trainButton.setEnabled(true);
    }

    public void disableTrain() {
	trainButton.setEnabled(false);
    }

    private final JFileChooser fileChooser = new JFileChooser();


    private javax.swing.JPanel actionPanel;
    private javax.swing.JLabel finalTestError;
    private javax.swing.JLabel finalTestErrorLabel;
    private javax.swing.JLabel finalTrainError;
    private javax.swing.JLabel finalTrainErrorLabel;
    private javax.swing.JPanel generalInfoPanel;
    private javax.swing.JPanel hiddenLayersPanel;
    private javax.swing.JSpinner hiddenLayersSpinner;
    private javax.swing.JPanel hiddenNeuronsPanel;
    private javax.swing.JSpinner hiddenNeuronsSpinner;
    private javax.swing.JLabel inertiaFactorLabel;
    private javax.swing.JSlider inertiaFactorSlider;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JLabel learningFactorLabel;
    private javax.swing.JSlider learningFactorSlider;
    private javax.swing.JButton loadTestDataButton;
    private javax.swing.JToolBar loadToolBar;
    private javax.swing.JButton loadTrainDataButton;
    private javax.swing.JPanel maxiterPanel;
    private javax.swing.JSpinner maxiterSpinner;
    private javax.swing.JTextField minimumImprovementField;
    private javax.swing.JPanel minimumImprovementPanel;
    private javax.swing.Box.Filler rightGlue;
    private javax.swing.JPanel spinnerOptionsPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel timesPanel;
    private javax.swing.JSpinner timesSpinner;
    private javax.swing.JButton trainButton;
    private javax.swing.JCheckBox useBiasCheckBox;
    // End of variables declaration//GEN-END:variables
}

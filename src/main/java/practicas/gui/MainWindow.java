/*
 *  NeuronNetwork: A class collection to build neuron networks
 *  Copyright (C) 2014  Pedro José Piquero Plaza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package practicas.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SpinnerModel;
import javax.swing.border.TitledBorder;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JProgressBar;
import javax.swing.JFileChooser;

import practicas.controller.MainController;


public class MainWindow extends JFrame {
    private static final long serialVersionUID = -4395839315288178863L;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JTabbedPane mainWindow;
    private JPanel configWindow;
    private JPanel loadAreaPanel;
    private JButton loadTrainData;
    private JButton loadTestData;
    private JCheckBox useBias;
    private JPanel learningFactorPanel;
    private JSlider learningFactor;
    private JPanel inertiaFactorPanel;
    private JSlider inertiaFactor;
    private JPanel spinnerOptionsPanel;
    private JPanel minimumImprovementPanel;
    private JSpinner minimumImprovement;
    private JPanel hiddenLayersPanel;
    private JSpinner hiddenLayers;
    private JPanel hiddenNeuronsPanel;
    private JSpinner hiddenNeurons;
    private JPanel maxiterPanel;
    private JSpinner maxiter;
    private JPanel timesPanel;
    private JSpinner times;
    private JLabel status;
    private JPanel actionPanel;
    private JButton trainButton;
    private JScrollPane outputWindow;
    private JPanel outputPanel;
    private JTextArea output;
    private JProgressBar progressBar;
    private final JFileChooser fileChooser = new JFileChooser();
    
    
    
    public MainWindow() {
	initializeComponents();
    }
    
    private void initializeComponents() {
	
	//Start window
	mainWindow = new JTabbedPane();
	this.add(mainWindow);
	
	//Main config window
	configWindow = new JPanel();
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setSize((int) (screenSize.getWidth() / 2.5), (int) (screenSize.getHeight() / 1.9));
	//this.add(window);
	mainWindow.addTab("Configuración", configWindow);
	configWindow.setLayout(new BoxLayout(configWindow, BoxLayout.Y_AXIS));
	this.setTitle("Multilayer Perceptron");
	
	//Load panel
	loadAreaPanel = new JPanel();
	loadTrainData = new JButton("Cargar datos de entrenamiento");
	loadTestData = new JButton("Cargar datos de test");
	useBias = new JCheckBox("Bias");
	useBias.setToolTipText("Indica si las neuronas tendrán o no bias");
	loadAreaPanel.setLayout(new BoxLayout(loadAreaPanel, BoxLayout.X_AXIS));
	loadAreaPanel.add(loadTrainData);
	loadAreaPanel.add(loadTestData);
	loadAreaPanel.add(Box.createHorizontalGlue());
	loadAreaPanel.add(useBias);
	loadAreaPanel.setBounds(5, 5, 30, 30);
	configWindow.add(loadAreaPanel);
	configWindow.add(Box.createVerticalStrut(10));
	
	//Learning factor panel
	learningFactorPanel = new JPanel();
	learningFactorPanel.setLayout(new BoxLayout(learningFactorPanel, BoxLayout.X_AXIS));
	learningFactorPanel.setBorder(new TitledBorder("Factor de aprendizaje"));
	learningFactor = new JSlider(0, 1000);
	learningFactor.setValue(900);
	learningFactor.setLabelTable(makeDictionaryForASlider(0, 1000, 100));
	learningFactor.setPaintLabels(true);
	learningFactor.setMinorTickSpacing(10);
	learningFactor.setMajorTickSpacing(50);
	learningFactor.setPaintTicks(true);
	learningFactor.setToolTipText("Indica la velocidad de aprendizaje de cada neurona.");
	learningFactorPanel.add(learningFactor);
	learningFactorPanel.setBounds(5, 5, 30, 30);
	configWindow.add(learningFactorPanel);
	configWindow.add(Box.createVerticalStrut(10));
	
	//Inertia factor panel
	inertiaFactorPanel = new JPanel();
	inertiaFactorPanel.setLayout(new BoxLayout(inertiaFactorPanel, BoxLayout.X_AXIS));
	inertiaFactorPanel.setBorder(new TitledBorder("Factor de inercia"));
	inertiaFactor = new JSlider(0, 1000);
	inertiaFactor.setValue(100);
	inertiaFactor.setLabelTable(makeDictionaryForASlider(0, 1000, 100));
	inertiaFactor.setPaintLabels(true);
	inertiaFactor.setMinorTickSpacing(10);
	inertiaFactor.setMajorTickSpacing(50);
	inertiaFactor.setPaintTicks(true);
	inertiaFactor.setToolTipText("Indica la fuerza que tendrá sobre el aprendizaje de la neurona los cambios producidos en iteraciones anteriores");
	inertiaFactorPanel.add(inertiaFactor);
	inertiaFactorPanel.setBounds(5, 5, 30, 30);
	configWindow.add(inertiaFactorPanel);
	configWindow.add(Box.createVerticalStrut(10));
	
	///Spinner options
	spinnerOptionsPanel = new JPanel();
	spinnerOptionsPanel.setLayout(new GridLayout(2, 3, 5, 5));
	
	// Minimum improvement
	minimumImprovementPanel = new JPanel();
	minimumImprovementPanel.setLayout(new BoxLayout(
		minimumImprovementPanel, BoxLayout.X_AXIS));
	minimumImprovementPanel.setBorder(new TitledBorder("Mejora mínima"));
	
	minimumImprovement = new JSpinner(new SpinnerNumberModel(1e-8, 0, 1,
		1e-8)) {
	    @Override
	    protected NumberEditor createEditor(SpinnerModel model) {
		return new NumberEditor(this, "0.00000000");
	    }
	};
	minimumImprovement.setToolTipText("Indica la mejora mínima entre cada retropropagación que debe mantenerse para continuar entrenando.");

	minimumImprovementPanel.add(minimumImprovement);
	spinnerOptionsPanel.add(minimumImprovementPanel);
	
	// Hidden layers
	hiddenLayersPanel = new JPanel();
	hiddenLayersPanel.setLayout(new BoxLayout(hiddenLayersPanel, BoxLayout.X_AXIS));
	hiddenLayersPanel.setBorder(new TitledBorder("Capas ocultas"));
	hiddenLayers = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
	hiddenLayers.setToolTipText("Indica el número de capas ocultas que tendrá la red neuronal");
	hiddenLayersPanel.add(hiddenLayers);
	spinnerOptionsPanel.add(hiddenLayersPanel);
	
	// Hidden neurons
	hiddenNeuronsPanel = new JPanel();
	hiddenNeuronsPanel.setLayout(new BoxLayout(hiddenNeuronsPanel, BoxLayout.X_AXIS));
	hiddenNeuronsPanel.setBorder(new TitledBorder("Neuronas ocultas"));
	hiddenNeurons = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
	hiddenNeurons.setToolTipText("Indica el número de neuronas por capa oculta en la red neuronal");
	hiddenNeuronsPanel.add(hiddenNeurons);
	spinnerOptionsPanel.add(hiddenNeuronsPanel);
	
	//Maxiter
	maxiterPanel = new JPanel();
	maxiterPanel.setLayout(new BoxLayout(maxiterPanel, BoxLayout.X_AXIS));
	maxiterPanel.setBorder(new TitledBorder("Iteraciones máximas"));
	maxiter = new JSpinner(new SpinnerNumberModel(1000, 300, Integer.MAX_VALUE, 100));
	maxiter.setToolTipText("Indica el número máximo de iteraciones que se pueden dar en la fase de entrenamiento.");
	maxiterPanel.add(maxiter);
	spinnerOptionsPanel.add(maxiterPanel);
	
	//Times
	timesPanel = new JPanel();
	timesPanel.setLayout(new BoxLayout(timesPanel, BoxLayout.X_AXIS));
	timesPanel.setBorder(new TitledBorder("Repeticiones"));
	times = new JSpinner(new SpinnerNumberModel(5, 5, Integer.MAX_VALUE, 1));
	times.setToolTipText("Indica el número de redes neuronales a entrenar.");
	timesPanel.add(times);
	spinnerOptionsPanel.add(timesPanel);
	
	configWindow.add(spinnerOptionsPanel);
	configWindow.add(Box.createVerticalStrut(10));
	
	//Action panel
	actionPanel = new JPanel();
	actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
	
	status = new JLabel("No se han cargado los datos aún");
	actionPanel.add(status);
	actionPanel.add(Box.createHorizontalGlue());
	
	trainButton = new JButton("Entrenar");
	trainButton.setEnabled(false);
	actionPanel.add(trainButton);
	configWindow.add(actionPanel);
	//configWindow.add(Box.createVerticalStrut(10));
	
	progressBar = new JProgressBar(0, (int) times.getValue() + 2);
	progressBar.setValue(0);
	configWindow.add(progressBar);
	
	//Output window
	outputPanel = new JPanel();
	outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
	output = new JTextArea("");
	output.setEditable(false);
	outputWindow = new JScrollPane(outputPanel);
	
	outputPanel.add(output);
	mainWindow.addTab("Salida", outputWindow);
	
	// Train button click listener
	loadTrainData.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		onLoadTrainButtonClicked();
	    }
	});

	// Test button click listener
	loadTestData.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		onLoadTestButtonClicked();
	    }
	});
	
	trainButton.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		onTrainButtonClicked();
	    }
	});

    }
    
    public void setStatus(String status) {
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	this.status.setText(sdf.format(cal.getTime()) + " " + status);
    }
    
    public void enableTrain() {
	this.trainButton.setEnabled(true);
    }
    
    public void disableTrain() {
	this.trainButton.setEnabled(false);
    }
    
    public double getLearningFactor() {
	return ((double) learningFactor.getValue() / (double) learningFactor.getMaximum());
    }
    
    public double getInertiaFactor() {
	return ((double) inertiaFactor.getValue() / (double) inertiaFactor.getMaximum());
    }
    
    public double getMinimumImprovement() {
	return (double) minimumImprovement.getValue();
    }
    
    public int getHiddenLayers() {
	return (int) hiddenLayers.getValue();
    }
    
    public int getHiddenNeurons() {
	return (int) hiddenNeurons.getValue();
    }
    
    public int getMaxiter() {
	return (int) maxiter.getValue();
    }
    
    public int getTimes() {
	return (int) times.getValue();
    }
    
    public void clearOutput() {
	output.setText(null);
    }
    
    public void appendOutput(String text) {
	output.append(text);
	output.append("\n");
    }
    
    public void setProgressBarMaximumValue(int maxValue) {
	progressBar.setMaximum(maxValue);
    }
    
    public void setProgressBarValue(int value) {
	progressBar.setValue(value);
    }
    
    public int getProgressBarMaximumValue() {
	return progressBar.getMaximum();
    }
    
    public void disableAllButtons() {
	loadTrainData.setEnabled(false);
	loadTestData.setEnabled(false);
	trainButton.setEnabled(false);
    }
    
    public void enableAllButtons() {
	loadTrainData.setEnabled(true);
	loadTestData.setEnabled(true);
	trainButton.setEnabled(true);
    }
    
    public boolean getUseBias() {
	return useBias.isSelected();
    }
    
    private void onLoadTrainButtonClicked() {
	int returnVal = fileChooser.showOpenDialog(this);
	
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    MainController.setTrainData(fileChooser.getSelectedFile().getAbsolutePath());
	}
    }
    
    private void onLoadTestButtonClicked() {
	int returnVal = fileChooser.showOpenDialog(this);
	
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    MainController.setTestData(fileChooser.getSelectedFile().getAbsolutePath());
	}
    }
    
    private void onTrainButtonClicked() {
	MainController.trainData();
    }
    
    private Dictionary<Integer, JLabel> makeDictionaryForASlider(int start, int end, int step) {
	Dictionary<Integer, JLabel> dic = new Hashtable<Integer, JLabel>();
	
	for(int i = start; i <= end; i += step) {
	    String str = String.valueOf((double) i / (double) end);
	    JLabel label = new JLabel(str);
	    dic.put(i, label);
	}
	
	return dic;
    }
}

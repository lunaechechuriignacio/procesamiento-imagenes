package procesador.domain;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import procesador.generador.GeneradorDeImagenes;

@SuppressWarnings("serial")
public class Editor extends javax.swing.JFrame implements MouseListener{

	private ProcesadorDeImagenes ObjProcesamiento = new ProcesadorDeImagenes();
	private ProcesadorDeImagenes ObjProcesamiento2 = new ProcesadorDeImagenes();
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JLabel contenedorDeImagen2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private JSlider slider = new JSlider(0,255,127);
	private JMenuBar menuBar = new JMenuBar();	
	private JMenu menuArchivo = new JMenu("Archivo");
	private JMenuItem itemCargar = new JMenuItem("Abrir Imagen");
	private JMenuItem itemGuardar = new JMenuItem("Guardar Imagen");
	private JMenuItem itemCerrar = new JMenuItem("Cerrar");
	private JMenu menuFiguras = new JMenu("Figuras");
	private JMenu menuFiltros = new JMenu("Filtros");
	private JMenuItem itemCirculo = new JMenuItem("Dibujar Circulo");
	private JMenuItem itemCuadrado = new JMenuItem("Dibujar Cuadrado");
	private JMenuItem itemGrises = new JMenuItem("Escala de Grises");
	private JMenuItem itemR = new JMenuItem("Banda R");
	private JMenuItem itemG = new JMenuItem("Banda G");
	private JMenuItem itemB = new JMenuItem("Banda B");
	private JMenuItem itemNegativoGris = new JMenuItem("Negativo");
	private JMenuItem itemNegativoColor = new JMenuItem("Negativo Color");
	private BufferedImage buffer1;
	private BufferedImage buffer2;
	private BufferedImage original;
	private JMenu menuDegrade = new JMenu("Degrades");
	private JMenuItem itemGris = new JMenuItem("Degrade grises");
	private JMenuItem itemColor = new JMenuItem("Degrade colores");
	private JMenu menuPixel = new JMenu("Pixeles");
	private JMenuItem itemGet = new JMenuItem("Obtener valor");
	private JMenuItem itemSet = new JMenuItem("Modificar valor");
	private JMenu menuPromedio = new JMenu("Seleccion");
	private JMenuItem itemSeleccionar = new JMenuItem("Seleccionar");	
	private JMenuItem itemPromedioGris = new JMenuItem("Promedio Grises");
	private JMenuItem itemCopiar = new JMenuItem("Copiar Imagen");
	private JMenuItem itemPromedioColor = new JMenuItem("Promedio Colores");
	private JMenu menuHistograma = new JMenu("Histograma");
	private JMenuItem itemHistograma = new JMenuItem("Crear Histograma");
	private JMenu menuUmbral = new JMenu("Umbral");
	private JMenuItem itemUmbralizar = new JMenuItem("Umbralizar");
	private JLabel mensaje = new JLabel("");
	private java.awt.Point puntoInicial=null;
	private java.awt.Point puntoFinal=null;
    private int puntosSeleccionados=0;
    private ChartPanel chartPanel;
    private JLabel valorUmbral = new JLabel();
	private boolean seleccionando = false;
    
	public Editor() {
		initComponents();
	}

	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane2 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		contenedorDeImagen2 = new javax.swing.JLabel();
		contenedorDeImagen.addMouseListener(this);
		this.setJMenuBar(crearMenu());
		definirFuncionCerrar();		
		agregarBotones();		
		this.setBounds(0, 0, 1270, 720);
		jScrollPane1.setBounds(0, 0, 600, 650);
		jScrollPane2.setBounds(600, 0, 600, 650);
		jScrollPane2.setViewportView(contenedorDeImagen2);
		jScrollPane1.setViewportView(contenedorDeImagen);;
		contenedorDeImagen.setVerticalAlignment(SwingConstants.TOP);
		contenedorDeImagen2.setVerticalAlignment(SwingConstants.TOP);
		valorUmbral = new JLabel("Umbral:");
		valorUmbral.setBounds(1230,30,70,20);
		valorUmbral.setVisible(false);
		slider.setVisible(false);
		slider.setBounds(1250, 50, 30, 100);
		slider.setOrientation(SwingConstants.VERTICAL);
		valorUmbral.setText("Umbral: "+slider.getValue());
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (buffer1 != null){
					borrarHistograma();
					buffer2 = ObjProcesamiento.umbralizarImagen(buffer1, slider.getValue());
					contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
					valorUmbral.setText("Umbral: "+slider.getValue());
				}
			}
		});
		this.setLayout(null);
		this.add(valorUmbral);
		this.add(jScrollPane1);
		this.add(jScrollPane2);
		this.add(slider);
		this.add(valorUmbral);
		mensaje.setBounds(10, 655, 500, 20);
		this.add(mensaje);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
	}

	private JMenuBar crearMenu() {
		menuArchivo.add(itemCargar);
		menuArchivo.add(itemGuardar);
		menuArchivo.add(itemCerrar);
		menuBar.add(menuArchivo);
		menuFiguras.add(itemCirculo);
		menuFiguras.add(itemCuadrado);
		menuBar.add(menuFiguras);
		menuDegrade.add(itemGris);
		menuDegrade.add(itemColor);
		menuBar.add(menuFiguras);
		menuFiltros.add(itemGrises);
		menuFiltros.add(itemR);
		menuFiltros.add(itemG);
		menuFiltros.add(itemB);
		menuFiltros.add(itemNegativoGris);
		menuFiltros.add(itemNegativoColor);
		menuBar.add(menuFiltros);
		menuBar.add(menuDegrade);
		menuPixel.add(itemGet);
		menuPixel.add(itemSet);
		menuBar.add(menuPixel);
		menuPromedio.add(itemSeleccionar);
		menuPromedio.add(itemCopiar);
		menuPromedio.add(itemPromedioGris);
		menuPromedio.add(itemPromedioColor);
		menuBar.add(menuPromedio);
		menuHistograma.add(itemHistograma);
		menuBar.add(menuHistograma);
		menuUmbral.add(itemUmbralizar);
		menuBar.add(menuUmbral);
		return menuBar;
	}

	private void definirFuncionCerrar() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Procesador de Imagenes");
	}

	private void agregarBotones() {
		agregarMenuCerrar();
		agregarMenuGuardar();
		agregarMenuCirculo();
		agregarMenuCargar();
		agregarMenuCuadrado();
		agregarMenuGrises();
		agregarMenuNegativoGris();
		agregarMenuNegativoColor();
		agregarMenuDegradeGris();
		agregarMenuDegradeColor();
		agregarMenuSeleccionar();
		agregarMenuR();
		agregarMenuG();
		agregarMenuB();
		agregarMenuGet();
		agregarMenuSet();
		agregarMenuCopiar();
		agregarMenuPromedioGris();
		agregarMenuPromedioColor();
		agregarMenuHistograma();
		agregarMenuUmbralizar();
	}

	private void agregarMenuCopiar() {
		itemCopiar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (seleccionando){
					Integer ancho = (int) (puntoFinal.getX()-puntoInicial.getX());
					Integer alto = (int) (puntoFinal.getY()-puntoInicial.getY());
					BufferedImage copia = new BufferedImage(ancho,alto,1);
					copiar(copia);
					redibujarImagen();
					seleccionando = false;
					resetPoints();	
				}
			}
		});
	}
	
	private void copiar(BufferedImage copia) {
		for (int i=(int)puntoInicial.getX(); i < (int)puntoFinal.getX(); i++){
			for (int j= (int)puntoInicial.getY(); j < (int)puntoFinal.getY(); j++){
				copia.setRGB(i-(int)puntoInicial.getX(), j-(int)puntoInicial.getY(), buffer1.getRGB(i, j));
				aplicarOperacion(copia);
			}
		}
		
	}

	private void agregarMenuSeleccionar() {
		itemSeleccionar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!seleccionando){
					seleccionando = true;
					original = new BufferedImage(buffer1.getWidth(),buffer1.getHeight(),1);
					resetPoints();
				}
			}
		});
	}
	
	private void agregarMenuUmbralizar() {
		itemUmbralizar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!slider.isVisible()){
					slider.setValue(126);
					valorUmbral.setText("Umbral: "+slider.getValue());
					valorUmbral.setVisible(true);
					slider.setVisible(true);
				}else{
					valorUmbral.setVisible(false);
					slider.setVisible(false);
				}
			}
		});		
	}

	private void agregarMenuGet() {
		itemGet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getPixelActionPerformed(evt);
			}
		});
	}
	
	private void getPixelActionPerformed(ActionEvent evt) {
		JTextField ejex = new JTextField();
		JTextField ejey = new JTextField();
		Object[] message = {
		    "CoordenadaX:", ejex,
		    "CoordenadaY:", ejey,
		};
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese las coordenadas del pixel", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			int i = Integer.valueOf(ejex.getText());
			int j = Integer.valueOf(ejey.getText());
			if ((validarAlto(j))&&(validarAncho(i))){
				Color color = new Color(buffer1.getRGB(i, j));
				mensaje.setText("El valor del pixel es: R "+ color.getRed()+" G "+color.getGreen()+" B "+color.getBlue());
			}else{
				JOptionPane.showMessageDialog(null, "Los pixeles exceden la imagen!");
			}
		}
	}
	
	private boolean validarAncho(int ancho){
		return ((ancho>=0)&&(ancho<this.ObjProcesamiento.getImage().getAncho()));
	}
	
	private boolean validarAlto(int alto){
		return ((alto>=0)&&(alto<this.ObjProcesamiento.getImage().getAlto()));
	}
	
	private boolean validarValorByte(int valor){
		return ((valor>=0)&&(valor<=255));
	}
	
	private void agregarMenuSet() {
		itemSet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setPixelActionPerformed(evt);
			}
		});
	}
	
	private void setPixelActionPerformed(ActionEvent evt) {
		JTextField ejex = new JTextField();
		JTextField ejey = new JTextField();
		JTextField valorR = new JTextField();
		JTextField valorG = new JTextField();
		JTextField valorB = new JTextField();
		Object[] message = {
		    "CoordenadaX:", ejex,
		    "CoordenadaY:", ejey,
		    "Red:", valorR,
		    "Green:", valorG,
		    "Blue:", valorB,
		};
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese las coordenadas del pixel y los valores RGB", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			int i = Integer.valueOf(ejex.getText());
			int j = Integer.valueOf(ejey.getText());
			int r = Integer.valueOf(valorR.getText());
			int g = Integer.valueOf(valorG.getText());
			int b = Integer.valueOf(valorB.getText());
			
			if (validarValorByte(r)&&validarValorByte(g)&&validarValorByte(b)){
				if ((validarAlto(j))&&(validarAncho(i))){
					Color color = new Color(r,g,b);
					buffer1.setRGB(i, j, color.getRGB());
					cargarImagen(buffer1);
					mensaje.setText("El nuevo valor del pixel es: R "+ color.getRed()+" G "+color.getGreen()+" B "+color.getBlue());
				}else{
					JOptionPane.showMessageDialog(null, "Los pixeles exceden la imagen!");
				}
			}else{
				JOptionPane.showMessageDialog(null, "Los valores RGB no son correctos!");
			}
		}
	}
	
	private void agregarMenuR() {
		itemR.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.canal(1,buffer1));
			}
		});
	}

	private void agregarMenuG() {
		itemG.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.canal(2,buffer1));
			}
		});
	}
	
	private void agregarMenuB() {
		itemB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.canal(3,buffer1));
			}
		});
	}

	private void agregarMenuDegradeGris() {
		itemGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				degradeGrisActionPerformed(evt);
			}
		});
	}
	
	private void degradeGrisActionPerformed(ActionEvent evt) {
		cargarImagen(new GeneradorDeImagenes().dezplegarDegradeGrises());
	}
	
	private void agregarMenuDegradeColor() {
		itemColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				degradeColorActionPerformed(evt);
			}
		});
	}
	
	private void degradeColorActionPerformed(ActionEvent evt) {
		cargarImagen(new GeneradorDeImagenes().dezplegarDegradeColor());
	}

	private void agregarMenuGrises() {
		itemGrises.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				grisesActionPerformed(evt);
			}
		});		
	}

	private void grisesActionPerformed(ActionEvent evt) {
		aplicarOperacion(ObjProcesamiento.pasarAEscalaDeGrises(buffer1));
	}
	
	private void agregarMenuNegativoGris() {
		itemNegativoGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarANegativoImagenGris(buffer1));
			}
		});		
	}

	private void agregarMenuNegativoColor() {
		itemNegativoColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarANegativoImagenColor(buffer1));
			}
		});		
	}
	
	private void aplicarOperacion(BufferedImage proceso) {
		buffer2 = proceso;
		borrarHistograma();
		contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
	}

	private void agregarMenuCuadrado() {
		itemCuadrado.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cargarImagen(new GeneradorDeImagenes().crearImagenBinariaCuadrado(200));			
			}
		});
	}
     
	private void agregarMenuPromedioGris() {
		itemPromedioGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {  
				if(puntoInicial!=null && puntoFinal!=null){
					ObjProcesamiento.promedioGrises(puntoInicial, puntoFinal);
					redibujarImagen();
					resetPoints();
					seleccionando = false;
				}
			}
		});
	}
	  
	private void agregarMenuPromedioColor() {
		itemPromedioColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {  
				if(puntoInicial!=null && puntoFinal!=null){
					 ObjProcesamiento.promedioColores(puntoInicial, puntoFinal);
					 redibujarImagen();
					 seleccionando = false;
					 resetPoints();	
				}
			}
		});
	}
	
	private void resetPoints(){
		puntoInicial=null;
		puntoFinal=null;
		puntosSeleccionados=0;
	}
	
	private void agregarMenuHistograma() {
		itemHistograma.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {;
				crearHistograma(ObjProcesamiento.histograma(), contenedorDeImagen2, Color.BLACK);
			}
		});
	}
	
	private void agregarMenuCerrar() {
		itemCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cerrarActionPerformed(evt);
			}
		});
	}
	
	private void agregarMenuCirculo() {
		itemCirculo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {  
				cargarImagen(new GeneradorDeImagenes().crearImagenBinariaCirculo(50));
			}
		});
	}
	
	private void cargarImagen(BufferedImage imagen){
		buffer1 = imagen;
		buffer2 = null;
		ObjProcesamiento.setBuffer(buffer1);
		borrarHistograma();
		contenedorDeImagen2.setIcon(null);
		contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}
	
	private void agregarMenuCargar(){
		itemCargar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {	
					cargarActionPermorfed();
				}catch(Exception e){
					System.out.println("ERROR DE CARGA ARCHIVO: "+ObjProcesamiento.getNombreArchivoImagen());
				}	
			}
		});
	}

	private void cargarActionPermorfed() {
		if (buffer1 == null){
			cargarImagen(ObjProcesamiento.abrirImagen());
		}else{
			this.aplicarOperacion(ObjProcesamiento2.abrirImagen());
		}
		mensaje.setText(ObjProcesamiento.getNombreArchivoImagen()+" - Ancho: " +
				ObjProcesamiento.getBuffer().getWidth() + " pixeles - Alto: "+ObjProcesamiento.getBuffer().getHeight()+ " pixeles");		
	}
	
	private void agregarMenuGuardar(){
		itemGuardar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
					try {
						guardarActionPerformed(evt);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Error al guardar la imagen!");
					}
			}
		});
	}


	private void guardarActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		if (buffer1 != null){
			JFileChooser fc=new JFileChooser();
			int seleccion=fc.showSaveDialog(this);
			if(seleccion==JFileChooser.APPROVE_OPTION){
			    File fichero=fc.getSelectedFile();
			    guardarImagen(fichero.getAbsolutePath());
			    System.out.println(fichero.getAbsolutePath());
			}
			
		}else{
			 JOptionPane.showMessageDialog(null, "No hay ninguna imagen cargada!");
		}
	}
	
	public void guardarImagen(String direccion) throws IOException{
		File fileOutput = new File(direccion+".bmp");
		if (buffer2 == null){
			ImageIO.write(this.buffer1, "bmp", fileOutput);
		}else{
			ImageIO.write(this.buffer2, "bmp", fileOutput);
		}
	}

	private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}

	@SuppressWarnings("deprecation")
	public void mouseClicked(MouseEvent ev) {
		if (seleccionando){
			if(puntosSeleccionados==0){
				if (estaDentroDeImagen(ev.getPoint())){
					puntoInicial=ev.getPoint();
					puntosSeleccionados++;
				}
			}else{	
				puntosSeleccionados++;
				if (puntosSeleccionados==3){
					seleccionando = false;
					redibujarImagen();
					resetPoints();
				}else{
					puntoFinal=ev.getPoint();
					if (clickValidos()){
						dibujarRectangulo(puntoInicial, puntoFinal);
						contenedorDeImagen.setCursor(new Cursor(DEFAULT_CURSOR));
					}else{
						resetPoints();
					}
				}
			}
		}
	}	
	
	private void redibujarImagen() {
			buffer1 = original;
			contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}

	private boolean estaDentroDeImagen(Point point) {
		return (0<point.getX()&&point.getX()<buffer1.getWidth()&&0<point.getY()&&point.getY()<buffer1.getHeight());
	}

	@SuppressWarnings("deprecation")
	public void mouseExited(MouseEvent ev) {
			contenedorDeImagen.setCursor(new Cursor(DEFAULT_CURSOR));
	}

	@SuppressWarnings("deprecation")
	public void mouseEntered(MouseEvent ev) {
		if (seleccionando){
			contenedorDeImagen.setCursor(new Cursor(MOVE_CURSOR));
		}
	}

	private boolean clickValidos(){
		return (((puntoInicial.getX()<puntoFinal.getX())
				&&(puntoInicial.getY()<puntoFinal.getY())))&&(estaDentroDeImagen(puntoFinal));
	}

	public void mousePressed(MouseEvent e) {
	}

	private void dibujarRectangulo(Point inicio, Point fin) {
		guardarOriginal();
		for (int i=(int)inicio.getX(); i < (int)fin.getX(); i++){
			for (int j= (int)inicio.getY(); j < (int)fin.getY(); j++){
				buffer1.setRGB( i,  (int)inicio.getY(), new Color(0,0,0).getRGB());
				buffer1.setRGB( i,  (int)fin.getY(), new Color(0,0,0).getRGB());
				buffer1.setRGB( (int)inicio.getX(),  j, new Color(0,0,0).getRGB());
				buffer1.setRGB( (int)fin.getX(),  j, new Color(0,0,0).getRGB());
				contenedorDeImagen.setIcon(new ImageIcon(buffer1));
			}
		}
	}

	private void guardarOriginal() {
		for (int i=0; i < buffer1.getWidth(); i++){
			for(int j =0; j < buffer1.getHeight(); j++){
				Color color = new Color (buffer1.getRGB(i, j));
				original.setRGB(i, j, color.getRGB());
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
	}
	
	private void crearHistograma(int[] histograma,JLabel jLabelHistograma,Color colorBarras) {
 
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String serie = "Number of p�xels";
        for (int i = 0; i < histograma.length; i++){
            dataset.addValue(histograma[i], serie, "" + i);
        }
        JFreeChart chart = ChartFactory.createBarChart("Frequency Histogram", null, null,
                                    dataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, colorBarras);
        chart.setAntiAlias(true);
        chart.setBackgroundPaint(new Color(214, 217, 223)); 
        jLabelHistograma.removeAll();
        jLabelHistograma.repaint();
        jLabelHistograma.setLayout(new java.awt.BorderLayout());
        chartPanel = new ChartPanel(chart);
        jLabelHistograma.add(chartPanel);
        jLabelHistograma.validate();
    }
	
	private void borrarHistograma() {
		if(chartPanel!=null){
			contenedorDeImagen2.remove(chartPanel);
			contenedorDeImagen2.repaint();
			contenedorDeImagen2.validate();
		}	
	}
}
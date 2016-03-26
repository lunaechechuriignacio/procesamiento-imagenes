package procesador.domain;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import aplication.Aplicacion;

public class ProcesadorDeImagenesPPM {

	private static String id;
	private static Integer ancho;
	private static Integer alto;
	private static Integer maxValue;
	private static Integer[][] pixeles;
	private static Integer[][] Rband;
	private static Integer[][] Gband;
	private static Integer[][] Bband;
	private static int cant = 0;
	private static int pos = 0;
	
	
	
	public static void main(String args[]) throws IOException {
		
		Aplicacion app = new Aplicacion();
		app.setVisible(true);
		
		cargarImagen();
		BufferedImage buff = mostrarImagen();
		BufferedImage circulo = crearImagenBinariaCirculo(70);
	
		mostrarEscalaDeGris();
		BufferedImage cuadrado = crearImagenBinariaCuadrado(85);
		dezplegarDegradeGrises();
		dezplegarDegradeColor();
		guardarImagen(buff, "imagen.jpg");
		guardarImagen(circulo,"circulo.jpg");
		guardarImagen(cuadrado,"cuadrado.jpg");
		
		
	}
	
	private static void dezplegarDegradeGrises() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(j,j,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		ventana(buff, 256, 256);		
	}
	
	private static void dezplegarDegradeColor() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(255-j,i,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		ventana(buff, 256, 256);
	}

	public static void cargarImagen() throws IOException{
		BufferedReader br = openFile();
		id = br.readLine();
		String secondLine = br.readLine();
		System.out.println("Numero Magico: "+ id);
		
		calcularAnchoAlto(secondLine);
		maxValue = Integer.valueOf(br.readLine());
		calcularMatrizDePixeles(br);
		System.out.println("Ancho: "+ ancho);
		System.out.println("Alto: "+ alto);
		System.out.println("MaxValue: "+ maxValue);
		
		br.close();
	}

	private static void calcularMatrizDePixeles(BufferedReader br) throws IOException {
		int c = 0;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				
				Rband[i][j] = br.read();
				if (Rband[i][j] > 255){
					Rband[i][j] = 125;
				}
				Gband[i][j] = br.read();
				if	(Gband[i][j] > 255){
					Gband[i][j] = 145;
				}
				Bband[i][j] = br.read();
				if (Bband[i][j] > 255){
					Bband[i][j] = 145;
				}
				c=c+3;
				pixeles[i][j] = (int)(Rband[i][j]+Gband[i][j]+Bband[i][j])/3;
//				System.out.print(Rband[i][j]+" "+Gband[i][j]+" "+Bband[i][j]+" ");
			}
//			System.out.println(" ");
		}
		System.out.println("cantidad de pixeles: "+c);		
	}

	private static void calcularAnchoAlto(String secondLine) {
		ancho = Integer.valueOf(secondLine.split("\\ ")[0]);
		alto = Integer.valueOf(secondLine.split("\\ ")[1]);
		pixeles = new Integer[ancho][alto]; 
		Rband = new Integer[ancho][alto];
		Gband = new Integer[ancho][alto];
		Bband = new Integer[ancho][alto];
		
	}

	private static BufferedReader openFile() throws FileNotFoundException {
		File imgFile =  new File("images.ppm");
		FileInputStream input = new FileInputStream(imgFile);
		InputStreamReader ir = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(ir);
		return br;
	}

	private static BufferedImage crearImagenBinariaCirculo(int radio) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				color = new Color(0,0,0);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		int r= radio;
		for (int x=-r; x<r; x++){
				int y = (int)Math.sqrt((r*r)-(x*x));
				int z = -(int)Math.sqrt((r*r)-(x*x));

				color = new Color(255,255,255);
				buff.setRGB(x+100, y+100, color.getRGB());
				buff.setRGB(x+101, y+100, color.getRGB());
				buff.setRGB(x+100, z+100, color.getRGB());
				buff.setRGB(x+101, z+100, color.getRGB());
				
				buff.setRGB(y+100, x+100, color.getRGB());
				buff.setRGB(y+101, x+100, color.getRGB());
				buff.setRGB(z+100, x+100, color.getRGB());
				buff.setRGB(z+101, x+100, color.getRGB());
		}
		ventana(buff, 200, 200);		
		return buff;
	}
	
	private static BufferedImage crearImagenBinariaCuadrado(int lado) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				color = new Color(0,0,0);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		for (int x=-lado/2; x<lado/2; x++){
				color = new Color(255,255,255);
				buff.setRGB(x+100, (lado/2)+100, color.getRGB());
				buff.setRGB(x+100, -(lado/2)+100, color.getRGB());
				buff.setRGB((lado/2)+100, x+100, color.getRGB());
				buff.setRGB(-(lado/2)+100, x+100, color.getRGB());
//				buff.setRGB(x+101, z+100, color.getRGB());
			
		}
		ventana(buff, 200, 200);		
		return buff;
	}
	

	private static void mostrarEscalaDeGris() {
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		Color color;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				color = new Color(pixeles[i][j],pixeles[i][j],pixeles[i][j]);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		ventana(buff, ancho, alto);
	}

	private static void guardarImagen(BufferedImage buff, String nombre) throws IOException {
		File fileOutput = new File(nombre);
		//ImageIO.write(this.imageActual, imagen.getTipo().toLowerCase(), fileOutput);
		ImageIO.write(buff, "jpg", fileOutput);
		
	}

	private static BufferedImage mostrarImagen() {
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		Color color;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				color = new Color(Rband[i][j],Gband[i][j],Bband[i][j]);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		ventana(buff, ancho, alto);
		return buff;
	}

	private static JFrame ven;
	
	private static void ventana(Image buff, int ancho, int alto){
        
        if (cant >= 4){
        	cant =0;
        	pos = 300;
        }
		ven = new JFrame("Imagen");
        ven.setLayout(null);
        ven.setVisible(true);
        ven.setBounds(cant*300,pos,ancho+40,alto+60);
        ven.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel l = new JLabel();
        l.setIcon(new ImageIcon(buff));
        l.setSize(ancho,alto);
        l.setBounds(10,10,ancho,alto);
        l.repaint();
        ven.add(l);
        ven.repaint();
        cant++;
        if (cant > 4){
        	cant =0;
        }
	}
}
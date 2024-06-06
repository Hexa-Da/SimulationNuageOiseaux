import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyEvent;


import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;

import java.awt.image.BufferedImage;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.border.EmptyBorder;



class Cases {
	
	int NumCase;
    int X, Y;
    int TailleCase;
    ArrayList<Cases> CaseVoisine;
    ArrayList<Oiseaux> Population;   

    public Cases(int NumCase, int X, int Y, int TailleCase) {
        this.NumCase = NumCase;
        this.X = X;
        this.Y = Y;
        this.TailleCase = TailleCase;
        CaseVoisine = new ArrayList<>();
        Population = new ArrayList<>();
    }
    
    public void ajouterVoisin(Cases voisin) {
        CaseVoisine.add(voisin);
    }
    
    public void dessiner(Graphics2D g2d) {
        g2d.setColor(Color.BLACK); 
        g2d.drawRect(X, Y, TailleCase, TailleCase); 
    }
}



class Grille extends JPanel {
	private static final long serialVersionUID = 1L;
	
	ArrayList<Cases> Grille;
	int TailleCase;
	int NombreColonnes, NombreLignes;

	public Grille(int TailleCase, int NombreColonnes, int NombreLignes) {
        Grille = new ArrayList<>();
        this.NombreLignes = NombreLignes;
        this.NombreColonnes = NombreColonnes;
        this.TailleCase = TailleCase;

                
        // Création des cases du cadrillage
        for (int i = 0; i < NombreLignes; i++) {
            for (int j = 0; j < NombreColonnes; j++) {
            	int NumCase = i * NombreColonnes + j; //0 à N-1
            	// Coordonnées du point en haut à gauche de chaque case
            	// X abcisse et Y ordonnée et origine en haut à gauche
                int X = j * TailleCase;
                int Y = i * TailleCase;
                
                

                Cases nouvelleCase = new Cases(NumCase, X, Y, TailleCase);
                Grille.add(nouvelleCase);
            }
        }

        // Création de la liste des voisins (9 cases)
        for (int i = 0; i < NombreLignes; i++) {
            for (int j = 0; j < NombreColonnes; j++) {
            	// Identification de la case avec NumCase
                int NumCase = i * NombreColonnes + j;
                //case est un mot clé donc on utilise c
                Cases c = Grille.get(NumCase);
                calculerVoisins(c, NombreColonnes, NombreLignes, Grille);
            }
        }
	}
	
    public void calculerVoisins(Cases Case, int NombreColonnes, int NombreLignes, ArrayList<Cases> Grille) {
    	// Récupération des infos 
        int X = Case.X;
        int Y = Case.Y;
        int NumCase = Case.NumCase;
        int TailleCase = Case.TailleCase;
       
        // Liste des déplacements possibles pour trouver les voisins
        int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

        // Parcours des déplacements possibles pour trouver les voisins
        for (int[] deplacement : deplacements) {
            int VoisinX = X + deplacement[0] * TailleCase;
            int VoisinY = Y + deplacement[1] * TailleCase;
            
            // Vérification si le voisin est dans les limites du cadrillage
            // A faire si les oiseau "rebondissent" sur les bords
            if (VoisinY >= 0 && VoisinY < NombreLignes * TailleCase &&
            		VoisinX >= 0 && VoisinX < NombreColonnes * TailleCase) {
            	
                // Calcul de l'indice du voisin dans le cadrillage
            	int VoisinNum = NumCase + deplacement[1] * NombreColonnes + deplacement[0]; 
                Cases voisin = Grille.get(VoisinNum); 
                Case.ajouterVoisin(voisin);

            }
            // A faire si les oiseau traversent les bords
            else {
                int VoisinNum = NumCase;
                if (VoisinX < 0) {
                    // Calcul de l'indice du voisin dans le cadrillage
                    VoisinNum += NombreColonnes-1;
                }
                if (VoisinX >= NombreColonnes * TailleCase) {
                    VoisinNum += - (NombreColonnes -1);
                }
                if (VoisinY < 0) {
                    VoisinNum += NombreColonnes*(NombreLignes-1);
                }
                if (VoisinY >= NombreLignes*TailleCase) {
                    VoisinNum -= NombreColonnes*(NombreLignes-1);
                }

                Cases voisin = Grille.get(VoisinNum); 
                Case.ajouterVoisin(voisin);
            
            }
        }
    }
    
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Cases c : Grille) {  
        	c.dessiner(g2d);
        }
	}
}



class Carte extends JPanel {

    Color[][] colors;
    BufferedImage bufferedImage;
    int largeur,hauteur;

    public Carte(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        
        creerCarte();
        bufferedImage = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);

        // Dessiner la matrice de couleurs dans l'image tampon
        Graphics2D img = bufferedImage.createGraphics();
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[i].length; j++) {
                img.setColor(colors[i][j]);
                img.fillRect(j, i, 1, 1);
            }
        }
        img.dispose();
    }

    private void creerCarte(){
        Vecti gridSize = new Vecti(largeur,hauteur);
		Vecti size = new Vecti(3,3); //On divise notre carte en une grille de 3 par 3
        long seed = (long) aleatoire(2<<10,2<<99); //Génère une seed aléatoire
        colors = mapGeneration(seed,gridSize,size);
    }

    public static double perlin(double x,double y, int[] permutation){
   
        double unit = 1.0/Math.sqrt(2.);
        double gradientList[][] = {{unit,unit},{-unit,unit},{unit,-unit},{-unit,-unit},{1.,0.},{-1.,0.},{0.,1.},{0.,-1.}};
  
        int x0 = (int)Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int)Math.floor(y);
        int y1 = y0 + 1;
        
        int xx = x0&255; //Donne une valeur entre 0 et 255
        int yy = y0&255;
        
        int aleaHG = permutation[xx + permutation[yy]] % 8;
        Vectf gradHG = new Vectf(gradientList[aleaHG][0],gradientList[aleaHG][1]);
            	
        int aleaHD = permutation[xx + 1 + permutation[yy]] % 8;
        Vectf gradHD = new Vectf(gradientList[aleaHD][0],gradientList[aleaHD][1]);
            	
        int aleaBG = permutation[xx + permutation[yy + 1]] % 8;
        Vectf gradBG = new Vectf(gradientList[aleaBG][0],gradientList[aleaBG][1]);	
            	
        int aleaBD = permutation[xx + 1 + permutation[yy + 1]] % 8;
        Vectf gradBD = new Vectf(gradientList[aleaBD][0],gradientList[aleaBD][1]);
        
        Vectf dHG = new Vectf(x - (double)x0,y - (double)y0);
    	Vectf dHD = new Vectf(x - (double)x1,y - (double)y0);
    	Vectf dBG = new Vectf(x - (double)x0,y - (double)y1);
    	Vectf dBD  = new Vectf(x - (double)x1,y - (double)y1);
        
        double contribHG = gradHG.produitScal(dHG);
    	double contribHD = gradHD.produitScal(dHD);
    	double contribBG = gradBG.produitScal(dBG);
    	double contribBD = gradBD.produitScal(dBD);
        
    	//Interpolation
    	double interpHGHD = interpolate(contribHG,contribHD,x - (double)x0);
    	double interpBGBD = interpolate(contribBG,contribBD,x - (double)x0);
    
    	double value = interpolate(interpHGHD,interpBGBD,y - (double)y0);
    	
    	return (value+1.)/2.; //On rearrange la distribution des valeurs
    }

	public static double interpolate(double a0, double a1, double t) {
	     return a0 + (a1 - a0) * fade(t);
	}
	
	public static double fade(double t) {
		return ((6*t - 15)*t + 10)*t*t*t;
	}

    public static Color[][] mapGeneration(long seed,Vecti gridSize,Vecti size){
		Random rand = new Random(seed);
		int[] permutation = new int[512];
		for (int i = 0; i < 256; i ++) {
			permutation[i] = rand.nextInt(256);
			permutation[i+56] = permutation[i];
		}		
		Color[][] colors = new Color[gridSize.x][gridSize.y];
		
		for (int i = 0; i < gridSize.x; i++) {
			for (int j = 0; j < gridSize.y; j++) {
				
				double x = ((double)i/(double)gridSize.x);
				double y = ((double)j/(double)gridSize.y);
				double n = perlin(size.x*x,size.y*y,permutation);
				
				//Eau
				if (n < 0.30) {
		        	colors[i][j] = Color.decode("#00d2ff");
				}else if (n>=0.30 && n < 0.33){
		        	colors[i][j] = Color.decode("#2edaff");
				}else if (n>=0.33 && n < 0.36){
		        	colors[i][j] = Color.decode("#50e0ff");
				}else if (n>=0.36 && n < 0.39){
		        	colors[i][j] = Color.decode("#73e6ff");
				}else if (n>=0.39 && n < 0.42){
		        	colors[i][j] = Color.decode("#9bedff");
		        //Sable
				}else if (n>=0.42 && n < 0.43){
		        	colors[i][j] = Color.decode("#f4f014");
		        }else if (n>=0.43 && n < 0.44){
		        	colors[i][j] = Color.decode("#f8ef14");
		        //Terre & Forets
		        }else if (n>=0.44 && n < 0.47){
		        	colors[i][j] = Color.decode("#00b300");
		        }else if (n>=0.47 && n < 0.54){
		        	colors[i][j] = Color.decode("#009a00");
		        }else if (n>=0.54 && n < 0.57){
		        	colors[i][j] = Color.decode("#008000");
		        }else if (n>=0.57 && n < 0.60){
		        	colors[i][j] = Color.decode("#006700");
		        }else if (n>=0.60 && n < 0.63){
		        	colors[i][j] = Color.decode("#004d00");
		        }else if (n>=0.63 && n < 0.65){
		        	colors[i][j] = Color.decode("#003400");
		        //Montagnes
		        }else if (n>=0.65 && n < 0.68){
		        	colors[i][j] = Color.decode("#808080");
		        }else if (n>=0.68 && n < 0.71){
		        	colors[i][j] = Color.decode("#737373");
		        }else if (n>=0.71 && n < 0.74){
		        	colors[i][j] = Color.decode("#666666");
		        //Neige
		        }else {
		        	colors[i][j] = Color.WHITE;
		        }
			}
		}
        return colors;
	}

    public static double aleatoire (double min, double max){
        return min + Math.random()*(max-min);
    }

    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bufferedImage, 0, 0, null);
    }
}

class Vectf{
    public Vectf(double x_, double y_){
       this.x = x_;
       this.y = y_;
       this.norme2 = x_*x_+y_*y_;
    }
    double x;
    double y;
    double norme2;
    
    public double produitScal(Vectf Vect2) {
    	return x * Vect2.x + y * Vect2.y;
    }
}

class Vecti{
    public Vecti(int x_, int y_){
       this.x = x_;
       this.y = y_;
       this.norme2 = x_*x_+y_*y_;
    }
    int x;
    int y;
    int norme2;
    
    public int produitScal(Vecti Vect2) {
    	return x * Vect2.x + y * Vect2.y;
    }
}



class Oiseaux {
	
    double x, y, z;
    double vx, vy, vz;
    int NumCase; 
    Color espece;
   
    public Oiseaux(double x, double y, double z, int NumCase, Color espece) {
    	
        this.x = x;
        this.y = y;
        this.z = z;
        this.NumCase = NumCase;
        this.espece = espece;
        // this.Matricule = Matricule;

        // Choix de la direction : (random.nextBoolean() ? 1 : -1)
        // Choix de la norm entre 0 et 1 inclus : random.nextInt(101)/100
        Random random = new Random();
        this.vx = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vy = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vz = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100 * 0.002;
    }
    
    public void Deplacer(int vitesse, int largeur, int hauteur, int plafond) {
    	// Deplacer(double angleOiseau, int vitesse, int largeur, int hauteur)
    	
        this.x += this.vx * vitesse;
        this.y += this.vy * vitesse;
        this.z += this.vz * vitesse;
        
     	// Traverser la fenêtre
        if (this.x < 0) {
       	    this.x = largeur;
        }
        if (this.y < 0) {
       	    this.y = hauteur;
        }
        if (this.x > largeur) {
       	    this.x = 0;
        }
        if (this.y > hauteur) {
       	    this.y = 0;
        }
        if (this.z <= 1 || this.z >= plafond) {
        	this.vz = -this.vz;
        }
    }

    public static double[] findMinimum(ArrayList<Double> numbers) {
        // Vérifiez si la liste est vide ou null
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("La liste est vide ou null.");
        }

        // Initialisez le minimum avec le premier élément de la liste
        double min = numbers.get(0);
        int index = 0;
        int indexmin = 0;
        // Parcourez la liste pour trouver le minimum
        for (double num : numbers) {
            if (num < min) {
                min = num; // Mettez à jour le minimum si un nombre plus petit est trouvé
                indexmin = index;
            }
            index += 1;
        }

        double[] resultat = {min, indexmin};
        return resultat;
    }

	public void Repulsion(ArrayList<Oiseaux> voisins, double CoeffRepulsion, double Vitesse_max, int Rayon, int  Largeur, int Hauteur) { 
	    if (voisins.isEmpty()) {
            return;
        }
        
        int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},{0,0}};
        double repelX = 0;
        double repelY = 0;
    
        for (Oiseaux voisin : voisins) {
            // Calculer la distance entre ce boid et le voisin
            ArrayList<Double> Distances_possibles = new ArrayList<>();
            for (int[] dep : deplacements){
                double distance = Math.sqrt(Math.pow(voisin.x+dep[0]*Largeur - this.x, 2) + Math.pow(voisin.y+dep[1]*Largeur - this.y, 2));
                Distances_possibles.add(distance);
            }
            double[] distmin = findMinimum(Distances_possibles);
            double distance = distmin[0];
            int indexmin = (int) distmin[1];

            // Si le voisin est trop proche
            if (distance < Rayon) {
                // Calculer la direction pour s'éloigner du voisin
                double directionX = this.x - (voisin.x+deplacements[indexmin][0]*Largeur);
                double directionY = this.y - (voisin.y+deplacements[indexmin][1]*Largeur);

                // Normaliser la direction
                double norm = Math.sqrt(directionX * directionX + directionY * directionY);
                if (norm > 0) {
                    directionX /= norm;
                    directionY /= norm;
                }
                
                // Ajouter à la force de répulsion
                repelX += directionX;
                repelY += directionY;

                if (this.espece != voisin.espece){
                    repelX *= 3/2;
                    repelY *= 3/2;
                }
            }
            
        }

        // Appliquer la force de répulsion
        this.vx += repelX * CoeffRepulsion;
        this.vy += repelY * CoeffRepulsion;
	}

	public void Alignement(ArrayList<Oiseaux> voisins, double coeffAlignement, double Vitesse_max, int RayonIn, int RayonOut, int  Largeur, int Hauteur) {
            if (voisins.isEmpty()) {
                return;
            }
            int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},{0,0}};
            double avgVx = 0;
            double avgVy = 0;
            int count = 0;

            for (Oiseaux voisin : voisins) {
                if (this.espece == voisin.espece){
                    // Calculer la distance entre ce boid et le voisin
                    ArrayList<Double> Distances_possibles = new ArrayList<>();
                    for (int[] dep : deplacements){
                        double distance = Math.sqrt(Math.pow(voisin.x+dep[0]*Largeur - this.x, 2) + Math.pow(voisin.y+dep[1]*Largeur - this.y, 2));
                        Distances_possibles.add(distance);
                    }
                    double[] distmin = findMinimum(Distances_possibles);
                    double distance = distmin[0];
                    // int indexmin = (int) distmin[1];


                    // Si le voisin est dans le 1er cercle
                    if (distance < RayonIn ){ //& distance >= RayonOut
                        avgVx += voisin.vx;
                        avgVy += voisin.vy;
                        count++;
                    }
                }
            }

            if (count > 0) {
                // Calculer la moyenne des vitesses des voisins
                avgVx /= count;
                avgVy /= count;

                // Normaliser la vitesse moyenne
                double avgSpeed = Math.sqrt(avgVx * avgVx + avgVy * avgVy);
                if (avgSpeed > 0) {
                    avgVx = (avgVx / avgSpeed) * Vitesse_max;
                    avgVy = (avgVy / avgSpeed) * Vitesse_max;
                }

                // Calculer la force d'alignement
                double steerVx = avgVx - this.vx;
                double steerVy = avgVy - this.vy;

                // Appliquer la force d'alignement
                this.vx += steerVx * coeffAlignement;
                this.vy += steerVy * coeffAlignement;
            }
        
	}

	public void Attraction(ArrayList<Oiseaux> voisins, double coeffAttraction, double Vitesse_max, int RayonIn, int RayonOut, int  Largeur, int Hauteur) {
        if (voisins.isEmpty()) {
            return;
        }
        int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},{0,0}};
        double avgX = 0;
        double avgY = 0;
        int count = 0;
    
        for (Oiseaux voisin : voisins) {
            if (this.espece == voisin.espece){
                ArrayList<Double> Distances_possibles = new ArrayList<>();
                for (int[] dep : deplacements){
                    double distance = Math.sqrt(Math.pow(voisin.x+dep[0]*Largeur - this.x, 2) + Math.pow(voisin.y+dep[1]*Largeur - this.y, 2));
                    Distances_possibles.add(distance);
                }
                double[] distmin = findMinimum(Distances_possibles);
                double Distance = distmin[0];
                int indexmin = (int) distmin[1];

                if (Distance < RayonIn ){ //& Distance >= RayonOut
                    avgX += (voisin.x+deplacements[indexmin][0]*Largeur);
                    avgY += (voisin.y+deplacements[indexmin][1]*Largeur);
                    count++;
                }
            }
        }
    
        if (count > 0) {
            avgX /= count;
            avgY /= count;
    
            // Calculer la direction vers le centre de masse des voisins
            double directionX = avgX - this.x;
            double directionY = avgY - this.y;
    
            // Normaliser la direction
            double distance = Math.sqrt(directionX * directionX + directionY * directionY);
            if (distance > 0) {
                directionX = (directionX / distance) * Vitesse_max;
                directionY = (directionY / distance) * Vitesse_max;
            }
    
            // Calculer la force d'attraction
            double steerX = directionX - this.vx;
            double steerY = directionY - this.vy;
    
            // Appliquer la force d'attraction
            this.vx += steerX * coeffAttraction;
            this.vy += steerY * coeffAttraction;
        }
	}
        
    public void NormerVitesse(double Vitesse){
        //règle la vitesse de l'oiseau à Vitesse, pour que tous les oiseaux aient la même vitesse.
        double Norme = Math.sqrt(this.vx*this.vx + this.vy*this.vy);
        this.vx = this.vx/Norme * Vitesse;
        this.vy = this.vy/Norme * Vitesse;
    }
        
    public void dessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Dessiner un curseur sous forme de flèche pour indiquer la direction de l'oiseau
        double cursorLength = 10*z; // Longueur du curseur
        double cursorAngle = Math.atan2(vy, vx); // Angle de la direction de l'oiseau

        // Coordonnées des points de la flèche
        int[] xPoints = {(int) x, (int) (x - cursorLength * Math.cos(cursorAngle - Math.PI / 8)), 
                         (int) (x - cursorLength * Math.cos(cursorAngle + Math.PI / 8))};
        int[] yPoints = {(int) y, (int) (y - cursorLength * Math.sin(cursorAngle - Math.PI / 8)), 
                         (int) (y - cursorLength * Math.sin(cursorAngle + Math.PI / 8))};

        // Dessiner la flèche
        Polygon arrow = new Polygon(xPoints, yPoints, 3);
        g2d.setColor(espece); // Couleur de la flèche (noir)
        g2d.fill(arrow);

        // Définir l'épaisseur de la ligne pour le contour noir
        float contourWidth = 2.0f; // Modifier cette valeur selon vos besoins
        g2d.setStroke(new BasicStroke(contourWidth)); // Définir l'épaisseur du contour

        // Ajouter un contour noir autour de la flèche
        g2d.setColor(Color.BLACK);
        g2d.draw(arrow);
    }
}



class NueeOiseaux extends JPanel { 
	private static final long serialVersionUID = 1L;
	
	// Valeur par défaut
    boolean isPaused = false;
    int Vitesse = 1; 
	int NombreOiseaux;
	double coeffRepulsion = 0.05; 
	double coeffAlignement = 0.1; 
	double coeffAttraction = 0.05; 
	double Vitmax=3;
    int RayonAt = 100;
    int RayonAl = 66;
    int RayonRe = 33; 
    
    ArrayList<Oiseaux> NueeOiseaux;
	int Largeur;
	int Hauteur; 
	int Plafond;
	int TailleCase;
	int NombreColonnes, NombreLignes;
	ArrayList<Cases> grille;
    Carte carte;
    
    public NueeOiseaux(int Largeur, int Hauteur, int Plafond, Grille grille, Carte carte) {
        this.NueeOiseaux = new ArrayList<>();  
    	
    	// Récupération des infos
    	this.Largeur = Largeur;
    	this.Hauteur = Hauteur;
    	this.Plafond = Plafond;
    	this.grille  = grille.Grille;
    	this.TailleCase = grille.TailleCase;
    	this.NombreColonnes = grille.NombreColonnes; 
    	this.NombreLignes = grille.NombreLignes;
        this.carte = carte;
        
        // Créer une minuterie pour mettre à jour la simulation toutes les 10 millisecondes
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    DeplacerOiseaux();
                    Boid();
                    repaint(); // Redessiner la fenêtre
                }
            }
        });

        timer.start();
    }
    
    public void DeplacerOiseaux() {
        for (Oiseaux oiseau : NueeOiseaux) {
        	
	        oiseau.Deplacer(this.Vitesse,this.Largeur,this.Hauteur,this.Plafond);
	        
	        double x = oiseau.x;
	        double y = oiseau.y;
	        int NumCase = oiseau.NumCase;
	        
	        // si l'oiseau change de case on màj les population
	        if (Coord_to_Num(x,y) != NumCase) {
	        	oiseau.NumCase = Coord_to_Num(x,y);
	        	for (Cases c : grille) {
	            	if (c.NumCase == oiseau.NumCase) {
	            		c.Population.add(oiseau);
	            	}
	        	}
	            // supprime l'oiseau de son ancienne case	
	            for (Cases c : grille) {
	            	if (c.NumCase == NumCase) {
	            		c.Population.remove(oiseau);
	            	}
	        	}
	        }
        }
    }
     
    public int Coord_to_Num(double x, double y) {
        int X = (int) x / TailleCase;
        int Y = (int) y / TailleCase;
        return Y * NombreColonnes + X;
    }
        
    public void setPause(boolean Pause) {
        isPaused = Pause;
    }
      
    public void setSpeed(int Vitesse) {
        this.Vitesse = Vitesse;
    }

    public void setRayonAt(int R) {
        this.RayonAt = R;
    }

    public void setRayonAl(int R) {
        this.RayonAl = R;
    }

    public void setRayonRe(int R) {
        this.RayonRe = R;
    }
         
    public void setNbrOiseaux(int NombreOiseaux) {
        this.NombreOiseaux = NombreOiseaux;
        updateOiseaux();
    }

    public void setVitmax(double Vitmax) {
    	this.Vitmax = Vitmax;
    }
     
    public void updateOiseaux() {
    	// on créé ou recréé une simulation
        NueeOiseaux.clear();
        for (int i = 0; i < NombreOiseaux; i++) {
        	// Math.random() renvoie une valeur entre O et 1
            double x = Math.random() * Largeur;
            double y = Math.random() * Hauteur;
            double z = Math.random() * (Plafond-1) + 1;
            int NumCase = Coord_to_Num(x, y);
            Color espece = Repartition();
			Oiseaux oiseau = new Oiseaux(x, y, z, NumCase,espece);
            NueeOiseaux.add(oiseau);
            
            // on initialise la population de chaque case
            for (Cases c : grille) {
            	if (c.NumCase == NumCase) {
            		c.Population.add(oiseau);
            	}
            }
        }
    }

    public Color Repartition() {
        // Définir les couleurs et leurs poids correspondants
        HashMap<Color, Integer> couleursEtPoids = new HashMap<>();
        couleursEtPoids.put(Color.RED, 0);    
        couleursEtPoids.put(Color.BLUE, 0);   
        couleursEtPoids.put(Color.GREEN, 0);  
        couleursEtPoids.put(Color.MAGENTA, 3); 

        // Créer une liste de couleurs pondérée basée sur les poids
        ArrayList<Color> couleursPonderees = new ArrayList<>();
        for (Map.Entry<Color, Integer> entry : couleursEtPoids.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                couleursPonderees.add(entry.getKey());
            }
        }

        // Sélectionner aléatoirement une couleur pondérée
        Color espece = couleursPonderees.get(new Random().nextInt(couleursPonderees.size()));
        return espece;
    }

    public void setRepulsion(double coeffRepulsion) {
        this.coeffRepulsion = coeffRepulsion;
    }
    
    public void setAlignement(double coeffAlignement) {
        this.coeffAlignement = coeffAlignement;
    }
    
    public void setAttraction(double coeffAttraction) {
        this.coeffAttraction = coeffAttraction;
    }
    
    public void Boid() {
    	
		for (Oiseaux oiseau1 : NueeOiseaux) {		
			for (Cases c : grille) {
				if (c.NumCase == oiseau1.NumCase) {
					ArrayList<Oiseaux> Voisin = new ArrayList<>(c.Population);
					for (Cases voisin : c.CaseVoisine) {
						Voisin.addAll(voisin.Population);	
					}

                    oiseau1.Repulsion(Voisin,coeffRepulsion,Vitmax,RayonRe, Largeur, Hauteur);
                    oiseau1.Alignement(Voisin,coeffAlignement,Vitmax,RayonAl,RayonRe, Largeur, Hauteur);
                    oiseau1.Attraction(Voisin,coeffAttraction,Vitmax,RayonAt,RayonAl, Largeur, Hauteur);
                    oiseau1.NormerVitesse(Vitmax);	
					
				}
			}
	    }
	}
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(carte.bufferedImage, 0, 0, carte);
        for (Cases c : grille) {  
        	c.dessiner(g2d);
        }
        for (Oiseaux oiseau : NueeOiseaux) {
            oiseau.dessiner(g2d);
        }    
    }
}




public class SimulationNueeOiseaux {
	
    private static boolean isPaused = false;

    public static void main(String[] args) throws Exception {
    	
    	// Paramètre de simulation
    	
    	// Réglage grand écran
   	    // int LargeurEcran = 1730; 
        // int HauteurEcran = 940;
    	// Réglage petit écran 
        int LargeurEcran = 1255; 
        int HauteurEcran = 725;
        
        // espace pour le pannel
        int PannelSpace = 180;
        
        // coté d'une case
        int TailleCase = 100; 
        
        // hauteur plafond
        int Plafond = 2;
        
        // Modifie l'aspect graphique de l'écran et du pannel
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        
        // paramètres par défaults
        JFrame frame = new JFrame("Simulation Nuée d'Oiseaux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(LargeurEcran+PannelSpace,HauteurEcran);

        
        int NombreColonnes = LargeurEcran/TailleCase; 
        int NombreLignes = HauteurEcran/TailleCase; 
        
        int Largeur = NombreColonnes*TailleCase;
        int Hauteur = NombreLignes*TailleCase;
        
        // Création du Cadrillage
        Grille grille = new Grille(TailleCase, NombreColonnes, NombreLignes);
    
        // Création de la Carte
        Carte carte = new Carte(LargeurEcran,LargeurEcran);
        // frame.add(carte);
        
        // Créer un nuage d'oiseaux sur le cadrillage
        NueeOiseaux nueeOiseaux = new NueeOiseaux(Largeur, Hauteur, Plafond, grille, carte); 
        frame.add(nueeOiseaux);


        // gestion du pannel de controle

        // Ajout un slider pour le rayon d'attraction entre 0 et 20, avec une valeur initiale de 50
        JSlider attractionSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
        attractionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double coeffAttraction = attractionSlider.getValue() * 0.01;
                nueeOiseaux.setAttraction(coeffAttraction);
            }
        });
        
        // Ajout un slider pour le rayon d'alignement entre 0 et 20, avec une valeur initiale de 10
        JSlider alignementSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        alignementSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double coeffAlignement = alignementSlider.getValue() * 0.01;
                nueeOiseaux.setAlignement(coeffAlignement);
            }
        });

        // Ajout un slider pour le rayons de répulsion entre 0 et 20, avec une valeur initiale de 5
        JSlider repulsionSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
        repulsionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double coeffRepulsion = repulsionSlider.getValue() * 0.01;
                nueeOiseaux.setRepulsion(coeffRepulsion);
            }
        });

        // slider rayon d'attraction
        JSlider RayonAtSlider = new JSlider(JSlider.HORIZONTAL, 1, TailleCase, 100);
        RayonAtSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int R = RayonAtSlider.getValue();
                nueeOiseaux.setRayonAt(R);
            }
        });

        // slider rayon d'alignement
        JSlider RayonAlSlider = new JSlider(JSlider.HORIZONTAL, 1, TailleCase, 66);
        RayonAlSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int R = RayonAlSlider.getValue();
                nueeOiseaux.setRayonAl(R);
            }
        });

        // slider rayon de répulsion
        JSlider RayonReSlider = new JSlider(JSlider.HORIZONTAL, 1, TailleCase, 33);
        RayonReSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int R = RayonReSlider.getValue();
                nueeOiseaux.setRayonRe(R);
            }
        });

        // Slider pour la vitesse maximale
        JSlider vitmaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 6, 1);
        vitmaxSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double Vitmax = vitmaxSlider.getValue();
                nueeOiseaux.setVitmax(Vitmax);
            }
        });

        // slider pour la vitesse 
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 6, 1);
        speedSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int Vitesse = speedSlider.getValue();
                nueeOiseaux.setSpeed(Vitesse);
            }
        });

        // Ajout d'un champ de texte pour entrer le nombre d'oiseaux
        JTextField nbrOiseauxField = new JTextField();
        //Définit une valeur par défaut de 5 oiseaux
        nbrOiseauxField.setText("5");
        
        // Ajout d'un bouton pour appliquer le changement
        JButton applyButton = new JButton("Appliquer");
        Action applyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // Lorsque ce bouton est cliqué
                try { 
                    // On récupère le nombre d'oiseaux entré dans le champ de texte
                    int NombreOiseaux = Integer.parseInt(nbrOiseauxField.getText());
                    if (NombreOiseaux > 10000) {
                        JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre entre 1 et 10000.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return; }
                    else {
                        nueeOiseaux.setNbrOiseaux(NombreOiseaux); }
                // Si un nombre non valide est entré
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }  
        };
        applyButton.addActionListener(applyAction);

        // Ajouter la liaison pour la touche Entrée
        nbrOiseauxField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "apply");
        nbrOiseauxField.getActionMap().put("apply", applyAction);

        // Ajout d'un bouton de pause
        JButton pauseButton = new JButton("Pause");
        Action pauseAction = new AbstractAction() {
            //Lorsque ce bouton est cliqué
            public void actionPerformed(ActionEvent e) {
                isPaused = !isPaused;
                nueeOiseaux.setPause(isPaused);
            }
        };
        pauseButton.addActionListener(pauseAction);

        // Ajouter la liaison pour la touche Espace
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "pause");
        frame.getRootPane().getActionMap().put("pause", pauseAction);

        
        
        // affichage du pannel de contrôle

        // création du panneau de contrôle avec un BoxLayout
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        
        controlPanel.add(Box.createVerticalStrut(20)); // Espacement vertical
        controlPanel.add(new JLabel("Coeff d'attraction:"));
        controlPanel.add(attractionSlider);
        controlPanel.add(Box.createVerticalStrut(20)); 
        controlPanel.add(new JLabel("Coeff d'alignement:"));
        controlPanel.add(alignementSlider);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(new JLabel("Coeff de répulsion:"));
        controlPanel.add(repulsionSlider);

        controlPanel.add(Box.createVerticalStrut(40));
        controlPanel.add(new JLabel("Rayon d'attraction:"));
        controlPanel.add(RayonAtSlider);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(new JLabel("Rayon d'alignement:"));
        controlPanel.add(RayonAlSlider);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(new JLabel("Rayon de répulsion:"));
        controlPanel.add(RayonReSlider);

        controlPanel.add(Box.createVerticalStrut(50));
        controlPanel.add(new JLabel("Vitesse maximale:"));
        controlPanel.add(vitmaxSlider);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(new JLabel("Vitesse:"));
        controlPanel.add(speedSlider);

        controlPanel.add(Box.createVerticalStrut(50));
        controlPanel.add(new JLabel("Nombre d'oiseaux:"));
        controlPanel.add(nbrOiseauxField);

        // Création du panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Utilisation de FlowLayout avec centrage des composants

        // Ajout du bouton "Appliquer"
        buttonPanel.add(applyButton);

        // Ajout de l'espacement horizontal
        buttonPanel.add(Box.createHorizontalStrut(20)); // Espacement horizontal de 20 pixels

        // Ajout du bouton "Pause"
        buttonPanel.add(pauseButton);

        // Ajout du panneau de boutons au controlPanel
        controlPanel.add(buttonPanel);
 
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        frame.add(controlPanel, BorderLayout.EAST);
         
        frame.setVisible(true);
    }
}




